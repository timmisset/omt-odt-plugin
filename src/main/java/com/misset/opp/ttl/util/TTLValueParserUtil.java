package com.misset.opp.ttl.util;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

public class TTLValueParserUtil {

    private TTLValueParserUtil() {
        // empty constructor
    }

    public static Individual parsePrimitive(String description) {
        if ("string".equals(description)) {
            return OppModelConstants.getXsdStringInstance();
        } else if ("boolean".equals(description)) {
            return OppModelConstants.getXsdBooleanInstance();
        } else if ("number".equals(description)) {
            return OppModelConstants.getXsdNumberInstance();
        } else if ("integer".equals(description)) {
            return OppModelConstants.getXsdIntegerInstance();
        } else if ("decimal".equals(description)) {
            return OppModelConstants.getXsdDecimalInstance();
        } else if ("dateTime".equals(description)) {
            return OppModelConstants.getXsdDatetimeInstance();
        } else if ("date".equals(description)) {
            return OppModelConstants.getXsdDateInstance();
        } else if ("void".equals(description)) {
            return OppModelConstants.getVoidResponse();
        } else if ("json".equals(description)) {
            return OppModelConstants.getJsonObject();
        }
        return null; // unknown
    }

    public static OntClass parsePrimitiveClass(String description) {
        if ("string".equals(description)) {
            return OppModelConstants.getXsdString();
        } else if ("boolean".equals(description)) {
            return OppModelConstants.getXsdBoolean();
        } else if ("number".equals(description)) {
            return OppModelConstants.getXsdNumber();
        } else if ("integer".equals(description)) {
            return OppModelConstants.getXsdInteger();
        } else if ("decimal".equals(description)) {
            return OppModelConstants.getXsdDecimal();
        } else if ("dateTime".equals(description)) {
            return OppModelConstants.getXsdDatetime();
        } else if ("date".equals(description)) {
            return OppModelConstants.getXsdDate();
        } else if ("json".equals(description)) {
            return OppModelConstants.getJson();
        }
        return null; // unknown
    }
}
