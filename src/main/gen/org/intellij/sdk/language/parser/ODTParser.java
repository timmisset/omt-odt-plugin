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
import static com.intellij.psi.impl.source.tree.JavaDocElementType.DOC_COMMENT;
import static com.misset.opp.odt.psi.ODTTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ODTParser implements PsiParser, LightPsiParser {

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
    return ODTFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[]{
          create_token_set_(LOGICAL_BLOCK, SCRIPT_LINE),
          create_token_set_(BOOLEAN_STATEMENT, EQUATION_STATEMENT, QUERY, QUERY_ARRAY,
                  QUERY_PATH),
          create_token_set_(ADD_TO_COLLECTION, ASSIGNMENT_STATEMENT, COLLECTION_STATEMENT, COMMAND_CALL,
                  DECLARE_VARIABLE, DEFINE_PREFIX, DEFINE_QUERY_STATEMENT, QUERY_STATEMENT,
                  REMOVE_FROM_COLLECTION, RETURN_STATEMENT, STATEMENT, VARIABLE_ASSIGNMENT),
          create_token_set_(CHOOSE_BLOCK, CONSTANT_VALUE, CURIE_ELEMENT, IDENTIFIER_STEP,
                  INTERPOLATED_STRING, IRI_STEP, NEGATED_STEP, OPERATOR_CALL,
                  OTHERWISE_PATH, QUERY_REVERSE_STEP, QUERY_STEP, SCHEMALESS_IRI_STEP,
                  SUB_QUERY, VARIABLE_STEP, WHEN_PATH),
  };

  /* ********************************************************** */
  // script
  static boolean ODTFile(PsiBuilder b, int l) {
    return script(b, l + 1);
  }

  /* ********************************************************** */
  // query ADD resolvableValue
  public static boolean addToCollection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addToCollection")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADD_TO_COLLECTION, "<add to collection>");
    r = query(b, l + 1, -1);
    r = r && consumeToken(b, ADD);
    r = r && resolvableValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // query EQUALS resolvableValue
  public static boolean assignmentStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignmentStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT_STATEMENT, "<assignment statement>");
    r = query(b, l + 1, -1);
    r = r && consumeToken(b, EQUALS);
    r = r && resolvableValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SYMBOL | BOOLEAN_OPERATOR
  public static boolean callName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "callName")) return false;
    if (!nextTokenIs(b, "<call name>", BOOLEAN_OPERATOR, SYMBOL)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CALL_NAME, "<call name>");
    r = consumeToken(b, SYMBOL);
    if (!r) r = consumeToken(b, BOOLEAN_OPERATOR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CHOOSE_OPERATOR
  //                                                 whenPath*
  //                                                 otherwisePath?
  //                                                 endPath?
  public static boolean chooseBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chooseBlock")) return false;
    if (!nextTokenIs(b, CHOOSE_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CHOOSE_OPERATOR);
    r = r && chooseBlock_1(b, l + 1);
    r = r && chooseBlock_2(b, l + 1);
    r = r && chooseBlock_3(b, l + 1);
    exit_section_(b, m, CHOOSE_BLOCK, r);
    return r;
  }

  // whenPath*
  private static boolean chooseBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chooseBlock_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!whenPath(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "chooseBlock_1", c)) break;
    }
    return true;
  }

  // otherwisePath?
  private static boolean chooseBlock_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chooseBlock_2")) return false;
    otherwisePath(b, l + 1);
    return true;
  }

  // endPath?
  private static boolean chooseBlock_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chooseBlock_3")) return false;
    endPath(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // assignmentStatement | addToCollection | removeFromCollection
  public static boolean collectionStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "collectionStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, COLLECTION_STATEMENT, "<collection statement>");
    r = assignmentStatement(b, l + 1);
    if (!r) r = addToCollection(b, l + 1);
    if (!r) r = removeFromCollection(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CURLY_OPEN script? CURLY_CLOSED
  public static boolean commandBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commandBlock")) return false;
    if (!nextTokenIs(b, CURLY_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_OPEN);
    r = r && commandBlock_1(b, l + 1);
    r = r && consumeToken(b, CURLY_CLOSED);
    exit_section_(b, m, COMMAND_BLOCK, r);
    return r;
  }

  // script?
  private static boolean commandBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commandBlock_1")) return false;
    script(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // AT callName flagSignature? signature?
  public static boolean commandCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commandCall")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AT);
    r = r && callName(b, l + 1);
    r = r && commandCall_2(b, l + 1);
    r = r && commandCall_3(b, l + 1);
    exit_section_(b, m, COMMAND_CALL, r);
    return r;
  }

  // flagSignature?
  private static boolean commandCall_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commandCall_2")) return false;
    flagSignature(b, l + 1);
    return true;
  }

  // signature?
  private static boolean commandCall_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commandCall_3")) return false;
    signature(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // STRING+ | INTEGER | DECIMAL | NULL | BOOLEAN | TYPED_VALUE | PRIMITIVE | interpolatedString
  public static boolean constantValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constantValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, CONSTANT_VALUE, "<constant value>");
    r = constantValue_0(b, l + 1);
    if (!r) r = consumeToken(b, INTEGER);
    if (!r) r = consumeToken(b, DECIMAL);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, BOOLEAN);
    if (!r) r = consumeToken(b, TYPED_VALUE);
    if (!r) r = consumeToken(b, PRIMITIVE);
    if (!r) r = interpolatedString(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // STRING+
  private static boolean constantValue_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constantValue_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, STRING)) break;
      if (!empty_element_parsed_guard_(b, "constantValue_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // namespacePrefix (SYMBOL | PRIMITIVE)
  public static boolean curieElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "curieElement")) return false;
    if (!nextTokenIs(b, SYMBOL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespacePrefix(b, l + 1);
    r = r && curieElement_1(b, l + 1);
    exit_section_(b, m, CURIE_ELEMENT, r);
    return r;
  }

  // SYMBOL | PRIMITIVE
  private static boolean curieElement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "curieElement_1")) return false;
    boolean r;
    r = consumeToken(b, SYMBOL);
    if (!r) r = consumeToken(b, PRIMITIVE);
    return r;
  }

  /* ********************************************************** */
  // DECLARE_VAR (variableAssignment | variable) (COMMA (variableAssignment | variable))*
  public static boolean declareVariable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declareVariable")) return false;
    if (!nextTokenIs(b, DECLARE_VAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DECLARE_VAR);
    r = r && declareVariable_1(b, l + 1);
    r = r && declareVariable_2(b, l + 1);
    exit_section_(b, m, DECLARE_VARIABLE, r);
    return r;
  }

  // variableAssignment | variable
  private static boolean declareVariable_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declareVariable_1")) return false;
    boolean r;
    r = variableAssignment(b, l + 1);
    if (!r) r = variable(b, l + 1);
    return r;
  }

  // (COMMA (variableAssignment | variable))*
  private static boolean declareVariable_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declareVariable_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!declareVariable_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "declareVariable_2", c)) break;
    }
    return true;
  }

  // COMMA (variableAssignment | variable)
  private static boolean declareVariable_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declareVariable_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && declareVariable_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // variableAssignment | variable
  private static boolean declareVariable_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declareVariable_2_0_1")) return false;
    boolean r;
    r = variableAssignment(b, l + 1);
    if (!r) r = variable(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DEFINE_START DEFINE_COMMAND defineName defineParam? LAMBDA commandBlock
  public static boolean defineCommandStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineCommandStatement")) return false;
    if (!nextTokenIs(b, DEFINE_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DEFINE_START, DEFINE_COMMAND);
    r = r && defineName(b, l + 1);
    r = r && defineCommandStatement_3(b, l + 1);
    r = r && consumeToken(b, LAMBDA);
    r = r && commandBlock(b, l + 1);
    exit_section_(b, m, DEFINE_COMMAND_STATEMENT, r);
    return r;
  }

  // defineParam?
  private static boolean defineCommandStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineCommandStatement_3")) return false;
    defineParam(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SYMBOL
  public static boolean defineName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineName")) return false;
    if (!nextTokenIs(b, SYMBOL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SYMBOL);
    exit_section_(b, m, DEFINE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // PARENTHESES_OPEN (variable (COMMA variable)*)? PARENTHESES_CLOSE
  public static boolean defineParam(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineParam")) return false;
    if (!nextTokenIs(b, PARENTHESES_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARENTHESES_OPEN);
    r = r && defineParam_1(b, l + 1);
    r = r && consumeToken(b, PARENTHESES_CLOSE);
    exit_section_(b, m, DEFINE_PARAM, r);
    return r;
  }

  // (variable (COMMA variable)*)?
  private static boolean defineParam_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineParam_1")) return false;
    defineParam_1_0(b, l + 1);
    return true;
  }

  // variable (COMMA variable)*
  private static boolean defineParam_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineParam_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    r = r && defineParam_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA variable)*
  private static boolean defineParam_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineParam_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!defineParam_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defineParam_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA variable
  private static boolean defineParam_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineParam_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PREFIX_DEFINE_START namespacePrefix IRI
  public static boolean definePrefix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definePrefix")) return false;
    if (!nextTokenIs(b, PREFIX_DEFINE_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PREFIX_DEFINE_START);
    r = r && namespacePrefix(b, l + 1);
    r = r && consumeToken(b, IRI);
    exit_section_(b, m, DEFINE_PREFIX, r);
    return r;
  }

  /* ********************************************************** */
  // DEFINE_START DEFINE_QUERY defineName defineParam? LAMBDA query
  public static boolean defineQueryStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineQueryStatement")) return false;
    if (!nextTokenIs(b, DEFINE_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DEFINE_START, DEFINE_QUERY);
    r = r && defineName(b, l + 1);
    r = r && defineQueryStatement_3(b, l + 1);
    r = r && consumeToken(b, LAMBDA);
    r = r && query(b, l + 1, -1);
    exit_section_(b, m, DEFINE_QUERY_STATEMENT, r);
    return r;
  }

  // defineParam?
  private static boolean defineQueryStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defineQueryStatement_3")) return false;
    defineParam(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ELSE_OPERATOR commandBlock
  public static boolean elseBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseBlock")) return false;
    if (!nextTokenIs(b, ELSE_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE_OPERATOR);
    r = r && commandBlock(b, l + 1);
    exit_section_(b, m, ELSE_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // END_OPERATOR
  public static boolean endPath(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "endPath")) return false;
    if (!nextTokenIs(b, END_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, END_OPERATOR);
    exit_section_(b, m, END_PATH, r);
    return r;
  }

  /* ********************************************************** */
  // TAG
  public static boolean flagSignature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flagSignature")) return false;
    if (!nextTokenIs(b, TAG)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TAG);
    exit_section_(b, m, FLAG_SIGNATURE, r);
    return r;
  }

  /* ********************************************************** */
  // DOT
  public static boolean identifierStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifierStep")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    exit_section_(b, m, IDENTIFIER_STEP, r);
    return r;
  }

  /* ********************************************************** */
  // IF_OPERATOR query
  public static boolean ifBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifBlock")) return false;
    if (!nextTokenIs(b, IF_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IF_OPERATOR);
    r = r && query(b, l + 1, -1);
    exit_section_(b, m, IF_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // INTERPOLATED_STRING_START interpolatedStringContent* INTERPOLATED_STRING_END
  public static boolean interpolatedString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interpolatedString")) return false;
    if (!nextTokenIs(b, INTERPOLATED_STRING_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INTERPOLATED_STRING_START);
    r = r && interpolatedString_1(b, l + 1);
    r = r && consumeToken(b, INTERPOLATED_STRING_END);
    exit_section_(b, m, INTERPOLATED_STRING, r);
    return r;
  }

  // interpolatedStringContent*
  private static boolean interpolatedString_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interpolatedString_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!interpolatedStringContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "interpolatedString_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // STRING | interpolation
  public static boolean interpolatedStringContent(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interpolatedStringContent")) return false;
    if (!nextTokenIs(b, "<interpolated string content>", INTERPOLATION_START, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INTERPOLATED_STRING_CONTENT, "<interpolated string content>");
    r = consumeToken(b, STRING);
    if (!r) r = interpolation(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // INTERPOLATION_START script? INTERPOLATION_END
  public static boolean interpolation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interpolation")) return false;
    if (!nextTokenIs(b, INTERPOLATION_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INTERPOLATION_START);
    r = r && interpolation_1(b, l + 1);
    r = r && consumeToken(b, INTERPOLATION_END);
    exit_section_(b, m, INTERPOLATION, r);
    return r;
  }

  // script?
  private static boolean interpolation_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interpolation_1")) return false;
    script(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IRI
  public static boolean iriStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "iriStep")) return false;
    if (!nextTokenIs(b, IRI)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IRI);
    exit_section_(b, m, IRI_STEP, r);
    return r;
  }

  /* ********************************************************** */
  // ifBlock commandBlock? (ELSE_OPERATOR ifBlock commandBlock?)* elseBlock?
  public static boolean logicalBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalBlock")) return false;
    if (!nextTokenIs(b, IF_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ifBlock(b, l + 1);
    r = r && logicalBlock_1(b, l + 1);
    r = r && logicalBlock_2(b, l + 1);
    r = r && logicalBlock_3(b, l + 1);
    exit_section_(b, m, LOGICAL_BLOCK, r);
    return r;
  }

  // commandBlock?
  private static boolean logicalBlock_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalBlock_1")) return false;
    commandBlock(b, l + 1);
    return true;
  }

  // (ELSE_OPERATOR ifBlock commandBlock?)*
  private static boolean logicalBlock_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalBlock_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logicalBlock_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logicalBlock_2", c)) break;
    }
    return true;
  }

  // ELSE_OPERATOR ifBlock commandBlock?
  private static boolean logicalBlock_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalBlock_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE_OPERATOR);
    r = r && ifBlock(b, l + 1);
    r = r && logicalBlock_2_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // commandBlock?
  private static boolean logicalBlock_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalBlock_2_0_2")) return false;
    commandBlock(b, l + 1);
    return true;
  }

  // elseBlock?
  private static boolean logicalBlock_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logicalBlock_3")) return false;
    elseBlock(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SYMBOL COLON
  public static boolean namespacePrefix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespacePrefix")) return false;
    if (!nextTokenIs(b, SYMBOL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SYMBOL, COLON);
    exit_section_(b, m, NAMESPACE_PREFIX, r);
    return r;
  }

  /* ********************************************************** */
  // NOT_OPERATOR (equationStatement | queryPath | queryArray)?
  public static boolean negatedStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negatedStep")) return false;
    if (!nextTokenIs(b, NOT_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOT_OPERATOR);
    r = r && negatedStep_1(b, l + 1);
    exit_section_(b, m, NEGATED_STEP, r);
    return r;
  }

  // (equationStatement | queryPath | queryArray)?
  private static boolean negatedStep_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negatedStep_1")) return false;
    negatedStep_1_0(b, l + 1);
    return true;
  }

  // equationStatement | queryPath | queryArray
  private static boolean negatedStep_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "negatedStep_1_0")) return false;
    boolean r;
    r = query(b, l + 1, 2);
    if (!r) r = queryPath(b, l + 1);
    if (!r) r = query(b, l + 1, 0);
    return r;
  }

  /* ********************************************************** */
  // callName flagSignature? signature?
  public static boolean operatorCall(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorCall")) return false;
    if (!nextTokenIs(b, "<operator call>", BOOLEAN_OPERATOR, SYMBOL)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATOR_CALL, "<operator call>");
    r = callName(b, l + 1);
    r = r && operatorCall_1(b, l + 1);
    r = r && operatorCall_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // flagSignature?
  private static boolean operatorCall_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorCall_1")) return false;
    flagSignature(b, l + 1);
    return true;
  }

  // signature?
  private static boolean operatorCall_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operatorCall_2")) return false;
    signature(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // OTHERWISE_OPERATOR LAMBDA? query?
  public static boolean otherwisePath(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "otherwisePath")) return false;
    if (!nextTokenIs(b, OTHERWISE_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OTHERWISE_OPERATOR);
    r = r && otherwisePath_1(b, l + 1);
    r = r && otherwisePath_2(b, l + 1);
    exit_section_(b, m, OTHERWISE_PATH, r);
    return r;
  }

  // LAMBDA?
  private static boolean otherwisePath_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "otherwisePath_1")) return false;
    consumeToken(b, LAMBDA);
    return true;
  }

  // query?
  private static boolean otherwisePath_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "otherwisePath_2")) return false;
    query(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // BRACKET_OPEN (rangeSelection | query) BRACKET_CLOSED
  public static boolean queryFilter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryFilter")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && queryFilter_1(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSED);
    exit_section_(b, m, QUERY_FILTER, r);
    return r;
  }

  // rangeSelection | query
  private static boolean queryFilter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryFilter_1")) return false;
    boolean r;
    r = rangeSelection(b, l + 1);
    if (!r) r = query(b, l + 1, -1);
    return r;
  }

  /* ********************************************************** */
  // (queryStep stepDecorator? queryFilter*) | queryFilter
  public static boolean queryOperationStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryOperationStep")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, QUERY_OPERATION_STEP, "<query operation step>");
    r = queryOperationStep_0(b, l + 1);
    if (!r) r = queryFilter(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // queryStep stepDecorator? queryFilter*
  private static boolean queryOperationStep_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryOperationStep_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = queryStep(b, l + 1);
    r = r && queryOperationStep_0_1(b, l + 1);
    r = r && queryOperationStep_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // stepDecorator?
  private static boolean queryOperationStep_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryOperationStep_0_1")) return false;
    stepDecorator(b, l + 1);
    return true;
  }

  // queryFilter*
  private static boolean queryOperationStep_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryOperationStep_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!queryFilter(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "queryOperationStep_0_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CARET queryStep
  public static boolean queryReverseStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryReverseStep")) return false;
    if (!nextTokenIs(b, CARET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CARET);
    r = r && queryStep(b, l + 1);
    exit_section_(b, m, QUERY_REVERSE_STEP, r);
    return r;
  }

  /* ********************************************************** */
  // query
  public static boolean queryStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, QUERY_STATEMENT, "<query statement>");
    r = query(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // queryReverseStep | constantValue | variableStep | curieElement | operatorCall | iriStep | identifierStep | schemalessIriStep |
  //          chooseBlock | negatedStep | subQuery
  public static boolean queryStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryStep")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, QUERY_STEP, "<query step>");
    r = queryReverseStep(b, l + 1);
    if (!r) r = constantValue(b, l + 1);
    if (!r) r = variableStep(b, l + 1);
    if (!r) r = curieElement(b, l + 1);
    if (!r) r = operatorCall(b, l + 1);
    if (!r) r = iriStep(b, l + 1);
    if (!r) r = identifierStep(b, l + 1);
    if (!r) r = schemalessIriStep(b, l + 1);
    if (!r) r = chooseBlock(b, l + 1);
    if (!r) r = negatedStep(b, l + 1);
    if (!r) r = subQuery(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // INTEGER (COMMA INTEGER)?
  public static boolean rangeSelection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeSelection")) return false;
    if (!nextTokenIs(b, INTEGER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INTEGER);
    r = r && rangeSelection_1(b, l + 1);
    exit_section_(b, m, RANGE_SELECTION, r);
    return r;
  }

  // (COMMA INTEGER)?
  private static boolean rangeSelection_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeSelection_1")) return false;
    rangeSelection_1_0(b, l + 1);
    return true;
  }

  // COMMA INTEGER
  private static boolean rangeSelection_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeSelection_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, INTEGER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // query REMOVE resolvableValue
  public static boolean removeFromCollection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "removeFromCollection")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REMOVE_FROM_COLLECTION, "<remove from collection>");
    r = query(b, l + 1, -1);
    r = r && consumeToken(b, REMOVE);
    r = r && resolvableValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // query | commandCall
  public static boolean resolvableValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resolvableValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESOLVABLE_VALUE, "<resolvable value>");
    r = query(b, l + 1, -1);
    if (!r) r = commandCall(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // RETURN_OPERATOR resolvableValue?
  public static boolean returnStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "returnStatement")) return false;
    if (!nextTokenIs(b, RETURN_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RETURN_OPERATOR);
    r = r && returnStatement_1(b, l + 1);
    exit_section_(b, m, RETURN_STATEMENT, r);
    return r;
  }

  // resolvableValue?
  private static boolean returnStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "returnStatement_1")) return false;
    resolvableValue(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // FORWARD_SLASH
  public static boolean rootIndicator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootIndicator")) return false;
    if (!nextTokenIs(b, FORWARD_SLASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FORWARD_SLASH);
    exit_section_(b, m, ROOT_INDICATOR, r);
    return r;
  }

  /* ********************************************************** */
  // SCHEMALESS_IRI
  public static boolean schemalessIriStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "schemalessIriStep")) return false;
    if (!nextTokenIs(b, SCHEMALESS_IRI)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SCHEMALESS_IRI);
    exit_section_(b, m, SCHEMALESS_IRI_STEP, r);
    return r;
  }

  /* ********************************************************** */
  // scriptLine+
  public static boolean script(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCRIPT, "<script>");
    r = scriptLine(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!scriptLine(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "script", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // DOC_COMMENT? (commandBlock | logicalBlock | defineCommandStatement | statement) SEMICOLON*
  public static boolean scriptLine(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptLine")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SCRIPT_LINE, "<script line>");
    r = scriptLine_0(b, l + 1);
    r = r && scriptLine_1(b, l + 1);
    r = r && scriptLine_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // DOC_COMMENT?
  private static boolean scriptLine_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptLine_0")) return false;
    consumeToken(b, DOC_COMMENT);
    return true;
  }

  // commandBlock | logicalBlock | defineCommandStatement | statement
  private static boolean scriptLine_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptLine_1")) return false;
    boolean r;
    r = commandBlock(b, l + 1);
    if (!r) r = logicalBlock(b, l + 1);
    if (!r) r = defineCommandStatement(b, l + 1);
    if (!r) r = statement(b, l + 1);
    return r;
  }

  // SEMICOLON*
  private static boolean scriptLine_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptLine_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, SEMICOLON)) break;
      if (!empty_element_parsed_guard_(b, "scriptLine_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PARENTHESES_OPEN (signatureArgument (COMMA signatureArgument)*)? PARENTHESES_CLOSE
  public static boolean signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signature")) return false;
    if (!nextTokenIs(b, PARENTHESES_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARENTHESES_OPEN);
    r = r && signature_1(b, l + 1);
    r = r && consumeToken(b, PARENTHESES_CLOSE);
    exit_section_(b, m, SIGNATURE, r);
    return r;
  }

  // (signatureArgument (COMMA signatureArgument)*)?
  private static boolean signature_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signature_1")) return false;
    signature_1_0(b, l + 1);
    return true;
  }

  // signatureArgument (COMMA signatureArgument)*
  private static boolean signature_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signature_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = signatureArgument(b, l + 1);
    r = r && signature_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA signatureArgument)*
  private static boolean signature_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signature_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!signature_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "signature_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA signatureArgument
  private static boolean signature_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signature_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && signatureArgument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // resolvableValue | commandBlock
  public static boolean signatureArgument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signatureArgument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIGNATURE_ARGUMENT, "<signature argument>");
    r = resolvableValue(b, l + 1);
    if (!r) r = commandBlock(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // declareVariable | variableAssignment | definePrefix | commandCall |
  //                                             collectionStatement | returnStatement | queryStatement | defineQueryStatement
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STATEMENT, "<statement>");
    r = declareVariable(b, l + 1);
    if (!r) r = variableAssignment(b, l + 1);
    if (!r) r = definePrefix(b, l + 1);
    if (!r) r = commandCall(b, l + 1);
    if (!r) r = collectionStatement(b, l + 1);
    if (!r) r = returnStatement(b, l + 1);
    if (!r) r = queryStatement(b, l + 1);
    if (!r) r = defineQueryStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ASTERIX | PLUS | QUESTION_MARK
  public static boolean stepDecorator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stepDecorator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STEP_DECORATOR, "<step decorator>");
    r = consumeToken(b, ASTERIX);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, QUESTION_MARK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // FORWARD_SLASH
  public static boolean stepSeperator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stepSeperator")) return false;
    if (!nextTokenIs(b, FORWARD_SLASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FORWARD_SLASH);
    exit_section_(b, m, STEP_SEPERATOR, r);
    return r;
  }

  /* ********************************************************** */
  // PARENTHESES_OPEN query PARENTHESES_CLOSE
  public static boolean subQuery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subQuery")) return false;
    if (!nextTokenIs(b, PARENTHESES_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARENTHESES_OPEN);
    r = r && query(b, l + 1, -1);
    r = r && consumeToken(b, PARENTHESES_CLOSE);
    exit_section_(b, m, SUB_QUERY, r);
    return r;
  }

  /* ********************************************************** */
  // VARIABLE_NAME
  public static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    if (!nextTokenIs(b, VARIABLE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE_NAME);
    exit_section_(b, m, VARIABLE, r);
    return r;
  }

  /* ********************************************************** */
  // variable (COMMA variable)* EQUALS variableValue
  public static boolean variableAssignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableAssignment")) return false;
    if (!nextTokenIs(b, VARIABLE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    r = r && variableAssignment_1(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    r = r && variableValue(b, l + 1);
    exit_section_(b, m, VARIABLE_ASSIGNMENT, r);
    return r;
  }

  // (COMMA variable)*
  private static boolean variableAssignment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableAssignment_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!variableAssignment_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variableAssignment_1", c)) break;
    }
    return true;
  }

  // COMMA variable
  private static boolean variableAssignment_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableAssignment_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable
  public static boolean variableStep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableStep")) return false;
    if (!nextTokenIs(b, VARIABLE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    exit_section_(b, m, VARIABLE_STEP, r);
    return r;
  }

  /* ********************************************************** */
  // queryStatement | commandCall
  public static boolean variableValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_VALUE, "<variable value>");
    r = queryStatement(b, l + 1);
    if (!r) r = commandCall(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // WHEN_OPERATOR query? LAMBDA? query?
  public static boolean whenPath(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whenPath")) return false;
    if (!nextTokenIs(b, WHEN_OPERATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHEN_OPERATOR);
    r = r && whenPath_1(b, l + 1);
    r = r && whenPath_2(b, l + 1);
    r = r && whenPath_3(b, l + 1);
    exit_section_(b, m, WHEN_PATH, r);
    return r;
  }

  // query?
  private static boolean whenPath_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whenPath_1")) return false;
    query(b, l + 1, -1);
    return true;
  }

  // LAMBDA?
  private static boolean whenPath_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whenPath_2")) return false;
    consumeToken(b, LAMBDA);
    return true;
  }

  // query?
  private static boolean whenPath_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whenPath_3")) return false;
    query(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // Expression root: query
  // Operator priority table:
  // 0: ATOM(queryPath)
  // 1: N_ARY(queryArray)
  // 2: N_ARY(booleanStatement)
  // 3: BINARY(equationStatement)
  public static boolean query(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "query")) return false;
    addVariant(b, "<query>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<query>");
    r = queryPath(b, l + 1);
    p = r;
    r = r && query_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean query_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "query_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 1 && consumeTokenSmart(b, PIPE)) {
        while (true) {
          r = report_error_(b, query(b, l, 1));
          if (!consumeTokenSmart(b, PIPE)) break;
        }
        exit_section_(b, l, m, QUERY_ARRAY, r, true, null);
      } else if (g < 2 && consumeTokenSmart(b, BOOLEAN_OPERATOR)) {
        while (true) {
          r = report_error_(b, query(b, l, 2));
          if (!consumeTokenSmart(b, BOOLEAN_OPERATOR)) break;
        }
        exit_section_(b, l, m, BOOLEAN_STATEMENT, r, true, null);
      } else if (g < 3 && consumeTokenSmart(b, CONDITIONAL_OPERATOR)) {
        r = query(b, l, 3);
        exit_section_(b, l, m, EQUATION_STATEMENT, r, true, null);
      } else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // rootIndicator? queryOperationStep (stepSeperator queryOperationStep)*
  public static boolean queryPath(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryPath")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, QUERY_PATH, "<query path>");
    r = queryPath_0(b, l + 1);
    r = r && queryOperationStep(b, l + 1);
    r = r && queryPath_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // rootIndicator?
  private static boolean queryPath_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryPath_0")) return false;
    rootIndicator(b, l + 1);
    return true;
  }

  // (stepSeperator queryOperationStep)*
  private static boolean queryPath_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryPath_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!queryPath_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "queryPath_2", c)) break;
    }
    return true;
  }

  // stepSeperator queryOperationStep
  private static boolean queryPath_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "queryPath_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stepSeperator(b, l + 1);
    r = r && queryOperationStep(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
