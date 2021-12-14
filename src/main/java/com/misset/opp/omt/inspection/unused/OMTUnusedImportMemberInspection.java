package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.misset.opp.omt.indexing.ImportedMembersIndex;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTImportMemberDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class OMTUnusedImportMemberInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof YAMLPsiElement)) {
                    return;
                }
                final OMTYamlDelegate delegate = OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) element);
                if (delegate instanceof OMTImportMemberDelegate) {
                    Optional.ofNullable(element.getReference())
                            .map(PsiReference::resolve)
                            .map(targetElement -> isNeverUsed(targetElement, element))
                            .ifPresentOrElse(
                                    psiReference -> {
                                        // noop, reference exists, can be used
                                    },
                                    () -> holder.registerProblem(element,
                                            "Import for " + element.getText() + " is never used",
                                            LIKE_UNUSED_SYMBOL));
                }

            }

            private PsiReference isNeverUsed(PsiElement targetElement,
                                             PsiElement importElement) {
                // all files that import this file are also part of the scope:
                PsiFile file = holder.getFile();
                List<OMTFile> importingFiles = ImportedMembersIndex.getImportingFiles(importElement.getText());
                List<PsiElement> placesToSearch = ReferencesSearch.search(file, new LocalSearchScope(importingFiles.toArray(PsiFile[]::new)))
                        .findAll()
                        .stream()
                        .map(PsiReference::getElement)
                        .collect(Collectors.toCollection(ArrayList::new));
                placesToSearch.add(file);

                return ReferencesSearch.search(targetElement, new LocalSearchScope(placesToSearch.toArray(PsiElement[]::new)))
                        .filtering(psiReference -> psiReference.getElement() != importElement)
                        .findFirst();
            }
        };
    }
}
