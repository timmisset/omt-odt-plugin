package com.misset.opp.odt.psi.impl.callable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class ODTBaseDefineCommandStatement extends ODTDefineStatement implements ODTDefineCommandStatement {
    public ODTBaseDefineCommandStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public String getCallId() {
        return "@" + getName();
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Collections.emptySet();
    }

    @Override
    public String getType() {
        return "DEFINE COMMAND";
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return false;
    }

}
