package com.misset.opp.callable.builtin.operators;

public class SplitOperator extends BuiltInOperator {
    private SplitOperator() { }
    public static final SplitOperator INSTANCE = new SplitOperator();

    @Override
    public String getName() {
        return "SPLIT";
    }

}
