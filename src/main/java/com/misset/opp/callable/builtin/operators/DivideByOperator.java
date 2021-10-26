package com.misset.opp.callable.builtin.operators;

public class DivideByOperator extends BuiltInOperator {
    private DivideByOperator() { }
    public static final DivideByOperator INSTANCE = new DivideByOperator();

    @Override
    public String getName() {
        return "DIVIDE_BY";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }
}
