package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTConstantValue;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableConstantValueStep extends ODTResolvableQueryStep implements ODTConstantValue {
    public ODTResolvableConstantValueStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    protected Set<OntResource> calculate() {
        final OppModel oppModel = OppModel.INSTANCE;
        final IElementType elementType = getNode().getFirstChildNode().getElementType();

        final OntResource result;
        if(elementType == ODTTypes.BOOLEAN) {
            result = oppModel.XSD_BOOLEAN_INSTANCE;
        } else {
            result = oppModel.OWL_THING;
        }

        // a constant value, like a string, boolean, number etc is considered
        // an instance of the specified data-type
        return Set.of(result);
    }
}
