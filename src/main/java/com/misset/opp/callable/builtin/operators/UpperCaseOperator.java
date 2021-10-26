package com.misset.opp.callable.builtin.operators;

public class UpperCaseOperator extends BuiltInOperator {
    private UpperCaseOperator() { }
    public static final UpperCaseOperator INSTANCE = new UpperCaseOperator();

    @Override
    public String getName() {
        return "UPPERCASE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
