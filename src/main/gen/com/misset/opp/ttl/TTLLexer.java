/* The following code was generated by JFlex 1.7.0 tweaked for IntelliJ platform */

// Based on RDF 1.1 Turtle
// https://www.w3.org/TR/turtle/

package com.misset.opp.ttl;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.psi.TTLIgnored;
import com.misset.opp.ttl.psi.TTLTypes;


/**
 * This class is a scanner generated by
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>TTL.flex</tt>
 */
class TTLLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
          0, 0
  };

  /**
   * Translates characters to character classes
   * Chosen bits are [10, 6, 5]
   * Total runtime size is 3328 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch >> 11] << 6) | ((ch >> 5) & 0x3f)] << 5) | (ch & 0x1f)];
  }

  /* The ZZ_CMAP_Z table has 544 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
          "\1\0\1\1\1\2\1\1\1\3\1\4\1\5\24\1\4\6\1\7\u0200\6");

  /* The ZZ_CMAP_Y table has 512 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
          "\1\0\1\1\1\2\1\3\1\4\1\5\2\6\20\7\3\10\1\11\230\7\1\12\13\7\1\13\1\14\1\15" +
                  "\1\16\10\7\1\17\123\20\37\7\1\17\1\21\77\7\110\20\46\7\1\17\1\16\17\7\1\22");

  /* The ZZ_CMAP_A table has 608 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
          "\11\2\1\25\1\17\2\34\1\17\22\2\1\46\1\0\1\16\1\45\1\0\1\40\1\0\1\12\1\67\1" +
                  "\70\1\0\1\14\1\43\1\13\1\7\1\0\12\6\1\4\1\44\1\1\1\0\1\3\1\0\1\10\1\65\1\64" +
                  "\2\41\1\30\1\63\2\11\1\53\6\11\1\36\1\11\1\52\1\66\1\11\1\22\2\11\1\37\2\11" +
                  "\1\26\1\20\1\27\1\71\1\5\1\2\1\50\1\47\2\41\1\15\1\42\2\11\1\54\2\11\1\72" +
                  "\1\55\1\23\1\57\1\56\1\11\1\60\1\51\1\61\1\21\2\11\1\62\2\11\3\2\7\0\1\35" +
                  "\32\0\1\24\26\0\1\33\10\0\27\31\1\0\50\31\60\33\16\31\1\0\1\31\1\32\37\31" +
                  "\13\24\1\0\2\31\32\0\2\35\5\0\1\24\17\0\2\33\36\0\1\24\20\0\40\31\60\0\1\24" +
                  "\75\31\2\0");

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int[] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
          "\1\0\2\1\1\2\1\1\1\3\1\4\5\1\1\5" +
                  "\1\6\1\7\1\5\2\1\1\10\1\11\1\12\1\13" +
                  "\2\1\1\14\1\15\1\1\1\0\1\16\3\0\1\17" +
                  "\3\20\3\0\1\21\3\0\1\22\4\0\2\12\2\0" +
                  "\1\23\1\24\3\0\1\25\1\26\2\0\2\20\1\0" +
                  "\1\21\1\0\1\22\1\0\1\27\2\0\2\12\6\0" +
                  "\2\20\7\0\2\12\1\30\1\31\1\16\1\0\1\32" +
                  "\1\20\3\0\1\33\2\12\2\20\1\34\1\35\1\36" +
                  "\2\12\1\37\10\12\1\40\1\41";

  private static int[] zzUnpackAction() {
    int[] result = new int[120];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int[] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int[] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
          "\0\0\0\73\0\166\0\261\0\354\0\u0127\0\u0162\0\u019d" +
                  "\0\u01d8\0\u0213\0\u024e\0\u0289\0\u02c4\0\u02ff\0\73\0\u033a" +
                  "\0\u0375\0\u03b0\0\73\0\73\0\u03eb\0\u01d8\0\u0426\0\u0461" +
                  "\0\73\0\73\0\u049c\0\u04d7\0\u0512\0\u054d\0\u0588\0\u05c3" +
                  "\0\u0588\0\u05fe\0\u0639\0\u0674\0\u01d8\0\u06af\0\u06ea\0\u0725" +
                  "\0\u0760\0\u0162\0\u079b\0\u07d6\0\u0811\0\u084c\0\u0887\0\u08c2" +
                  "\0\u08fd\0\u0938\0\u0973\0\u09ae\0\73\0\73\0\u0512\0\u09e9" +
                  "\0\u0a24\0\u0a5f\0\u0a9a\0\u0a9a\0\u0ad5\0\u0b10\0\u0b4b\0\u0b86" +
                  "\0\73\0\u0bc1\0\73\0\u0bfc\0\73\0\u0c37\0\u0c72\0\u0cad" +
                  "\0\u0ce8\0\u0d23\0\u0d5e\0\u0d99\0\u0dd4\0\u0a5f\0\u0e0f\0\u0e4a" +
                  "\0\u0e85\0\u0ec0\0\u0efb\0\u0f36\0\u0f71\0\u0fac\0\u0fe7\0\u1022" +
                  "\0\u105d\0\u1098\0\u01d8\0\u01d8\0\u10d3\0\u110e\0\u05fe\0\u1149" +
                  "\0\u1184\0\u11bf\0\u11fa\0\u01d8\0\u1235\0\u1270\0\u12ab\0\u12e6" +
                  "\0\73\0\u1321\0\u01d8\0\u135c\0\u1397\0\u05fe\0\u13d2\0\u140d" +
                  "\0\u1448\0\u1483\0\u14be\0\u14f9\0\u1534\0\u156f\0\u15aa\0\u15e5";

  private static int[] zzUnpackRowMap() {
    int[] result = new int[120];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int[] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int[] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
          "\1\2\1\3\2\2\1\4\1\5\1\6\1\7\1\10" +
                  "\1\11\1\12\2\13\1\11\1\14\1\15\1\2\3\11" +
                  "\2\15\1\16\1\17\2\11\1\20\1\2\2\15\1\21" +
                  "\1\11\1\2\1\11\1\22\1\23\1\24\1\25\1\15" +
                  "\1\11\1\26\10\11\1\27\2\11\1\30\2\11\1\31" +
                  "\1\32\1\33\1\11\73\0\1\34\3\0\12\34\3\0" +
                  "\4\34\1\0\6\34\1\0\11\34\1\0\22\34\1\0" +
                  "\1\34\4\0\3\35\2\0\1\35\3\0\1\35\3\0" +
                  "\3\35\4\0\3\35\3\0\2\35\1\0\2\35\4\0" +
                  "\20\35\3\0\1\35\4\0\1\36\74\0\1\6\1\37" +
                  "\5\0\1\40\12\0\1\40\50\0\1\41\75\0\1\42" +
                  "\3\0\1\42\3\0\3\42\4\0\1\42\5\0\2\42" +
                  "\1\0\2\42\4\0\1\43\6\42\1\44\10\42\3\0" +
                  "\1\42\4\0\1\4\2\45\2\0\1\45\1\46\1\45" +
                  "\1\0\1\45\3\0\3\45\4\0\4\45\2\0\2\45" +
                  "\1\0\2\45\4\0\20\45\3\0\1\45\12\47\1\50" +
                  "\4\47\1\0\1\51\52\47\6\0\1\6\1\52\63\0" +
                  "\16\53\1\54\1\0\1\55\52\53\17\0\1\15\4\0" +
                  "\2\15\4\0\1\15\1\0\2\15\10\0\1\15\43\0" +
                  "\1\56\4\0\2\56\4\0\1\56\1\0\2\56\10\0" +
                  "\1\56\30\0\1\4\2\45\2\0\1\45\1\46\1\45" +
                  "\1\0\1\45\1\0\1\15\1\0\3\45\2\15\2\0" +
                  "\2\45\1\20\1\45\2\15\2\45\1\0\2\45\3\0" +
                  "\1\15\20\45\3\0\1\45\4\0\1\4\2\45\2\0" +
                  "\1\45\1\46\1\45\1\0\1\45\3\0\3\45\4\0" +
                  "\4\45\2\0\2\45\1\0\2\45\4\0\3\45\1\57" +
                  "\14\45\3\0\1\45\4\0\1\4\2\45\2\0\1\45" +
                  "\1\46\1\45\1\0\1\45\3\0\3\45\4\0\4\45" +
                  "\2\0\2\45\1\0\2\45\4\0\1\45\1\60\16\45" +
                  "\3\0\1\45\17\61\1\0\26\61\1\62\24\61\4\0" +
                  "\1\4\2\45\2\0\1\45\1\46\1\45\1\0\1\45" +
                  "\3\0\3\45\4\0\4\45\2\0\2\45\1\0\2\45" +
                  "\4\0\11\45\1\63\6\45\3\0\1\45\4\0\1\4" +
                  "\2\45\2\0\1\45\1\46\1\45\1\0\1\45\3\0" +
                  "\3\45\4\0\4\45\2\0\2\45\1\0\2\45\4\0" +
                  "\16\45\1\64\1\45\3\0\1\45\71\0\1\65\1\0" +
                  "\1\34\2\0\1\66\12\34\3\0\4\34\1\0\6\34" +
                  "\1\0\11\34\1\0\22\34\1\0\1\34\4\0\3\35" +
                  "\1\67\1\0\1\35\1\70\1\35\1\0\1\35\3\0" +
                  "\3\35\4\0\4\35\2\0\2\35\1\71\2\35\4\0" +
                  "\20\35\3\0\1\35\5\0\2\72\2\0\1\72\3\0" +
                  "\1\72\3\0\3\72\4\0\3\72\3\0\2\72\1\0" +
                  "\2\72\4\0\20\72\3\0\1\72\6\0\1\41\6\0" +
                  "\1\40\12\0\1\40\50\0\1\73\4\0\2\74\67\0" +
                  "\1\42\1\75\2\0\1\42\3\0\3\42\4\0\1\42" +
                  "\5\0\2\42\1\0\2\42\4\0\20\42\3\0\1\42" +
                  "\11\0\1\42\1\75\2\0\1\42\3\0\3\42\4\0" +
                  "\1\42\5\0\2\42\1\0\2\42\4\0\1\42\1\76" +
                  "\16\42\3\0\1\42\11\0\1\42\1\75\2\0\1\42" +
                  "\3\0\3\42\4\0\1\42\5\0\2\42\1\0\2\42" +
                  "\4\0\11\42\1\77\6\42\3\0\1\42\17\100\1\0" +
                  "\14\100\2\0\35\100\12\47\1\101\4\47\1\0\1\51" +
                  "\52\47\12\0\1\102\72\0\1\47\3\0\1\47\1\0" +
                  "\1\47\2\0\1\47\16\0\1\47\4\0\1\47\10\0" +
                  "\2\47\11\0\16\53\1\103\1\0\1\55\52\53\16\0" +
                  "\1\104\66\0\1\53\3\0\1\53\1\0\1\53\2\0" +
                  "\1\53\16\0\1\53\4\0\1\53\10\0\2\53\30\0" +
                  "\1\56\4\0\2\56\1\0\1\105\2\0\1\56\1\0" +
                  "\2\56\10\0\1\56\30\0\1\4\2\45\2\0\1\45" +
                  "\1\46\1\45\1\0\1\45\3\0\3\45\4\0\1\106" +
                  "\3\45\2\0\2\45\1\0\2\45\4\0\20\45\3\0" +
                  "\1\45\4\0\1\4\2\45\2\0\1\45\1\46\1\45" +
                  "\1\0\1\45\3\0\3\45\4\0\4\45\2\0\2\45" +
                  "\1\0\2\45\4\0\20\45\3\0\1\107\17\61\1\0" +
                  "\72\61\1\0\27\61\1\110\4\61\1\111\16\61\4\0" +
                  "\1\4\2\45\2\0\1\45\1\46\1\45\1\0\1\45" +
                  "\3\0\1\112\2\45\4\0\4\45\2\0\2\45\1\0" +
                  "\2\45\4\0\20\45\3\0\1\45\4\0\1\4\2\45" +
                  "\2\0\1\45\1\46\1\45\1\0\1\45\3\0\3\45" +
                  "\4\0\4\45\2\0\2\45\1\0\2\45\4\0\17\45" +
                  "\1\113\3\0\1\45\12\0\1\114\66\0\1\115\6\0" +
                  "\1\115\12\0\1\115\10\0\2\115\4\0\2\115\12\0" +
                  "\3\115\12\0\2\72\1\116\1\0\1\72\1\0\1\72" +
                  "\1\0\1\72\3\0\3\72\4\0\4\72\2\0\2\72" +
                  "\1\0\2\72\4\0\20\72\3\0\1\72\6\0\1\73" +
                  "\77\0\1\117\70\0\1\42\1\75\2\0\1\42\3\0" +
                  "\3\42\4\0\1\42\5\0\2\42\1\0\2\42\4\0" +
                  "\2\42\1\120\15\42\3\0\1\42\11\0\1\42\1\75" +
                  "\2\0\1\121\3\0\3\42\4\0\1\42\5\0\2\42" +
                  "\1\0\2\42\4\0\20\42\3\0\1\42\12\0\1\122" +
                  "\60\0\12\102\1\123\5\102\1\124\52\102\16\104\1\125" +
                  "\1\104\1\126\52\104\4\0\1\4\2\45\2\0\1\45" +
                  "\1\46\1\45\1\0\1\45\3\0\3\45\4\0\4\45" +
                  "\2\0\2\45\1\0\2\45\4\0\14\45\1\127\3\45" +
                  "\3\0\1\45\4\0\1\4\2\45\2\0\1\45\1\46" +
                  "\1\45\1\0\1\45\3\0\3\45\4\0\4\45\2\0" +
                  "\2\45\1\0\2\45\4\0\2\45\1\130\15\45\3\0" +
                  "\1\45\17\61\1\0\30\61\1\131\41\61\1\0\35\61" +
                  "\1\132\15\61\4\0\1\4\2\45\2\0\1\45\1\46" +
                  "\1\45\1\0\1\133\3\0\3\45\4\0\4\45\2\0" +
                  "\2\45\1\0\2\45\4\0\20\45\3\0\1\45\4\0" +
                  "\1\4\2\45\2\0\1\45\1\46\1\45\1\0\1\45" +
                  "\3\0\3\45\4\0\1\134\3\45\2\0\2\45\1\0" +
                  "\2\45\4\0\20\45\3\0\1\45\12\0\1\135\66\0" +
                  "\1\35\6\0\1\35\12\0\1\35\10\0\2\35\4\0" +
                  "\2\35\12\0\3\35\17\0\1\136\71\0\1\42\1\75" +
                  "\2\0\1\137\3\0\3\42\4\0\1\42\5\0\2\42" +
                  "\1\0\2\42\4\0\20\42\3\0\1\42\11\0\1\42" +
                  "\1\75\2\0\1\42\3\0\3\42\4\0\1\42\5\0" +
                  "\2\42\1\0\1\42\1\140\4\0\20\42\3\0\1\42" +
                  "\5\0\2\45\2\0\1\45\1\46\1\45\1\0\1\45" +
                  "\3\0\3\45\4\0\4\45\2\0\2\45\1\0\2\45" +
                  "\4\0\20\45\3\0\1\45\12\102\1\141\5\102\1\124" +
                  "\52\102\12\0\1\102\3\0\1\102\1\0\1\102\2\0" +
                  "\1\102\16\0\1\102\4\0\1\102\10\0\2\102\11\0" +
                  "\16\104\1\142\1\104\1\126\52\104\12\0\1\104\3\0" +
                  "\1\104\1\0\1\104\2\0\1\104\16\0\1\104\4\0" +
                  "\1\104\10\0\2\104\15\0\1\4\2\45\2\0\1\45" +
                  "\1\46\1\45\1\0\1\45\3\0\3\45\4\0\4\45" +
                  "\2\0\2\45\1\0\2\45\4\0\4\45\1\143\13\45" +
                  "\3\0\1\45\4\0\1\4\2\45\2\0\1\45\1\46" +
                  "\1\45\1\0\1\144\3\0\3\45\4\0\4\45\2\0" +
                  "\2\45\1\0\2\45\4\0\20\45\3\0\1\45\17\61" +
                  "\1\0\31\61\1\145\40\61\1\0\36\61\1\146\14\61" +
                  "\73\135\6\0\1\147\2\0\1\147\3\0\1\147\3\0" +
                  "\3\147\4\0\1\147\5\0\2\147\1\0\2\147\4\0" +
                  "\20\147\3\0\1\147\11\0\1\42\1\75\2\0\1\42" +
                  "\3\0\3\42\4\0\1\42\5\0\2\42\1\0\2\42" +
                  "\4\0\5\42\1\150\12\42\3\0\1\42\12\102\1\151" +
                  "\5\102\1\124\52\102\16\0\1\152\60\0\1\4\2\45" +
                  "\2\0\1\45\1\46\1\45\1\0\1\45\3\0\3\45" +
                  "\4\0\4\45\2\0\1\45\1\153\1\0\2\45\4\0" +
                  "\20\45\3\0\1\45\15\61\1\154\1\61\1\0\72\61" +
                  "\1\0\37\61\1\155\13\61\6\0\1\147\2\0\1\147" +
                  "\1\75\2\0\1\147\3\0\3\147\4\0\1\147\5\0" +
                  "\2\147\1\0\2\147\4\0\20\147\3\0\1\147\11\0" +
                  "\1\42\1\75\2\0\1\42\3\0\3\42\4\0\1\42" +
                  "\5\0\2\42\1\0\2\42\4\0\13\42\1\156\4\42" +
                  "\3\0\1\42\16\104\1\0\1\104\1\126\52\104\17\61" +
                  "\1\0\2\61\1\157\67\61\1\0\40\61\1\160\31\61" +
                  "\1\0\32\61\1\161\37\61\1\0\41\61\1\162\30\61" +
                  "\1\0\33\61\1\163\36\61\1\0\31\61\1\164\25\61" +
                  "\1\165\12\61\1\0\57\61\1\166\12\61\1\0\72\61" +
                  "\1\0\26\61\1\167\43\61\1\0\26\61\1\170\24\61" +
                  "\17\167\1\0\53\167\17\170\1\0\53\170";

  private static int[] zzUnpackTrans() {
    int[] result = new int[5664];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int[] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
          "Unknown internal scanner error",
          "Error: could not match input",
          "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
          "\1\0\1\11\14\1\1\11\3\1\2\11\4\1\2\11" +
                  "\1\1\1\0\1\1\3\0\4\1\3\0\1\1\3\0" +
                  "\1\1\4\0\2\1\2\0\2\11\3\0\2\1\2\0" +
                  "\2\1\1\0\1\11\1\0\1\11\1\0\1\11\2\0" +
                  "\2\1\6\0\2\1\7\0\5\1\1\0\2\1\3\0" +
                  "\5\1\1\11\17\1";

  private static int[] zzUnpackAttribute() {
    int[] result = new int[120];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int[] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /**
   * the input device
   */
  private java.io.Reader zzReader;

  /**
   * the current state of the DFA
   */
  private int zzState;

  /**
   * the current lexical state
   */
  private int zzLexicalState = YYINITIAL;

  /**
   * this buffer contains the current text to be matched and is
   * the source of the yytext() string
   */
  private CharSequence zzBuffer = "";

  /**
   * the textposition at the last accepting state
   */
  private int zzMarkedPos;

  /**
   * the current text position in the buffer
   */
  private int zzCurrentPos;

  /**
   * startRead marks the beginning of the yytext() string in the buffer
   */
  private int zzStartRead;

  /**
   * endRead marks the last character in the buffer, that has been read
   * from input
   */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /**
   * zzAtEOF == true <=> the scanner is at the EOF
   */
  private boolean zzAtEOF;

  /**
   * denotes if the user-EOF-code has already been executed
   */
  private boolean zzEOFDone;


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  TTLLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /**
   * Unpacks the compressed character translation table.
   *
   * @param packed the packed character translation table
   * @return the unpacked character translation table
   */
  private static char[] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead + pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos - zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   * <p>
   * They will be read again by then next call of the scanning method
   *
   * @param number the number of characters to be read again.
   *               This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;

    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int[] zzTransL = ZZ_TRANS;
    int[] zzRowMapL = ZZ_ROWMAP;
    int[] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ((zzAttributes & 1) == 1) {
        zzAction = zzState;
      }


      zzForAction:
      {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          } else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          } else {
            // store back cached positions
            zzCurrentPos = zzCurrentPosL;
            zzMarkedPos = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL = zzCurrentPos;
            zzMarkedPosL = zzMarkedPos;
            zzBufferL = zzBuffer;
            zzEndReadL = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            } else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput)];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ((zzAttributes & 1) == 1) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        zzDoEOF();
        return null;
      } else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: {
            return TokenType.BAD_CHARACTER;
          }
          // fall through
          case 34:
            break;
          case 2: {
            return TTLTypes.PNAME_NS;
          }
          // fall through
          case 35:
            break;
          case 3: {
            return TTLTypes.INTEGER;
          }
          // fall through
          case 36:
            break;
          case 4: {
            return TTLTypes.DOT;
          }
          // fall through
          case 37:
            break;
          case 5: {
            return TokenType.WHITE_SPACE;
          }
          // fall through
          case 38:
            break;
          case 6: {
            return TTLTypes.BRACKET_OPEN;
          }
          // fall through
          case 39:
            break;
          case 7: {
            return TTLTypes.BRACKET_CLOSE;
          }
          // fall through
          case 40:
            break;
          case 8: {
            return TTLTypes.COMMA;
          }
          // fall through
          case 41:
            break;
          case 9: {
            return TTLTypes.SEMICOLON;
          }
          // fall through
          case 42:
            break;
          case 10: {
            return TTLIgnored.COMMENT;
          }
          // fall through
          case 43:
            break;
          case 11: {
            return TTLTypes.A;
          }
          // fall through
          case 44:
            break;
          case 12: {
            return TTLTypes.PARENTHESES_OPEN;
          }
          // fall through
          case 45:
            break;
          case 13: {
              return TTLTypes.PARENTHESES_CLOSE;
          }
          // fall through
          case 46:
            break;
          case 14: {
            return TTLTypes.PNAME_LN;
          }
          // fall through
          case 47:
            break;
          case 15: {
            return TTLTypes.DECIMAL;
          }
          // fall through
          case 48:
            break;
          case 16: {
            return TTLTypes.LANGTAG;
          }
          // fall through
          case 49:
            break;
          case 17: {
            return TTLTypes.STRING_LITERAL_SINGLE_QUOTE;
          }
          // fall through
          case 50:
            break;
          case 18: {
            return TTLTypes.STRING_LITERAL_QUOTE;
          }
          // fall through
          case 51:
            break;
          case 19: {
            return TTLTypes.DATATYPE_LEADING;
          }
          // fall through
          case 52:
            break;
          case 20: {
            return TTLTypes.IRIREF;
          }
          // fall through
          case 53:
            break;
          case 21: {
            return TTLTypes.BLANK_NODE_LABEL;
          }
          // fall through
          case 54:
            break;
          case 22: {
            return TTLTypes.DOUBLE;
          }
          // fall through
          case 55:
            break;
          case 23: {
            return TTLTypes.ANON;
          }
          // fall through
          case 56:
            break;
          case 24: {
            return TTLTypes.TRUE;
          }
          // fall through
          case 57:
            break;
          case 25: {
            return TTLTypes.BASE_LEADING;
          }
          // fall through
          case 58:
            break;
          case 26: {
            return TTLTypes.ATBASE;
          }
          // fall through
          case 59:
            break;
          case 27: {
            return TTLTypes.FALSE;
          }
          // fall through
          case 60:
            break;
          case 28: {
            return TTLTypes.STRING_LITERAL_LONG_SINGLE_QUOTE;
          }
          // fall through
          case 61:
            break;
          case 29: {
            return TTLTypes.STRING_LITERAL_LONG_QUOTE;
          }
          // fall through
          case 62:
            break;
          case 30: {
            return TTLTypes.PREFIX_LEADING;
          }
          // fall through
          case 63:
            break;
          case 31: {
            return TTLTypes.ATPREFIX;
          }
          // fall through
          case 64:
            break;
          case 32: {
            return TTLTypes.BASE_URI;
          }
          // fall through
          case 65:
            break;
          case 33: {
            return TTLTypes.IMPORT_URI;
            } 
            // fall through
          case 66: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
