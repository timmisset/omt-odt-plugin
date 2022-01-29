package com.misset.opp.odt.syntax;

import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTElementType;

/**
 * Additional types that are only used for the highlighting lexer
 */
public interface ODTHighlightingTypes {

    IElementType KEYWORD = new ODTElementType("KEYWORD");
    IElementType OPERATOR_SIGN = new ODTElementType("OPERATOR_SIGN");
    IElementType BRACKETS = new ODTElementType("BRACKETS");
    IElementType PARENTHESES = new ODTElementType("PARENTHESES");
    IElementType OPERATOR_CALLNAME = new ODTElementType("OPERATOR_CALLNAME");
    IElementType COMMAND_CALLNAME = new ODTElementType("COMMAND_CALLNAME");
    IElementType EMPTY = new ODTElementType("EMPTY");
    IElementType CURIE_PREFIX = new ODTElementType("CURIE_PREFIX");
    IElementType CURIE_LOCALNAME = new ODTElementType("CURIE_LOCALNAME");
    IElementType DEFINE_NAME = new ODTElementType("DEFINE_NAME");
    IElementType NUMBER = new ODTElementType("NUMBER");
    IElementType PATH_DELIMITER = new ODTElementType("PATH_DELIMITER");
    IElementType IDENTIFIER = new ODTElementType("IDENTIFIER");

}
