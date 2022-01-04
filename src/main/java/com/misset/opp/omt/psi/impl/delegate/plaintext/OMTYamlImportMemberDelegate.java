package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.indexing.OMTImportedMembersIndex;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTYamlImportMemberDelegate extends YAMLPlainTextImpl implements OMTYamlDelegate,
        SupportsSafeDelete {
    YAMLPlainTextImpl value;

    public OMTYamlImportMemberDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newName) {
        final YAMLKeyValue value = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return value.replace(value);
    }

    @Override
    public PsiReference getReference() {
        return new OMTImportMemberReference(value);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }

    @Override
    public void delete() throws IncorrectOperationException {
        OMTRefactoringUtil.removeFromSequence(this);
    }

    @Override
    public boolean isUnused() {
        PsiFile containingFile = value.getContainingFile();
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(targetElement -> isNeverUsed(targetElement, value, containingFile))
                .orElse(true);
    }

    private boolean isNeverUsed(PsiElement targetElement,
                                PsiElement importElement,
                                PsiFile containingFile) {
        // all files that import this file are also part of the scope:
        List<OMTFile> importingFiles = OMTImportedMembersIndex.getImportingFiles(importElement.getText());
        List<PsiElement> placesToSearch = ReferencesSearch.search(containingFile, new LocalSearchScope(importingFiles.toArray(PsiFile[]::new)))
                .findAll()
                .stream()
                .map(PsiReference::getElement)
                .collect(Collectors.toCollection(ArrayList::new));
        placesToSearch.add(containingFile);

        return ReferencesSearch.search(targetElement, new LocalSearchScope(placesToSearch.toArray(PsiElement[]::new)))
                .filtering(psiReference -> psiReference.getElement() != importElement)
                .findFirst() == null;
    }
}
