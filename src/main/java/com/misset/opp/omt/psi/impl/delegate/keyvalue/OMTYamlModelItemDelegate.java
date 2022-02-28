package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.indexing.OMTImportedMembersIndex;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OMTYamlModelItemDelegate extends YAMLKeyValueImpl implements OMTYamlDelegate,
        SupportsSafeDelete {

    private final YAMLKeyValue keyValue;
    private YAMLMapping mapping = null;
    private final OMTModelItemMetaType metaType = new OMTModelItemMetaType("modelItem");

    public OMTYamlModelItemDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
        YAMLValue value = keyValue.getValue();
        if (value instanceof YAMLMapping) {
            this.mapping = (YAMLMapping) value;
        }
    }

    public boolean isCallable() {
        if (mapping == null) {
            return false;
        }
        return metaType.isCallable(mapping);
    }

    public PsiElement getKey() {
        return keyValue.getKey();
    }

    @Override
    public PsiElement getOriginalElement() {
        return keyValue;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        final ArrayList<PsiFile> psiFiles = new ArrayList<>();
        psiFiles.add(keyValue.getContainingFile());
        psiFiles.addAll(OMTImportedMembersIndex.getImportingFiles(getProject(), getName()));
        final List<VirtualFile> targetFiles = psiFiles.stream().map(PsiFile::getVirtualFile)
                .filter(Objects::nonNull).collect(Collectors.toList());
        return GlobalSearchScope.filesScope(getProject(), targetFiles);
    }

    @Override
    public boolean isUnused() {
        return !isCallable() || ReferencesSearch.search(keyValue, keyValue.getUseScope()).findFirst() == null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        OMTRefactoringUtil.removeEOLToken(keyValue);
        super.delete();
    }
}
