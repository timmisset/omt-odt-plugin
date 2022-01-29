// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.misset.opp.odt;

import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.syntax.ODTHighlightingTypes;
import com.misset.opp.odt.psi.ODTIgnored;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.JavaDocElementType;

%%

%class ODTHighlightingLexer
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

%state COMMAND_CALLNAME
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
    "PREFIX"                                                  { yybegin(PREFIX); return ODTHighlightingTypes.KEYWORD; }
    "DEFINE"                                                  { yybegin(DEFINE); return ODTHighlightingTypes.KEYWORD; }
    "/**"                                                     { return ODTIgnored.DOC_COMMENT_START; }

    {BOOLEAN}                                                 { return ODTTypes.BOOLEAN; }
    {NULL}                                                    { return ODTTypes.NULL; }
    {CURIE}                                                   { yypushback(yylength()); yybegin(PREFIX); return ODTHighlightingTypes.EMPTY; }


    "VAR"                                                     { return ODTHighlightingTypes.KEYWORD; }
    {VARIABLENAME}                                            { return ODTTypes.VARIABLE_NAME; }

    "!"+{SYMBOL}                                              { return ODTTypes.TAG; }

    // the lambda is used for assigning the actual query/command block to it's constructor
    // and to assign a path to case condition
    "=>"                                                            { return ODTTypes.LAMBDA; }
    "="                                                             { return ODTHighlightingTypes.OPERATOR_SIGN; }

    // ODT operators
    "AND" | "OR"                                                    { return ODTHighlightingTypes.KEYWORD; }
    "NOT"                                                           { return ODTHighlightingTypes.KEYWORD; }
    ">=" | "<=" | "==" | ">" | "<"                                  { return ODTHighlightingTypes.OPERATOR_SIGN; }
    "@" {SYMBOL}                                                    {
              yypushback(yylength() - 1);
              yybegin(COMMAND_CALLNAME);
              return ODTTypes.AT; }
    "IF"                                                            { return ODTHighlightingTypes.KEYWORD; }
    "ELSE"                                                          { return ODTHighlightingTypes.KEYWORD; }
    "CHOOSE"                                                        { return ODTHighlightingTypes.KEYWORD; }
    "WHEN"                                                          { return ODTHighlightingTypes.KEYWORD; }
    "OTHERWISE"                                                     { return ODTHighlightingTypes.KEYWORD; }
    "END"                                                           { return ODTHighlightingTypes.KEYWORD; }
    "RETURN"                                                        { return ODTHighlightingTypes.KEYWORD; }

    {IRI}                                                           { return ODTTypes.IRI; }
    {SCHEMALESS_IRI}                                                { return ODTTypes.SCHEMALESS_IRI; }
    {STRING}                                                        { return ODTTypes.STRING; }
    {INTERPOLATED_STRING}                                           {
          yypushback(yylength() - 1); // pushback all but the opening backtick
          enterInterpolation();
          return ODTTypes.INTERPOLATED_STRING_START;
                                                                    }
    {INTEGER}                                                       { return ODTHighlightingTypes.NUMBER; }
    {DECIMAL}                                                       { return ODTHighlightingTypes.NUMBER; }
    {TYPED_VALUE}                                                   { return ODTTypes.TYPED_VALUE; }
    {PRIMITIVE}                                                     { return ODTTypes.PRIMITIVE; }
    {SYMBOL}                                                        { return ODTHighlightingTypes.OPERATOR_CALLNAME; }
    "|"                                                             { return ODTTypes.PIPE; }
    "@"                                                             { return ODTTypes.AT; }
    ":"                                                             { return ODTTypes.COLON; }
    "="                                                             { return ODTHighlightingTypes.OPERATOR_SIGN; }
    ","                                                             { return ODTTypes.COMMA; }
    ";"                                                             { return ODTTypes.SEMICOLON; }
    // Open and Close need to be seperate for the PairedBraceMatcher to do its work
    "{"                                                             { return ODTTypes.CURLY_OPEN; }
    "}"                                                             {
          if(inInterpolation) { enterInterpolation(); return ODTTypes.INTERPOLATION_END; }
          return ODTTypes.CURLY_CLOSED; }
    "/"                                                             { return ODTHighlightingTypes.PATH_DELIMITER; }
    "^"                                                             { return ODTTypes.CARET; }
    "\."                                                            { return ODTHighlightingTypes.IDENTIFIER; }
    "["                                                             { return ODTHighlightingTypes.BRACKETS; }
    "]"                                                             { return ODTHighlightingTypes.BRACKETS; }
    "("                                                             { return ODTHighlightingTypes.PARENTHESES; }
    ")"                                                             { return ODTHighlightingTypes.PARENTHESES; }
    "+"                                                             { return ODTHighlightingTypes.OPERATOR_SIGN; }
    "+="                                                            { return ODTHighlightingTypes.OPERATOR_SIGN; }
    "-="                                                            { return ODTHighlightingTypes.OPERATOR_SIGN; }
    "*"                                                             { return ODTTypes.ASTERIX; }
    "?"                                                             { return ODTTypes.QUESTION_MARK; }
    ";"                                                             { return ODTTypes.SEMICOLON; }
}
<COMMAND_CALLNAME> {
    {SYMBOL}                                                        {
          yybegin(YYINITIAL);
          return ODTHighlightingTypes.COMMAND_CALLNAME; }
}

<PREFIX> {
    ":"                          { return ODTTypes.COLON; }
    {CURIE_PREFIX}               { yypushback(1); return ODTHighlightingTypes.CURIE_PREFIX; }
    {IRI}                        { yybegin(YYINITIAL); return ODTTypes.IRI; }
    {SYMBOL}                     { yybegin(YYINITIAL); return ODTHighlightingTypes.CURIE_LOCALNAME; }
    "<"[^>]*">"                  { return TokenType.BAD_CHARACTER; }
    { WHITE_SPACE }+             { return TokenType.WHITE_SPACE; }
}
<DEFINE> {
    "QUERY"                      { return ODTHighlightingTypes.KEYWORD; }
    "COMMAND"                    { return ODTHighlightingTypes.KEYWORD; }
    {SYMBOL}                     { return ODTHighlightingTypes.DEFINE_NAME; }
    "=>"                         { yybegin(YYINITIAL); return ODTTypes.LAMBDA; }
    "("                          { yybegin(DEFINE_PARAMS); return ODTHighlightingTypes.PARENTHESES; }
    { WHITE_SPACE }+             { return TokenType.WHITE_SPACE; }
}
<DEFINE_PARAMS> {
    {VARIABLENAME}               { return ODTTypes.VARIABLE_NAME; }
    ","                          { return ODTTypes.COMMA; }
    ")"                          { yybegin(DEFINE); return ODTHighlightingTypes.PARENTHESES; }
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
