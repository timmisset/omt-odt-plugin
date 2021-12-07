package com.misset.opp.callable.builtin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.util.TriConsumer;
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

    protected String getShorthandSyntax() { return null; }

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
        Callable.super.validate(call, holder);
        String flag = call.getFlag();
        if(flag != null && !getFlags().contains(flag)) {
            holder.registerProblem(call.getFlagElement(),
                    "Illegal flag, options are: " + String.join(", ", getFlags()),
                    ProblemHighlightType.ERROR);
        }
        String shorthandSyntax = getShorthandSyntax();
        if(shorthandSyntax != null) {
            holder.registerProblem(call, "Prefer syntax shorthand: " + shorthandSyntax, ProblemHighlightType.WEAK_WARNING);
        }

        specificValidation(call, holder);
    }

    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {

    }

    protected void validateNamedGraphArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateNamedGraph(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateInstancesArgument(int index,
                                             PsiCall call,
                                             ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateInstances(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
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

    protected void validateNumberArgument(int index,
                                          PsiCall call,
                                          ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateNumber(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateIntegerArgument(int index,
                                           PsiCall call,
                                           ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateInteger(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateJSONArgument(int index,
                                        PsiCall call,
                                        ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateJSON(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateStringArgument(int index,
                                          PsiCall call,
                                          ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateString(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateClassNameArgument(int index,
                                             PsiCall call,
                                             ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateClassName(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateGraphShapeArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.numberOfArguments() >= index) {
            TTLValidationUtil.validateGraphShape(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateAllArguments(PsiCall call,
                                        ProblemsHolder holder,
                                        TriConsumer<Integer, PsiCall, ProblemsHolder> validation) {
        if (call.numberOfArguments() > 0) {
            for (int i = 0; i < call.numberOfArguments(); i++) {
                validation.accept(i, call, holder);
            }
        }
    }

    protected void validateAllArgumentsCompatible(PsiCall call,
                                                  ProblemsHolder holder) {
        if (call.numberOfArguments() == 1) {
            TTLValidationUtil.validateCompatibleTypes(
                    call.getCallInputType(), call.resolveSignatureArgument(0),
                    holder, call);
        }
        else if(call.numberOfArguments() >= 2) {
            Set<OntResource> ontResources = call.resolveSignatureArgument(0);
            for(int i = 1; i < call.numberOfArguments(); i++) {
                TTLValidationUtil.validateCompatibleTypes(
                        ontResources, call.resolveSignatureArgument(i),
                        holder, call.getCallSignatureElement());
            }
        }
    }
}
