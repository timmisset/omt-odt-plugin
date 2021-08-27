package com.misset.opp.omt.psi.impl.model.modelitems;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.util.OMTModelUtil;
import com.misset.opp.omt.util.OMTYamlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OMTEmptyModelItem extends YAMLKeyValueImpl implements OMTModelItem {
    private static final TokenSet TAG = TokenSet.create(YAMLTokenTypes.TAG);
    public OMTEmptyModelItem(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public OMTModelItemType getType() {
        return OMTModelItemType.valueOf(
                OMTYamlUtil.getTagIdentifier(this)
        );
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public List<OMTVariable> getDeclaredVariables() {
        return Collections.emptyList();
    }
}
