package com.misset.opp.callable.builtin.operators;

public class OrOperator extends BuiltInOperator {
    private OrOperator() { }
    public static final OrOperator INSTANCE = new OrOperator();

    @Override
    public String getName() {
        return "OR";
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
