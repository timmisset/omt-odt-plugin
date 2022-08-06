package com.misset.opp.ttl.model.constants;

public enum XSD {

    BOOLEAN("boolean"),
    BOOLEAN_INSTANCE("boolean_INSTANCE"),
    NUMBER("number"),
    NUMBER_INSTANCE("number_INSTANCE"),

    DECIMAL("decimal"),
    DECIMAL_INSTANCE("decimal_INSTANCE"),

    DATE("date"),
    DATE_INSTANCE("date_INSTANCE"),

    DATETIME("dateTime"),
    DATETIME_INSTANCE("dateTime_INSTANCE"),

    DURATION("duration"),
    DURATION_INSTANCE("duration_INSTANCE"),

    INTEGER("integer"),
    INTEGER_INSTANCE("integer_INSTANCE"),

    STRING("string"),
    STRING_INSTANCE("string_INSTANCE");

    public static final String NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

    private final String localName;

    XSD(String localName) {
        this.localName = localName;
    }

    public String getUri() {
        return NAMESPACE + localName;
    }

}
