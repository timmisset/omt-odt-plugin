package com.misset.opp.callable.builtin.operators;

public class ExistsOperator extends BuiltInBooleanOperator {
    private ExistsOperator() {
    }

    public static final ExistsOperator INSTANCE = new ExistsOperator();

    @Override
    public String getName() {
        return "EXISTS";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
