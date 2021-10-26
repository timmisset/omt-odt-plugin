package com.misset.opp.callable.builtin.operators;

public class MapOperator extends BuiltInOperator {
    private MapOperator() { }
    public static final MapOperator INSTANCE = new MapOperator();

    @Override
    public String getName() {
        return "MAP";
    }

}
