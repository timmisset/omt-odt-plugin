package com.misset.opp.callable.builtin.operators;

public class LastOperator extends BuiltInOperator {
    private LastOperator() { }
    public static final LastOperator INSTANCE = new LastOperator();

    @Override
    public String getName() {
        return "LAST";
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
