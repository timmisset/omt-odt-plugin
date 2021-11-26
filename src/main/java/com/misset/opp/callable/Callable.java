package com.misset.opp.callable;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
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

    default List<String> getFlags() {
        return Collections.emptyList();
    }

    default Set<OntResource> getSecondReturnArgument() { return Collections.emptySet(); }

    default @NotNull Set<OntResource> resolve() { return Collections.emptySet(); }

    /**
     * Validate the call, check the number of arguments, types etc
     */
    default void validate(PsiCall call,
                          ProblemsHolder holder) {
        final int i = call.numberOfArguments();
        if (i < minNumberOfArguments() || i > maxNumberOfArguments()) {
            holder.registerProblem(call.getCallSignatureElement(),
                    "Expects between " + minNumberOfArguments() + " and " + maxNumberOfArguments() + "arguments",
                    ProblemHighlightType.ERROR);
        }
    }
}
