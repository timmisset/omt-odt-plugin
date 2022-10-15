package com.misset.opp.resolvable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.util.OntologyValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 * If the PsiCallable wraps a PsiElement it should be returned using the getOriginalElement() method
 */
public abstract class PsiCallableImpl extends ASTWrapperPsiElement implements PsiCallable {
    protected PsiCallableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void validate(PsiCall call, ProblemsHolder holder) {
        PsiCallable.super.validate(call, holder);
        for (int i = 0; i < call.getNumberOfArguments(); i++) {
            validateType(call, holder, i);
            validateValue(call, holder, i);
        }
    }

    private void validateType(PsiCall call, ProblemsHolder holder, int i) {
        Set<OntResource> argumentType = call.resolveSignatureArgument(i);
        Set<OntResource> paramType = getParamType(i);

        if (paramType != null && !argumentType.isEmpty() && !paramType.isEmpty()) {
            PsiElement signatureArgumentElement = call.getCallSignatureArgumentElement(i);
            OntologyValidationUtil.getInstance(holder.getProject()).validateCompatibleTypes(paramType, argumentType, holder, signatureArgumentElement);
        }
    }

    @Override
    public void validateValue(PsiCall call, ProblemsHolder holder, int i) {
        Set<String> paramValues = getParamValues(i);
        if (paramValues.isEmpty()) {
            return;
        }

        String argumentValue = call.getSignatureValue(i);
        // strip the quotes before comparing
        if (argumentValue != null) {
            argumentValue = argumentValue.replaceAll("^['\"](.*)['\"]$", "$1");
            PsiElement signatureArgumentElement = call.getCallSignatureArgumentElement(i);
            OntologyValidationUtil.getInstance(holder.getProject()).validateValues(paramValues, argumentValue, holder, signatureArgumentElement);
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

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return Collections.emptyList();
    }
}
