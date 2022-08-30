package com.misset.opp.odt.psi;

import com.intellij.psi.tree.IElementType;

public final class ODTIgnored {
    public static final IElementType END_OF_LINE_COMMENT = new ODTElementType("END_OF_LINE_COMMENT");
    public static final IElementType MULTILINE = new ODTElementType("MULTILINE_COMMENT");
    public static final IElementType DOC_COMMENT_START = new ODTElementType("DOC_COMMENT_START");

    private ODTIgnored() {
        // empty constructor
    }
}
