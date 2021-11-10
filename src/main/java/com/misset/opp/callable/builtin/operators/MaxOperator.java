package com.misset.opp.callable.builtin.operators;

public class MaxOperator extends BuiltInCollectionOperator {
    private MaxOperator() { }
    public static final MaxOperator INSTANCE = new MaxOperator();

    @Override
    public String getName() {
        return "MAX";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
