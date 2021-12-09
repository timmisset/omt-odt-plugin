package com.misset.opp.callable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.ttl.util.TTLValidationUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 */
public interface PsiCallable extends Callable, PsiElement {
    Logger LOGGER = Logger.getInstance(PsiCallable.class);

    PsiElement getCallTarget();

    Set<OntResource> getParamType(int index);

    @Override
    default void validate(PsiCall call, ProblemsHolder holder) {
        Callable.super.validate(call, holder);
        LoggerUtil.runWithLogger(LOGGER,
                "Validation of call " + call.getCallId(),
                () -> {
                    for (int i = 0; i < call.numberOfArguments(); i++) {
                        int finalI = i;
                        Set<OntResource> argumentType = LoggerUtil.computeWithLogger(
                                LOGGER,
                                "Calculating signature argument " + i + " for " + call.getCallId(),
                                () -> call.resolveSignatureArgument(finalI));
                        Set<OntResource> paramType = LoggerUtil.computeWithLogger(
                                LOGGER,
                                "Calculating parameter " + i + " for " + getName(),
                                () -> getParamType(finalI));
                        if (paramType != null && !argumentType.isEmpty() && !paramType.isEmpty()) {
                            TTLValidationUtil.validateCompatibleTypes(paramType, argumentType, holder, call.getCallSignatureArgumentElement(i));
                        }
                    }
                });
    }
}
