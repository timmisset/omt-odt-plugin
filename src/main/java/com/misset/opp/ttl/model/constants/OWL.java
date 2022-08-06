package com.misset.opp.ttl.model.constants;

public enum OWL {

    CLASS("Class"),
    THING("Thing"),
    THING_INSTANCE("Thing_INSTANCE"),
    ONTOLOGY("Ontology"),
    IMPORTS("imports");

    public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";

    private final String localName;

    OWL(String localName) {
        this.localName = localName;
    }

    public String getUri() {
        return NAMESPACE + localName;
    }

}
