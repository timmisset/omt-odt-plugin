package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
import org.apache.jena.graph.NodeFactory;
import org.jetbrains.annotations.NotNull;

public abstract class TTLIriAbstract extends ASTWrapperPsiElement implements TTLQualifiedIriResolver {
    protected TTLIriAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        String qualifiedIri = getQualifiedIri();
        return qualifiedIri != null ? NodeFactory.createURI(qualifiedIri).getLocalName() : null;
    }
}
