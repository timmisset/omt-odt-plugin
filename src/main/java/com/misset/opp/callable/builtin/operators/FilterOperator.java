package com.misset.opp.callable.builtin.operators;

public class FilterOperator extends BuiltInCollectionOperator {
    // todo:
    // implement a filter mechanism just like for the filter that is part of the queryStep

    private FilterOperator() { }
    public static final FilterOperator INSTANCE = new FilterOperator();

    @Override
    public String getName() {
        return "FILTER";
    }

}
