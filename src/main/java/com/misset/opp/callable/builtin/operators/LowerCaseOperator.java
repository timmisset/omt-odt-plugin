package com.misset.opp.callable.builtin.operators;

public class LowerCaseOperator extends BuiltInStringOperator {
    private LowerCaseOperator() { }
    public static final LowerCaseOperator INSTANCE = new LowerCaseOperator();

    @Override
    public String getName() {
        return "LOWERCASE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
