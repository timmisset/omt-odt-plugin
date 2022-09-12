package com.misset.opp.odt.psi.impl.resolvable.callable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTCommandBlock;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTDefineCommandStatementAbstract extends ODTDefineStatementAbstract implements ODTDefineCommandStatement {
    protected ODTDefineCommandStatementAbstract(@NotNull ASTNode node) {
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
        return Optional.ofNullable(getReturnType())
                .orElseGet(this::doResolve);
    }

    private Set<OntResource> doResolve() {
        return Optional.of(getCommandBlock())
                .map(ODTCommandBlock::getScript)
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    public CallableType getType() {
        return CallableType.DEFINE_COMMAND;
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return false;
    }

    @Override
    public boolean requiresInput() {
        return false;
    }
}
