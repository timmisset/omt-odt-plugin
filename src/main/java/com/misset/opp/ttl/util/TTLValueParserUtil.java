package com.misset.opp.ttl.util;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

public class TTLValueParserUtil {

    public static Individual parsePrimitive(String description) {
        if ("string".equals(description)) {
            return OppModel.INSTANCE.XSD_STRING_INSTANCE;
        } else if ("boolean".equals(description)) {
            return OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE;
        } else if ("number".equals(description)) {
            return OppModel.INSTANCE.XSD_NUMBER_INSTANCE;
        } else if ("integer".equals(description)) {
            return OppModel.INSTANCE.XSD_INTEGER_INSTANCE;
        } else if ("decimal".equals(description)) {
            return OppModel.INSTANCE.XSD_DECIMAL_INSTANCE;
        } else if ("dateTime".equals(description)) {
            return OppModel.INSTANCE.XSD_DATETIME_INSTANCE;
        } else if ("date".equals(description)) {
            return OppModel.INSTANCE.XSD_DATE_INSTANCE;
        }
        return null; // unknown
    }

    public static OntClass parsePrimitiveClass(String description) {
        if ("string".equals(description)) {
            return OppModel.INSTANCE.XSD_STRING;
        } else if ("boolean".equals(description)) {
            return OppModel.INSTANCE.XSD_BOOLEAN;
        } else if ("number".equals(description)) {
            return OppModel.INSTANCE.XSD_NUMBER;
        } else if ("integer".equals(description)) {
            return OppModel.INSTANCE.XSD_INTEGER;
        } else if ("decimal".equals(description)) {
            return OppModel.INSTANCE.XSD_DECIMAL;
        } else if ("dateTime".equals(description)) {
            return OppModel.INSTANCE.XSD_DATETIME;
        } else if ("date".equals(description)) {
            return OppModel.INSTANCE.XSD_DATE;
        }
        return null; // unknown
    }
}
