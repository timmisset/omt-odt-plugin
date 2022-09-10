package com.misset.opp.odt.testcase;

import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.ODTLexerAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ODTLexerTestCase extends ODTTestCase {
    @BeforeEach
    public final void setUp() {
        // no fixture required, don't call super
        // extends OMTTestCase for assertion support
    }

    @AfterEach
    protected final void tearDown() {
        // no fixture required, don't call super
        // extends OMTTestCase for assertion support
    }

    protected boolean hasBadCharacter(String content) {
        return getElements(content).contains("BAD_CHARACTER");
    }

    protected boolean hasElement(String content, IElementType... tokenType) {
        final List<String> collect = Arrays.stream(tokenType)
                .map(IElementType::toString)
                .collect(Collectors.toList());
        return getElements(content).stream().anyMatch(collect::contains);
    }

    protected List<String> getElements(String content) {
        return getElements(content, 0, content.length());
    }

    protected List<String> getElements(String content, int start, int end) {
        ODTLexerAdapter lexer = new ODTLexerAdapter();
        lexer.start(content, start, end, 0);
        List<String> elements = new ArrayList<>();
        IElementType element = lexer.getTokenType();
        if (element != null) {
            elements.add(element.toString());
        }
        boolean cont = true;
        while (cont) {
            lexer.advance();
            element = lexer.getTokenType();
            if (element != null) {
                elements.add(element.toString());
            } else {
                cont = false;
            }
        }
        return elements;
    }

}
