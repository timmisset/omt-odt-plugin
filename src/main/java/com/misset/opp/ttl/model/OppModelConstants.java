package com.misset.opp.ttl.model;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import java.util.List;
import java.util.Set;

public class OppModelConstants {
    public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
    public static final String SHACL = "http://www.w3.org/ns/shacl#";
    public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String OWL = "http://www.w3.org/2002/07/owl#";
    public static final String OPP = "http://ontologie.politie.nl/def/politie#";
    public static final String PLATFORM = "http://ontologie.politie.nl/def/platform#";
    public static Property SHACL_PATH;
    public static Property SHACL_CLASS;
    public static Property SHACL_MINCOUNT;
    public static Property SHACL_MAXCOUNT;
    public static Property SHACL_DATATYPE;
    public static Property SHACL_PROPERTY;
    public static Property SHACL_PROPERYSHAPE;
    public static Property RDFS_SUBCLASS_OF;
    public static Property RDF_TYPE;
    public static List<Property> CLASS_MODEL_PROPERTIES;
    public static OntClass OWL_CLASS;
    public static OntClass OWL_THING_CLASS;
    public static Individual OWL_THING_INSTANCE;
    public static OntClass OPP_CLASS;
    public static OntClass JSON;
    public static OntClass GRAPH_SHAPE;
    public static OntClass GRAPH_CLASS;
    public static OntClass NAMED_GRAPH_CLASS;
    public static OntClass TRANSIENT_GRAPH_CLASS;
    public static Individual IRI;
    public static Individual JSON_OBJECT;
    public static Individual BLANK_NODE;
    public static Individual ERROR;
    public static Individual NAMED_GRAPH;
    public static Individual TRANSIENT_GRAPH;
    public static Individual MEDEWERKER_GRAPH;
    public static Individual VOID;
    public static OntClass XSD_BOOLEAN;
    public static OntClass XSD_STRING;
    public static OntClass XSD_NUMBER;
    public static OntClass XSD_INTEGER;
    public static OntClass XSD_DECIMAL;
    public static OntClass XSD_DATE;
    public static OntClass XSD_DATETIME;
    public static OntClass XSD_DURATION;
    public static Individual XSD_BOOLEAN_INSTANCE;
    public static Individual XSD_STRING_INSTANCE;
    public static Individual XSD_NUMBER_INSTANCE;
    public static Individual XSD_INTEGER_INSTANCE;
    public static Individual XSD_DECIMAL_INSTANCE;
    public static Individual XSD_DATE_INSTANCE;
    public static Individual XSD_DATETIME_INSTANCE;
    public static Individual XSD_DURATION_INSTANCE;

    public static void setConstants(OppModel model, OppModelCache modelCache, OntModel ontologyModel) {
        setProperties(model, ontologyModel);
        setPrimitives(model, modelCache, ontologyModel);
    }

    private static void setPrimitives(OppModel model, OppModelCache modelCache, OntModel ontologyModel) {
        XSD_BOOLEAN = model.createClass(XSD + "boolean", ontologyModel);
        XSD_BOOLEAN_INSTANCE = model.createIndividual(XSD_BOOLEAN);

        XSD_STRING = model.createClass(XSD + "string", ontologyModel);
        XSD_STRING_INSTANCE = model.createIndividual(XSD_STRING);

        XSD_NUMBER = model.createClass(XSD + "number", ontologyModel);
        XSD_NUMBER_INSTANCE = model.createIndividual(XSD_NUMBER);

        XSD_DECIMAL = model.createClass(XSD + "decimal", ontologyModel);
        XSD_DECIMAL.addSuperClass(XSD_NUMBER);
        XSD_DECIMAL_INSTANCE = model.createIndividual(XSD_DECIMAL);
        modelCache.cacheSuperclasses(XSD_DECIMAL.getURI(), XSD_DECIMAL);

        XSD_INTEGER = model.createClass(XSD + "integer", ontologyModel);
        // by making XSD_INTEGER a subclass of XSD_DECIMAL, it will allow type checking
        // to accept an integer at a decimal position, but not the other way around
        XSD_INTEGER.addSuperClass(XSD_DECIMAL);
        XSD_INTEGER_INSTANCE = model.createIndividual(XSD_INTEGER);
        modelCache.cacheSuperclasses(XSD_INTEGER.getURI(), XSD_INTEGER);

        XSD_DATETIME = model.createClass(XSD + "dateTime", ontologyModel);
        XSD_DATETIME_INSTANCE = model.createIndividual(XSD_DATETIME);

        XSD_DATE = model.createClass(XSD + "date", ontologyModel);
        // by making XSD_DATE a subclass of XSD_DATETIME, it will allow type checking
        // to accept a date at a datetime position, but not the other way around
        XSD_DATE.addSuperClass(XSD_DATETIME);
        XSD_DATE_INSTANCE = model.createIndividual(XSD_DATE);
        modelCache.cacheSuperclasses(XSD_DATE.getURI(), XSD_DATE);

        XSD_DURATION = model.createClass(XSD + "duration", ontologyModel);
        XSD_DURATION_INSTANCE = model.createIndividual(XSD_DURATION);
    }

    private static void setProperties(OppModel model, OntModel ontologyModel) {
        SHACL_PATH = model.createProperty(SHACL + "path", ontologyModel);
        SHACL_CLASS = model.createProperty(SHACL + "class", ontologyModel);
        SHACL_DATATYPE = model.createProperty(SHACL + "datatype", ontologyModel);
        SHACL_MINCOUNT = model.createProperty(SHACL + "minCount", ontologyModel);
        SHACL_MAXCOUNT = model.createProperty(SHACL + "maxCount", ontologyModel);
        SHACL_PROPERTY = model.createProperty(SHACL + "property", ontologyModel);
        SHACL_PROPERYSHAPE = model.createProperty(SHACL + "PropertyShape", ontologyModel);

        RDFS_SUBCLASS_OF = model.createProperty(RDFS + "subClassOf", ontologyModel);
        RDF_TYPE = model.createProperty(RDF + "type", ontologyModel);

        OWL_CLASS = model.createClass(OWL + "Class", ontologyModel);
        OWL_THING_CLASS = model.createClass(OWL + "Thing", ontologyModel);
        OWL_THING_INSTANCE = model.createIndividual(OWL_THING_CLASS, OWL + "Thing_INSTANCE");

        OPP_CLASS = model.createClass(OPP + "Class", ontologyModel);
        JSON = model.createClass(OPP + "JSON", ontologyModel);
        JSON_OBJECT = model.createIndividual(JSON, OPP + "JSON_OBJECT");
        BLANK_NODE = model.createIndividual(OPP_CLASS, OPP + "BLANK_NODE");
        IRI = model.createIndividual(OPP_CLASS, OPP + "IRI");
        ERROR = model.createIndividual(OPP_CLASS, OPP + "ERROR");
        VOID = model.createIndividual(OPP_CLASS, OPP + "VOID");

        GRAPH_CLASS = model.createClass(PLATFORM + "Graph", ontologyModel);
        NAMED_GRAPH_CLASS = model.createClass(PLATFORM + "NamedGraph", ontologyModel);
        GRAPH_SHAPE = model.createClass(PLATFORM + "GraphShape", ontologyModel);
        TRANSIENT_GRAPH_CLASS = model.createClass("http://ontologie.politie.nl/internal/transient#TransientNamedGraph", ontologyModel);

        NAMED_GRAPH = model.createIndividual(NAMED_GRAPH_CLASS, NAMED_GRAPH_CLASS.getURI() + "_INSTANCE");
        MEDEWERKER_GRAPH = model.createIndividual(NAMED_GRAPH_CLASS, NAMED_GRAPH_CLASS.getURI() + "_MEDEWERKERGRAPH");
        TRANSIENT_GRAPH = model.createIndividual(TRANSIENT_GRAPH_CLASS, TRANSIENT_GRAPH_CLASS.getURI() + "_INSTANCE");

        CLASS_MODEL_PROPERTIES = List.of(RDFS_SUBCLASS_OF, RDF_TYPE);
    }

    public static Set<OntClass> listXSDTypes() {
        return Set.of(
                XSD_BOOLEAN,
                XSD_STRING,
                XSD_INTEGER,
                XSD_DECIMAL,
                XSD_DATETIME,
                XSD_DATE,
                XSD_DURATION);
    }
}
