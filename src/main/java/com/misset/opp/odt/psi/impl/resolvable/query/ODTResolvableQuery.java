package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryFilter;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.resolvable.psi.PsiResolvableQuery;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableQuery extends ODTBaseResolvable implements ODTQuery, ODTResolvable, PsiResolvableQuery {
    public ODTResolvableQuery(@NotNull ASTNode node) {
        super(node);
    }

    public Set<OntResource> resolveFromSet(Set<OntResource> fromSet) {
        return resolve();
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        if (getParent() instanceof ODTQueryFilter) {
            TTLValidationUtil.validateBoolean(resolve(), holder, this);
        }
    }
}
