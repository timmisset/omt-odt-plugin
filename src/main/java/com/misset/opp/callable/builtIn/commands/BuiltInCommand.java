package com.misset.opp.callable.builtIn.commands;

import com.misset.opp.callable.Callable;

import java.util.Collections;
import java.util.List;

public abstract class BuiltInCommand implements Callable {
    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public String getDescription(String context) {
        // todo: load from the markdown
        return "";
    }

    @Override
    public int maxNumberOfArguments() {
        // by default, the max number of arguments equals the min
        return minNumberOfArguments();
    }

    @Override
    public int minNumberOfArguments() {
        // by default, the number of arguments is 1
        return 1;
    }

    /**
     * Returns the callId which is the name prefixed with an '@' to indicate that it must be called as such
     */
    public String getCallId() { return "@" + getName(); }


    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public List<LocalVariable> getLocalVariables() {
        return Collections.emptyList();
    }
}
