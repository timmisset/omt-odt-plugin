package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.psi.PsiResolvable;
import com.misset.opp.resolvable.psi.PsiResolvableScript;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ODTResolvableScript extends ODTBaseResolvable implements PsiResolvableScript {
    protected ODTResolvableScript(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve(Context context) {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, PsiResolvable.class))
                .map(psiResolvable -> psiResolvable.resolve(context))
                .orElse(Collections.emptySet());
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        Collection<ODTReturnStatement> returnStatements = PsiTreeUtil.findChildrenOfType(this, ODTReturnStatement.class);
        if (returnStatements.isEmpty()) {
            return Collections.singleton(OppModelConstants.getVoidResponse());
        }
        return returnStatements
                .stream()
                .map(ODTReturnStatement::getResolvableValue)
                .filter(Objects::nonNull)
                .map(Resolvable::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
