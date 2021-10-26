package com.misset.opp.callable.builtin.operators;

public class SomeOperator extends BuiltInOperator {
    private SomeOperator() { }
    public static final SomeOperator INSTANCE = new SomeOperator();

    @Override
    public String getName() {
        return "SOME";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
