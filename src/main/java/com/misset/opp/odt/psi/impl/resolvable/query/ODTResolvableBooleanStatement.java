package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTBooleanStatement;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableBooleanStatement extends ODTResolvableQuery implements ODTBooleanStatement {
    public ODTResolvableBooleanStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN);
    }
}
