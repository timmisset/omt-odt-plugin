package com.misset.opp.callable.builtin.operators;

public class SkipOperator extends BuiltInOperator {
    private SkipOperator() { }
    public static final SkipOperator INSTANCE = new SkipOperator();

    @Override
    public String getName() {
        return "SKIP";
    }

}
