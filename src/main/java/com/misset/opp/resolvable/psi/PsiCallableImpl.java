package com.misset.opp.resolvable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
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
                        validateType(call, holder, i);
                        validateValue(call, holder, i);
                    }
                });
    }

    private void validateType(PsiCall call, ProblemsHolder holder, int i) {
        Set<OntResource> argumentType = LoggerUtil.computeWithLogger(
                LOGGER,
                "Calculating signature argument type " + i + " for " + call.getCallId(),
                () -> call.resolveSignatureArgument(i));
        Set<OntResource> paramType = LoggerUtil.computeWithLogger(
                LOGGER,
                "Calculating parameter " + i + " for " + getName(),
                () -> getParamType(i));
        if (paramType != null && !argumentType.isEmpty() && !paramType.isEmpty()) {
            PsiElement signatureArgumentElement = call.getCallSignatureArgumentElement(i);
            TTLValidationUtil.validateCompatibleTypes(paramType, argumentType, holder, signatureArgumentElement);
        }
    }

    @Override
    public void validateValue(PsiCall call, ProblemsHolder holder, int i) {
        Set<String> paramValues = LoggerUtil.computeWithLogger(
                LOGGER,
                "Calculating parameter " + i + " for " + getName(),
                () -> getParamValues(i));
        if (paramValues.isEmpty()) {
            return;
        }

        String argumentValue = LoggerUtil.computeWithLogger(
                LOGGER,
                "Calculating signature argument value " + i + " for " + call.getCallId(),
                () -> call.getSignatureValue(i));
        // strip the quotes before comparing
        if (argumentValue != null) {
            argumentValue = argumentValue.replaceAll("^['\"](.*)['\"]$", "$1");
            PsiElement signatureArgumentElement = call.getCallSignatureArgumentElement(i);
            TTLValidationUtil.validateValues(paramValues, argumentValue, holder, signatureArgumentElement);
        }

    }

    @Override
    public String getDescription(Project project) {
        return null;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }
}
