package com.misset.opp.callable.builtin.operators;

public class IndexOfOperator extends BuiltInIntegerOperator {
    private IndexOfOperator() { }
    public static final IndexOfOperator INSTANCE = new IndexOfOperator();

    @Override
    public String getName() {
        return "INDEX_OF";
    }

}
