package com.misset.opp.callable.builtin.operators;

public class CatchOperator extends BuiltInOperator {
    private CatchOperator() { }
    public static final CatchOperator INSTANCE = new CatchOperator();

    @Override
    public String getName() {
        return "CATCH";
    }

}
