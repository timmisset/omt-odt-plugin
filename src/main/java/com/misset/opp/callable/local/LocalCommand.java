package com.misset.opp.callable.local;

import com.misset.opp.callable.Callable;
import com.misset.opp.callable.builtIn.commands.LocalVariable;

import java.util.Collections;
import java.util.List;

public abstract class LocalCommand implements Callable {

    public static final LocalCommand CANCEL = new Cancel();
    public static final LocalCommand COMMIT = new Commit();
    public static final LocalCommand DONE = new Done();
    public static final LocalCommand GET_ERROR_STATE = new GetErrorState();
    public static final LocalCommand HAS_ERROR = new HasError();
    public static final LocalCommand ROLLBACK = new Rollback();

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 0;
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    /**
     * Returns the callId which is the name prefixed with an '@' to indicate that it must be called as such
     */
    public String getCallId() { return "@" + getName(); }

    @Override
    public List<LocalVariable> getLocalVariables() {
        return Collections.emptyList();
    }
}
