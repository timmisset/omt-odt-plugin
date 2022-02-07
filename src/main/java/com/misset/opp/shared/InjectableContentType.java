package com.misset.opp.shared;

/**
 * The Injectable (host) position returns a template type that can be used by the hosted language
 * to make specific (template) completions for the given position
 */
public enum InjectableContentType {

    /**
     * The Injectable position is dedicated for Query entries
     */
    Query,
    /**
     * The Injected position is dedicated for Command entries
     */
    Command,

    /**
     * Position that expects a query that returns a graphShape
     */
    GraphShapeQuery,

    None

}
