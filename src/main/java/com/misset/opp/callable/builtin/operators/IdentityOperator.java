package com.misset.opp.callable.builtin.operators;

public class IdentityOperator extends BuiltInOperator {
    private IdentityOperator() { }
    public static final IdentityOperator INSTANCE = new IdentityOperator();

    @Override
    public String getName() {
        return "IDENTITY";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
