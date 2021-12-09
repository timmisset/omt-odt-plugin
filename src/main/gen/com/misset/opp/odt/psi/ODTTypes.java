// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.psi.impl.*;

public interface ODTTypes {

  IElementType ADD_TO_COLLECTION = new ODTElementType("ADD_TO_COLLECTION");
  IElementType ASSIGNMENT_STATEMENT = new ODTElementType("ASSIGNMENT_STATEMENT");
  IElementType BOOLEAN_STATEMENT = new ODTElementType("BOOLEAN_STATEMENT");
  IElementType CALL_NAME = new ODTElementType("CALL_NAME");
  IElementType CHOOSE_BLOCK = new ODTElementType("CHOOSE_BLOCK");
  IElementType COLLECTION_STATEMENT = new ODTElementType("COLLECTION_STATEMENT");
  IElementType COMMAND_BLOCK = new ODTElementType("COMMAND_BLOCK");
  IElementType COMMAND_CALL = new ODTElementType("COMMAND_CALL");
  IElementType CONSTANT_VALUE = new ODTElementType("CONSTANT_VALUE");
  IElementType CURIE_ELEMENT = new ODTElementType("CURIE_ELEMENT");
  IElementType DECLARE_VARIABLE = new ODTElementType("DECLARE_VARIABLE");
  IElementType DEFINE_COMMAND_STATEMENT = new ODTElementType("DEFINE_COMMAND_STATEMENT");
  IElementType DEFINE_NAME = new ODTElementType("DEFINE_NAME");
  IElementType DEFINE_PARAM = new ODTElementType("DEFINE_PARAM");
  IElementType DEFINE_PREFIX = new ODTElementType("DEFINE_PREFIX");
  IElementType DEFINE_QUERY_STATEMENT = new ODTElementType("DEFINE_QUERY_STATEMENT");
  IElementType ELSE_BLOCK = new ODTElementType("ELSE_BLOCK");
  IElementType END_PATH = new ODTElementType("END_PATH");
  IElementType EQUATION_STATEMENT = new ODTElementType("EQUATION_STATEMENT");
  IElementType FLAG_SIGNATURE = new ODTElementType("FLAG_SIGNATURE");
  IElementType IDENTIFIER_STEP = new ODTElementType("IDENTIFIER_STEP");
  IElementType IF_BLOCK = new ODTElementType("IF_BLOCK");
  IElementType INTERPOLATED_STRING = new ODTElementType("INTERPOLATED_STRING");
  IElementType INTERPOLATED_STRING_CONTENT = new ODTElementType("INTERPOLATED_STRING_CONTENT");
  IElementType INTERPOLATION = new ODTElementType("INTERPOLATION");
  IElementType IRI_STEP = new ODTElementType("IRI_STEP");
  IElementType LOGICAL_BLOCK = new ODTElementType("LOGICAL_BLOCK");
  IElementType NAMESPACE_PREFIX = new ODTElementType("NAMESPACE_PREFIX");
  IElementType NEGATED_STEP = new ODTElementType("NEGATED_STEP");
  IElementType OPERATOR_CALL = new ODTElementType("OPERATOR_CALL");
  IElementType OTHERWISE_PATH = new ODTElementType("OTHERWISE_PATH");
  IElementType QUERY = new ODTElementType("QUERY");
  IElementType QUERY_ARRAY = new ODTElementType("QUERY_ARRAY");
  IElementType QUERY_FILTER = new ODTElementType("QUERY_FILTER");
  IElementType QUERY_OPERATION_STEP = new ODTElementType("QUERY_OPERATION_STEP");
  IElementType QUERY_PATH = new ODTElementType("QUERY_PATH");
  IElementType QUERY_REVERSE_STEP = new ODTElementType("QUERY_REVERSE_STEP");
  IElementType QUERY_STATEMENT = new ODTElementType("QUERY_STATEMENT");
  IElementType QUERY_STEP = new ODTElementType("QUERY_STEP");
  IElementType RANGE_SELECTION = new ODTElementType("RANGE_SELECTION");
  IElementType REMOVE_FROM_COLLECTION = new ODTElementType("REMOVE_FROM_COLLECTION");
  IElementType RESOLVABLE_VALUE = new ODTElementType("RESOLVABLE_VALUE");
  IElementType RETURN_STATEMENT = new ODTElementType("RETURN_STATEMENT");
  IElementType SCHEMALESS_IRI_STEP = new ODTElementType("SCHEMALESS_IRI_STEP");
  IElementType SCRIPT = new ODTElementType("SCRIPT");
  IElementType SCRIPT_LINE = new ODTElementType("SCRIPT_LINE");
  IElementType SIGNATURE = new ODTElementType("SIGNATURE");
  IElementType SIGNATURE_ARGUMENT = new ODTElementType("SIGNATURE_ARGUMENT");
  IElementType STATEMENT = new ODTElementType("STATEMENT");
  IElementType STEP_DECORATOR = new ODTElementType("STEP_DECORATOR");
  IElementType SUB_QUERY = new ODTElementType("SUB_QUERY");
  IElementType VARIABLE = new ODTElementType("VARIABLE");
  IElementType VARIABLE_ASSIGNMENT = new ODTElementType("VARIABLE_ASSIGNMENT");
  IElementType VARIABLE_STEP = new ODTElementType("VARIABLE_STEP");
  IElementType VARIABLE_VALUE = new ODTElementType("VARIABLE_VALUE");
  IElementType WHEN_PATH = new ODTElementType("WHEN_PATH");

  IElementType ADD = new ODTTokenType("ADD");
  IElementType ASTERIX = new ODTTokenType("ASTERIX");
  IElementType AT = new ODTTokenType("AT");
  IElementType BOOLEAN = new ODTTokenType("BOOLEAN");
  IElementType BOOLEAN_OPERATOR = new ODTTokenType("BOOLEAN_OPERATOR");
  IElementType BRACKET_CLOSED = new ODTTokenType("BRACKET_CLOSED");
  IElementType BRACKET_OPEN = new ODTTokenType("BRACKET_OPEN");
  IElementType CARET = new ODTTokenType("CARET");
  IElementType CHOOSE_OPERATOR = new ODTTokenType("CHOOSE_OPERATOR");
  IElementType COLON = new ODTTokenType("COLON");
  IElementType COMMA = new ODTTokenType("COMMA");
  IElementType CONDITIONAL_OPERATOR = new ODTTokenType("CONDITIONAL_OPERATOR");
  IElementType CURLY_CLOSED = new ODTTokenType("CURLY_CLOSED");
  IElementType CURLY_OPEN = new ODTTokenType("CURLY_OPEN");
  IElementType DECIMAL = new ODTTokenType("DECIMAL");
  IElementType DECLARE_VAR = new ODTTokenType("DECLARE_VAR");
  IElementType DEFINE_COMMAND = new ODTTokenType("DEFINE_COMMAND");
  IElementType DEFINE_QUERY = new ODTTokenType("DEFINE_QUERY");
  IElementType DEFINE_START = new ODTTokenType("DEFINE_START");
  IElementType DOT = new ODTTokenType("DOT");
  IElementType ELSE_OPERATOR = new ODTTokenType("ELSE_OPERATOR");
  IElementType END_OPERATOR = new ODTTokenType("END_OPERATOR");
  IElementType EQUALS = new ODTTokenType("EQUALS");
  IElementType FORWARD_SLASH = new ODTTokenType("FORWARD_SLASH");
  IElementType IF_OPERATOR = new ODTTokenType("IF_OPERATOR");
  IElementType INTEGER = new ODTTokenType("INTEGER");
  IElementType INTERPOLATED_STRING_END = new ODTTokenType("INTERPOLATED_STRING_END");
  IElementType INTERPOLATED_STRING_START = new ODTTokenType("INTERPOLATED_STRING_START");
  IElementType INTERPOLATION_END = new ODTTokenType("INTERPOLATION_END");
  IElementType INTERPOLATION_START = new ODTTokenType("INTERPOLATION_START");
  IElementType IRI = new ODTTokenType("IRI");
  IElementType LAMBDA = new ODTTokenType("LAMBDA");
  IElementType NOT_OPERATOR = new ODTTokenType("NOT_OPERATOR");
  IElementType NULL = new ODTTokenType("NULL");
  IElementType OTHERWISE_OPERATOR = new ODTTokenType("OTHERWISE_OPERATOR");
  IElementType PARENTHESES_CLOSE = new ODTTokenType("PARENTHESES_CLOSE");
  IElementType PARENTHESES_OPEN = new ODTTokenType("PARENTHESES_OPEN");
  IElementType PIPE = new ODTTokenType("PIPE");
  IElementType PLUS = new ODTTokenType("PLUS");
  IElementType PREFIX_DEFINE_START = new ODTTokenType("PREFIX_DEFINE_START");
  IElementType PRIMITIVE = new ODTTokenType("PRIMITIVE");
  IElementType QUESTION_MARK = new ODTTokenType("QUESTION_MARK");
  IElementType REMOVE = new ODTTokenType("REMOVE");
  IElementType RETURN_OPERATOR = new ODTTokenType("RETURN_OPERATOR");
  IElementType SCHEMALESS_IRI = new ODTTokenType("SCHEMALESS_IRI");
  IElementType SEMICOLON = new ODTTokenType("SEMICOLON");
  IElementType STRING = new ODTTokenType("STRING");
  IElementType SYMBOL = new ODTTokenType("SYMBOL");
  IElementType TAG = new ODTTokenType("TAG");
  IElementType TYPED_VALUE = new ODTTokenType("TYPED_VALUE");
  IElementType VARIABLE_NAME = new ODTTokenType("VARIABLE_NAME");
  IElementType WHEN_OPERATOR = new ODTTokenType("WHEN_OPERATOR");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADD_TO_COLLECTION) {
        return new ODTAddToCollectionImpl(node);
      } else if (type == ASSIGNMENT_STATEMENT) {
        return new ODTAssignmentStatementImpl(node);
      } else if (type == BOOLEAN_STATEMENT) {
        return new ODTBooleanStatementImpl(node);
      } else if (type == CALL_NAME) {
        return new ODTCallNameImpl(node);
      } else if (type == CHOOSE_BLOCK) {
        return new ODTChooseBlockImpl(node);
      } else if (type == COMMAND_BLOCK) {
        return new ODTCommandBlockImpl(node);
      } else if (type == COMMAND_CALL) {
        return new ODTCommandCallImpl(node);
      } else if (type == CONSTANT_VALUE) {
        return new ODTConstantValueImpl(node);
      } else if (type == CURIE_ELEMENT) {
        return new ODTCurieElementImpl(node);
      } else if (type == DECLARE_VARIABLE) {
        return new ODTDeclareVariableImpl(node);
      } else if (type == DEFINE_COMMAND_STATEMENT) {
        return new ODTDefineCommandStatementImpl(node);
      } else if (type == DEFINE_NAME) {
        return new ODTDefineNameImpl(node);
      } else if (type == DEFINE_PARAM) {
        return new ODTDefineParamImpl(node);
      } else if (type == DEFINE_PREFIX) {
        return new ODTDefinePrefixImpl(node);
      } else if (type == DEFINE_QUERY_STATEMENT) {
        return new ODTDefineQueryStatementImpl(node);
      } else if (type == ELSE_BLOCK) {
        return new ODTElseBlockImpl(node);
      } else if (type == END_PATH) {
        return new ODTEndPathImpl(node);
      } else if (type == EQUATION_STATEMENT) {
        return new ODTEquationStatementImpl(node);
      } else if (type == FLAG_SIGNATURE) {
        return new ODTFlagSignatureImpl(node);
      } else if (type == IDENTIFIER_STEP) {
        return new ODTIdentifierStepImpl(node);
      } else if (type == IF_BLOCK) {
        return new ODTIfBlockImpl(node);
      } else if (type == INTERPOLATED_STRING) {
        return new ODTInterpolatedStringImpl(node);
      } else if (type == INTERPOLATED_STRING_CONTENT) {
        return new ODTInterpolatedStringContentImpl(node);
      } else if (type == INTERPOLATION) {
        return new ODTInterpolationImpl(node);
      } else if (type == IRI_STEP) {
        return new ODTIriStepImpl(node);
      } else if (type == LOGICAL_BLOCK) {
        return new ODTLogicalBlockImpl(node);
      } else if (type == NAMESPACE_PREFIX) {
        return new ODTNamespacePrefixImpl(node);
      } else if (type == NEGATED_STEP) {
        return new ODTNegatedStepImpl(node);
      } else if (type == OPERATOR_CALL) {
        return new ODTOperatorCallImpl(node);
      } else if (type == OTHERWISE_PATH) {
        return new ODTOtherwisePathImpl(node);
      } else if (type == QUERY_ARRAY) {
        return new ODTQueryArrayImpl(node);
      } else if (type == QUERY_FILTER) {
        return new ODTQueryFilterImpl(node);
      } else if (type == QUERY_OPERATION_STEP) {
        return new ODTQueryOperationStepImpl(node);
      } else if (type == QUERY_PATH) {
        return new ODTQueryPathImpl(node);
      } else if (type == QUERY_REVERSE_STEP) {
        return new ODTQueryReverseStepImpl(node);
      } else if (type == QUERY_STATEMENT) {
        return new ODTQueryStatementImpl(node);
      } else if (type == RANGE_SELECTION) {
        return new ODTRangeSelectionImpl(node);
      } else if (type == REMOVE_FROM_COLLECTION) {
        return new ODTRemoveFromCollectionImpl(node);
      } else if (type == RESOLVABLE_VALUE) {
        return new ODTResolvableValueImpl(node);
      } else if (type == RETURN_STATEMENT) {
        return new ODTReturnStatementImpl(node);
      } else if (type == SCHEMALESS_IRI_STEP) {
        return new ODTSchemalessIriStepImpl(node);
      } else if (type == SCRIPT) {
        return new ODTScriptImpl(node);
      } else if (type == SCRIPT_LINE) {
        return new ODTScriptLineImpl(node);
      } else if (type == SIGNATURE) {
        return new ODTSignatureImpl(node);
      } else if (type == SIGNATURE_ARGUMENT) {
        return new ODTSignatureArgumentImpl(node);
      } else if (type == STEP_DECORATOR) {
        return new ODTStepDecoratorImpl(node);
      } else if (type == SUB_QUERY) {
        return new ODTSubQueryImpl(node);
      } else if (type == VARIABLE) {
        return new ODTVariableImpl(node);
      } else if (type == VARIABLE_ASSIGNMENT) {
        return new ODTVariableAssignmentImpl(node);
      } else if (type == VARIABLE_STEP) {
        return new ODTVariableStepImpl(node);
      } else if (type == VARIABLE_VALUE) {
        return new ODTVariableValueImpl(node);
      } else if (type == WHEN_PATH) {
        return new ODTWhenPathImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
