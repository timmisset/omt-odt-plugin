package com.misset.opp.callable.builtin.operators;

public class AddOperator extends BuiltInOperator {
    private AddOperator() { }
    public static final AddOperator INSTANCE = new AddOperator();

    @Override
    public String getName() {
        return "ADD";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

}
