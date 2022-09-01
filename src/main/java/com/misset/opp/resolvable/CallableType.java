package com.misset.opp.resolvable;

public enum CallableType {

    ACTIVITY("Activity"),
    BUILTIN_COMMAND("Builtin Command"),
    BUILTIN_OPERATOR("Builtin Operator"),
    COMPONENT("Component"),
    DEFINE_COMMAND("DEFINE COMMAND"),
    DEFINE_QUERY("DEFINE QUERY"),
    GRAPH_SHAPE_HANDLER("GraphShapeHandler"),
    LOCAL_COMMAND("Local Command"),
    LOADABLE("Loadable"),
    PROCEDURE("Procedure"),
    ONTOLOGY("Ontology"),
    STANDALONE_QUERY("StandaloneQuery"),
    UNKNOWN;

    private final String description;

    CallableType(String description) {
        this.description = description;
    }

    CallableType() {
        this.description = this.name();
    }

    public String getDescription() {
        return description;
    }
}
