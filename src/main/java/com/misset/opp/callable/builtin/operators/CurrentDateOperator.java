package com.misset.opp.callable.builtin.operators;

public class CurrentDateOperator extends BuiltInOperator {
    private CurrentDateOperator() { }
    public static final CurrentDateOperator INSTANCE = new CurrentDateOperator();

    @Override
    public String getName() {
        return "CURRENT_DATE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
