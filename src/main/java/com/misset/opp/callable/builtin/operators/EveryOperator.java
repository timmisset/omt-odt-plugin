package com.misset.opp.callable.builtin.operators;

public class EveryOperator extends BuiltInBooleanOperator {
    private EveryOperator() { }
    public static final EveryOperator INSTANCE = new EveryOperator();

    @Override
    public String getName() {
        return "EVERY";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

}
