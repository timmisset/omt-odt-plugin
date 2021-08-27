package com.misset.opp.omt.psi.model.modelitems;

import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.model.OMTVariableDeclarator;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public interface OMTModelItem extends OMTVariableDeclarator {

    OMTModelItemType getType();

    boolean isEmpty();
}
