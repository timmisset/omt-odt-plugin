package com.misset.opp.odt.documentation;

import com.intellij.openapi.project.Project;
import com.misset.opp.odt.psi.ODTPsiElement;

public interface ODTDocumented extends ODTPsiElement {

    String getDocumentation(Project project);
}
