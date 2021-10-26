package com.misset.opp.callable.builtin.operators;

public class IfEmptyOperator extends BuiltInOperator {
    private IfEmptyOperator() { }
    public static final IfEmptyOperator INSTANCE = new IfEmptyOperator();

    @Override
    public String getName() {
        return "IF_EMPTY";
    }

}
