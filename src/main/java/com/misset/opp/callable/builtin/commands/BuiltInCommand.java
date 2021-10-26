package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.Builtin;

public abstract class BuiltInCommand extends Builtin {
    @Override
    public boolean isCommand() {
        return true;
    }

    /**
     * Returns the callId which is the name prefixed with an '@' to indicate that it must be called as such
     */
    public String getCallId() { return "@" + getName(); }


    @Override
    public boolean isVoid() {
        return true;
    }
}
