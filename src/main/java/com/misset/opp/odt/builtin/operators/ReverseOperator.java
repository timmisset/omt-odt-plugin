package com.misset.opp.odt.builtin.operators;

public class ReverseOperator extends BuiltInCollectionOperator {
    private ReverseOperator() { }
    public static final ReverseOperator INSTANCE = new ReverseOperator();

    @Override
    public String getName() {
        return "REVERSE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
