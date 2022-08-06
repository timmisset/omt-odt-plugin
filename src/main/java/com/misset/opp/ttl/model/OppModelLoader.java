package com.misset.opp.ttl.model;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.io.FileUtil;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.model.constants.OWL;
import com.misset.opp.ttl.model.constants.RDF;
import org.apache.commons.io.FileUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Service
/*
  Helper class to generate a OntologyModel (Apache Jena) with all imports recursively processed as SubModels
  The default import handler doesn't work with the current setup of the Opp Model
 */
public final class OppModelLoader {

    private static final String TTL = "TTL";
    private static final String MISSET_IT_NL = "https://misset-it.nl";
    private static final String REFERENTIEDATA = "http://ontologie.politie.nl/referentiedata";
    private final HashMap<Resource, OntModel> ontologies = new HashMap<>();
    private final List<Resource> processed = new ArrayList<>();

    private static OntModel model;
    private static Collection<File> modelFiles = Collections.emptyList();
    private ProgressIndicator indicator = ProgressManager.getGlobalProgressIndicator();

    public OppModelLoader() {
    }

    public OppModel read(File file) {
        return read(file, indicator);
    }

    public OppModel read(File file, ProgressIndicator indicator) {
        this.indicator = indicator;
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        init(file);
        return new OppModel(model);
    }

    public static Collection<File> getModelFiles() {
        return modelFiles;
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

    private void init(File rootFile) {
        if (!rootFile.exists()) {
            return;
        }
        setIndicatorText("Reading ontology file");
        // load all other ontology files in the subfolders (recursively)
        modelFiles = FileUtils.listFiles(rootFile.getParentFile(), new String[]{TTLFileType.EXTENSION}, true);
        modelFiles
                .stream()
                .filter(file -> !FileUtil.filesEqual(file, rootFile))
                .peek(file -> setIndicatorText2(file.getName()))
                .map(this::readFile)
                .filter(Objects::nonNull)
                .map(this::getSubmodel)
                .forEach(subModel -> ontologies.put(getOntologyResource(subModel), subModel));
        ontologies.keySet().forEach(resource -> model.addLoadedImport(resource.getURI()));
        model.read(readFile(rootFile), MISSET_IT_NL, TTL);
        processImports(model);
        processData();
    }

    private void processImports(OntModel model) {
        final Resource ontologyResource = getOntologyResource(model);
        // add first to prevent recursion
        processed.add(ontologyResource);
        final Property imports = model.createProperty(OWL.IMPORTS.getUri());
        model.getOntResource(ontologyResource)
                .listProperties(imports)
                .forEach(statement -> bindImports(model, statement));
    }

    private void processData() {
        // todo: replace with configuration
        final Resource modelResource = model.getResource(REFERENTIEDATA);
        // the data files are loaded separately from the model, they are not part of the ontology import tree
        final OntModel data = ontologies.get(modelResource);
        if (data != null) {
            model.addSubModel(data);
        }
    }

    private void bindImports(OntModel model, Statement statement) {
        Optional.ofNullable(statement.getObject())
                .map(RDFNode::asResource)
                .map(ontologies::get)
                .filter(subModel -> !processed.contains(getOntologyResource(subModel)))
                .ifPresent(subModel -> {
                    model.addSubModel(subModel);
                    processImports(subModel);
                });
    }

    private OntModel getSubmodel(InputStream inputStream) {
        final OntModel ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        ontologyModel.read(inputStream, null, TTL);
        return ontologyModel;
    }

    private Resource getOntologyResource(OntModel model) {
        final Property rdtType = model.createProperty(RDF.TYPE.getUri());
        final Property Ontology = model.createProperty(OWL.ONTOLOGY.getUri());
        final ResIterator resIterator = model.listSubjectsWithProperty(rdtType, Ontology);
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
