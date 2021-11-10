package com.misset.opp.callable.builtin.operators;

public class NotOperator extends BuiltInBooleanOperator {
    private NotOperator() { }
    public static final NotOperator INSTANCE = new NotOperator();

    @Override
    public String getName() {
        return "NOT";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
