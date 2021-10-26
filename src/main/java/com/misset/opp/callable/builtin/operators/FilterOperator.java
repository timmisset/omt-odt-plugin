package com.misset.opp.callable.builtin.operators;

public class FilterOperator extends BuiltInOperator {
    private FilterOperator() { }
    public static final FilterOperator INSTANCE = new FilterOperator();

    @Override
    public String getName() {
        return "FILTER";
    }
}
