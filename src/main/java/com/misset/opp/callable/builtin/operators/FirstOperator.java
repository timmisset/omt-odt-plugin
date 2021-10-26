package com.misset.opp.callable.builtin.operators;

public class FirstOperator extends BuiltInOperator {
    private FirstOperator() { }
    public static final FirstOperator INSTANCE = new FirstOperator();

    @Override
    public String getName() {
        return "FIRST";
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
