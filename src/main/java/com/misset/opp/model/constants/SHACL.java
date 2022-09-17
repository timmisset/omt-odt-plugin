package com.misset.opp.model.constants;

public enum SHACL {

    CLASS("class"),
    DATATYPE("datatype"),
    MIN_COUNT("minCount"),
    MAX_COUNT("maxCount"),
    PATH("path"),
    PROPERTY("property"),
    PROPERTY_SHAPE("PropertyShape");
    public static final String NAMESPACE = "http://www.w3.org/ns/shacl#";

    private final String localName;

    SHACL(String localName) {
        this.localName = localName;
    }

    public String getUri() {
        return NAMESPACE + localName;
    }

}
