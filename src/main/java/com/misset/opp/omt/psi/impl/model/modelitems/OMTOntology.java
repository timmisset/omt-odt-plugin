package com.misset.opp.omt.psi.impl.model.modelitems;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.impl.model.OMTNonEmptyModelItem;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import org.jetbrains.annotations.NotNull;

public class OMTOntology extends OMTNonEmptyModelItem implements OMTModelItem {

    public OMTOntology(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public OMTModelItemType getType() {
        return OMTModelItemType.Ontology;
    }
}
