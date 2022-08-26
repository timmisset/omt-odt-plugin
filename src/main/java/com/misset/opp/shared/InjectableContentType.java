package com.misset.opp.shared;

/**
 * The Injectable (host) position returns a template type that can be used by the hosted language
 * to make specific (template) completions for the given position
 */
public enum InjectableContentType {

    /**
     * The Injectable position is dedicated for Query entries
     */
    QueryBlock(),
    /**
     * The Injected position is dedicated for Command entries
     */
    CommandBlock(),

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

    None();
    private final boolean isQueryStatement;

    InjectableContentType(boolean isQueryStatement) {
        this.isQueryStatement = isQueryStatement;
    }

    InjectableContentType() {
        this.isQueryStatement = false;
    }

    public boolean isQueryStatement() {
        return this.isQueryStatement;
    }

}
