package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.Builtin;

import java.util.List;

public abstract class BuiltInOperator extends Builtin {

    protected static final List<String> IGNORE_CASE_FLAG = List.of("ignoreCase");

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    public String getCallId() { return getName(); }
}
