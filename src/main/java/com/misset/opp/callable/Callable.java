package com.misset.opp.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.builtin.commands.LocalVariable;
import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A callable implementation can be a callable Psi element such as an OMT modelItem but
 * can also be an ODT Defined Statement, a local command or a built-in command.
 *
 * An ODTCall.getCallable() will resolve to an implementation of Callable and is able to
 * determine call validity
 */
public interface Callable extends Resolvable {

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

    default Set<OntResource> resolve() { return Collections.emptySet(); }

    /**
     * Resolve the output based on the input
     * For example, a specific operator that returns the same type as it receives
     */
    default Set<OntResource> resolve(Set<OntResource> resources) { return Collections.emptySet(); }

    /**
     * Resolve the output based on the input and the call arguments
     * For example, a specific operator that returns the same type as it receives
     */
    default Set<OntResource> resolve(Set<OntResource> resources, ODTCall call) { return Collections.emptySet(); }

    /**
     * Validate the call, check the number of arguments, types etc
     */
    default void validate(ODTCall call, ProblemsHolder holder) { return; }
}
