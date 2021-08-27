package com.misset.opp.omt.psi.model;

import com.misset.opp.omt.psi.OMTVariable;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public interface OMTVariableDeclarator extends YAMLPsiElement {

    List<OMTVariable> getDeclaredVariables();

}
