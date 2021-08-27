package com.misset.opp.omt.psi.wrapper;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.impl.OMTVariableImpl;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class OMTElementFactory {

    /**
     * Generic wrapper functionality for the ASTNode
     */
    private static final Key<PsiElement> WRAPPER = new Key<>("Wrapper");
    protected static <T extends PsiElement> T wrap(ASTNode node, T wrapper) {
        node.putUserData(WRAPPER, wrapper);
        return wrapper;
    }
    protected static boolean isWrapped(ASTNode node) {
        return node.getUserData(WRAPPER) != null;
    }
    protected static <T extends PsiElement> T getWrapper(ASTNode node, Class<T> clazz) {
        return clazz.cast(node.getUserData(WRAPPER));
    }

    public static OMTVariable getVariable(YAMLSequenceItem sequenceItem) {
        final ASTNode node = sequenceItem.getNode();
        if(isWrapped(node)) { return getWrapper(node, OMTVariable.class); }

        return wrap(node, new OMTVariableImpl(node));
    }

}
