// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static com.misset.opp.ttl.psi.TTLTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TTLParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return TTLFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[]{
          create_token_set_(BASE, DIRECTIVE, SPARQL_BASE, SPARQL_PREFIX),
  };

  /* ********************************************************** */
  // declaredBaseUri? declaredImportUri* statement*
  static boolean TTLFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TTLFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TTLFile_0(b, l + 1);
    r = r && TTLFile_1(b, l + 1);
    r = r && TTLFile_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // declaredBaseUri?
  private static boolean TTLFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TTLFile_0")) return false;
    declaredBaseUri(b, l + 1);
    return true;
  }

  // declaredImportUri*
  private static boolean TTLFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TTLFile_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!declaredImportUri(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TTLFile_1", c)) break;
    }
    return true;
  }

  // statement*
  private static boolean TTLFile_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TTLFile_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TTLFile_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ATBASE IRIREF DOT
  public static boolean base(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base")) return false;
    if (!nextTokenIs(b, ATBASE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ATBASE, IRIREF, DOT);
    exit_section_(b, m, BASE, r);
    return r;
  }

  /* ********************************************************** */
  // BLANK_NODE_LABEL | ANON
  public static boolean blankNode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blankNode")) return false;
    if (!nextTokenIs(b, "<blank node>", ANON, BLANK_NODE_LABEL)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLANK_NODE, "<blank node>");
    r = consumeToken(b, BLANK_NODE_LABEL);
    if (!r) r = consumeToken(b, ANON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BRACKET_OPEN predicateObjectList BRACKET_CLOSE
  public static boolean blankNodePropertyList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blankNodePropertyList")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && predicateObjectList(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
    exit_section_(b, m, BLANK_NODE_PROPERTY_LIST, r);
    return r;
  }

  /* ********************************************************** */
  // TRUE | FALSE
  public static boolean booleanLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "booleanLiteral")) return false;
    if (!nextTokenIs(b, "<boolean literal>", FALSE, TRUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_LITERAL, "<boolean literal>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PARENTHESES_OPEN object* PARENTHESES_CLOSE
  public static boolean collection(PsiBuilder b, int l) {
      if (!recursion_guard_(b, l, "collection")) return false;
      if (!nextTokenIs(b, PARENTHESES_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
      r = consumeToken(b, PARENTHESES_OPEN);
      r = r && collection_1(b, l + 1);
      r = r && consumeToken(b, PARENTHESES_CLOSE);
      exit_section_(b, m, COLLECTION, r);
    return r;
  }

  // object*
  private static boolean collection_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "collection_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!object(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "collection_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // BASE_URI
  public static boolean declaredBaseUri(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaredBaseUri")) return false;
    if (!nextTokenIs(b, BASE_URI)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BASE_URI);
    exit_section_(b, m, DECLARED_BASE_URI, r);
    return r;
  }

  /* ********************************************************** */
  // IMPORT_URI
  public static boolean declaredImportUri(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaredImportUri")) return false;
    if (!nextTokenIs(b, IMPORT_URI)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IMPORT_URI);
    exit_section_(b, m, DECLARED_IMPORT_URI, r);
    return r;
  }

  /* ********************************************************** */
  // prefixId | base | sparqlPrefix | sparqlBase
  public static boolean directive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "directive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, DIRECTIVE, "<directive>");
    r = prefixId(b, l + 1);
    if (!r) r = base(b, l + 1);
    if (!r) r = sparqlPrefix(b, l + 1);
    if (!r) r = sparqlBase(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IRIREF | prefixedName
  public static boolean iri(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "iri")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IRI, "<iri>");
    r = consumeToken(b, IRIREF);
    if (!r) r = prefixedName(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // rdfLiteral | numericLiteral | booleanLiteral
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL, "<literal>");
    r = rdfLiteral(b, l + 1);
    if (!r) r = numericLiteral(b, l + 1);
    if (!r) r = booleanLiteral(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // INTEGER | DECIMAL | DOUBLE
  public static boolean numericLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numericLiteral")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMERIC_LITERAL, "<numeric literal>");
    r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, DECIMAL);
    if (!r) r = consumeToken(b, DOUBLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // iri | blankNode | collection | blankNodePropertyList | literal
  public static boolean object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OBJECT, "<object>");
    r = iri(b, l + 1);
    if (!r) r = blankNode(b, l + 1);
    if (!r) r = collection(b, l + 1);
    if (!r) r = blankNodePropertyList(b, l + 1);
    if (!r) r = literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // object (COMMA object)*
  public static boolean objectList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "objectList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OBJECT_LIST, "<object list>");
    r = object(b, l + 1);
    r = r && objectList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA object)*
  private static boolean objectList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "objectList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!objectList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "objectList_1", c)) break;
    }
    return true;
  }

  // COMMA object
  private static boolean objectList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "objectList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && object(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // iri
  public static boolean predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE, "<predicate>");
    r = iri(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // verb objectList
  public static boolean predicateObject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicateObject")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_OBJECT, "<predicate object>");
    r = verb(b, l + 1);
    r = r && objectList(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // predicateObject (SEMICOLON (predicateObject)?)*
  public static boolean predicateObjectList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicateObjectList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE_OBJECT_LIST, "<predicate object list>");
    r = predicateObject(b, l + 1);
    r = r && predicateObjectList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (SEMICOLON (predicateObject)?)*
  private static boolean predicateObjectList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicateObjectList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!predicateObjectList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "predicateObjectList_1", c)) break;
    }
    return true;
  }

  // SEMICOLON (predicateObject)?
  private static boolean predicateObjectList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicateObjectList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    r = r && predicateObjectList_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (predicateObject)?
  private static boolean predicateObjectList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicateObjectList_1_0_1")) return false;
    predicateObjectList_1_0_1_0(b, l + 1);
    return true;
  }

  // (predicateObject)
  private static boolean predicateObjectList_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicateObjectList_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = predicateObject(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ATPREFIX PNAME_NS IRIREF DOT
  public static boolean prefixId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefixId")) return false;
    if (!nextTokenIs(b, ATPREFIX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ATPREFIX, PNAME_NS, IRIREF, DOT);
    exit_section_(b, m, PREFIX_ID, r);
    return r;
  }

  /* ********************************************************** */
  // PNAME_LN | PNAME_NS
  public static boolean prefixedName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefixedName")) return false;
    if (!nextTokenIs(b, "<prefixed name>", PNAME_LN, PNAME_NS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREFIXED_NAME, "<prefixed name>");
    r = consumeToken(b, PNAME_LN);
    if (!r) r = consumeToken(b, PNAME_NS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // string (LANGTAG | DATATYPE_LEADING iri)?
  public static boolean rdfLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rdfLiteral")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RDF_LITERAL, "<rdf literal>");
    r = string(b, l + 1);
    r = r && rdfLiteral_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (LANGTAG | DATATYPE_LEADING iri)?
  private static boolean rdfLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rdfLiteral_1")) return false;
    rdfLiteral_1_0(b, l + 1);
    return true;
  }

  // LANGTAG | DATATYPE_LEADING iri
  private static boolean rdfLiteral_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rdfLiteral_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGTAG);
    if (!r) r = rdfLiteral_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DATATYPE_LEADING iri
  private static boolean rdfLiteral_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rdfLiteral_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DATATYPE_LEADING);
    r = r && iri(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BASE_LEADING IRIREF
  public static boolean sparqlBase(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sparqlBase")) return false;
    if (!nextTokenIs(b, BASE_LEADING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BASE_LEADING, IRIREF);
    exit_section_(b, m, SPARQL_BASE, r);
    return r;
  }

  /* ********************************************************** */
  // PREFIX_LEADING PNAME_NS IRIREF
  public static boolean sparqlPrefix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sparqlPrefix")) return false;
    if (!nextTokenIs(b, PREFIX_LEADING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PREFIX_LEADING, PNAME_NS, IRIREF);
    exit_section_(b, m, SPARQL_PREFIX, r);
    return r;
  }

  /* ********************************************************** */
  // directive | triples DOT
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = directive(b, l + 1);
    if (!r) r = statement_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // triples DOT
  private static boolean statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = triples(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // STRING_LITERAL_QUOTE | STRING_LITERAL_SINGLE_QUOTE | STRING_LITERAL_LONG_SINGLE_QUOTE | STRING_LITERAL_LONG_QUOTE
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING, "<string>");
    r = consumeToken(b, STRING_LITERAL_QUOTE);
    if (!r) r = consumeToken(b, STRING_LITERAL_SINGLE_QUOTE);
    if (!r) r = consumeToken(b, STRING_LITERAL_LONG_SINGLE_QUOTE);
    if (!r) r = consumeToken(b, STRING_LITERAL_LONG_QUOTE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // iri | blankNode | collection
  public static boolean subject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subject")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SUBJECT, "<subject>");
    r = iri(b, l + 1);
    if (!r) r = blankNode(b, l + 1);
    if (!r) r = collection(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // subject predicateObjectList | blankNodePropertyList predicateObjectList?
  public static boolean triples(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "triples")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TRIPLES, "<triples>");
    r = triples_0(b, l + 1);
    if (!r) r = triples_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // subject predicateObjectList
  private static boolean triples_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "triples_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = subject(b, l + 1);
    r = r && predicateObjectList(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // blankNodePropertyList predicateObjectList?
  private static boolean triples_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "triples_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = blankNodePropertyList(b, l + 1);
    r = r && triples_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // predicateObjectList?
  private static boolean triples_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "triples_1_1")) return false;
    predicateObjectList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // predicate | A
  public static boolean verb(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "verb")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VERB, "<verb>");
    r = predicate(b, l + 1);
    if (!r) r = consumeToken(b, A);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
