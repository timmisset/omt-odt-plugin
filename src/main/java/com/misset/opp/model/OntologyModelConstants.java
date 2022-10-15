package com.misset.opp.model;

import com.misset.opp.model.constants.*;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.List;
import java.util.Set;

public final class OntologyModelConstants {
    private static final OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
    private static OntProperty shaclPath;
    private static OntProperty shaclClass;
    private static OntProperty shaclMincount;
    private static OntProperty shaclMaxcount;
    private static OntProperty shaclDatatype;
    private static OntProperty shaclProperty;
    private static OntProperty shaclProperyshape;
    private static OntProperty rdfsSubclassOf;
    private static OntProperty rdfType;
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
    private static List<OntProperty> classModelProperties;

    static {
        // create the static members with an initial model. Once the OntologyModel is actually loaded
        // these resources have to be registered there to be part of the caching mechanism
        initConstants(ontModel);
    }

    private OntologyModelConstants() {
        // empty constructor
    }

    public static OntModel getOntModel() {
        return ontModel;
    }

    public static void initConstants(OntModel ontologyModel) {
        setProperties(ontologyModel);
        setClasses(ontologyModel);
        setIndividuals(ontologyModel);
        setPrimitives(ontologyModel);
    }

    private static void setPrimitives(OntModel ontologyModel) {
        xsdBoolean = ontologyModel.createClass(XSD.BOOLEAN.getUri());
        xsdBoolean.setSuperClass(owlThingClass);
        xsdBooleanInstance = ontologyModel.createIndividual(XSD.BOOLEAN_INSTANCE.getUri(), xsdBoolean);
        xsdBooleanTrue = ontologyModel.createTypedLiteral(true);
        xsdBooleanFalse = ontologyModel.createTypedLiteral(false);

        xsdString = ontologyModel.createClass(XSD.STRING.getUri());
        xsdString.setSuperClass(owlThingClass);
        xsdStringInstance = ontologyModel.createIndividual(XSD.STRING_INSTANCE.getUri(), xsdString);

        xsdNumber = ontologyModel.createClass(XSD.NUMBER.getUri());
        xsdNumber.setSuperClass(owlThingClass);
        xsdNumberInstance = ontologyModel.createIndividual(XSD.NUMBER_INSTANCE.getUri(), xsdNumber);

        xsdDecimal = ontologyModel.createClass(XSD.DECIMAL.getUri());
        xsdDecimal.addSuperClass(xsdNumber);
        xsdDecimalInstance = ontologyModel.createIndividual(XSD.DECIMAL_INSTANCE.getUri(), xsdDecimal);

        xsdInteger = ontologyModel.createClass(XSD.INTEGER.getUri());
        // by making XSD_INTEGER a subclass of XSD_DECIMAL, it will allow type checking
        // to accept an integer at a decimal position, but not the other way around
        xsdInteger.addSuperClass(xsdDecimal);
        xsdIntegerInstance = ontologyModel.createIndividual(XSD.INTEGER_INSTANCE.getUri(), xsdInteger);

        xsdDatetime = ontologyModel.createClass(XSD.DATETIME.getUri());
        xsdDatetime.setSuperClass(owlThingClass);
        xsdDatetimeInstance = ontologyModel.createIndividual(XSD.DATETIME_INSTANCE.getUri(), xsdDatetime);

        xsdDate = ontologyModel.createClass(XSD.DATE.getUri());
        // by making XSD_DATE a subclass of XSD_DATETIME, it will allow type checking
        // to accept a date at a datetime position, but not the other way around
        xsdDate.addSuperClass(xsdDatetime);
        xsdDateInstance = ontologyModel.createIndividual(XSD.DATE_INSTANCE.getUri(), xsdDate);

        xsdDuration = ontologyModel.createClass(XSD.DURATION.getUri());
        xsdDuration.setSuperClass(owlThingClass);
        xsdDurationInstance = ontologyModel.createIndividual(XSD.DURATION_INSTANCE.getUri(), xsdDuration);
    }

    private static void setProperties(OntModel ontologyModel) {
        shaclPath = ontologyModel.createOntProperty(SHACL.PATH.getUri());
        shaclClass = ontologyModel.createOntProperty(SHACL.CLASS.getUri());
        shaclDatatype = ontologyModel.createOntProperty(SHACL.DATATYPE.getUri());
        shaclMincount = ontologyModel.createOntProperty(SHACL.MIN_COUNT.getUri());
        shaclMaxcount = ontologyModel.createOntProperty(SHACL.MAX_COUNT.getUri());
        shaclProperty = ontologyModel.createOntProperty(SHACL.PROPERTY.getUri());
        shaclProperyshape = ontologyModel.createOntProperty(SHACL.PROPERTY_SHAPE.getUri());

        rdfsSubclassOf = ontologyModel.createOntProperty(RDFS.SUBCLASS_OF.getUri());
        rdfType = ontologyModel.createOntProperty(RDF.TYPE.getUri());

        classModelProperties = List.of(rdfsSubclassOf, rdfType);
    }

    private static void setIndividuals(OntModel ontologyModel) {
        owlThingInstance = ontologyModel.createIndividual(OWL.THING_INSTANCE.getUri(), getOwlThingClass());

        jsonObject = ontologyModel.createIndividual(PLATFORM.JSON_OBJECT.getUri(), getJson());
        iri = ontologyModel.createIndividual(PLATFORM.IRI.getUri(), getOppClass());
        error = ontologyModel.createIndividual(PLATFORM.ERROR.getUri(), getOppClass());
        voidResponse = ontologyModel.createIndividual(PLATFORM.VOID.getUri(), getOppClass());

        namedGraph = ontologyModel.createIndividual(getNamedGraphClass().getURI() + "_INSTANCE", getNamedGraphClass());
        medewerkerGraph = ontologyModel.createIndividual(getNamedGraphClass().getURI() + "_MEDEWERKERGRAPH", getNamedGraphClass());
        transientGraph = ontologyModel.createIndividual(getTransientGraphClass().getURI() + "_INSTANCE", getTransientGraphClass());
        blankNode = ontologyModel.createIndividual(PLATFORM.BLANK_NODE.getUri(), getOppClass());
    }

    private static void setClasses(OntModel ontologyModel) {
        rdfsResource = ontologyModel.createClass(RDFS.RESOURCE.getUri());
        rdfsClass = ontologyModel.createClass(RDFS.CLASS.getUri());
        rdfsClass.addSuperClass(rdfsResource);

        owlClass = ontologyModel.createClass(OWL.CLASS.getUri());
        owlClass.addSuperClass(rdfsClass);

        owlThingClass = ontologyModel.createClass(OWL.THING.getUri());
        owlThingClass.setRDFType(owlClass);

        oppClass = ontologyModel.createClass(PLATFORM.CLASS.getUri());
        oppClass.addSuperClass(owlThingClass);

        json = ontologyModel.createClass(PLATFORM.JSON.getUri());
        json.addSuperClass(oppClass);

        graphClass = ontologyModel.createClass(PLATFORM.GRAPH.getUri());
        graphClass.addSuperClass(oppClass);

        namedGraphClass = ontologyModel.createClass(PLATFORM.NAMED_GRAPH.getUri());
        namedGraphClass.addSuperClass(graphClass);

        graphShape = ontologyModel.createClass(PLATFORM.GRAPH_SHAPE.getUri());
        graphShape.addSuperClass(oppClass);

        transientGraphClass = ontologyModel.createClass(PLATFORM.TRANSIENT_GRAPH.getUri());
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

    public static OntProperty getShaclPath() {
        return shaclPath;
    }

    public static OntProperty getShaclClass() {
        return shaclClass;
    }

    public static OntProperty getShaclMincount() {
        return shaclMincount;
    }

    public static OntProperty getShaclMaxcount() {
        return shaclMaxcount;
    }

    public static OntProperty getShaclDatatype() {
        return shaclDatatype;
    }

    public static OntProperty getShaclProperty() {
        return shaclProperty;
    }

    public static OntProperty getShaclProperyshape() {
        return shaclProperyshape;
    }

    public static OntProperty getRdfsSubclassOf() {
        return rdfsSubclassOf;
    }

    public static OntProperty getRdfType() {
        return rdfType;
    }

    public static List<OntProperty> getClassModelProperties() {
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
