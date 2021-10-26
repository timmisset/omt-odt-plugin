package com.misset.opp.callable.builtin.operators;

public class TypeOperator extends BuiltInOperator {
    private TypeOperator() { }
    public static final TypeOperator INSTANCE = new TypeOperator();

    @Override
    public String getName() {
        return "TYPE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
