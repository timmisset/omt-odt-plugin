package com.misset.opp.omt.psi.impl.model.modelitems;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.psi.impl.model.OMTModelItemAbstract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OMTProcedure extends OMTModelItemAbstract implements OMTModelItem {

    public OMTProcedure(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public OMTModelItemType getType() {
        return OMTModelItemType.Procedure;
    }

    @Override
    public List<OMTVariable> getDeclaredVariables() {
        return getVariables();
    }
}
