package com.misset.opp.odt.psi.impl.call;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTOperatorCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * The OMTCommandCallImpl wraps the auto-generated version
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
    public Set<OntResource> resolve() {
        return Collections.emptySet();
    }
}
