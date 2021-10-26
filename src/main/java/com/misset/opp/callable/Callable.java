package com.misset.opp.callable;

import com.misset.opp.callable.builtin.commands.LocalVariable;

import java.util.Collections;
import java.util.List;

/**
 * A callable implementation can be a callable Psi element such as an OMT modelItem but
 * can also be an ODT Defined Statement, a local command or a built-in command.
 *
 * An ODTCall.getCallable() will resolve to an implementation of Callable and is able to
 * determine call validity
 */
public interface Callable {

    String getName();
    String getDescription(String context);
    String getCallId();

    int minNumberOfArguments();
    int maxNumberOfArguments();

    boolean isVoid();
    boolean isCommand();

    default List<LocalVariable> getLocalVariables() {
        return Collections.emptyList();
    }

    default List<String> getFlags() {
        return Collections.emptyList();
    }
}
