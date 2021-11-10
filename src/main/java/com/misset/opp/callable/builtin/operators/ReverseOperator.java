package com.misset.opp.callable.builtin.operators;

public class ReverseOperator extends BuiltInCollectionOperator {
    private ReverseOperator() { }
    public static final ReverseOperator INSTANCE = new ReverseOperator();

    @Override
    public String getName() {
        return "REPLACE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
