package com.misset.opp.callable.builtin.operators;

public class SubstringOperator extends BuiltInStringOperator {
    private SubstringOperator() { }
    public static final SubstringOperator INSTANCE = new SubstringOperator();

    @Override
    public String getName() {
        return "SUBSTRING";
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
