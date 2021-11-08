package com.misset.opp.odt.psi.impl.callable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
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
public abstract class ODTDefineNameNamed extends ASTWrapperPsiElement implements PsiNamedElement, ODTResolvable {
    public ODTDefineNameNamed(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
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
    public Set<OntResource> resolve() {
        return ((ODTDefineStatement)getParent()).resolve();
    }
}
