package com.misset.opp.ttl.model.constants;

public enum RDF {

    TYPE("type");

    public static final String NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    private final String localName;

    RDF(String localName) {
        this.localName = localName;
    }

    public String getUri() {
        return NAMESPACE + localName;
    }

}
