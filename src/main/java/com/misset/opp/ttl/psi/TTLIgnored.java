package com.misset.opp.ttl.psi;

import com.intellij.psi.tree.IElementType;

public final class TTLIgnored {
    public static final IElementType COMMENT = new TTLElementType("COMMENT");

    private TTLIgnored() {
        // empty constructor
    }
}
