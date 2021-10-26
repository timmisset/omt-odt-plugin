package com.misset.opp.callable.builtin.operators;

import java.util.List;

public class HasOperator extends BuiltInOperator {
    private HasOperator() { }
    public static final HasOperator INSTANCE = new HasOperator();

    @Override
    public String getName() {
        return "HAS";
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
