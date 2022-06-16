package com.misset.opp.ttl.util;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

public class TTLValueParserUtil {

    public static Individual parsePrimitive(String description) {
        if ("string".equals(description)) {
            return OppModelConstants.XSD_STRING_INSTANCE;
        } else if ("boolean".equals(description)) {
            return OppModelConstants.XSD_BOOLEAN_INSTANCE;
        } else if ("number".equals(description)) {
            return OppModelConstants.XSD_NUMBER_INSTANCE;
        } else if ("integer".equals(description)) {
            return OppModelConstants.XSD_INTEGER_INSTANCE;
        } else if ("decimal".equals(description)) {
            return OppModelConstants.XSD_DECIMAL_INSTANCE;
        } else if ("dateTime".equals(description)) {
            return OppModelConstants.XSD_DATETIME_INSTANCE;
        } else if ("date".equals(description)) {
            return OppModelConstants.XSD_DATE_INSTANCE;
        } else if ("void".equals(description)) {
            return OppModelConstants.VOID;
        } else if ("json".equals(description)) {
            return OppModelConstants.JSON_OBJECT;
        }
        return null; // unknown
    }

    public static OntClass parsePrimitiveClass(String description) {
        if ("string".equals(description)) {
            return OppModelConstants.XSD_STRING;
        } else if ("boolean".equals(description)) {
            return OppModelConstants.XSD_BOOLEAN;
        } else if ("number".equals(description)) {
            return OppModelConstants.XSD_NUMBER;
        } else if ("integer".equals(description)) {
            return OppModelConstants.XSD_INTEGER;
        } else if ("decimal".equals(description)) {
            return OppModelConstants.XSD_DECIMAL;
        } else if ("dateTime".equals(description)) {
            return OppModelConstants.XSD_DATETIME;
        } else if ("date".equals(description)) {
            return OppModelConstants.XSD_DATE;
        } else if ("json".equals(description)) {
            return OppModelConstants.JSON;
        }
        return null; // unknown
    }
}
