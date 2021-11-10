package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTResolvableValue;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTResolvableValueBase extends ASTWrapperPsiElement implements ODTResolvableValue {
    public ODTResolvableValueBase(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        if(getQuery() != null) { return getQuery().resolve(); }
        if(getCommandCall() != null) { return getCommandCall().resolve(); }
        return Collections.emptySet();
    }
}
