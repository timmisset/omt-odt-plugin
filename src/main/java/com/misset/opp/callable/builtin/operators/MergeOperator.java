package com.misset.opp.callable.builtin.operators;

public class MergeOperator extends BuiltInOperator {
    private MergeOperator() { }
    public static final MergeOperator INSTANCE = new MergeOperator();

    @Override
    public String getName() {
        return "MERGE";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

}
