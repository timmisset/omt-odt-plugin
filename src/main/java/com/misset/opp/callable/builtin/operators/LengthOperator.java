package com.misset.opp.callable.builtin.operators;

public class LengthOperator extends BuiltInOperator {
    private LengthOperator() { }
    public static final LengthOperator INSTANCE = new LengthOperator();

    @Override
    public String getName() {
        return "LENGTH";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
