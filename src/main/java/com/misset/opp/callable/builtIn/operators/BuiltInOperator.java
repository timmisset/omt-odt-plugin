package com.misset.opp.callable.builtIn.operators;

import com.misset.opp.callable.Callable;

public abstract class BuiltInOperator implements Callable {

    @Override
    public boolean isCommand() {
        return false;
    }

    public String getCallId() { return getName(); }
}
