package com.misset.opp.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.builtin.commands.LocalVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Any callable class / psiElement that can evaluate a call with OntResource input/output
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

    default Set<OntResource> getSecondReturnArgument() { return Collections.emptySet(); }

    default @NotNull Set<OntResource> resolve() { return Collections.emptySet(); }


    /**
     * Resolve the output based on the input
     * For example, a specific operator that returns the same type as it receives
     */
    default Set<OntResource> resolve(Set<OntResource> resources) { return resolve(); }

    /**
     * Resolve the output based on the input and the call arguments
     * For example, a specific operator that returns the same type as it receives
     */
    default Set<OntResource> resolve(Set<OntResource> resources,
                                     Call call) {
        return resolve(resources);
    }

    /**
     * Validate the call, check the number of arguments, types etc
     */
    default void validate(Call call,
                          ProblemsHolder holder) {
        return;
    }
}
