package com.misset.opp.callable.builtin.operators;

public class PickOperator extends BuiltInOperator {
    private PickOperator() { }
    public static final PickOperator INSTANCE = new PickOperator();

    @Override
    public String getName() {
        return "PICK";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }
}
