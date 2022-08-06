package com.misset.opp.ttl.model.constants;

/**
 * Contains some fake IRIs that are not actually used in the language but required by the plugin
 * to make sense of certain casts.
 */
public enum PLATFORM {

    // Fictional class to instantiate specific individuals IRI, ERROR and VOID
    CLASS("CLASS"),
    GRAPH("Graph"),
    GRAPH_SHAPE("GraphShape"),

    // Used when casting from/to Json data
    JSON_OBJECT("JSON_OBJECT"),
    // class representation for a JSON_OBJECT
    JSON("JSON"),
    NAMED_GRAPH("NamedGraph"),
    // Used when casting from/to IRI
    IRI("IRI"),
    // Used to indicate that the outcome a method is an error and should be caught via the IF_ERROR operator
    ERROR("ERROR"),
    // Used to indicate that a method has no response
    TRANSIENT_GRAPH("TRANSIENT_GRAPH"),
    VOID("VOID"),
    ;

    public static final String NAMESPACE = "http://ontologie.politie.nl/def/platform#";

    private final String localName;

    PLATFORM(String localName) {
        this.localName = localName;
    }

    public String getUri() {
        return NAMESPACE + localName;
    }

}
