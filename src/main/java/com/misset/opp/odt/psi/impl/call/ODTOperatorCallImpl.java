package com.misset.opp.odt.psi.impl.call;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.odt.psi.ODTOperatorCall;
import com.misset.opp.odt.psi.ODTQueryStep;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * The OperatorCall is considered both a Call and a QueryStep
 * The ODTBaseCall provides all required Call abstract methods and ODTOperatorCall interface
 * extends the ODTQueryStep interface to enforce the implementation of QueryStep behavior
 *
 * @see com/misset/opp/odt/ODT.bnf
 * The class both extends the QueryStep interface and mixin the abstract class
 */
public abstract class ODTOperatorCallImpl extends ODTBaseCall implements ODTOperatorCall {
    public ODTOperatorCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getCallId() {
        return getName();
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, ODTQueryStep.class))
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }
}
