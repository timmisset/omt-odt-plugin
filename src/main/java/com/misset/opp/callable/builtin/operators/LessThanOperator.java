package com.misset.opp.callable.builtin.operators;

public class LessThanOperator extends BuiltInOperator {
    private LessThanOperator() { }
    public static final LessThanOperator INSTANCE = new LessThanOperator();

    @Override
    public String getName() {
        return "LESS_THAN";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

}
