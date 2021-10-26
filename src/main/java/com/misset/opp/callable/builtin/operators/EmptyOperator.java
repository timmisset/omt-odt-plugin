package com.misset.opp.callable.builtin.operators;

public class EmptyOperator extends BuiltInOperator {
    private EmptyOperator() { }
    public static final EmptyOperator INSTANCE = new EmptyOperator();

    @Override
    public String getName() {
        return "EMPTY";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
