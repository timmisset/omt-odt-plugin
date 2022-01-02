package com.misset.opp.odt.psi.impl.callable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        return PsiTreeUtil.findChildrenOfType(this, ODTReturnStatement.class)
                .stream()
                .map(ODTReturnStatement::getResolvableValue)
                .filter(Objects::nonNull)
                .map(Resolvable::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public String getType() {
        return "DEFINE COMMAND";
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
