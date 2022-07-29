package com.misset.opp.omt.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OMTFile extends PsiFile {
    static boolean isModuleFileName(String name) {
        return name.endsWith(".module.omt") || name.equals("module.omt");
    }

    static boolean isInterfaceFileName(String name) {
        return name.endsWith(".interface.omt");
    }

    YAMLMapping getRootMapping();

    Map<String, List<PsiCall>> getAllInjectedPsiCalls();

    HashMap<String, List<PsiCallable>> getExportingMembersMap();

    HashMap<String, List<PsiCallable>> getDeclaredExportingMembersMap();

    HashMap<String, List<PsiCallable>> getImportingMembersMap();

    Map<String, String> getAvailableNamespaces(PsiElement element);

    String getModuleName();

    OMTFile getModuleFile();
}
