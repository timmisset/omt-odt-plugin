package com.misset.opp.odt.psi.impl.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Named version of th ODTDefineName to allow FindUsage and renaming
 */
public abstract class ODTDefineNameNamed extends ASTWrapperPsiElement implements PsiNamedElement, ODTResolvable, PsiJavaDocumentedElement {
    public ODTDefineNameNamed(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Override
    public ODTDefineStatement getParent() {
        return (ODTDefineStatement) super.getParent();
    }

    @Override
    public @Nullable @NlsSafe String getName() {
        return getText();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        final ODTFile containingFile = (ODTFile) getContainingFile();
        return containingFile.getExportingMemberUseScope();
    }

    @Override
    public @Nullable PsiDocComment getDocComment() {
        return getParent().getDocComment();
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getParent().resolve();
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {

    }
}
