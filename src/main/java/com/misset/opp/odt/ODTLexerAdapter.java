package com.misset.opp.odt;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.RestartableLexer;
import com.intellij.lexer.TokenIterator;
import org.jetbrains.annotations.NotNull;

public class ODTLexerAdapter extends FlexAdapter implements RestartableLexer {
    public ODTLexerAdapter() {
        super(new ODTLexer(null));
    }

    public ODTLexerAdapter(FlexLexer lexer) {
        super(lexer);
    }

    @Override
    public int getStartState() {
        return ODTLexer.YYINITIAL;
    }

    @Override
    public boolean isRestartableState(int state) {
        return false;
    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState, TokenIterator tokenIterator) {
        super.start(buffer, startOffset, endOffset, initialState);
    }

}
