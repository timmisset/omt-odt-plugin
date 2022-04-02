package com.misset.opp.resolvable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

public interface PsiCallable extends Callable, PsiElement, PsiResolvable {

    Set<OntResource> getParamType(int index);

    /**
     * Returns a list with acceptable values (strings only) which can be used to restrict
     * the used values in a signature argument
     */
    default Set<String> getParamValues(int index) {
        return Collections.emptySet();
    }

    default void validateValue(PsiCall call, ProblemsHolder holder, int i) {
    }

}
