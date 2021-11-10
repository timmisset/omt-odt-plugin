package com.misset.opp.callable.builtin.operators;

public class TakeOperator extends BuiltInCollectionOperator {
    private TakeOperator() { }
    public static final TakeOperator INSTANCE = new TakeOperator();

    @Override
    public String getName() {
        return "TAKE";
    }

}
