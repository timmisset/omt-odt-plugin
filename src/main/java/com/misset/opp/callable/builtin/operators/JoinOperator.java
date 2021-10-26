package com.misset.opp.callable.builtin.operators;

public class JoinOperator extends BuiltInOperator {
    private JoinOperator() { }
    public static final JoinOperator INSTANCE = new JoinOperator();

    @Override
    public String getName() {
        return "JOIN";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }
}
