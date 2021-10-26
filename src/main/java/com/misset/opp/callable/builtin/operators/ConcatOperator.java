package com.misset.opp.callable.builtin.operators;

public class ConcatOperator extends BuiltInOperator {
    private ConcatOperator() { }
    public static final ConcatOperator INSTANCE = new ConcatOperator();

    @Override
    public String getName() {
        return "CONCAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }
}
