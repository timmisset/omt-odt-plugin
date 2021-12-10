package com.misset.opp.odt.formatter;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The PairedBraceMatcher provides pair highlighting, but also auto-inserts the complementary closing tag
 * The ODTFormattingContext makes sure the indentation is set correctly.
 * <p>
 * example:
 * IF true {<caret>
 * results in:
 * IF true {
 * ....         <-- the indentation is set by the ODTFormattingContext
 * } <-- the closing curly is set by the PairedBraceMatcher
 */
public class ODTPairedBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] pairs = new BracePair[]{
            new BracePair(ODTTypes.CURLY_OPEN, ODTTypes.CURLY_CLOSED, true),
            new BracePair(ODTTypes.BRACKET_OPEN, ODTTypes.BRACKET_CLOSED, true),
            new BracePair(ODTTypes.PARENTHESES_OPEN, ODTTypes.PARENTHESES_CLOSE, true),
    };

    @Override
    public BracePair @NotNull [] getPairs() {
        return pairs;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
