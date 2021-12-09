package com.misset.opp.omt.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.HashMap;
import java.util.List;

public interface OMTFile extends PsiFile {
    HashMap<String, List<PsiElement>> getExportingMembersMap();
}
