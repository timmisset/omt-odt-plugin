package com.misset.opp.model;

import com.misset.opp.model.constants.*;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

import java.util.List;
import java.util.Set;

public final class OntologyModelConstants {
    private static Property shaclPath;
    private static Property shaclClass;
    private static Property shaclMincount;
    private static Property shaclMaxcount;
    private static Property shaclDatatype;
    private static Property shaclProperty;
    private static Property shaclProperyshape;
    private static Property rdfsSubclassOf;
    private static Property rdfType;
    private static List<Property> classModelProperties;
    private static OntClass rdfsClass;
    private static OntClass rdfsResource;
    private static OntClass owlClass;
    private static OntClass owlThingClass;
    private static Individual owlThingInstance;
    private static OntClass oppClass;
    private static OntClass json;
    private static OntClass graphShape;
    private static OntClass graphClass;
    private static OntClass namedGraphClass;
    private static OntClass transientGraphClass;
    private static Individual iri;
    private static Individual jsonObject;
    private static Individual blankNode;
    private static Individual error;
    private static Individual namedGraph;
    private static Individual transientGraph;
    private static Individual medewerkerGraph;
    private static Individual voidResponse;
    private static OntClass xsdBoolean;
    private static OntClass xsdString;
    private static OntClass xsdNumber;
    private static OntClass xsdInteger;
    private static OntClass xsdDecimal;
    private static OntClass xsdDate;
    private static OntClass xsdDatetime;
    private static OntClass xsdDuration;
    private static Individual xsdBooleanInstance;
    private static Individual xsdStringInstance;
    private static Individual xsdNumberInstance;
    private static Individual xsdIntegerInstance;
    private static Individual xsdDecimalInstance;
    private static Individual xsdDateInstance;
    private static Individual xsdDatetimeInstance;
    private static Individual xsdDurationInstance;

    private static Literal xsdBooleanFalse;
    private static Literal xsdBooleanTrue;

    static {
        // create the static members with an initial model. Once the OntologyModel is actually loaded
        // these resources have to be registered there to be part of the caching mechanism
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntologyModel ontologyModel = new OntologyModel(ontModel);
        setConstants(ontologyModel, ontModel);
    }

    private OntologyModelConstants() {
        // empty constructor
    }

    public static void setConstants(OntologyModel model, OntModel ontologyModel) {
        setProperties(model, ontologyModel);
        setClasses(model, ontologyModel);
        setIndividuals(model);
        setPrimitives(model, ontologyModel);
    }

    private static void setPrimitives(OntologyModel model, OntModel ontologyModel) {
        xsdBoolean = model.createClass(XSD.BOOLEAN.getUri(), ontologyModel, getOwlThingClass());
        xsdBooleanInstance = model.createIndividual(getXsdBoolean(), XSD.BOOLEAN_INSTANCE.getUri());
        xsdBooleanTrue = ontologyModel.createTypedLiteral(true);
        xsdBooleanFalse = ontologyModel.createTypedLiteral(false);

        xsdString = model.createClass(XSD.STRING.getUri(), ontologyModel, getOwlThingClass());
        xsdStringInstance = model.createIndividual(getXsdString(), XSD.STRING_INSTANCE.getUri());

        xsdNumber = model.createClass(XSD.NUMBER.getUri(), ontologyModel, getOwlThingClass());
        xsdNumberInstance = model.createIndividual(getXsdNumber(), XSD.NUMBER_INSTANCE.getUri());

        xsdDecimal = model.createClass(XSD.DECIMAL.getUri(), ontologyModel, List.of(getOwlThingClass(), getXsdNumber()));
        xsdDecimalInstance = model.createIndividual(getXsdDecimal(), XSD.DECIMAL_INSTANCE.getUri());

        xsdInteger = model.createClass(XSD.INTEGER.getUri(), ontologyModel, List.of(getOwlThingClass(), getXsdDecimal()));
        // by making XSD_INTEGER a subclass of XSD_DECIMAL, it will allow type checking
        // to accept an integer at a decimal position, but not the other way around
        xsdIntegerInstance = model.createIndividual(getXsdInteger(), XSD.INTEGER_INSTANCE.getUri());

        xsdDatetime = model.createClass(XSD.DATETIME.getUri(), ontologyModel, getOwlThingClass());
        xsdDatetimeInstance = model.createIndividual(getXsdDatetime(), XSD.DATETIME_INSTANCE.getUri());

        xsdDate = model.createClass(XSD.DATE.getUri(), ontologyModel, List.of(getOwlThingClass(), getXsdDatetime()));
        // by making XSD_DATE a subclass of XSD_DATETIME, it will allow type checking
        // to accept a date at a datetime position, but not the other way around
        xsdDateInstance = model.createIndividual(getXsdDate(), XSD.DATE_INSTANCE.getUri());

        xsdDuration = model.createClass(XSD.DURATION.getUri(), ontologyModel, getOwlThingClass());
        xsdDurationInstance = model.createIndividual(getXsdDuration(), XSD.DURATION_INSTANCE.getUri());
    }

    private static void setProperties(OntologyModel model, OntModel ontologyModel) {
        shaclPath = model.createProperty(SHACL.PATH.getUri(), ontologyModel);
        shaclClass = model.createProperty(SHACL.CLASS.getUri(), ontologyModel);
        shaclDatatype = model.createProperty(SHACL.DATATYPE.getUri(), ontologyModel);
        shaclMincount = model.createProperty(SHACL.MIN_COUNT.getUri(), ontologyModel);
        shaclMaxcount = model.createProperty(SHACL.MAX_COUNT.getUri(), ontologyModel);
        shaclProperty = model.createProperty(SHACL.PROPERTY.getUri(), ontologyModel);
        shaclProperyshape = model.createProperty(SHACL.PROPERTY_SHAPE.getUri(), ontologyModel);

        rdfsSubclassOf = model.createProperty(RDFS.SUBCLASS_OF.getUri(), ontologyModel);
        rdfType = model.createProperty(RDF.TYPE.getUri(), ontologyModel);

        classModelProperties = List.of(getRdfsSubclassOf(), getRdfType());
    }

    private static void setIndividuals(OntologyModel model) {
        owlThingInstance = model.createIndividual(getOwlThingClass(), OWL.THING_INSTANCE.getUri());

        jsonObject = model.createIndividual(getJson(), PLATFORM.JSON_OBJECT.getUri());
        iri = model.createIndividual(getOppClass(), PLATFORM.IRI.getUri());
        error = model.createIndividual(getOppClass(), PLATFORM.ERROR.getUri());
        voidResponse = model.createIndividual(getOppClass(), PLATFORM.VOID.getUri());

        namedGraph = model.createIndividual(getNamedGraphClass(), getNamedGraphClass().getURI() + "_INSTANCE");
        medewerkerGraph = model.createIndividual(getNamedGraphClass(), getNamedGraphClass().getURI() + "_MEDEWERKERGRAPH");
        transientGraph = model.createIndividual(getTransientGraphClass(), getTransientGraphClass().getURI() + "_INSTANCE");
        blankNode = model.createIndividual(getOppClass(), PLATFORM.BLANK_NODE.getUri());
    }

    private static void setClasses(OntologyModel model, OntModel ontologyModel) {
        rdfsResource = model.createClass(RDFS.RESOURCE.getUri(), ontologyModel);
        rdfsClass = model.createClass(RDFS.CLASS.getUri(), ontologyModel, getRdfsResource());
        owlClass = model.createClass(OWL.CLASS.getUri(), ontologyModel, getRdfsClass());
        owlThingClass = model.createClass(OWL.THING.getUri(), ontologyModel);
        getOwlThingClass().setRDFType(getOwlClass());

        oppClass = model.createClass(PLATFORM.CLASS.getUri(), ontologyModel, getOwlThingClass());
        json = model.createClass(PLATFORM.JSON.getUri(), ontologyModel, getOwlThingClass());
        json.addSuperClass(oppClass);
        graphClass = model.createClass(PLATFORM.GRAPH.getUri(), ontologyModel, getOwlThingClass());
        graphClass.addSuperClass(oppClass);
        namedGraphClass = model.createClass(PLATFORM.NAMED_GRAPH.getUri(), ontologyModel, getOwlThingClass());
        namedGraphClass.addSuperClass(graphClass);
        graphShape = model.createClass(PLATFORM.GRAPH_SHAPE.getUri(), ontologyModel, getOwlThingClass());
        graphShape.addSuperClass(oppClass);
        transientGraphClass = model.createClass(PLATFORM.TRANSIENT_GRAPH.getUri(), ontologyModel, getOwlThingClass());
        transientGraphClass.addSuperClass(graphClass);
    }

    public static Set<OntClass> listXSDTypes() {
        return Set.of(
                getXsdBoolean(),
                getXsdString(),
                getXsdInteger(),
                getXsdDecimal(),
                getXsdDatetime(),
                getXsdDate(),
                getXsdDuration());
    }

    public static Property getShaclPath() {
        return shaclPath;
    }

    public static Property getShaclClass() {
        return shaclClass;
    }

    public static Property getShaclMincount() {
        return shaclMincount;
    }

    public static Property getShaclMaxcount() {
        return shaclMaxcount;
    }

    public static Property getShaclDatatype() {
        return shaclDatatype;
    }

    public static Property getShaclProperty() {
        return shaclProperty;
    }

    public static Property getShaclProperyshape() {
        return shaclProperyshape;
    }

    public static Property getRdfsSubclassOf() {
        return rdfsSubclassOf;
    }

    public static Property getRdfType() {
        return rdfType;
    }

    public static List<Property> getClassModelProperties() {
        return classModelProperties;
    }

    public static OntClass getRdfsClass() {
        return rdfsClass;
    }

    public static OntClass getRdfsResource() {
        return rdfsResource;
    }

    public static OntClass getOwlClass() {
        return owlClass;
    }

    public static OntClass getOwlThingClass() {
        return owlThingClass;
    }

    public static Individual getOwlThingInstance() {
        return owlThingInstance;
    }

    public static OntClass getOppClass() {
        return oppClass;
    }

    public static OntClass getJson() {
        return json;
    }

    public static OntClass getGraphShape() {
        return graphShape;
    }

    public static OntClass getGraphClass() {
        return graphClass;
    }

    public static OntClass getNamedGraphClass() {
        return namedGraphClass;
    }

    public static OntClass getTransientGraphClass() {
        return transientGraphClass;
    }

    public static Individual getIri() {
        return iri;
    }

    public static Individual getJsonObject() {
        return jsonObject;
    }

    public static Individual getBlankNode() {
        return blankNode;
    }

    public static Individual getError() {
        return error;
    }

    public static Individual getNamedGraph() {
        return namedGraph;
    }

    public static Individual getTransientGraph() {
        return transientGraph;
    }

    public static Individual getMedewerkerGraph() {
        return medewerkerGraph;
    }

    public static Individual getVoidResponse() {
        return voidResponse;
    }

    public static OntClass getXsdBoolean() {
        return xsdBoolean;
    }

    public static OntClass getXsdString() {
        return xsdString;
    }

    public static OntClass getXsdNumber() {
        return xsdNumber;
    }

    public static OntClass getXsdInteger() {
        return xsdInteger;
    }

    public static OntClass getXsdDecimal() {
        return xsdDecimal;
    }

    public static OntClass getXsdDate() {
        return xsdDate;
    }

    public static OntClass getXsdDatetime() {
        return xsdDatetime;
    }

    public static OntClass getXsdDuration() {
        return xsdDuration;
    }

    public static Individual getXsdBooleanInstance() {
        return xsdBooleanInstance;
    }

    public static Individual getXsdStringInstance() {
        return xsdStringInstance;
    }

    public static Individual getXsdNumberInstance() {
        return xsdNumberInstance;
    }

    public static Individual getXsdIntegerInstance() {
        return xsdIntegerInstance;
    }

    public static Individual getXsdDecimalInstance() {
        return xsdDecimalInstance;
    }

    public static Individual getXsdDateInstance() {
        return xsdDateInstance;
    }

    public static Individual getXsdDatetimeInstance() {
        return xsdDatetimeInstance;
    }

    public static Individual getXsdDurationInstance() {
        return xsdDurationInstance;
    }

    public static Literal getXsdBooleanFalse() {
        return xsdBooleanFalse;
    }

    public static Literal getXsdBooleanTrue() {
        return xsdBooleanTrue;
    }
}
