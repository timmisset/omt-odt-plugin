package com.misset.opp.odt;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCommandBlock;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ODTFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    private static final TokenSet COMMAND_BLOCK = TokenSet.create(ODTTypes.SCRIPT);
    private static final TokenSet DEFINE_QUERY_STATEMENT =
            TokenSet.create(ODTTypes.QUERY_ARRAY, ODTTypes.QUERY_PATH, ODTTypes.EQUATION_STATEMENT, ODTTypes.BOOLEAN_STATEMENT);

    private static final Function<ASTNode, ASTNode> getQueryNode = node -> {
        ASTNode[] children = node.getChildren(DEFINE_QUERY_STATEMENT);
        return children[0];
    };

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        return buildFoldRegionsWithOffset(root, 0);
    }

    public FoldingDescriptor @NotNull [] buildFoldRegionsWithOffset(@NotNull PsiElement root, int offset) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        // script parts:
        collectDescriptors(descriptors, root, ODTCommandBlock.class, node -> node, offset);

        // queries:
        collectDescriptors(descriptors, root, ODTDefineQueryStatement.class, getQueryNode, offset);

        // javadocs:
        collectDescriptors(descriptors, root, PsiDocComment.class, node -> node, offset);

        return descriptors.toArray(FoldingDescriptor[]::new);
    }

    private void collectDescriptors(List<FoldingDescriptor> descriptors,
                                    PsiElement root,
                                    Class<? extends PsiElement> clazz,
                                    Function<ASTNode, ASTNode> nodeToFold,
                                    int offset) {
        PsiTreeUtil.findChildrenOfType(root, clazz).forEach(
                psiElement -> {
                    ASTNode node = nodeToFold.apply(psiElement.getNode());
                    descriptors.add(new FoldingDescriptor(
                            node,
                            new TextRange(node.getTextRange().getStartOffset() + offset,
                                    node.getTextRange().getStartOffset() + node.getText().trim().length() + offset),
                            FoldingGroup.newGroup(descriptors.size() + "_foldingGroup")
                    ));
                }
        );
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        if (node.getElementType() == ODTTypes.COMMAND_BLOCK) {
            return getCollapsedCommandBlock(node);
        }
        return "...";
    }

    private String getCollapsedCommandBlock(ASTNode node) {
        ASTNode[] children = node.getChildren(COMMAND_BLOCK);
        if (children.length == 0) {
            return "{...}";
        }

        ASTNode child = children[0];
        if (child != null && child.getText().length() < 10) {
            return "{ " + child.getText() + " }";
        } else {
            return "{...}";
        }
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
