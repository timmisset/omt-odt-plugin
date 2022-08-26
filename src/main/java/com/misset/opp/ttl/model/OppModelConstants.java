package com.misset.opp.ttl.model;

import com.misset.opp.ttl.model.constants.*;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import java.util.List;
import java.util.Set;

public class OppModelConstants {
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
    public static OntClass RDFS_CLASS;
    public static OntClass RDFS_RESOURCE;
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

    public static void setConstants(OppModel model, OntModel ontologyModel) {
        setProperties(model, ontologyModel);
        setClasses(model, ontologyModel);
        setIndividuals(model);
        setPrimitives(model, ontologyModel);
    }

    private static void setPrimitives(OppModel model, OntModel ontologyModel) {
        XSD_BOOLEAN = model.createClass(XSD.BOOLEAN.getUri(), ontologyModel, OWL_THING_CLASS);
        XSD_BOOLEAN_INSTANCE = model.createIndividual(XSD_BOOLEAN, XSD.BOOLEAN_INSTANCE.getUri());

        XSD_STRING = model.createClass(XSD.STRING.getUri(), ontologyModel, OWL_THING_CLASS);
        XSD_STRING_INSTANCE = model.createIndividual(XSD_STRING, XSD.STRING_INSTANCE.getUri());

        XSD_NUMBER = model.createClass(XSD.NUMBER.getUri(), ontologyModel, OWL_THING_CLASS);
        XSD_NUMBER_INSTANCE = model.createIndividual(XSD_NUMBER, XSD.NUMBER_INSTANCE.getUri());

        XSD_DECIMAL = model.createClass(XSD.DECIMAL.getUri(), ontologyModel, List.of(OWL_THING_CLASS, XSD_NUMBER));
        XSD_DECIMAL_INSTANCE = model.createIndividual(XSD_DECIMAL, XSD.DECIMAL_INSTANCE.getUri());

        XSD_INTEGER = model.createClass(XSD.INTEGER.getUri(), ontologyModel, List.of(OWL_THING_CLASS, XSD_DECIMAL));
        // by making XSD_INTEGER a subclass of XSD_DECIMAL, it will allow type checking
        // to accept an integer at a decimal position, but not the other way around
        XSD_INTEGER_INSTANCE = model.createIndividual(XSD_INTEGER, XSD.INTEGER_INSTANCE.getUri());

        XSD_DATETIME = model.createClass(XSD.DATETIME.getUri(), ontologyModel, OWL_THING_CLASS);
        XSD_DATETIME_INSTANCE = model.createIndividual(XSD_DATETIME, XSD.DATETIME_INSTANCE.getUri());

        XSD_DATE = model.createClass(XSD.DATE.getUri(), ontologyModel, List.of(OWL_THING_CLASS, XSD_DATETIME));
        // by making XSD_DATE a subclass of XSD_DATETIME, it will allow type checking
        // to accept a date at a datetime position, but not the other way around
        XSD_DATE_INSTANCE = model.createIndividual(XSD_DATE, XSD.DATE_INSTANCE.getUri());

        XSD_DURATION = model.createClass(XSD.DURATION.getUri(), ontologyModel, OWL_THING_CLASS);
        XSD_DURATION_INSTANCE = model.createIndividual(XSD_DURATION, XSD.DURATION_INSTANCE.getUri());
    }

    private static void setProperties(OppModel model, OntModel ontologyModel) {
        SHACL_PATH = model.createProperty(SHACL.PATH.getUri(), ontologyModel);
        SHACL_CLASS = model.createProperty(SHACL.CLASS.getUri(), ontologyModel);
        SHACL_DATATYPE = model.createProperty(SHACL.DATATYPE.getUri(), ontologyModel);
        SHACL_MINCOUNT = model.createProperty(SHACL.MIN_COUNT.getUri(), ontologyModel);
        SHACL_MAXCOUNT = model.createProperty(SHACL.MAX_COUNT.getUri(), ontologyModel);
        SHACL_PROPERTY = model.createProperty(SHACL.PROPERTY.getUri(), ontologyModel);
        SHACL_PROPERYSHAPE = model.createProperty(SHACL.PROPERTY_SHAPE.getUri(), ontologyModel);

        RDFS_SUBCLASS_OF = model.createProperty(RDFS.SUBCLASS_OF.getUri(), ontologyModel);
        RDF_TYPE = model.createProperty(RDF.TYPE.getUri(), ontologyModel);

        CLASS_MODEL_PROPERTIES = List.of(RDFS_SUBCLASS_OF, RDF_TYPE);
    }

    private static void setIndividuals(OppModel model) {
        OWL_THING_INSTANCE = model.createIndividual(OWL_THING_CLASS, OWL.THING_INSTANCE.getUri());

        JSON_OBJECT = model.createIndividual(JSON, PLATFORM.JSON_OBJECT.getUri());
        IRI = model.createIndividual(OPP_CLASS, PLATFORM.IRI.getUri());
        ERROR = model.createIndividual(OPP_CLASS, PLATFORM.ERROR.getUri());
        VOID = model.createIndividual(OPP_CLASS, PLATFORM.VOID.getUri());

        NAMED_GRAPH = model.createIndividual(NAMED_GRAPH_CLASS, NAMED_GRAPH_CLASS.getURI() + "_INSTANCE");
        MEDEWERKER_GRAPH = model.createIndividual(NAMED_GRAPH_CLASS, NAMED_GRAPH_CLASS.getURI() + "_MEDEWERKERGRAPH");
        TRANSIENT_GRAPH = model.createIndividual(TRANSIENT_GRAPH_CLASS, TRANSIENT_GRAPH_CLASS.getURI() + "_INSTANCE");
        BLANK_NODE = model.createIndividual(OPP_CLASS, OPP + "BLANK_NODE");
    }

    private static void setClasses(OppModel model, OntModel ontologyModel) {
        RDFS_RESOURCE = model.createClass(RDFS.RESOURCE.getUri(), ontologyModel);
        RDFS_CLASS = model.createClass(RDFS.CLASS.getUri(), ontologyModel, RDFS_RESOURCE);
        OWL_CLASS = model.createClass(OWL.CLASS.getUri(), ontologyModel, RDFS_CLASS);
        OWL_THING_CLASS = model.createClass(OWL.THING.getUri(), ontologyModel);
        OWL_THING_CLASS.setRDFType(OWL_CLASS);

        OPP_CLASS = model.createClass(PLATFORM.CLASS.getUri(), ontologyModel, OWL_THING_CLASS);
        JSON = model.createClass(PLATFORM.JSON.getUri(), ontologyModel, OWL_THING_CLASS);
        GRAPH_CLASS = model.createClass(PLATFORM.GRAPH.getUri(), ontologyModel, OWL_THING_CLASS);
        NAMED_GRAPH_CLASS = model.createClass(PLATFORM.NAMED_GRAPH.getUri(), ontologyModel, OWL_THING_CLASS);
        GRAPH_SHAPE = model.createClass(PLATFORM.GRAPH_SHAPE.getUri(), ontologyModel, OWL_THING_CLASS);
        TRANSIENT_GRAPH_CLASS = model.createClass(com.misset.opp.ttl.model.constants.PLATFORM.TRANSIENT_GRAPH.getUri(), ontologyModel, OWL_THING_CLASS);
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
