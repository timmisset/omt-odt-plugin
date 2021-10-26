package com.misset.opp.callable.builtin.operators;

public class ReplaceOperator extends BuiltInOperator {
    private ReplaceOperator() { }
    public static final ReplaceOperator INSTANCE = new ReplaceOperator();

    @Override
    public String getName() {
        return "REPLACE";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }
}
