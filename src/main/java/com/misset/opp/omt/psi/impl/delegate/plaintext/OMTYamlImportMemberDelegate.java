package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.indexing.OMTImportedMembersIndex;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.*;
import java.util.stream.Collectors;

public class OMTYamlImportMemberDelegate extends YAMLPlainTextImpl implements OMTYamlDelegate,
        SupportsSafeDelete {
    YAMLPlainTextImpl value;

    private static final Logger LOGGER = Logger.getInstance(OMTYamlImportMemberDelegate.class);

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
        if (!(containingFile instanceof OMTFile)) {
            return false;
        }
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(targetElement -> isNeverUsed(targetElement, value, (OMTFile) containingFile))
                .orElse(true);
    }

    private boolean isNeverUsed(PsiElement targetElement,
                                PsiElement importElement,
                                OMTFile containingFile) {
        String name = targetElement instanceof PsiNamedElement ? ((PsiNamedElement) targetElement).getName() : targetElement.getText();
        boolean local = isUsedInLocalScope(targetElement, importElement, containingFile, name);
        if (local) {
            return false;
        }

        // all files that import this file are also part of the scope:
        Set<OMTFile> placesToSearch = getPlacesToSearch(containingFile, importElement);
        return !isUsedInGlobalScope(placesToSearch, targetElement, name);
    }

    private boolean isUsedInLocalScope(PsiElement targetElement,
                                       PsiElement importElement,
                                       OMTFile file,
                                       String name) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "LOCAL Call search for " + importElement.getText(),
                () -> isUsedInFile(file, importElement, targetElement, name));
    }

    private boolean isUsedInGlobalScope(Set<OMTFile> placesToSearch, PsiElement target, String name) {
        return placesToSearch.stream()
                .anyMatch(file -> isImportedByFile(file, target, name));
    }

    private boolean isImportedByFile(OMTFile file, PsiElement targetElement, String name) {
        return file.getImportingMembersMap()
                .getOrDefault(name, Collections.emptyList())
                .stream()
                .anyMatch(targetElement::equals);
    }

    private boolean isUsedInFile(OMTFile file, PsiElement importElement, PsiElement targetElement, String name) {

        return Optional.ofNullable(file.getAllInjectedPsiCalls()
                        .get(name))
                .stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(PsiCall::getCallable)
                .filter(PsiElement.class::isInstance)
                .map(PsiElement.class::cast)
                .map(PsiElement::getOriginalElement)
                .anyMatch(targetElement::equals) ||
                ReferencesSearch.search(targetElement, new LocalSearchScope(file))
                        .anyMatch(reference -> reference.getElement() != importElement);
    }

    private Set<OMTFile> getPlacesToSearch(OMTFile containingFile, PsiElement importElement) {
        return LoggerUtil.computeWithLogger(LOGGER, "getPlacesToSearch for " + importElement.getText(), () -> {
            List<OMTFile> importingFiles = OMTImportedMembersIndex.getImportingFiles(importElement.getText());
            return ReferencesSearch.search(containingFile, new LocalSearchScope(importingFiles.toArray(PsiFile[]::new)))
                    .findAll()
                    .stream()
                    .map(PsiReference::getElement)
                    .map(PsiElement::getContainingFile)
                    .filter(OMTFile.class::isInstance)
                    .map(OMTFile.class::cast)
                    .collect(Collectors.toSet());
        });
    }
}
