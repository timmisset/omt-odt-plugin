package com.misset.opp.odt.builtin;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.util.TriConsumer;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class Builtin implements Callable {

    @Override
    public String getDescription(String context, Project project) {
        return BuiltinDocumentationService.getInstance(project).getDocumentation(this);
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
                                           PsiCall call) {
        return resolveFrom(resources);
    }

    public Set<OntResource> resolveError(Set<OntResource> resources,
                                         PsiCall call) {
        return resources;
    }

    private boolean hasError(Set<OntResource> resources) {
        return resources.stream().anyMatch(OppModel.INSTANCE.ERROR::equals);
    }

    @Override
    public final @NotNull Set<OntResource> resolve(Context context) {
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
     * @see Builtin#specificValidation(com.misset.opp.resolvable.psi.PsiCall, com.intellij.codeInspection.ProblemsHolder)
     * member to implement validation specific to a Builtin Member
     */
    @Override
    public final void validate(PsiCall call,
                               ProblemsHolder holder) {
        Callable.super.validate(call, holder);
        specificValidation(call, holder);
    }

    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {

    }

    protected void validateNamedGraphArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateNamedGraph(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateInstancesArgument(int index,
                                             PsiCall call,
                                             ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateInstances(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected boolean validateBooleanArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            return TTLValidationUtil.validateBoolean(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    protected void validateNumberArgument(int index,
                                          PsiCall call,
                                          ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateNumber(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateIntegerArgument(int index,
                                           PsiCall call,
                                           ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateInteger(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateJSONArgument(int index,
                                        PsiCall call,
                                        ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateJSON(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateStringArgument(int index,
                                          PsiCall call,
                                          ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateString(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateClassNameArgument(int index,
                                             PsiCall call,
                                             ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateClassName(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateGraphShapeArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            TTLValidationUtil.validateGraphShape(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    protected void validateAllArguments(PsiCall call,
                                        ProblemsHolder holder,
                                        TriConsumer<Integer, PsiCall, ProblemsHolder> validation) {
        if (call.getNumberOfArguments() > 0) {
            for (int i = 0; i < call.getNumberOfArguments(); i++) {
                validation.accept(i, call, holder);
            }
        }
    }

    protected void validateAllArgumentsCompatible(PsiCall call,
                                                  ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 1) {
            TTLValidationUtil.validateCompatibleTypes(
                    call.resolvePreviousStep(), call.resolveSignatureArgument(0),
                    holder, call);
        } else if (call.getNumberOfArguments() >= 2) {
            Set<OntResource> ontResources = call.resolveSignatureArgument(0);
            for (int i = 1; i < call.getNumberOfArguments(); i++) {
                TTLValidationUtil.validateCompatibleTypes(
                        ontResources, call.resolveSignatureArgument(i),
                        holder, call.getCallSignatureElement());
            }
        }
    }

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
        return new HashMap<>();
    }

    /**
     * Checks if the Builtin member always returns the same output, for example, boolean operators
     * always return a boolean, regardless of the input, and should override this method with true;
     */
    protected boolean hasFixedReturnType() {
        return false;
    }
}
