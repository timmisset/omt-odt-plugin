package com.misset.opp.callable.builtin.operators;

public class TruncOperator extends BuiltInCollectionOperator {
    private TruncOperator() { }
    public static final TruncOperator INSTANCE = new TruncOperator();

    @Override
    public String getName() {
        return "TRUNC";
    }

}
