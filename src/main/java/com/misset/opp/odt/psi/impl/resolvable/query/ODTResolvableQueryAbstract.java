package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryFilter;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableAbstract;
import com.misset.opp.odt.psi.resolvable.query.ODTResolvableQuery;
import org.jetbrains.annotations.NotNull;

public abstract class ODTResolvableQueryAbstract extends ODTResolvableAbstract implements ODTQuery, ODTResolvableQuery {
    protected ODTResolvableQueryAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        if (getParent() instanceof ODTQueryFilter) {
            OntologyValidationUtil.getInstance(holder.getProject()).validateBoolean(resolve(), holder, this);
        }
    }
}
