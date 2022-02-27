package com.misset.opp.resolvable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.misset.opp.ttl.util.TTLValidationUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 * If the PsiCallable wraps a PsiElement it should be returned using the getOriginalElement() method
 */
public abstract class PsiCallableImpl extends ASTWrapperPsiElement implements PsiCallable {
    Logger LOGGER = Logger.getInstance(PsiCallableImpl.class);

    public PsiCallableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void validate(PsiCall call, ProblemsHolder holder) {
        PsiCallable.super.validate(call, holder);
        LoggerUtil.runWithLogger(LOGGER,
                "Validation of call " + call.getCallId(),
                () -> {
                    for (int i = 0; i < call.getNumberOfArguments(); i++) {
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

    @Override
    public String getDescription(String context) {
        return null;
    }
}
