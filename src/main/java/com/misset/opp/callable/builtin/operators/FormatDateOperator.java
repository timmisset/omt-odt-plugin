package com.misset.opp.callable.builtin.operators;

public class FormatDateOperator extends BuiltInOperator {
    private FormatDateOperator() { }
    public static final FormatDateOperator INSTANCE = new FormatDateOperator();

    @Override
    public String getName() {
        return "FORMAT_DATE";
    }
}
