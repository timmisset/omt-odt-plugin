package com.misset.opp.odt.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.misset.opp.odt.psi.impl.variables.ODTDefinedVariableImpl;
import com.misset.opp.odt.psi.impl.variables.ODTUsageVariableImpl;
import org.jetbrains.annotations.Nullable;

import static com.misset.opp.odt.psi.ODTTypes.DECLARE_VARIABLE;
import static com.misset.opp.odt.psi.ODTTypes.VARIABLE;
import static com.misset.opp.odt.psi.ODTTypes.VARIABLE_ASSIGNMENT;
import static com.misset.opp.odt.psi.ODTTypes.VARIABLE_VALUE;

/**
 * The ODT PsiElement interfaces and their implementation classes are auto-generated based on the
 * DSL lexer and grammar parser.
 * The ODTFactoryInterceptor class will take more context into consideration and 'intercept' the
 * creation process and provide a more specific class instead of the auto-generated one
 */
public class ODTFactoryInterceptor {
    private static final TokenSet ALL_VARIABLE_CONTAINERS = TokenSet.create(VARIABLE_ASSIGNMENT, VARIABLE_VALUE, DECLARE_VARIABLE);
    private static final TokenSet DECLARED_VARIABLE_CONTAINERS = TokenSet.create(VARIABLE_ASSIGNMENT, DECLARE_VARIABLE);

    public static PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();
        if(type == VARIABLE) {
            if(DECLARED_VARIABLE_CONTAINERS.contains(getParentType(node, ALL_VARIABLE_CONTAINERS))) {
                return new ODTDefinedVariableImpl(node);
            } else {
                return new ODTUsageVariableImpl(node);
            }
        }
        return null;
    }

    /**
     * Returns the first parent that matches any of the given types
     */
    @Nullable
    private static IElementType getParentType(ASTNode node, TokenSet tokenSet) {
        while(node != null && !tokenSet.contains(node.getElementType())) {
            node = node.getTreeParent();
        }
        return node != null ? node.getElementType() : null;
    }
}
