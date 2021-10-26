package com.misset.opp.callable.builtin.operators;

public class RepeatOperator extends BuiltInOperator {
    private RepeatOperator() { }
    public static final RepeatOperator INSTANCE = new RepeatOperator();

    @Override
    public String getName() {
        return "REPEAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }
}
