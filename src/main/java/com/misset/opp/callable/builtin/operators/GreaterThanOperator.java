package com.misset.opp.callable.builtin.operators;

public class GreaterThanOperator extends BuiltInOperator {
    private GreaterThanOperator() { }
    public static final GreaterThanOperator INSTANCE = new GreaterThanOperator();

    @Override
    public String getName() {
        return "GREATER_THAN";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

}
