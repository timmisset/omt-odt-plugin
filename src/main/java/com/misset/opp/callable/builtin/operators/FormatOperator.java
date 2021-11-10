package com.misset.opp.callable.builtin.operators;

public class FormatOperator extends BuiltInStringOperator {
    private FormatOperator() { }
    public static final FormatOperator INSTANCE = new FormatOperator();

    @Override
    public String getName() {
        return "FORMAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }
}
