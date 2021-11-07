package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.ProblemsHolder;

public interface ODTInspectedElement {
    void inspect(ProblemsHolder holder);
}
