package com.misset.opp.callable.builtin.operators;

public class OrderByOperator extends BuiltInCollectionOperator {
    private OrderByOperator() { }
    public static final OrderByOperator INSTANCE = new OrderByOperator();

    @Override
    public String getName() {
        return "ORDER_BY";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
