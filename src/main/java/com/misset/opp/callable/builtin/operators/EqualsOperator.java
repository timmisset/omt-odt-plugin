package com.misset.opp.callable.builtin.operators;

import java.util.List;

public class EqualsOperator extends BuiltInOperator {
    private EqualsOperator() { }
    public static final EqualsOperator INSTANCE = new EqualsOperator();

    @Override
    public String getName() {
        return "EQUALS";
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
