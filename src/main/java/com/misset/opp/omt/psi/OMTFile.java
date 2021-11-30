package com.misset.opp.omt.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface OMTFile extends PsiFile {
    GlobalSearchScope getMemberUsageScope(boolean isExportable);

    HashMap<String, List<PsiElement>> getExportingMembersMap();

    Collection<OMTFile> getImportedBy();
}
