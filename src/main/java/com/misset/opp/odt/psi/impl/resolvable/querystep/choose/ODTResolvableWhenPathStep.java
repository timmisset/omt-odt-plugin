package com.misset.opp.odt.psi.impl.resolvable.querystep.choose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTWhenPath;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableQueryStepBase;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableWhenPathStep extends ODTResolvableQueryStepBase implements ODTWhenPath {
    protected ODTResolvableWhenPathStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        final List<ODTQuery> queryList = getQueryList();
        if (queryList.isEmpty()) {
            return Collections.emptySet();
        }
        if (queryList.size() == 1) {
            return resolveQuery(0);
        }
        return resolveQuery(1);
    }

    private Set<OntResource> resolveQuery(int index) {
        return Optional.ofNullable(getQueryList().get(index))
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }

    public ODTQuery getCondition() {
        if (getQueryList().isEmpty()) {
            return null;
        }
        return getQueryList().get(0);
    }

    public ODTQuery getThen() {
        if (getQueryList().size() != 2) {
            return null;
        }
        return getQueryList().get(1);
    }

    @Override
    protected PsiElement getAnnotationRange() {
        return getFirstChild();
    }
}
