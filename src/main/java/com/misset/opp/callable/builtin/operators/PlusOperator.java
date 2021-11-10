package com.misset.opp.callable.builtin.operators;

public class PlusOperator extends BuiltinMathOperator {
    private PlusOperator() { }
    public static final PlusOperator INSTANCE = new PlusOperator();

    @Override
    public String getName() {
        return "PLUS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }
}
