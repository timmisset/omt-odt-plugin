package com.misset.opp.callable.builtin.operators;

public class SumOperator extends BuiltInOperator {
    private SumOperator() { }
    public static final SumOperator INSTANCE = new SumOperator();

    @Override
    public String getName() {
        return "SUM";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
