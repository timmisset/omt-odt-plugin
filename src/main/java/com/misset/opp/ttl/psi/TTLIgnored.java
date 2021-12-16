package com.misset.opp.ttl.psi;

import com.intellij.psi.tree.IElementType;

public interface TTLIgnored {
    IElementType COMMENT = new TTLElementType("COMMENT");
}
