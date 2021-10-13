package com.misset.opp.omt.psi.impl.model.modelitems;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.impl.model.OMTModelItemAbstract;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class OMTOntology extends OMTModelItemAbstract implements OMTModelItem {

    public OMTOntology(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public OMTModelItemType getType() {
        return OMTModelItemType.Ontology;
    }

    @Override
    public List<OMTVariable> getDeclaredVariables() {
        return Collections.emptyList();
    }
}
