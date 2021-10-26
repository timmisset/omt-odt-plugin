package com.misset.opp.callable.builtin.operators;

public class DurationOperator extends BuiltInOperator {
    private DurationOperator() { }
    public static final DurationOperator INSTANCE = new DurationOperator();

    @Override
    public String getName() {
        return "DURATION";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }
}
