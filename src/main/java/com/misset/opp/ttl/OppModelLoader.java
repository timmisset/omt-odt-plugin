package com.misset.opp.ttl;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.io.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
/*
  Helper class to generate a OntologyModel (Apache Jena) with all imports recursively processed as SubModels
  The default import handler doesn't work with the current setup of the Opp Model
 */
public final class OppModelLoader {

    private final HashMap<Resource, OntModel> ontologies = new HashMap<>();
    private final List<Resource> processed = new ArrayList<>();
    Logger logger = Logger.getInstance(OppModelLoader.class);

    private static OntModel model;
    private ProgressIndicator indicator = ProgressManager.getGlobalProgressIndicator();

    public OppModelLoader() { }
    public OppModel read(File file) {
        return read(file, indicator);
    }
    public OppModel read(File file, ProgressIndicator indicator) {
        this.indicator = indicator;
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        init(file);
        return new OppModel(model);
    }
    private void setIndicatorText(String text) {
        if(indicator == null) { return; }
        indicator.setText(text);
    }
    private void setIndicatorText2(String text) {
        if(indicator == null) { return; }
        indicator.setText2(text);
    }
    private void init(File rootFile) {
        if(!rootFile.exists()) { return; }
        setIndicatorText("Reading ontology file");
        // load all other ontology files in the subfolders (recursively)
        FileUtils.listFiles(rootFile.getParentFile(), new String[] { "ttl" }, true)
                .stream()
                .filter(file -> !FileUtil.filesEqual(file, rootFile))
                .peek(file -> setIndicatorText2(file.getName()))
                .map(this::readFile)
                .filter(Objects::nonNull)
                .map(this::getSubmodel)
                .forEach(subModel -> ontologies.put(getOntologyResource(subModel), subModel));
        model.read(readFile(rootFile), null, "TTL");
        processImports(model);
    }
    private void processImports(OntModel model) {
        final Resource ontologyResource = getOntologyResource(model);
        // add first to prevent recursion
        processed.add(ontologyResource);
        final Property imports = model.createProperty("http://www.w3.org/2002/07/owl#imports");
        model.getOntResource(ontologyResource)
                .listProperties(imports)
                .forEach(statement -> bindImports(model, statement));
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
        ontologyModel.read(inputStream, null, "TTL");
        return ontologyModel;
    }
    private Resource getOntologyResource(OntModel model) {
        final Property rdtType = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        final Property Ontology = model.createProperty("http://www.w3.org/2002/07/owl#Ontology");
        final ResIterator resIterator = model.listSubjectsWithProperty(rdtType, Ontology);
        return resIterator.hasNext() ? resIterator.next() : model.createResource();
    }
    private FileInputStream readFile(File file) {
        try {
            logger.warn("Reading file: " + file.getAbsolutePath());
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
