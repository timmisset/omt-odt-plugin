package com.misset.opp.callable.builtin.operators;

public class LogOperator extends BuiltInCollectionOperator {
    private LogOperator() { }
    public static final LogOperator INSTANCE = new LogOperator();

    @Override
    public String getName() {
        return "LOG";
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
