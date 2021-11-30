package com.misset.opp.callable.builtin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
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

    /**
     * Generic validation that should be called on every Builtin member, such as the number of arguments
     * Since this method is always required it should not be overridden, instead override the
     *
     * @see Builtin#specificValidation(com.misset.opp.callable.psi.PsiCall, com.intellij.codeInspection.ProblemsHolder)
     * member to implement validation specific to a Builtin Member
     */
    @Override
    public final void validate(PsiCall call,
                               ProblemsHolder holder) {
        final int i = call.numberOfArguments();
        if (!passesMinArguments(i) || !passesMaxArguments(i)) {
            holder.registerProblem(call.getCallSignatureElement(),
                    "Expects " + getExpectedArgumentsMessage() + " arguments. Call has " + i + " arguments",
                    ProblemHighlightType.ERROR);
        }
        specificValidation(call, holder);
    }

    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {

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

    protected boolean validateNamedGraphArgument(int index,
                                                 PsiCall call,
                                                 ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateNamedGraph(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected boolean validateInstancesArgument(int index,
                                                PsiCall call,
                                                ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateInstances(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected boolean validateBooleanArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateBoolean(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected boolean validateJSONArgument(int index,
                                           PsiCall call,
                                           ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateJSON(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected boolean validateStringArgument(int index,
                                             PsiCall call,
                                             ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateString(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected boolean validateClassNameArgument(int index,
                                                PsiCall call,
                                                ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateClassName(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected boolean validateGraphShapeArgument(int index,
                                                 PsiCall call,
                                                 ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            return TTLValidationUtil.validateGraphShape(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }
}
