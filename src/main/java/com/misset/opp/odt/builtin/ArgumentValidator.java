package com.misset.opp.odt.builtin;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.util.TriConsumer;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class ArgumentValidator {

    private ArgumentValidator() {
        // empty constructor
    }

    public static void validateNamedGraphArgument(int index,
                                                  PsiCall call,
                                                  ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateNamedGraph(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateInstancesArgument(int index,
                                                 PsiCall call,
                                                 ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateInstances(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static boolean validateBooleanArgument(int index,
                                                  PsiCall call,
                                                  ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            return OntologyValidationUtil.validateBoolean(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
        return true;
    }

    public static void validateNumberArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateNumber(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateIntegerArgument(int index,
                                               PsiCall call,
                                               ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateInteger(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateJSONArgument(int index,
                                            PsiCall call,
                                            ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateJSON(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateStringArgument(int index,
                                              PsiCall call,
                                              ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateString(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateClassNameArgument(int index,
                                                 PsiCall call,
                                                 ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateClassName(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateGraphShapeArgument(int index,
                                                  PsiCall call,
                                                  ProblemsHolder holder) {
        if (call.getNumberOfArguments() >= index) {
            OntologyValidationUtil.validateGraphShape(call.resolveSignatureArgument(index),
                    holder,
                    call.getCallSignatureArgumentElement(index));
        }
    }

    public static void validateAllArguments(PsiCall call,
                                            ProblemsHolder holder,
                                            TriConsumer<Integer, PsiCall, ProblemsHolder> validation) {
        if (call.getNumberOfArguments() > 0) {
            for (int i = 0; i < call.getNumberOfArguments(); i++) {
                validation.accept(i, call, holder);
            }
        }
    }

    public static void validateAllArgumentsCompatible(PsiCall call,
                                                      ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 1) {
            OntologyValidationUtil.validateCompatibleTypes(
                    call.resolvePreviousStep(), call.resolveSignatureArgument(0),
                    holder, call);
        } else if (call.getNumberOfArguments() >= 2) {
            Set<OntResource> ontResources = call.resolveSignatureArgument(0);
            for (int i = 1; i < call.getNumberOfArguments(); i++) {
                OntologyValidationUtil.validateCompatibleTypes(
                        ontResources, call.resolveSignatureArgument(i),
                        holder, call.getCallSignatureElement());
            }
        }
    }
}
