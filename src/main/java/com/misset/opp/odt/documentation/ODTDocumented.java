package com.misset.opp.odt.documentation;

import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTPsiElement;

public interface ODTDocumented extends ODTPsiElement {

    String getDocumentation();

    PsiElement getDocumentationElement();

}
