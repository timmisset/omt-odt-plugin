package com.misset.opp.omt;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTKeyValueImpl;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTPlainTextImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLParserDefinition;

/**
 * Little hack to hook into the YAML element creation
 * All OMT elements are part of the YAML + OMT language. However, the ParserDefinition selecting
 * mechanism, when the PsiElements are created, is looking at the root language.
 * <p>
 * For that reason, this parser is registered as pure yaml with a 'first' priority.
 */
public class YAMLOMTParserDefinition extends YAMLParserDefinition implements ParserDefinition {
    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        final IElementType type = node.getElementType();
        if (type == KEY_VALUE_PAIR) {
            return new YAMLOMTKeyValueImpl(node);
        } else if (type == SCALAR_PLAIN_VALUE) {
            return new YAMLOMTPlainTextImpl(node);
        }
        return super.createElement(node);
    }

}
