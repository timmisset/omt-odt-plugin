// Based on RDF 1.1 Turtle
// https://www.w3.org/TR/turtle/

package com.misset.opp.ttl;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.psi.TTLTypes;
import com.misset.opp.ttl.psi.TTLIgnored;

%%

%class TTLLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%eof{  return;
%eof}

%state PREFIX

IRIREF	                                =	"<" ([^\x00-\x20\<\>\"\{\}\|\^\`\\] | {UCHAR})+ ">" /* #x00=NULL #01-#x1F=control codes #x20=space */
PNAME_NS                                =	{PN_PREFIX}? ":"
PNAME_LN                                =	{PNAME_NS} {PN_LOCAL}
BLANK_NODE_LABEL                        =	"_:" ({PN_CHARS_U} | [0-9]) (({PN_CHARS} | ".")* {PN_CHARS})?
LANGTAG                                 =	"@" [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
INTEGER                                 =	[+-]? [0-9]+
DECIMAL                                 =	[+-]? [0-9]* "." [0-9]+
DOUBLE                                  =	[+-]? ([0-9]+ "." [0-9]* {EXPONENT} | "." [0-9]+ {EXPONENT} | [0-9]+ {EXPONENT})
EXPONENT                                =	[eE] [+-]? [0-9]+
STRING_LITERAL_QUOTE                    =	"\"" ([^\x22\x5C\n\r] | {ECHAR} | {UCHAR})* "\"" /* #x22=" #x5C=\ #xA=new line (\n) #xD=carriage return (\r) */
STRING_LITERAL_SINGLE_QUOTE             =	"\'" ([^\x27\x5C\n\r] | {ECHAR} | {UCHAR})* "\'" /* #x27=' #x5C=\ #xA=new line (\n) #xD=carriage return (\r) */
STRING_LITERAL_LONG_SINGLE_QUOTE        =	"\'\'\'" (("\'" | "\'\'")? ([^\'\\] | {ECHAR} | {UCHAR}))* "\'\'\'"
STRING_LITERAL_LONG_QUOTE               =	"\"\"\"" (("\"" | "\"\"\"")? ([^\"\\] | {ECHAR} | {UCHAR}))* "\"\"\""
UCHAR                                   =	"\u" {HEX} {HEX} {HEX} {HEX} | "\U" {HEX} {HEX} {HEX} {HEX} {HEX} {HEX} {HEX} {HEX}
ECHAR                                   =	"\\" [tbnrf\"\'\\]
/*
Official documentation:
#x20=space #x9=character tabulation #xD=carriage return #xA=new line.
Use any whitespace instead
*/
WHITE_SPACE                             =	\s+
ANON                                    =	"[" {WHITE_SPACE} "]"
PN_CHARS_BASE                           =	[A-Z] | [a-z] | [\u00C0-\u00D6] | [\u00D8-\u00F6] | [\u00F8-\u02FF] | [\u0370-\u037D] | [\u037F-\u1FFF] | [\u200C-\u200D] | [\u2070-\u218F] | [\u2C00-\u2FEF] | [\u3001-\uD7FF] | [\uF900-\uFDCF] | [\uFDF0-\uFFFD]
PN_CHARS_U                              =	{PN_CHARS_BASE} | "_"
PN_CHARS                                =	{PN_CHARS_U} | "-" | [0-9] | \u00B7 | [\u0300-\u036F] | [\u203F-\u2040]
PN_PREFIX                               =	{PN_CHARS_BASE} (({PN_CHARS} | '.')* {PN_CHARS})?
PN_LOCAL                                =	({PN_CHARS_U} | ":" | [0-9] | PLX) (({PN_CHARS} | "." | ":" | {PLX})* ({PN_CHARS} | ":" | {PLX}))?
PLX                                     =	{PERCENT} | {PN_LOCAL_ESC}
PERCENT                                 =	"%" {HEX} {HEX}
HEX                                     =	[0-9] | [A-F] | [a-f]
PN_LOCAL_ESC                            =	'\' ('_' | '~' | '.' | '-' | '!' | '\$' | '&' | "'" | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '\/' | '?' | '#' | '@' | '%')

BASE_URI                                =   "# baseURI: " [^\r\n]*
IMPORT_URI                              =   "# imports: " [^\r\n]*
COMMENT                                 =   "#" [^\r\n]*
ATPREFIX                                =   "@prefix"
PREFIX                                  =   "PREFIX"
ATBASE                                  =   "@base"
BASE                                    =   "BASE"
DOT                                     =   "\."
A                                       =   "a"
SEMICOLON                               =   ";"
COMMA                                   =   ","
BRACKET_OPEN                            =   "["
BRACKET_CLOSE                           =   "]"
PARENTHESES_OPEN                        =   "("
PARENTHESES_CLOSE                       =   ")"
DATATYPE_LEADING                        =   "^^"
TRUE                                    =   "true"
FALSE                                   =   "false"
%%
<YYINITIAL> {

    {IRIREF}                            { return TTLTypes.IRIREF; }
    {PNAME_NS}                          { return TTLTypes.PNAME_NS; }
    {PNAME_LN}                          {
          yypushback(yylength() - yytext().toString().indexOf(":") - 1);
          yybegin(PREFIX);
          return TTLTypes.PNAME_NS;
      }
    {BLANK_NODE_LABEL}                  { return TTLTypes.BLANK_NODE_LABEL; }
    {INTEGER}                           { return TTLTypes.INTEGER; }
    {DECIMAL}                           { return TTLTypes.DECIMAL; }
    {DOUBLE}                            { return TTLTypes.DOUBLE; }
    {STRING_LITERAL_QUOTE}              { return TTLTypes.STRING_LITERAL_QUOTE; }
    {STRING_LITERAL_SINGLE_QUOTE}       { return TTLTypes.STRING_LITERAL_SINGLE_QUOTE; }
    {STRING_LITERAL_LONG_SINGLE_QUOTE}  { return TTLTypes.STRING_LITERAL_LONG_SINGLE_QUOTE; }
    {STRING_LITERAL_LONG_QUOTE}         { return TTLTypes.STRING_LITERAL_LONG_QUOTE; }
    {ANON}                              { return TTLTypes.ANON; }
    {ATPREFIX}                          { return TTLTypes.ATPREFIX; }
    {PREFIX}                            { return TTLTypes.PREFIX_LEADING; }
    {ATBASE}                            { return TTLTypes.ATBASE; }
    {BASE}                              { return TTLTypes.BASE_LEADING; }
    {DOT}                               { return TTLTypes.DOT; }
    {A}                                 { return TTLTypes.A; }
    {SEMICOLON}                         { return TTLTypes.SEMICOLON; }
    {COMMA}                             { return TTLTypes.COMMA; }
    {BRACKET_OPEN}                      { return TTLTypes.BRACKET_OPEN; }
    {BRACKET_CLOSE}                     { return TTLTypes.BRACKET_CLOSE; }
    {PARENTHESES_OPEN}                  { return TTLTypes.PARENTHESES_OPEN; }
    {PARENTHESES_CLOSE}                 { return TTLTypes.PARENTHESES_CLOSE; }
    {DATATYPE_LEADING}                  { return TTLTypes.DATATYPE_LEADING; }
    {TRUE}                              { return TTLTypes.TRUE; }
    {FALSE}                             { return TTLTypes.FALSE; }

    {BASE_URI}                          { return TTLTypes.BASE_URI; }
    {IMPORT_URI}                        { return TTLTypes.IMPORT_URI; }

    {COMMENT}                           { return TTLIgnored.COMMENT; }
    {WHITE_SPACE}                       { return TokenType.WHITE_SPACE; }

    {LANGTAG}                           { return TTLTypes.LANGTAG; }

    [^]                                 { return TokenType.BAD_CHARACTER; }
}

<PREFIX> {
    {PN_LOCAL}                          {
          yybegin(YYINITIAL);
          return TTLTypes.PN_LOCAL; }
    [^]                                 { return TokenType.BAD_CHARACTER; }
}
