package com.misset.opp.odt.documentation;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

public interface ODTDocumented extends PsiElement {

    String getDocumentation(Project project);
}
