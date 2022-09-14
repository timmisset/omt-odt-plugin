package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTTypeFilterProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;

public abstract class ODTScriptLineAbstract extends ODTDocumentedScriptLine implements ODTScriptLine, ODTTypeFilterProvider {
    protected ODTScriptLineAbstract(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * The ScriptLine is the top-level element for any type filtration. It doesn't actually filter anything
     * but once it's reached it can safely be assumed there is no real type filtration required between
     * the position of the Completion placeholder and this ScriptLine.
     * <p>
     * Not having such a terminal would mean that nested scripts, for example in CallArguments, would be
     * filtered by those CallArguments even though the actual lookup is on a much deeper level and shouldn't be
     * filtered.
     */
    @Override
    public Predicate<Set<OntResource>> getTypeFilter(PsiElement element) {
        return ODTTypeFilterProvider.ACCEPT_ALL;
    }
}
