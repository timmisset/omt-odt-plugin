package com.misset.opp.callable.builtin.operators;

public class ElementsOperator extends BuiltInOperator {
    private ElementsOperator() { }
    public static final ElementsOperator INSTANCE = new ElementsOperator();

    @Override
    public String getName() {
        return "ELEMENTS";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

}
