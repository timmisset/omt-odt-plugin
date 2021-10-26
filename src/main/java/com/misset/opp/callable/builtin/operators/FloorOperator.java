package com.misset.opp.callable.builtin.operators;

public class FloorOperator extends BuiltInOperator {
    private FloorOperator() { }
    public static final FloorOperator INSTANCE = new FloorOperator();

    @Override
    public String getName() {
        return "FLOOR";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
