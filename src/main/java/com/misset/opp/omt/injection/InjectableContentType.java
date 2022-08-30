package com.misset.opp.omt.injection;

/**
 * The Injectable (host) position returns a template type that can be used by the hosted language
 * to make specific (template) completions for the given position
 */
public enum InjectableContentType {

    /**
     * The Injectable position is dedicated for Query entries
     */
    QUERY_BLOCK(false),
    /**
     * The Injected position is dedicated for Command entries
     */
    COMMAND_BLOCK(false),

    /**
     * Position that expects a query that returns a graphShape
     */
    GRAPH_SHAPE_QUERY(true),

    /**
     * Position that expects a query that returns a boolean
     */
    BOOLEAN_QUERY(true),

    /**
     * Position that expects a query
     */
    QUERY(true),

    NONE(false);
    private final boolean isQueryStatement;

    InjectableContentType(boolean isQueryStatement) {
        this.isQueryStatement = isQueryStatement;
    }

    public boolean isQueryStatement() {
        return this.isQueryStatement;
    }

}
