package com.misset.opp.callable.builtin.operators;

public class IndexOfOperator extends BuiltInOperator {
    private IndexOfOperator() { }
    public static final IndexOfOperator INSTANCE = new IndexOfOperator();

    @Override
    public String getName() {
        return "INDEX_OF";
    }
}
