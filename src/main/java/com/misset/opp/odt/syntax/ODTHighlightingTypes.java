package com.misset.opp.odt.syntax;

import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTElementType;

/**
 * Additional types that are only used for the highlighting lexer
 */
public final class ODTHighlightingTypes {
    public static final IElementType KEYWORD = new ODTElementType("KEYWORD");
    public static final IElementType OPERATOR_SIGN = new ODTElementType("OPERATOR_SIGN");
    public static final IElementType PARENTHESES = new ODTElementType("PARENTHESES");
    public static final IElementType OPERATOR_CALLNAME = new ODTElementType("OPERATOR_CALLNAME");
    public static final IElementType COMMAND_CALLNAME = new ODTElementType("COMMAND_CALLNAME");
    public static final IElementType EMPTY = new ODTElementType("EMPTY");
    public static final IElementType CURIE_PREFIX = new ODTElementType("CURIE_PREFIX");
    public static final IElementType CURIE_LOCALNAME = new ODTElementType("CURIE_LOCALNAME");
    public static final IElementType DEFINE_NAME = new ODTElementType("DEFINE_NAME");
    public static final IElementType NUMBER = new ODTElementType("NUMBER");
    public static final IElementType PATH_DELIMITER = new ODTElementType("PATH_DELIMITER");
    public static final IElementType IDENTIFIER = new ODTElementType("IDENTIFIER");

    private ODTHighlightingTypes() {
        // empty constructor
    }

}
