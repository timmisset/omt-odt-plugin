package com.misset.opp.callable.builtin.operators;

public class RoundOperator extends BuiltInOperator {
    private RoundOperator() { }
    public static final RoundOperator INSTANCE = new RoundOperator();

    @Override
    public String getName() {
        return "ROUND";
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
