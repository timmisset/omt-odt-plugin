package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class OMTYamlPrefixIriDelegate extends YAMLKeyValueImpl implements
        OMTYamlDelegate,
        SupportsSafeDelete {
    private final YAMLKeyValue keyValue;

    public OMTYamlPrefixIriDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
    }

    public PsiElement getKey() {
        return keyValue.getKey();
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.fileScope(keyValue.getContainingFile());
    }

    @Override
    public boolean isUnused() {
        return ReferencesSearch.search(keyValue, keyValue.getUseScope()).findFirst() == null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        OMTRefactoringUtil.removeEOLToken(keyValue);
        super.delete();
    }
}
