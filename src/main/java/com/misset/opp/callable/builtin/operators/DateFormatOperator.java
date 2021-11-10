package com.misset.opp.callable.builtin.operators;

public class DateFormatOperator extends BuiltInStringOperator {
    private DateFormatOperator() { }
    public static final DateFormatOperator INSTANCE = new DateFormatOperator();

    @Override
    public String getName() {
        // that's correct, the classname and function call in this ODT operator are different
        return "FORMAT_DATE";
    }

}
