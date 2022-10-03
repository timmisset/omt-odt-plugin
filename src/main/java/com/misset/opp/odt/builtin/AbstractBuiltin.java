package com.misset.opp.odt.builtin;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public abstract class AbstractBuiltin implements Callable {

    @Override
    public String getDescription(Project project) {
        return BuiltinDocumentationService.getInstance(project).getDocumentation(this);
    }

    @Override
    public int maxNumberOfArguments() {
        return minNumberOfArguments();
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    protected abstract OntResource resolveSingle();

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(resolveSingle())
                .map(Set::of)
                .orElse(Collections.emptySet());
    }

    protected abstract Set<OntResource> resolveFrom(Set<OntResource> resources);

    protected abstract Set<OntResource> resolveFrom(PsiCall call);

    protected abstract Set<OntResource> resolveFrom(Set<OntResource> resources, PsiCall call);

    protected abstract Set<OntResource> resolveError(Set<OntResource> resources, PsiCall call);

    private boolean hasError(Set<OntResource> resources) {
        return resources.stream().anyMatch(OntologyModelConstants.getError()::equals);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public final @NotNull Set<OntResource> resolve(Context context) {
        if (isVoid()) {
            return Collections.singleton(OntologyModelConstants.getVoidResponse());
        }
        if (hasFixedReturnType()) {
            return resolve();
        }
        PsiCall call = context.getCall();
        Set<OntResource> inputResources = call.resolvePreviousStep();
        if (hasError(inputResources)) {
            return resolveError(inputResources, call);
        }
        return resolveFrom(inputResources, call);
    }

    /**
     * Generic validation that should be called on every Builtin member, such as the number of arguments
     * Since this method is always required it should not be overridden, instead override the
     *
     * @see AbstractBuiltin#specificValidation(com.misset.opp.resolvable.psi.PsiCall, com.intellij.codeInspection.ProblemsHolder)
     * member to implement validation specific to a Builtin Member
     */
    @Override
    public final void validate(PsiCall call,
                               ProblemsHolder holder) {
        Callable.super.validate(call, holder);
        specificValidation(call, holder);
    }

    protected abstract void specificValidation(PsiCall call, ProblemsHolder holder);

    @Override
    public final Set<OntResource> getAcceptableArgumentType(int index, PsiCall call) {
        Set<OntResource> acceptableArgumentTypeWithContext = getAcceptableArgumentTypeWithContext(index, call);
        if (acceptableArgumentTypeWithContext == null) {
            return Callable.super.getAcceptableArgumentType(index, call);
        }
        return acceptableArgumentTypeWithContext;
    }

    /**
     * Returns null if there are no specifics, empty set if it accepts no input
     */
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return null;
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        return new HashMap<>();
    }

    @Override
    public Map<Integer, String> getParameterNames() {
        return IntStream.range(0, getParameters().size())
                .boxed()
                .collect(toMap(i -> i, this::getParameterName));
    }

    private String getParameterName(int i) {
        List<String> parameters = getParameters();
        if (i == parameters.size() - 1 && maxNumberOfArguments() == -1) {
            return "..." + parameters.get(i);
        } else if (i >= minNumberOfArguments()) {
            return parameters.get(i) + "?";
        }
        return parameters.get(i);
    }

    protected abstract List<String> getParameters();

    /**
     * Checks if the Builtin member always returns the same output, for example, boolean operators
     * always return a boolean, regardless of the input, and should override this method with true;
     */
    protected boolean hasFixedReturnType() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return Collections.emptyList();
    }
}
