package com.misset.opp.callable.builtin.operators;

public class AndOperator extends BuiltInBooleanOperator {
    private AndOperator() { }
    public static final AndOperator INSTANCE = new AndOperator();

    @Override
    public String getName() {
        return "AND";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

}
