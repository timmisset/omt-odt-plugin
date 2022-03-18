// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.psi.impl.*;
import com.misset.opp.ttl.stubs.object.TTLObjectStubElementType;
import com.misset.opp.ttl.stubs.subject.TTLSubjectStubElementType;

public interface TTLTypes {

  IElementType BASE = new TTLElementType("BASE");
  IElementType BLANK_NODE = new TTLElementType("BLANK_NODE");
  IElementType BLANK_NODE_PROPERTY_LIST = new TTLElementType("BLANK_NODE_PROPERTY_LIST");
  IElementType BOOLEAN_LITERAL = new TTLElementType("BOOLEAN_LITERAL");
  IElementType COLLECTION = new TTLElementType("COLLECTION");
  IElementType DECLARED_BASE_URI = new TTLElementType("DECLARED_BASE_URI");
  IElementType DECLARED_IMPORT_URI = new TTLElementType("DECLARED_IMPORT_URI");
  IElementType DIRECTIVE = new TTLElementType("DIRECTIVE");
  IElementType IRI = new TTLElementType("IRI");
  IElementType LITERAL = new TTLElementType("LITERAL");
  IElementType NUMERIC_LITERAL = new TTLElementType("NUMERIC_LITERAL");
  IElementType OBJECT = new TTLObjectStubElementType("OBJECT");
  IElementType OBJECT_LIST = new TTLElementType("OBJECT_LIST");
  IElementType PREDICATE = new TTLElementType("PREDICATE");
  IElementType PREDICATE_OBJECT = new TTLElementType("PREDICATE_OBJECT");
  IElementType PREDICATE_OBJECT_LIST = new TTLElementType("PREDICATE_OBJECT_LIST");
  IElementType PREFIXED_NAME = new TTLElementType("PREFIXED_NAME");
  IElementType PREFIX_ID = new TTLElementType("PREFIX_ID");
  IElementType RDF_LITERAL = new TTLElementType("RDF_LITERAL");
  IElementType SPARQL_BASE = new TTLElementType("SPARQL_BASE");
  IElementType SPARQL_PREFIX = new TTLElementType("SPARQL_PREFIX");
  IElementType STATEMENT = new TTLElementType("STATEMENT");
  IElementType STRING = new TTLElementType("STRING");
  IElementType SUBJECT = new TTLSubjectStubElementType("SUBJECT");
  IElementType TRIPLES = new TTLElementType("TRIPLES");
  IElementType VERB = new TTLElementType("VERB");

  IElementType A = new TTLTokenType("A");
  IElementType ANON = new TTLTokenType("ANON");
  IElementType ATBASE = new TTLTokenType("ATBASE");
  IElementType ATPREFIX = new TTLTokenType("ATPREFIX");
  IElementType BASE_LEADING = new TTLTokenType("BASE_LEADING");
  IElementType BASE_URI = new TTLTokenType("BASE_URI");
  IElementType BLANK_NODE_LABEL = new TTLTokenType("BLANK_NODE_LABEL");
  IElementType BRACKET_CLOSE = new TTLTokenType("BRACKET_CLOSE");
  IElementType BRACKET_OPEN = new TTLTokenType("BRACKET_OPEN");
  IElementType COMMA = new TTLTokenType("COMMA");
  IElementType DATATYPE_LEADING = new TTLTokenType("DATATYPE_LEADING");
  IElementType DECIMAL = new TTLTokenType("DECIMAL");
  IElementType DOT = new TTLTokenType("DOT");
  IElementType DOUBLE = new TTLTokenType("DOUBLE");
  IElementType FALSE = new TTLTokenType("FALSE");
  IElementType IMPORT_URI = new TTLTokenType("IMPORT_URI");
  IElementType INTEGER = new TTLTokenType("INTEGER");
  IElementType IRIREF = new TTLTokenType("IRIREF");
  IElementType LANGTAG = new TTLTokenType("LANGTAG");
  IElementType PARENTHESES_CLOSE = new TTLTokenType("PARENTHESES_CLOSE");
  IElementType PARENTHESES_OPEN = new TTLTokenType("PARENTHESES_OPEN");
  IElementType PNAME_LN = new TTLTokenType("PNAME_LN");
  IElementType PNAME_NS = new TTLTokenType("PNAME_NS");
  IElementType PREFIX_LEADING = new TTLTokenType("PREFIX_LEADING");
  IElementType SEMICOLON = new TTLTokenType("SEMICOLON");
  IElementType STRING_LITERAL_LONG_QUOTE = new TTLTokenType("STRING_LITERAL_LONG_QUOTE");
  IElementType STRING_LITERAL_LONG_SINGLE_QUOTE = new TTLTokenType("STRING_LITERAL_LONG_SINGLE_QUOTE");
  IElementType STRING_LITERAL_QUOTE = new TTLTokenType("STRING_LITERAL_QUOTE");
  IElementType STRING_LITERAL_SINGLE_QUOTE = new TTLTokenType("STRING_LITERAL_SINGLE_QUOTE");
  IElementType TRUE = new TTLTokenType("TRUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BASE) {
        return new TTLBaseImpl(node);
      } else if (type == BLANK_NODE) {
        return new TTLBlankNodeImpl(node);
      } else if (type == BLANK_NODE_PROPERTY_LIST) {
        return new TTLBlankNodePropertyListImpl(node);
      } else if (type == BOOLEAN_LITERAL) {
        return new TTLBooleanLiteralImpl(node);
      } else if (type == COLLECTION) {
        return new TTLCollectionImpl(node);
      } else if (type == DECLARED_BASE_URI) {
        return new TTLDeclaredBaseUriImpl(node);
      } else if (type == DECLARED_IMPORT_URI) {
        return new TTLDeclaredImportUriImpl(node);
      } else if (type == DIRECTIVE) {
        return new TTLDirectiveImpl(node);
      } else if (type == IRI) {
        return new TTLIriImpl(node);
      } else if (type == LITERAL) {
        return new TTLLiteralImpl(node);
      } else if (type == NUMERIC_LITERAL) {
        return new TTLNumericLiteralImpl(node);
      } else if (type == OBJECT) {
        return new TTLObjectImpl(node);
      } else if (type == OBJECT_LIST) {
        return new TTLObjectListImpl(node);
      } else if (type == PREDICATE) {
        return new TTLPredicateImpl(node);
      } else if (type == PREDICATE_OBJECT) {
        return new TTLPredicateObjectImpl(node);
      } else if (type == PREDICATE_OBJECT_LIST) {
        return new TTLPredicateObjectListImpl(node);
      } else if (type == PREFIXED_NAME) {
        return new TTLPrefixedNameImpl(node);
      } else if (type == PREFIX_ID) {
        return new TTLPrefixIdImpl(node);
      } else if (type == RDF_LITERAL) {
        return new TTLRdfLiteralImpl(node);
      } else if (type == SPARQL_BASE) {
        return new TTLSparqlBaseImpl(node);
      } else if (type == SPARQL_PREFIX) {
        return new TTLSparqlPrefixImpl(node);
      } else if (type == STATEMENT) {
        return new TTLStatementImpl(node);
      } else if (type == STRING) {
        return new TTLStringImpl(node);
      } else if (type == SUBJECT) {
        return new TTLSubjectImpl(node);
      } else if (type == TRIPLES) {
        return new TTLTriplesImpl(node);
      } else if (type == VERB) {
        return new TTLVerbImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
