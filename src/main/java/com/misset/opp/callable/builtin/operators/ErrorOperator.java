package com.misset.opp.callable.builtin.operators;

public class ErrorOperator extends BuiltInOperator {
    private ErrorOperator() { }
    public static final ErrorOperator INSTANCE = new ErrorOperator();

    @Override
    public String getName() {
        return "ERROR";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }
}
