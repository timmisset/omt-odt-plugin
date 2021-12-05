package com.misset.opp.callable;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.builtin.Builtin;
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
     * Generic validation that should be called on every Callable member, such as the number of arguments
     * If additional validations are required, make sure to also call this method
     */
    default void validate(PsiCall call,
                               ProblemsHolder holder) {
        final int i = call.numberOfArguments();
        if (!passesMinArguments(i) || !passesMaxArguments(i)) {
            PsiElement callSignatureElement = call.getCallSignatureElement();
            holder.registerProblem(callSignatureElement != null ? callSignatureElement : call,
                    "Expects " + getExpectedArgumentsMessage() + " arguments. Call has " + i + " arguments",
                    ProblemHighlightType.ERROR);
        }
    }

    private boolean passesMinArguments(int numberOfArguments) {
        return numberOfArguments >= minNumberOfArguments();
    }

    private boolean passesMaxArguments(int numberOfArguments) {
        return maxNumberOfArguments() >= numberOfArguments || maxNumberOfArguments() == -1;
    }

    private String getExpectedArgumentsMessage() {
        if (minNumberOfArguments() == 0) {
            if (maxNumberOfArguments() > -1) {
                return "at most " + maxNumberOfArguments() + " arguments";
            } else {
                return "no arguments";
            }
        } else if (minNumberOfArguments() > 0) {
            if (maxNumberOfArguments() == -1) {
                return "at least " + minNumberOfArguments() + " arguments";
            } else {
                return "between " + minNumberOfArguments() + " and " + maxNumberOfArguments() + " arguments";
            }
        }
        return null;
    }
}
