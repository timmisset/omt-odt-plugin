package com.misset.opp.callable.builtin.operators;

public class TraverseOperator extends BuiltInOperator {
    private TraverseOperator() { }
    public static final TraverseOperator INSTANCE = new TraverseOperator();

    @Override
    public String getName() {
        return "TRAVERSE";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }
}
