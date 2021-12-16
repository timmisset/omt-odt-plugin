package com.misset.opp.ttl;

import com.intellij.lexer.FlexAdapter;

public class TTLLexerAdapter extends FlexAdapter {
    public TTLLexerAdapter() {
        super(new TTLLexer(null));
    }
}
