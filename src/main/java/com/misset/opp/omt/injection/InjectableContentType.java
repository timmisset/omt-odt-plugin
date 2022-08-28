package com.misset.opp.omt.injection;

/**
 * The Injectable (host) position returns a template type that can be used by the hosted language
 * to make specific (template) completions for the given position
 */
public enum InjectableContentType {

    /**
     * The Injectable position is dedicated for Query entries
     */
    QueryBlock(false),
    /**
     * The Injected position is dedicated for Command entries
     */
    CommandBlock(false),

    /**
     * Position that expects a query that returns a graphShape
     */
    GraphShapeQuery(true),

    /**
     * Position that expects a query that returns a boolean
     */
    BooleanQuery(true),

    /**
     * Position that expects a query
     */
    Query(true),

    None(false);
    private final boolean isQueryStatement;

    InjectableContentType(boolean isQueryStatement) {
        this.isQueryStatement = isQueryStatement;
    }

    public boolean isQueryStatement() {
        return this.isQueryStatement;
    }

}
