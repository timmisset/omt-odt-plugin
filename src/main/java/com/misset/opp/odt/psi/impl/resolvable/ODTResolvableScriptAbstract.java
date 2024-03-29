package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.psi.PsiResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ODTResolvableScriptAbstract extends ODTResolvableAbstract {
    protected ODTResolvableScriptAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve(@Nullable Context context) {
        return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, PsiResolvable.class))
                .map(psiResolvable -> psiResolvable.resolve(context))
                .orElse(Collections.emptySet());
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        Collection<ODTReturnStatement> returnStatements = PsiTreeUtil.findChildrenOfType(this, ODTReturnStatement.class);
        if (returnStatements.isEmpty()) {
            return Collections.singleton(OntologyModelConstants.getVoidResponse());
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
