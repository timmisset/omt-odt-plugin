package com.misset.opp.callable.builtin.operators;

public class CastOperator extends BuiltInOperator {
    private CastOperator() { }
    public static final CastOperator INSTANCE = new CastOperator();

    @Override
    public String getName() {
        return "CAST";
    }

}
