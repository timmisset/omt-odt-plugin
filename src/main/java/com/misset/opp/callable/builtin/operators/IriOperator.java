package com.misset.opp.callable.builtin.operators;

public class IriOperator extends BuiltInOperator {
    private IriOperator() { }
    public static final IriOperator INSTANCE = new IriOperator();

    @Override
    public String getName() {
        return "IRI";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
