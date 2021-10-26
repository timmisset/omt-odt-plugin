package com.misset.opp.callable.builtin.operators;

public class GreaterThanEqualsOperator extends BuiltInOperator {
    private GreaterThanEqualsOperator() { }
    public static final GreaterThanEqualsOperator INSTANCE = new GreaterThanEqualsOperator();

    @Override
    public String getName() {
        return "GREATER_THAN_EQUALS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

}
