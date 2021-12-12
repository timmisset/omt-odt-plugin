package com.misset.opp.odt.completion;

import com.intellij.patterns.PsiJavaElementPattern;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.*;

import static com.intellij.patterns.PsiJavaPatterns.psiElement;

public interface CompletionPatterns {


    PsiJavaElementPattern.Capture<PsiElement> QUERY_STEP = psiElement().inside(ODTQueryStep.class);
    PsiJavaElementPattern.Capture<PsiElement> INSIDE_DEFINED_QUERY = psiElement().inside(ODTDefineQueryStatement.class);
    PsiJavaElementPattern.Capture<PsiElement> INSIDE_QUERY_FILTER = psiElement().inside(ODTQueryFilter.class);
    PsiJavaElementPattern.Capture<PsiElement> FIRST_QUERY_STEP = QUERY_STEP.atStartOf(
            psiElement(ODTQueryPath.class)
    );
    PsiJavaElementPattern.Capture<PsiElement> AFTER_FIRST_QUERY_STEP = QUERY_STEP.andNot(FIRST_QUERY_STEP);
    PsiJavaElementPattern.Capture<PsiElement> VARIABLE_ASSIGNMENT_VALUE = psiElement().inside(ODTVariableAssignment.class);
    PsiJavaElementPattern.Capture<PsiElement> SIGNATURE_ARGUMENT = psiElement().inside(ODTSignatureArgument.class);


}
