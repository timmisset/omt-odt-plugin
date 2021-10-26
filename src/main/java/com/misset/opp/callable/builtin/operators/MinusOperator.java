package com.misset.opp.callable.builtin.operators;

public class MinusOperator extends BuiltInOperator {
    private MinusOperator() { }
    public static final MinusOperator INSTANCE = new MinusOperator();

    @Override
    public String getName() {
        return "MINUS";
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
