
/* MiniJava language lexer specification */

/* Note that this lexer specification is not tuned for speed.
   It is in fact quite slow on integer and floating point literals, 
   because the input is read twice and the methods used to parse
   the numbers are not very fast. 
   For a production quality application (e.g. a Java compiler) 
   this could be optimized */


%%

%public
%class Lexer

%unicode

%line
%column


%{
  StringBuilder string = new StringBuilder();
  
  private JSymbol symbol(Token type) {
    return new JSymbol(type, yyline+1, yycolumn+1);
  }

  private JSymbol symbol(Token type, Object value) {
    return new JSymbol(type, yyline+1, yycolumn+1, value);
  }

  /** 
   * assumes correct representation of a long value for 
   * specified radix in scanner buffer from <code>start</code> 
   * to <code>end</code> 
   */
  private long parseLong(int start, int end, int radix) {
    long result = 0;
    long digit;

    for (int i = start; i < end; i++) {
      digit  = Character.digit(yycharat(i),radix);
      result*= radix;
      result+= digit;
    }

    return result;
  }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | 
          {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/*" "*"+ [^/*] ~"*/"

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

// HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
// HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
// HexDigit          = [0-9a-fA-F]

// OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
// OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
// OctDigit          = [0-7]
    
/* floating point literals         
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+
*/

/* string and character literals */
//StringCharacter = [^\r\n\"\\]
/* SingleCharacter = [^\r\n\'\\] */

%state STRING

%%

<YYINITIAL> {

  /* keywords */
  "skip"						 { return symbol(Token.SKIP); }	
  "if"                           { return symbol(Token.IF); }
  "else"                         { return symbol(Token.ELSE); }
  "endif"                        { return symbol(Token.ENDIF); }
  "while"                        { return symbol(Token.WHILE); }
  "endwhile"                     { return symbol(Token.ENDWHILE); }
  "assert"                       { return symbol(Token.ASSERT); }
     
  /* separators */
  "("                            { return symbol(Token.LPAREN); }
  ")"                            { return symbol(Token.RPAREN); }
  ";"                            { return symbol(Token.SEMICOLON); }
  
  /* operators */  
  ":="							 { return symbol(Token.EQSIGN); }
  "=="                           { return symbol(Token.EQUALS); }
  "<="                           { return symbol(Token.LESSEQUALS); }
  "not"                          { return symbol(Token.NOT); }
  "and"                          { return symbol(Token.AND); }
  "or"                           { return symbol(Token.OR); }  
  "+"                            { return symbol(Token.PLUS); }
  "-"                            { return symbol(Token.MINUS); }

  /* numeric literals */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. */
  "-2147483648"                  { return symbol(Token.INTLIT, new Integer(Integer.MIN_VALUE)); }
  
  {DecIntegerLiteral}            { return symbol(Token.INTLIT, new Integer(yytext())); }
  {DecLongLiteral}               { return symbol(Token.INTLIT, new Long(yytext().substring(0,yylength()-1))); }
  
  // {HexIntegerLiteral}            { return symbol(Token.INTLIT, new Integer((int) parseLong(2, yylength(), 16))); }
  // {HexLongLiteral}               { return symbol(Token.INTLIT, new Long(parseLong(2, yylength()-1, 16))); }
 
  // {OctIntegerLiteral}            { return symbol(Token.INTLIT, new Integer((int) parseLong(0, yylength(), 8))); }  
  // {OctLongLiteral}               { return symbol(Token.INTLIT, new Long(parseLong(0, yylength()-1, 8))); }
  
  
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */ 
  {Identifier}                   { return symbol(Token.ID, yytext()); }  
}

// <STRING> {
  // \"                             { yybegin(YYINITIAL); return symbol(Token.STRINGLIT, string.toString()); }
  
  // {StringCharacter}+             { string.append( yytext() ); }
  
  // /* escape sequences */
  // "\\b"                          { string.append( '\b' ); }
  // "\\t"                          { string.append( '\t' ); }
  // "\\n"                          { string.append( '\n' ); }
  // "\\f"                          { string.append( '\f' ); }
  // "\\r"                          { string.append( '\r' ); }
  // "\\\""                         { string.append( '\"' ); }
  // "\\'"                          { string.append( '\'' ); }
  // "\\\\"                         { string.append( '\\' ); }
  // \\[0-3]?{OctDigit}?{OctDigit}  { char val = (char) Integer.parseInt(yytext().substring(1),8);
                        				   // string.append( val ); }
  
  // /* error cases */
  // \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  // {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
// }



/* error fallback */
[^]                              { return symbol(Token.BAD); }
<<EOF>>                          { return symbol(Token.EOF); }