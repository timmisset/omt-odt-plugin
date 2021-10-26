package com.misset.opp.callable.builtin.operators;

import java.util.List;

public class DistinctOperator extends BuiltInOperator {
    private DistinctOperator() { }
    public static final DistinctOperator INSTANCE = new DistinctOperator();

    @Override
    public String getName() {
        return "DISTINCT";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public List<String> getFlags() {
        return IGNORE_CASE_FLAG;
    }
}
