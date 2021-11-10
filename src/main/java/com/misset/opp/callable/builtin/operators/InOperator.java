package com.misset.opp.callable.builtin.operators;

import java.util.List;

public class InOperator extends BuiltInBooleanOperator {
    private InOperator() { }
    public static final InOperator INSTANCE = new InOperator();

    @Override
    public String getName() {
        return "IN";
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
