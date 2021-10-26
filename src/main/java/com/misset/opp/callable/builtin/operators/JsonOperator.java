package com.misset.opp.callable.builtin.operators;

public class JsonOperator extends BuiltInOperator {
    private JsonOperator() { }
    public static final JsonOperator INSTANCE = new JsonOperator();

    @Override
    public String getName() {
        return "JSON";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
