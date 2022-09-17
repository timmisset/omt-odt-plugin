package com.misset.opp.model.constants;

public enum RDFS {

    SUBCLASS_OF("subClassOf"),
    RESOURCE("Resource"),
    CLASS("Class");

    public static final String NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";

    private final String localName;

    RDFS(String localName) {
        this.localName = localName;
    }

    public String getUri() {
        return NAMESPACE + localName;
    }

}
