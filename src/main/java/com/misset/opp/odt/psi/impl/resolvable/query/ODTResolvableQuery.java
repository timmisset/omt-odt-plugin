package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryFilter;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.jetbrains.annotations.NotNull;

public abstract class ODTResolvableQuery extends ODTBaseResolvable implements ODTQuery, ODTResolvable {
    protected ODTResolvableQuery(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        if (getParent() instanceof ODTQueryFilter) {
            TTLValidationUtil.validateBoolean(resolve(), holder, this);
        }
    }

    public abstract boolean requiresInput();
}
