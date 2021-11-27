package com.misset.opp.callable.builtin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class Builtin implements Callable {

    @Override
    public String getDescription(String context) {
        return BuiltinDocumentationService.getDocumentation(this);
    }

    public String getMarkdownFilename() {
        return getClass().getSimpleName();
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
     * Convenience method
     * most BuiltIn operators and commands will only return a single result type
     */
    public OntResource resolveSingle() {
        return null;
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(resolveSingle())
                .map(Set::of)
                .orElse(Collections.emptySet());
    }

    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resolve();
    }

    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        return resolveFrom(resources);
    }

    public final Set<OntResource> resolveError(Set<OntResource> resources) {
        return resources;
    }

    public Set<OntResource> resolveError(Set<OntResource> resources,
                                         Call call) {
        return resources;
    }

    private boolean hasError(Set<OntResource> resources) {
        return resources.stream().anyMatch(OppModel.INSTANCE.ERROR::equals);
    }

    @Override
    public final Set<OntResource> resolve(Set<OntResource> resources) {
        if(hasError(resources)) {
            return resolveError(resources);
        }
        return resolveFrom(resources);
    }

    @Override
    public final Set<OntResource> resolve(Set<OntResource> resources,
                                          Call call) {
        if (hasError(resources)) {
            return resolveError(resources, call);
        }
        return resolveFrom(resources, call);
    }

    @Override
    public void validate(PsiCall call,
                         ProblemsHolder holder) {
        final int i = call.numberOfArguments();
        if (!passesMinArguments(i) || !passesMaxArguments(i)) {
            holder.registerProblem(call.getCallSignatureElement(),
                    "Expects " + getExpectedArgumentsMessage() + " arguments. Call has " + i + " arguments",
                    ProblemHighlightType.ERROR);
        }
    }

    protected boolean passesMinArguments(int numberOfArguments) {
        return numberOfArguments >= minNumberOfArguments();
    }

    protected boolean passesMaxArguments(int numberOfArguments) {
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
