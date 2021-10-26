package com.misset.opp.callable.builtin.operators;

public class MinOperator extends BuiltInOperator {
    private MinOperator() { }
    public static final MinOperator INSTANCE = new MinOperator();

    @Override
    public String getName() {
        return "MIN";
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
