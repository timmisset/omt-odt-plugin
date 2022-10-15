package com.misset.opp.model;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.misset.opp.model.constants.OWL;
import com.misset.opp.model.constants.RDF;
import com.misset.opp.ttl.TTLFileType;
import org.apache.commons.io.FileUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/*
  Helper class to generate a OntologyModel (Apache Jena) with all imports recursively processed as SubModels
  The default import handler doesn't work with the current setup of the Opp Model
 */
@Service
public final class OntologyModelLoader {

    private final Project project;
    private Collection<File> modelFiles = Collections.emptyList();

    private static final String TTL = "TTL";
    private static final String MISSET_IT_NL = "https://misset-it.nl";
    private static final String REFERENTIEDATA = "http://ontologie.politie.nl/referentiedata";
    private final HashMap<Resource, OntModel> ontologies = new HashMap<>();
    private ProgressIndicator indicator = ProgressIndicatorProvider.getGlobalProgressIndicator();
    private OntologyModel ontologyModel;

    public OntologyModelLoader(Project project) {
        this.project = project;
    }

    public static OntologyModelLoader getInstance(@NotNull Project project) {
        return project.getService(OntologyModelLoader.class);
    }

    public OntologyModel read(File file) {
        return read(file, indicator);
    }

    public OntologyModel read(File file, ProgressIndicator indicator) {
        this.indicator = indicator;
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        init(file, model);
        ontologyModel = new OntologyModel(project);
        ontologyModel.init(model);
        return ontologyModel;
    }

    public Collection<File> getModelFiles() {
        return modelFiles;
    }

    public OntologyModel getOntologyModel() {
        return ontologyModel;
    }

    private void setIndicatorText(String text) {
        if (indicator == null) {
            return;
        }
        indicator.setText(text);
    }

    private void setIndicatorText2(String text) {
        if (indicator == null) {
            return;
        }
        indicator.setText2(text);
    }

    private void init(File rootFile, OntModel model) {
        if (!rootFile.exists()) {
            return;
        }
        setIndicatorText("Reading ontology file");
        // load all other ontology files in the subfolders (recursively)
        modelFiles = FileUtils.listFiles(rootFile.getParentFile(), new String[]{TTLFileType.EXTENSION}, true);
        modelFiles
                .stream()
                .filter(file -> !FileUtil.filesEqual(file, rootFile))
                .map(file -> {
                    setIndicatorText2(file.getName());
                    return file;
                })
                .map(this::readFile)
                .filter(Objects::nonNull)
                .map(this::getSubmodel)
                .forEach(subModel -> ontologies.put(getOntologyResource(subModel), subModel));
        ontologies.keySet().forEach(resource -> model.addLoadedImport(resource.getURI()));
        model.read(readFile(rootFile), MISSET_IT_NL, TTL);
        processImports(model, new ArrayList<>());
        processData(model);
    }

    private void processImports(OntModel model, List<Resource> processed) {
        final Resource ontologyResource = getOntologyResource(model);
        // add first to prevent recursion
        processed.add(ontologyResource);
        final Property imports = model.createProperty(OWL.IMPORTS.getUri());
        model.getOntResource(ontologyResource)
                .listProperties(imports)
                .forEach(statement -> bindImports(model, statement, processed));
    }

    private void processData(OntModel model) {
        // open issue: https://github.com/timmisset/omt-odt-plugin/issues/129
        final Resource modelResource = model.getResource(REFERENTIEDATA);
        // the data files are loaded separately from the model, they are not part of the ontology import tree
        final OntModel data = ontologies.get(modelResource);
        if (data != null) {
            model.addSubModel(data);
        }
    }

    private void bindImports(OntModel model, Statement statement, List<Resource> processed) {
        Optional.ofNullable(statement.getObject())
                .map(RDFNode::asResource)
                .map(ontologies::get)
                .filter(subModel -> !processed.contains(getOntologyResource(subModel)))
                .ifPresent(subModel -> {
                    model.addSubModel(subModel);
                    processImports(subModel, processed);
                });
    }

    private OntModel getSubmodel(InputStream inputStream) {
        final OntModel subModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        subModel.read(inputStream, null, TTL);
        return subModel;
    }

    private Resource getOntologyResource(OntModel model) {
        final Property rdtType = model.createProperty(RDF.TYPE.getUri());
        final Property ontology = model.createProperty(OWL.ONTOLOGY.getUri());
        final ResIterator resIterator = model.listSubjectsWithProperty(rdtType, ontology);
        return resIterator.hasNext() ? resIterator.next() : model.createResource();
    }

    private FileInputStream readFile(File file) {
        try {
            setIndicatorText2(file.getName());
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
