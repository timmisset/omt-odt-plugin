package com.misset.opp.callable.builtin.operators;

public class SkipOperator extends BuiltInCollectionOperator {
    private SkipOperator() { }
    public static final SkipOperator INSTANCE = new SkipOperator();

    @Override
    public String getName() {
        return "SKIP";
    }

}
