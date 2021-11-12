package com.misset.opp.odt.psi.impl.callable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTBaseDefineQueryStatement extends ODTDefineStatement implements ODTDefineQueryStatement {
    public ODTBaseDefineQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    public String getCallId() { return getName(); }


    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }
}
