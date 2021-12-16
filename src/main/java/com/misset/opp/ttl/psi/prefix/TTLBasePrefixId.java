package com.misset.opp.ttl.psi.prefix;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLTypes;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public abstract class TTLBasePrefixId extends ASTWrapperPsiElement implements PsiNameIdentifierOwner, TTLPrefixHolder {
    private static final TokenSet IRIREF = TokenSet.create(TTLTypes.IRIREF);

    public TTLBasePrefixId(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public int getTextOffset() {
        return Optional.ofNullable(getNameIdentifier())
                .map(PsiElement::getTextOffset)
                .orElse(0);
    }

    @Override
    public String getPrefixId() {
        String id = getPrefixIdTextRange().substring(getText());
        if (id.isBlank()) {
            return "unnamed";
        }
        return id;
    }

    @Override
    public TextRange getPrefixIdTextRange() {
        return Optional.ofNullable(getNameIdentifier())
                .map(PsiElement::getTextRangeInParent)
                .orElse(TextRange.EMPTY_RANGE);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return PsiTreeUtil.nextVisibleLeaf(getFirstChild());
    }

    @Override
    public String getName() {
        return getPrefixId();
    }

    @Override
    public String getIri() {
        return Arrays.stream(getNode().getChildren(IRIREF))
                .map(ASTNode::getText)
                .map(TTLResourceUtil::fromIriRef)
                .findFirst()
                .orElse(null);
    }

}
