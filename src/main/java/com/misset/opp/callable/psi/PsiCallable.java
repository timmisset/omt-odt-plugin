package com.misset.opp.callable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 */
public interface PsiCallable extends Callable, PsiElement {

    PsiElement getCallTarget();

    Set<OntResource> getParamType(int index);

    @Override
    default void validate(PsiCall call, ProblemsHolder holder) {
        Callable.super.validate(call, holder);
        for(int i = 0; i < call.numberOfArguments(); i++) {
            Set<OntResource> argumentType = call.resolveSignatureArgument(i);
            Set<OntResource> paramType = getParamType(i);
            if(!argumentType.isEmpty() && !paramType.isEmpty()) {
                TTLValidationUtil.validateCompatibleTypes(paramType, argumentType, holder, call.getCallSignatureArgumentElement(i));
            }
        }
    }
}
