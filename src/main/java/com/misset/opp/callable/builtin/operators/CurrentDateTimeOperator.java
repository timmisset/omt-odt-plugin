package com.misset.opp.callable.builtin.operators;

public class CurrentDateTimeOperator extends BuiltInOperator {
    private CurrentDateTimeOperator() { }
    public static final CurrentDateTimeOperator INSTANCE = new CurrentDateTimeOperator();

    @Override
    public String getName() {
        return "CURRENT_DATETIME";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
