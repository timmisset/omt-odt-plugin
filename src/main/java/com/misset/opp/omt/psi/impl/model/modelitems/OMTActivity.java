package com.misset.opp.omt.psi.impl.model.modelitems;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.psi.impl.model.OMTModelItemAbstract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OMTActivity extends OMTModelItemAbstract implements OMTModelItem {

    public OMTActivity(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public OMTModelItemType getType() {
        return OMTModelItemType.Activity;
    }

    @Override
    public List<OMTVariable> getDeclaredVariables() {
        return getVariables();
    }
}
