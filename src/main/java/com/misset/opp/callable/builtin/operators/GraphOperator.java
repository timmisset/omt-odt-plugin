package com.misset.opp.callable.builtin.operators;

public class GraphOperator extends BuiltInOperator {
    private GraphOperator() { }
    public static final GraphOperator INSTANCE = new GraphOperator();

    @Override
    public String getName() {
        return "GRAPH";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
