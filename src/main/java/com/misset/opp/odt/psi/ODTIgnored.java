package com.misset.opp.odt.psi;

import com.intellij.psi.tree.IElementType;

public interface ODTIgnored {
    IElementType END_OF_LINE_COMMENT = new ODTElementType("END_OF_LINE_COMMENT");
    IElementType MULTILINE = new ODTElementType("MULTILINE_COMMENT");
    IElementType DOC_COMMENT_START = new ODTElementType("DOC_COMMENT_START");
}
