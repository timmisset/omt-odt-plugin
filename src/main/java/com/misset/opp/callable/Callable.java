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

    /**
     * Returns the description for this callable item
     * The context might provide some additional information about the location of the call that can be included in the
     * output string. The returned string should be HTML based.
     */
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
