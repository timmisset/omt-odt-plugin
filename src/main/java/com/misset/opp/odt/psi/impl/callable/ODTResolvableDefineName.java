package com.misset.opp.odt.psi.impl.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * Named version of th ODTDefineName to allow FindUsage and renaming
 */
public abstract class ODTResolvableDefineName extends ODTASTWrapperPsiElement implements PsiNamedElement, ODTResolvable, PsiJavaDocumentedElement {
    public ODTResolvableDefineName(@NotNull ASTNode node) {
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
        final ODTFile containingFile = getContainingFile();
        return containingFile.getExportingMemberUseScope(getName());
    }

    public String getCallId() {
        return getParent().getCallId();
    }

    public boolean hasSameIdentifier(@Nullable ODTResolvableDefineName defineName) {
        if (defineName == null) {
            return false;
        }
        return defineName.getCallId().equals(getCallId());
    }

    @Override
    public @Nullable PsiDocComment getDocComment() {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, PsiJavaDocumentedElement.class))
                .map(PsiJavaDocumentedElement::getDocComment)
                .orElse(null);
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
