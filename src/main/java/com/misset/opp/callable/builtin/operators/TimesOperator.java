package com.misset.opp.callable.builtin.operators;

public class TimesOperator extends BuiltinMathOperator {
    private TimesOperator() { }
    public static final TimesOperator INSTANCE = new TimesOperator();

    @Override
    public String getName() {
        return "TIMES";
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
