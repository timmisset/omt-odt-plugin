// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.misset.opp.odt;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.ODTIgnored;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.JavaDocElementType;

%%

%class ODTLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%eof{  return;
%eof}

// ODT Grammatica scanner

/*
    SYMBOL bevat niet de volledige unicode basic-latin set omdat veel karakters speciale betekenis hebben
    in de ODT grammatica.
*/

WHITE_SPACE=                    [\n\ \f\t\ ]
ALPHA=                          [A-Za-z]
UNDERSCORE=                     [_]
DIGIT=                          [0-9]
LATIN_EXT_A=                    [\u0100-\u017F] // Zie: http://en.wikipedia.org/wiki/Latin_script_in_Unicode
SYMBOL=                         ({ALPHA}|{DIGIT}|{UNDERSCORE})({ALPHA}|{DIGIT}|{LATIN_EXT_A}|[_@\-])*

SCHEME=                         ({ALPHA}|{DIGIT})({ALPHA}|{DIGIT}|[+.-])*
IRI=                            "<"{SCHEME}":"({SYMBOL}|[?&#/+*.-])+">"
SCHEMALESS_IRI=                 "<"({SYMBOL}|[?&#/+*.-])+">"
BNODE=                          "<"{UNDERSCORE}":"({SYMBOL}|[?&#/+*.-])+">"

CURIE_PREFIX=                   ({ALPHA}({ALPHA}|{DIGIT})*)?":"
CURIE=                          {CURIE_PREFIX}{SYMBOL}

STRING=                         (\"[^\"\\]*(\\.[^\"\\]*)*\")|(\'[^\'\\]*(\\.[^\'\\]*)*\')
INTERPOLATED_STRING=            (\`[^\`\\]*(\\.[^\`\\]*)*\`)
INTEGER=                        \-?([1-9][0-9]+|[0-9])
DECIMAL=                        {INTEGER}\.[0-9]+
BOOLEAN=                        "true"|"false"|"TRUE"|"FALSE"|"True"|"False"
NULL=                           "null"
PRIMITIVE=                      "string"|"number"|"integer"|"decimal"|"date"|"dateTime"|"time"|"boolean"

TYPED_VALUE=                    {STRING}"^^"({IRI}|{CURIE})
VARIABLENAME=                   "$"{SYMBOL}

// ignored
END_OF_LINE_COMMENT=            ("#" | "\/\/")[^\r\n]*
JAVADOCS=                        \/\*\*\s*\n([^\*]|(\*[^\/]))+\*\/
MULTILINECOMMENT=                \/\*\s*\n([^\*]|(\*[^\/]))+\*\/

// YYINITIAL state can only have a limited selection of tokens that can trigger indentation
RESERVED_NAME=                  "IF"

%state FORCED_NAME
%state PREFIX
%state DEFINE
%state DEFINE_PARAMS
%state INTERPOLATION

%{
    boolean inInterpolation = false;
    void enterInterpolation() {
        inInterpolation = true;
        yybegin(INTERPOLATION);
    }
    void leaveInterpolation() {
        inInterpolation = false;
        yybegin(YYINITIAL);
    }
%}

%%
<YYINITIAL> {
    "PREFIX"                                                  { yybegin(PREFIX); return ODTTypes.PREFIX_DEFINE_START; }
    "DEFINE"                                                  { yybegin(DEFINE); return ODTTypes.DEFINE_START; }

    {BOOLEAN}                                                 { return ODTTypes.BOOLEAN; }
    {NULL}                                                    { return ODTTypes.NULL; }


    "VAR"                                                     { return ODTTypes.DECLARE_VAR; }
    {VARIABLENAME}                                            { return ODTTypes.VARIABLE_NAME; }

    "!"+{SYMBOL}                                              { return ODTTypes.TAG; }

    // the lambda is used for assigning the actual query/command block to it's constructor
    // and to assign a path to case condition
    "=>"                                                            { return ODTTypes.LAMBDA; }
    "="                                                             { return ODTTypes.EQUALS; }

    // ODT operators
    // certain operators are used for assertions and should be recognized. They can be used within querysteps (grammar part)
    // when making filters or other boolean assertions
    "AND(" | "OR(" | "NOT("                                         { yypushback(1); return ODTTypes.SYMBOL; }
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
    {SCHEMALESS_IRI}                                                { return ODTTypes.SCHEMALESS_IRI; }
    {STRING}                                                        { return ODTTypes.STRING; }
    {INTERPOLATED_STRING}                                           {
          yypushback(yylength() - 1); // pushback all but the opening backtick
          enterInterpolation();
          return ODTTypes.INTERPOLATED_STRING_START;
                                                                    }
    {INTEGER}                                                       { return ODTTypes.INTEGER; }
    {DECIMAL}                                                       { return ODTTypes.DECIMAL; }
    {TYPED_VALUE}                                                   { return ODTTypes.TYPED_VALUE; }
    {PRIMITIVE}                                                     { return ODTTypes.PRIMITIVE; }
    {SYMBOL}                                                        { return ODTTypes.SYMBOL; }
    "|"                                                             { return ODTTypes.PIPE; }
    "@"                                                             { return ODTTypes.AT; }
    ":"                                                             { return ODTTypes.COLON; }
    "="                                                             { return ODTTypes.EQUALS; }
    ","                                                             { return ODTTypes.COMMA; }
    ";"                                                             { return ODTTypes.SEMICOLON; }
    "{"                                                             { return ODTTypes.CURLY_OPEN; }
    "}"                                                             {
          if(inInterpolation) { enterInterpolation(); return ODTTypes.INTERPOLATION_END; }
          return ODTTypes.CURLY_CLOSED; }
    "/"                                                             { return ODTTypes.FORWARD_SLASH; }
    "^"                                                             { return ODTTypes.CARET; }
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
}
<FORCED_NAME> {
    {SYMBOL}                                                        {
          yybegin(YYINITIAL);
          return ODTTypes.SYMBOL; }
}

<PREFIX> {
    ":"                          { return ODTTypes.COLON; }
    {CURIE_PREFIX}               { yypushback(1); return ODTTypes.SYMBOL; }
    {IRI}                        { yybegin(YYINITIAL); return ODTTypes.IRI; }
    "<"[^>]*">"                  { return TokenType.BAD_CHARACTER; }
    { WHITE_SPACE }+             { return TokenType.WHITE_SPACE; }
}
<DEFINE> {
    "QUERY"                      { return ODTTypes.DEFINE_QUERY; }
    "COMMAND"                    { return ODTTypes.DEFINE_COMMAND; }
    {SYMBOL}                     { return ODTTypes.SYMBOL; }
    "=>"                         { yybegin(YYINITIAL); return ODTTypes.LAMBDA; }
    "("                          { yybegin(DEFINE_PARAMS); return ODTTypes.PARENTHESES_OPEN; }
    { WHITE_SPACE }+             { return TokenType.WHITE_SPACE; }
}
<DEFINE_PARAMS> {
    {VARIABLENAME}               { return ODTTypes.VARIABLE_NAME; }
    ","                          { return ODTTypes.COMMA; }
    ")"                          { yybegin(DEFINE); return ODTTypes.PARENTHESES_CLOSE; }
    { WHITE_SPACE }+             { return TokenType.WHITE_SPACE; }
}
<INTERPOLATION> {
    "${"                         { yybegin(YYINITIAL); return ODTTypes.INTERPOLATION_START; }
    "`"                          { leaveInterpolation(); return ODTTypes.INTERPOLATED_STRING_END; }
    [^\$\`]+                       { return ODTTypes.STRING; }
}
{JAVADOCS}                                                      {
                                                                    return JavaDocElementType.DOC_COMMENT; // can be an indent/dedent token or JAVADOCS_START
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

// Not all states have an escape. When the SCALAR hits a bad character it should result in a syntax error
[^]                                                                  { return TokenType.BAD_CHARACTER; }
