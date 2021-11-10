package com.misset.opp.callable.builtin.operators;

public class TrimOperator extends BuiltInStringOperator {
    private TrimOperator() { }
    public static final TrimOperator INSTANCE = new TrimOperator();

    @Override
    public String getName() {
        return "TRIM";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
