// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.misset.opp.odt;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.ODTIgnored;
import com.intellij.psi.TokenType;

%%

%class ODTLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%eof{  return;
%eof}

WHITE_SPACE=                    [\n\ \f\t\ ]
ALPHA=                          [A-Za-z]
UNDERSCORE=                     [_]
DIGIT=                          [0-9]
STRING=                         (\"[^\"\\]*(\\.[^\"\\]*)*\")|(\'[^\'\\]*(\\.[^\'\\]*)*\')
INTEGER=                        \-?([1-9][0-9]+|[0-9])
DECIMAL=                        {INTEGER}\.[0-9]+
BOOLEAN=                        "true"|"false"|"TRUE"|"FALSE"|"True"|"False"
NULL=                           "null"

LATIN_EXT_A=                    [\u0100-\u017F] // Zie: http://en.wikipedia.org/wiki/Latin_script_in_Unicode
SYMBOL=                         ({ALPHA}|{DIGIT}|{LATIN_EXT_A}|[_@\-])+
SCHEME=                         {ALPHA}({ALPHA}|{DIGIT}|[+.-])*
IRI=                            "<"{SCHEME}":"({SYMBOL}|[?&#/+*.-])+">"
INCOMPLETE_IRI=                 "<"{SCHEME}":"({SYMBOL}|[?&#/+*.-])+
NAME=                           {ALPHA}({ALPHA}|{DIGIT}|{UNDERSCORE})*
CURIE=                          ({NAME})?":"{SYMBOL}
TYPED_VALUE=                    {STRING}"^^"({IRI}|{CURIE})
VARIABLENAME=                   "$"{NAME} | "$_"

// ignored
END_OF_LINE_COMMENT=            ("#" | "\/\/")[^\r\n]*
JAVADOCS=                        \/\*\*\s*\n([^\*]|(\*[^\/]))+\*\*\/
MULTILINECOMMENT=                \/\*\s*\n([^\*]|(\*[^\/]))+\*\/

// YYINITIAL state can only have a limited selection of tokens that can trigger indentation
RESERVED_NAME=                  "IF"

%state FORCED_NAME

%%
<YYINITIAL> {
    // Firstly, we need to check if the SCALAR state is still applicable
    // Only the indentation of the INITIAL state is recorded, so when the indentation in the SCALAR state is
    // <= than the last recorded indentation we can exit the scalar
    {BOOLEAN}                                                 { return ODTTypes.BOOLEAN; }
    {NULL}                                                    { return ODTTypes.NULL; }

    "DEFINE"                                                        { return ODTTypes.DEFINE_START; }
    "QUERY"                                                         { return ODTTypes.DEFINE_QUERY; }
    "COMMAND"                                                       { return ODTTypes.DEFINE_COMMAND; }
    "VAR"                                                           { return ODTTypes.DECLARE_VAR; }
    "PREFIX"                                                        { return ODTTypes.PREFIX_DEFINE_START; }
    {VARIABLENAME}                                                  { return ODTTypes.VARIABLE_NAME; }

    "!"+{NAME}                                                      { return ODTTypes.TAG; }

    // the lambda is used for assigning the actual query/command block to it's constructor
    // and to assign a path to case condition
    "=>"                                                            { return ODTTypes.LAMBDA; }
    "="                                                             { return ODTTypes.EQUALS; }

    // ODT operators
    // certain operators are used for assertions and should be recognized. They can be used within querysteps (grammar part)
    // when making filters or other boolean assertions
    "AND(" | "OR(" | "NOT("                                         { yypushback(1); return ODTTypes.NAME; }
    "AND" | "OR"                                                    { return ODTTypes.BOOLEAN_OPERATOR; }
    "NOT"                                                           { return ODTTypes.NOT_OPERATOR; }
    ">=" | "<=" | "==" | ">" | "<"                                  { return ODTTypes.CONDITIONAL_OPERATOR; }
    "@" {RESERVED_NAME}                                             {
          yypushback(yylength() - 1);
          yybegin(FORCED_NAME);
          return ODTTypes.AT; }
    "IF"                                                            { return ODTTypes.IF_OPERATOR; }
    "ELSE"                                                          { return ODTTypes.ELSE_OPERATOR; }
    "CHOOSE"                                                        { return ODTTypes.CHOOSE_OPERATOR; }
    "WHEN"                                                          { return ODTTypes.WHEN_OPERATOR; }
    "OTHERWISE"                                                     { return ODTTypes.OTHERWISE_OPERATOR; }
    "END"                                                           { return ODTTypes.END_OPERATOR; }
    "RETURN"                                                        { return ODTTypes.RETURN_OPERATOR; }

    {IRI}                                                           { return ODTTypes.IRI; }
    {INCOMPLETE_IRI}                                                { return TokenType.BAD_CHARACTER; }
    {STRING}                                                        { return ODTTypes.STRING; }
    {INTEGER}                                                       { return ODTTypes.INTEGER; }
    {DECIMAL}                                                       { return ODTTypes.DECIMAL; }
    {TYPED_VALUE}                                                   { return ODTTypes.TYPED_VALUE; }
    "<"{NAME}">"                                                    { return ODTTypes.OWLPROPERTY; }
    {NAME}                                                          { return ODTTypes.NAME; }
    "|"                                                             { return ODTTypes.PIPE; }
    "@"                                                             { return ODTTypes.AT; }
    ":"                                                             { return ODTTypes.COLON; }
    "="                                                             { return ODTTypes.EQUALS; }
    ","                                                             { return ODTTypes.COMMA; }
    ";"                                                             { return ODTTypes.SEMICOLON; }
    "{"                                                             { return ODTTypes.CURLY_OPEN; }
    "}"                                                             { return ODTTypes.CURLY_CLOSED; }
    "/"                                                             { return ODTTypes.FORWARD_SLASH; }
    "/DUMMY_SLASH"                                                  { return ODTTypes.DUMMY_SLASH; }
    "^"                                                             { return ODTTypes.CARET; }
    "[]"                                                            { return ODTTypes.EMPTY_ARRAY; }
    "["                                                             { return ODTTypes.BRACKET_OPEN; }
    "]"                                                             { return ODTTypes.BRACKET_CLOSED; }
    "+"                                                             { return ODTTypes.PLUS; }
    "("                                                             { return ODTTypes.PARENTHESES_OPEN; }
    ")"                                                             { return ODTTypes.PARENTHESES_CLOSE; }
    "\."                                                            { return ODTTypes.DOT; }
    "+="                                                            { return ODTTypes.ADD; }
    "-="                                                            { return ODTTypes.REMOVE; }
    "*"                                                             { return ODTTypes.ASTERIX; }
    "?"                                                             { return ODTTypes.QUESTION_MARK; }
    ";"                                                             { return ODTTypes.SEMICOLON; }

    // Javadocs in the Scalar are not indented but are anchored directly as leading block to the next Psi element
    {JAVADOCS}                                                      {
                                                                        return ODTIgnored.JAVADOCS; // can be an indent/dedent token or JAVADOCS_START
                                                                    }
    {MULTILINECOMMENT}                                              {
                                                                        return ODTIgnored.MULTILINE; // can be an indent/dedent token or JAVADOCS_START
                                                                    }
    {END_OF_LINE_COMMENT}                                           {
                                                                        return ODTIgnored.END_OF_LINE_COMMENT;
                                                                    }
    { WHITE_SPACE }+                                                {
                                                                        return TokenType.WHITE_SPACE;
      }
}
<FORCED_NAME> {
    {NAME}                                                          {
          yybegin(YYINITIAL);
          return ODTTypes.NAME; }
}

// Not all states have an escape. When the SCALAR hits a bad character it should result in a syntax error
[^]                                                                  { return TokenType.BAD_CHARACTER; }
