package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTNegatedStep;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableNegatedStep extends ODTResolvableQueryStep implements ODTNegatedStep {
    public ODTResolvableNegatedStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> calculate() {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN);
    }
}
