package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTNegatedStep;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableNegatedStep extends ODTResolvableQueryStepBase implements ODTNegatedStep {
    public ODTResolvableNegatedStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        final Set<OntResource> filter = Optional
                .ofNullable(getQuery())
                .map(odtQuery -> odtQuery.filter(resources)).orElse(Collections.emptySet());
        if (!filter.containsAll(resources)) {
            // a filter was applied. The outcome must be negated by removing
            // the survivors of the filter from the response
            resources.removeAll(filter);
        }
        return resources;
    }

    @Override
    protected PsiElement getAnnotationRange() {
        // only annotate the NOT part, not the entire negated part
        return getFirstChild();
    }
}
