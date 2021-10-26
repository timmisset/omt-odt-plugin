package com.misset.opp.callable.builtin.operators;

import java.util.List;

public class InOperator extends BuiltInOperator {
    private InOperator() { }
    public static final InOperator INSTANCE = new InOperator();

    @Override
    public String getName() {
        return "IF_EMPTY";
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
