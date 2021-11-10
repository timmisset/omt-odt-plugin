package com.misset.opp.callable.builtin.operators;

public class CountOperator extends BuiltInIntegerOperator {
    private CountOperator() { }
    public static final CountOperator INSTANCE = new CountOperator();

    @Override
    public String getName() {
        return "COUNT";
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
