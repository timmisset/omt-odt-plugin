package com.misset.opp.odt.builtin.operators;

public class ConcatOperator extends BuiltInStringOperator {
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
