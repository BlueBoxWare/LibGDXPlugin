/* The following code was generated by JFlex 1.7.0-SNAPSHOT tweaked for IntelliJ platform */

package com.gmail.blueboxware.libgdxplugin.filetypes.skin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import static com.intellij.psi.TokenType.WHITE_SPACE;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0-SNAPSHOT
 * from the specification file <tt>_SkinLexer.flex</tt>
 */
public class _SkinLexer implements FlexLexer {

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
   * Chosen bits are [7, 7, 7]
   * Total runtime size is 1928 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[ZZ_CMAP_Z[ch>>14]|((ch>>7)&0x7f)]<<7)|(ch&0x7f)];
  }

  /* The ZZ_CMAP_Z table has 68 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\103\200");

  /* The ZZ_CMAP_Y table has 256 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\53\2\1\3\22\2\1\4\37\2\1\3\237\2");

  /* The ZZ_CMAP_A table has 640 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\20\1\2\2\1\1\2\22\0\1\20\1\0\1\6\4\0\1\10\2\0\1\5\1\16\1\25\1\11\1"+
    "\14\1\4\1\12\11\13\1\17\12\0\1\15\25\0\1\23\1\7\1\24\3\0\1\33\3\0\1\31\1\32"+
    "\5\0\1\34\1\0\1\36\3\0\1\27\1\35\1\26\1\30\5\0\1\21\1\0\1\22\7\0\1\1\32\0"+
    "\1\3\337\0\1\3\177\0\13\3\35\0\2\1\5\0\1\3\57\0\1\3\40\0");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\2\1\1\1\3\1\4\1\1\2\5"+
    "\1\1\1\6\1\7\1\10\1\11\1\12\1\13\3\1"+
    "\1\14\1\15\1\3\2\0\1\4\1\1\2\5\3\1"+
    "\1\14\1\15\1\1\1\5\3\1\1\0\1\15\1\16"+
    "\1\1\1\17\1\15\1\20";

  private static int [] zzUnpackAction() {
    int [] result = new int[46];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
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
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\37\0\76\0\135\0\174\0\233\0\272\0\331"+
    "\0\370\0\u0117\0\u0136\0\u0155\0\u0155\0\u0155\0\u0155\0\u0155"+
    "\0\u0155\0\u0174\0\u0193\0\u01b2\0\u01d1\0\u01f0\0\u0155\0\u020f"+
    "\0\u022e\0\u0155\0\u024d\0\u026c\0\u0136\0\u028b\0\u02aa\0\u02c9"+
    "\0\u02e8\0\u0307\0\u0326\0\u0345\0\u0364\0\u0383\0\u03a2\0\u03c1"+
    "\0\37\0\37\0\u03e0\0\37\0\u0155\0\37";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[46];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
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
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\3\1\5\1\2\1\6\1\2"+
    "\1\7\1\10\1\11\1\12\1\13\2\2\1\14\1\4"+
    "\1\15\1\16\1\17\1\20\1\21\1\22\3\2\1\23"+
    "\3\2\1\24\2\2\1\0\3\2\1\0\1\2\1\0"+
    "\6\2\7\0\12\2\1\3\1\4\1\3\2\2\1\0"+
    "\1\2\1\0\6\2\1\0\1\4\5\0\11\2\1\0"+
    "\3\4\14\0\1\4\16\0\2\2\1\0\1\2\1\25"+
    "\1\26\1\0\1\2\1\0\6\2\7\0\11\2\2\6"+
    "\1\0\3\6\1\27\1\30\27\6\2\7\1\0\4\7"+
    "\1\31\1\32\26\7\2\2\1\0\3\2\1\0\1\2"+
    "\1\0\1\2\1\11\1\12\1\13\2\2\7\0\13\2"+
    "\1\0\3\2\1\0\1\2\1\0\3\2\1\33\1\34"+
    "\1\2\7\0\3\2\1\34\7\2\1\0\3\2\1\0"+
    "\1\2\1\0\1\2\2\12\1\33\1\34\1\2\7\0"+
    "\3\2\1\34\7\2\1\0\3\2\1\0\1\2\1\0"+
    "\1\2\2\35\3\2\7\0\11\2\37\0\2\2\1\0"+
    "\3\2\1\0\1\2\1\0\6\2\7\0\1\2\1\36"+
    "\11\2\1\0\3\2\1\0\1\2\1\0\6\2\7\0"+
    "\5\2\1\37\5\2\1\0\3\2\1\0\1\2\1\0"+
    "\6\2\7\0\2\2\1\40\6\2\1\25\1\2\1\0"+
    "\3\25\1\41\1\25\1\41\6\25\7\41\11\25\2\26"+
    "\1\42\2\26\1\43\1\42\1\26\1\42\6\26\7\42"+
    "\11\26\2\6\1\0\34\6\2\7\1\0\34\7\2\2"+
    "\1\0\3\2\1\0\1\2\1\0\1\2\2\44\3\2"+
    "\7\0\13\2\1\0\3\2\1\0\1\2\1\0\3\35"+
    "\2\2\1\35\7\0\13\2\1\0\3\2\1\0\1\2"+
    "\1\0\6\2\7\0\2\2\1\45\10\2\1\0\3\2"+
    "\1\0\1\2\1\0\6\2\7\0\6\2\1\46\4\2"+
    "\1\0\3\2\1\0\1\2\1\0\6\2\7\0\6\2"+
    "\1\47\2\2\1\41\2\0\34\41\5\42\1\50\31\42"+
    "\2\26\1\42\1\26\1\51\1\43\1\42\1\26\1\42"+
    "\6\26\7\42\11\26\2\2\1\0\3\2\1\0\1\2"+
    "\1\0\1\2\2\44\1\2\1\34\1\2\7\0\3\2"+
    "\1\34\7\2\1\0\3\2\1\0\1\2\1\0\6\2"+
    "\7\0\3\2\1\52\7\2\1\0\3\2\1\0\1\2"+
    "\1\0\6\2\7\0\7\2\1\53\3\2\1\0\3\2"+
    "\1\0\1\2\1\0\6\2\7\0\6\2\1\54\2\2"+
    "\4\42\1\55\1\50\31\42\2\2\1\0\3\2\1\0"+
    "\1\2\1\0\6\2\7\0\3\2\1\56\5\2";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1023];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
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
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\12\1\6\11\5\1\1\11\2\0\1\11\15\1"+
    "\1\0\4\1\1\11\1\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[46];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
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

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _SkinLexer() {
    this((java.io.Reader)null);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public _SkinLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
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
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
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
   * Returns the character at position <tt>pos</tt> from the
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
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
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
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return UNQUOTED_STRING;
            }
          case 17: break;
          case 2: 
            { return WHITE_SPACE;
            }
          case 18: break;
          case 3: 
            { return DOUBLE_QUOTED_STRING;
            }
          case 19: break;
          case 4: 
            { return SINGLE_QUOTED_STRING;
            }
          case 20: break;
          case 5: 
            { return NUMBER;
            }
          case 21: break;
          case 6: 
            { return COLON;
            }
          case 22: break;
          case 7: 
            { return L_CURLY;
            }
          case 23: break;
          case 8: 
            { return R_CURLY;
            }
          case 24: break;
          case 9: 
            { return L_BRACKET;
            }
          case 25: break;
          case 10: 
            { return R_BRACKET;
            }
          case 26: break;
          case 11: 
            { return COMMA;
            }
          case 27: break;
          case 12: 
            { return LINE_COMMENT;
            }
          case 28: break;
          case 13: 
            { return BLOCK_COMMENT;
            }
          case 29: break;
          case 14: 
            { return TRUE;
            }
          case 30: break;
          case 15: 
            { return NULL;
            }
          case 31: break;
          case 16: 
            { return FALSE;
            }
          case 32: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
