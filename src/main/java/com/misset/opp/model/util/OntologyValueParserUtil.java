package com.misset.opp.model.util;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

public class OntologyValueParserUtil {

    private OntologyValueParserUtil() {
        // empty constructor
    }

    public static Individual parsePrimitive(String description) {
        if ("string".equals(description)) {
            return OntologyModelConstants.getXsdStringInstance();
        } else if ("boolean".equals(description)) {
            return OntologyModelConstants.getXsdBooleanInstance();
        } else if ("number".equals(description)) {
            return OntologyModelConstants.getXsdNumberInstance();
        } else if ("integer".equals(description)) {
            return OntologyModelConstants.getXsdIntegerInstance();
        } else if ("decimal".equals(description)) {
            return OntologyModelConstants.getXsdDecimalInstance();
        } else if ("dateTime".equals(description)) {
            return OntologyModelConstants.getXsdDatetimeInstance();
        } else if ("date".equals(description)) {
            return OntologyModelConstants.getXsdDateInstance();
        } else if ("void".equals(description)) {
            return OntologyModelConstants.getVoidResponse();
        } else if ("json".equals(description)) {
            return OntologyModelConstants.getJsonObject();
        }
        return null; // unknown
    }

    public static OntClass parsePrimitiveClass(String description) {
        if ("string".equals(description)) {
            return OntologyModelConstants.getXsdString();
        } else if ("boolean".equals(description)) {
            return OntologyModelConstants.getXsdBoolean();
        } else if ("number".equals(description)) {
            return OntologyModelConstants.getXsdNumber();
        } else if ("integer".equals(description)) {
            return OntologyModelConstants.getXsdInteger();
        } else if ("decimal".equals(description)) {
            return OntologyModelConstants.getXsdDecimal();
        } else if ("dateTime".equals(description)) {
            return OntologyModelConstants.getXsdDatetime();
        } else if ("date".equals(description)) {
            return OntologyModelConstants.getXsdDate();
        } else if ("json".equals(description)) {
            return OntologyModelConstants.getJson();
        }
        return null; // unknown
    }
}
