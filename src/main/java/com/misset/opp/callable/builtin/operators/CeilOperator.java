package com.misset.opp.callable.builtin.operators;

public class CeilOperator extends BuiltInOperator {
    private CeilOperator() { }
    public static final CeilOperator INSTANCE = new CeilOperator();

    @Override
    public String getName() {
        return "CEIL";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
