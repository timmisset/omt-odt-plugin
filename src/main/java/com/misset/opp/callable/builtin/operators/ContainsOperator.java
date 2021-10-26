package com.misset.opp.callable.builtin.operators;

import java.util.List;

public class ContainsOperator extends BuiltInOperator {
    private ContainsOperator() { }
    public static final ContainsOperator INSTANCE = new ContainsOperator();

    @Override
    public String getName() {
        return "CONTAINS";
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
