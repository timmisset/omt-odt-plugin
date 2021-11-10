package com.misset.opp.callable.builtin.operators;

public class LessThanEqualsOperator extends BuiltInBooleanOperator {
    private LessThanEqualsOperator() { }
    public static final LessThanEqualsOperator INSTANCE = new LessThanEqualsOperator();

    @Override
    public String getName() {
        return "LESS_THAN_EQUALS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

}
