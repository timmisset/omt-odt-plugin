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
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ODTFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        return buildFoldRegionsWithOffset(root, 0);
    }

    public FoldingDescriptor @NotNull [] buildFoldRegionsWithOffset(@NotNull PsiElement root, int offset) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        // script parts:
        collectDescriptors(descriptors, root, ODTLogicalBlock.class, offset);
        collectDescriptors(descriptors, root, ODTIfBlock.class, offset);
        collectDescriptors(descriptors, root, ODTElseBlock.class, offset);
        collectDescriptors(descriptors, root, ODTCommandBlock.class, offset);

        // queries:
        collectDescriptors(descriptors, root, ODTDefineQueryStatement.class, offset);
        collectDescriptors(descriptors, root, ODTDefineCommandStatement.class, offset);

        // javadocs:
        collectDescriptors(descriptors, root, PsiDocComment.class, offset);

        return descriptors.toArray(FoldingDescriptor[]::new);
    }

    private void collectDescriptors(List<FoldingDescriptor> descriptors,
                                    PsiElement root,
                                    Class<? extends PsiElement> clazz,
                                    int offset) {
        PsiTreeUtil.findChildrenOfType(root, clazz).forEach(
                psiElement -> descriptors.add(new FoldingDescriptor(
                        psiElement.getNode(),
                        new TextRange(psiElement.getTextRange().getStartOffset() + offset,
                                psiElement.getTextRange().getStartOffset() + psiElement.getText().trim().length() + offset),
                        FoldingGroup.newGroup(descriptors.size() + "_foldingGroup")
                ))
        );
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
