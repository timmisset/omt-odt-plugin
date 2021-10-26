package com.misset.opp.callable.builtin.operators;

public class FindSubjectsOperator extends BuiltInOperator {
    private FindSubjectsOperator() { }
    public static final FindSubjectsOperator INSTANCE = new FindSubjectsOperator();

    @Override
    public String getName() {
        return "FIND_SUBJECTS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

}
