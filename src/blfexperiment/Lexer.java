/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

/**
 * This performs low level lexical analysis ( badly )  @see  {@link blfexperiment.BFLExpressionParser} 
 * nessasry because we want more control over the lexing process. That is 
 * it's not an even process. 
 * 
 * @author Sheep Dalton
 */
class Lexer 
{ 
    public final static int TT_EOL = java.io.StreamTokenizer.TT_EOL ; // 10 !
    public final static int TT_EOF = java.io.StreamTokenizer.TT_EOF ; // -1 
    public final static int TT_NUMBER =  java.io.StreamTokenizer.TT_NUMBER ; // -2 
    public final static int TT_WORD   = java.io.StreamTokenizer.TT_WORD ; // -3 
    public final static int TT_WHITESPACE  = java.io.StreamTokenizer.TT_WORD - 1 ; // -4
    public final static int TT_SYMBOL  = java.io.StreamTokenizer.TT_WORD - 2 ;
    public final static int TT_STRING  = java.io.StreamTokenizer.TT_WORD - 3; 
    public final static int NO_TOKEN = -1000; 
    
    int     currenToken = NO_TOKEN ;
    int uniCodeLetter = NO_TOKEN ; 
   // String sVal = null ; 
    int currentLine ; 
    int lineNumber = 1 ;
    int charNumber = 1 ; 
    boolean skipWhiteSpace  = true ; 
    Deque<Token>  tokenQue = new LinkedList<>(); 
    
     //=================================
    public interface Token
    { 
       public  int getTokenType();   
    }
    //=================================
    public class WordToken implements Token 
    { 
        StringBuffer  theText ; 
        WordToken(){ theText = new StringBuffer(); } 
        String getText(){ return theText.toString(); } 
        void addLetter(int letter ) { theText.appendCodePoint( letter); } 
        @Override
        public int getTokenType(){ return TT_WORD; } 
        @Override
        public String toString(){ return  "{"+theText+"}"; } 
    }
    //=================================
    public class StringToken extends WordToken 
    { 
          StringToken(){ super(); }  
          @Override
        public int getTokenType(){ return TT_STRING; } 
    }
    //=================================
    public class NumberToken extends WordToken
    { 
        public NumberToken(){ super(); } 
        String getNumberAsText(){ return getText(); } 
        public int getTokenType(){ return TT_NUMBER; } 
    }
    //=================================
    public class WhiteSpace implements Token
    { 
        int count = 0  ; 
        WhiteSpace(  ) {count  = 0 ; } 
        void addSpace(){ count += 1; } 
        void addTab(){ count +=  4 ; } // tab is 4 spaces 
        int  getCount(){ return count; } 
        
        @Override
        public int getTokenType(){ return TT_WHITESPACE; }  
        @Override
        public String toString(){ return "whitedspace #"+ count; } 
    }
    //=================================
    public class SingleSymbol implements Token
    { 
        int symbol ;
        public SingleSymbol( int symbol ) {  this.symbol = symbol ; } 
        public int getSymbol(){ return symbol; } 
        @Override
        public int getTokenType(){ return TT_SYMBOL; } 
        @Override
        public String toString(){ return "<"+ (char)symbol +"> (" + symbol+")" ; } 
    }
    
    //=================================
    public class EndOfLine implements Token
    { 
        EndOfLine(  ) { } 
        @Override
        public int getTokenType(){ return TT_EOL; }  
        @Override
        public String toString(){ return  " END OF LINE ";  } 
    }
    //=================================
    public class EndOfFile implements Token
    { 
        EndOfFile(  ) { } 
        @Override
        public int getTokenType(){ return TT_EOF; }  
         @Override
        public String toString(){ return  " END OF FILE ";  } 
    }
    //=================================

    Reader input = null ;
     
    public Lexer(  Reader r )
    { 
       assert r != null : "only use valid readers not null  "; 
       this.input = r ;
    }
    //--------------------------------------------------------------------------
    public int getLineNumber() { return lineNumber ; } 
    //--------------------------------------------------------------------------
    /**
     *  method available for subclass to override 
     *  A comment has been found and this keeps it. 
     * @param s 
     */
    private void endOfComment( String s ) 
    { 
        // for sub class to override when a comment is found 
    }
    //--------------------------------------------------------------------------
    protected void pushBackToStream(int letter )
    { 
         assert currenToken == NO_TOKEN; 
         assert letter != NO_TOKEN; 
         currenToken = letter; 
    }
    //--------------------------------------------------------------------------
    /**
     *  Gets the next codePoint - will skip comments and store in comment var. 
     * @return 
     */
    protected int getNextCharacter() 
    {  
    if( currenToken != NO_TOKEN )
    {
         //System.out.println("U leter cahced " + ( uniCodeLetter) + " ="+ (char)uniCodeLetter);
        int old = currenToken ;
        currenToken = NO_TOKEN; 
        
        return old; 
    } 
    try{ 
           uniCodeLetter =  input.read();
          //System.out.println("U leter " + ( uniCodeLetter) + " ="+ (char)uniCodeLetter);
           charNumber += 1; 
           // should handle comments. endOfComment(s); 
           return uniCodeLetter ; 
        }catch( IOException  e )
        { 
           System.err.println("getNextCharacter::end of file reached." +e); 
           uniCodeLetter = TT_EOF; 
           return TT_EOF  ; 
        }
    }
    //--------------------------------------------------------------------------
    /**
     * showCurrentPosition - 
     * @return 
     */
    String showCurrentPosition()
    { 
       if( ! tokenQue.isEmpty()){ Token t = tokenQue.peekFirst(); return t.toString(); } 
       return " char " + (char)uniCodeLetter + " ";
    }
    //--------------------------------------------------------------------------
    /**
     *  removeNextToken get the next token ( or EOF token if EOF ) 
     * @return 
     */
    public Token removeNextToken( )
    { 
       if( ! tokenQue.isEmpty())
       {  
           if( isSkipingWhiteSpace() ) 
           { while( tokenQue.peekFirst().getTokenType()==TT_WHITESPACE )
            { 
                tokenQue.pollFirst(); // remove 
            }
           }
           if( !tokenQue.isEmpty() ) return tokenQue.pollFirst();
       } 
   
       int unicodeChar  = getNextCharacter();
        
       if(  unicodeChar == TT_EOF)return new EndOfFile();
       if(  unicodeChar == '\n'  || unicodeChar ==  '\r' ) return new EndOfLine(); 
       
       if( Character.isWhitespace(unicodeChar) )
       { 
            WhiteSpace  ws = new WhiteSpace();
            do 
            { 
             if( unicodeChar == '\t')ws.addTab();
             else 
             { 
                if( Character.isWhitespace(unicodeChar) ) ws.addSpace();
             }
             unicodeChar = getNextCharacter();
            } while( Character.isWhitespace(unicodeChar) && unicodeChar != TT_EOF); 
           
           
          if( ! isSkipingWhiteSpace()){ pushBackToStream( unicodeChar ); return ws ; } 
          //else  { System.out.println("\n<skip space>"); } 
       }
       
       if(Character.isLetter(unicodeChar)) // OR IS  '?' -- So can say alive?() 
       { 
           WordToken wt = new WordToken(); 
           while(  Character.isLetter( unicodeChar ) )
           { 
               wt.addLetter(unicodeChar);
               unicodeChar = getNextCharacter();  
           } 
           pushBackToStream( unicodeChar );  
           return wt ; 
       }
      if( Character.isDigit(unicodeChar) )
      { 
         NumberToken nt =  new NumberToken(); 
         do 
         {
            nt.addLetter(unicodeChar);
            //System.out.println("#"+ nt.getText());
            unicodeChar = getNextCharacter();  
         }while( Character.isDigit(unicodeChar) );
         pushBackToStream( unicodeChar );  
         return nt ;     
      } 
      if( unicodeChar == '"' )
      { 
           StringToken wt = new StringToken(); 
            //"wt.addLetter(unicodeChar);
            unicodeChar = getNextCharacter();  
           while( !(  unicodeChar == '"' ||  unicodeChar == '\n' || unicodeChar == TT_EOF)   )
           { 
               wt.addLetter(unicodeChar);
               unicodeChar = getNextCharacter();  
           } 
           System.out.println("LITERAL"+wt.toString());
           //@@@ TODO THROW ERROR if next token is \n or EOF 
          // pushBackToStream( unicodeChar );  
           return wt ; 
      }
      if( unicodeChar == '“' )// WORD  SMART QUOTES- you start with smart quotes you end with smart quotes.
      { 
           StringToken wt = new StringToken(); // initally empty string.
           unicodeChar = getNextCharacter(); 
           
           while( !(  unicodeChar == '”' ||  unicodeChar == '\n' || unicodeChar == TT_EOF)   )
           { 
               wt.addLetter(unicodeChar);
               unicodeChar = getNextCharacter();  
           } 
           //@@@ TODO THROW ERROR if next token is \n or EOF 
           
           return wt ; 
      }
      // check for -- for 
      return new SingleSymbol( unicodeChar); 
    }
    //--------------------------------------------------------------------------
    /**
     * pushTokenBackToHead 
     * @param t the token to push back.
     */
    void pushTokenBackToHead( Token t )
    { 
        tokenQue.addFirst(t);
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    public StringToken removeNextTokenAsLiteralString()
    { 
        Token t   =  removeNextToken( ); 
        if( t.getTokenType() != this.TT_STRING )
        { 
           pushTokenBackToHead(t); 
           return null ;
        } 
        StringToken s = (StringToken)t; 
        return s; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    public SingleSymbol removeNextTokenAsSymbol()
    { 
        Token t =  removeNextToken( ); 
        if( t.getTokenType() != TT_SYMBOL )
        { 
           pushTokenBackToHead(t); 
           return null ;
        } 
        SingleSymbol s = (SingleSymbol)t; 
        return s; 
    }
    //--------------------------------------------------------------------------
    /**
     *  get removeNextToken Word
     * @return 
     */
    public WordToken removeNextTokenAsWord()
    { 
        Token t =  removeNextToken( ); 
        if( t.getTokenType() != TT_WORD )
        { 
           pushTokenBackToHead(t); assert false ; 
           return null ;
        } 
        WordToken s = (WordToken)t; assert s != null ; 
        return s; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    public WhiteSpace removeNextTokenAsWhiteSpace()
    { 
        Token t =  removeNextToken( ); 
        if( t.getTokenType() != TT_WHITESPACE )
        { 
           pushTokenBackToHead(t); 
           return null ;
        } 
        WhiteSpace s = (WhiteSpace)t; 
        return s; 
    }
    //--------------------------------------------------------------------------
    public boolean hasQuoteStringAvailable()
    { 
        Token t = removeNextToken(); 
        pushTokenBackToHead( t );
        //System.out.println("$$$" + t.toString());
      return t.getTokenType() ==  TT_STRING ; 
    }
    //--------------------------------------------------------------------------
    /**
     *  is whitespace available 
     * @return 
     */
    public boolean hasWhiteSpaceAvailable()
    { 
        Token t = removeNextToken(); 
      pushTokenBackToHead( t );
        //System.out.println("$$$" + t.toString());
      return t.getTokenType() ==  TT_WHITESPACE ; 
    }
    //--------------------------------------------------------------------------
    /**
     *  is next time hasEOF ? 
     * @return 
     */
    boolean hasEOFAvilable()
    { 
      Token t = removeNextToken(); 
      pushTokenBackToHead( t );
        //System.out.println("$$$" + t.toString());
      return t.getTokenType() ==  TT_EOF; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @return 
     */
    boolean hasAnyWordAvilable()
    { 
      Token t = removeNextToken(); 
      pushTokenBackToHead( t );
        //System.out.println("$$$" + t.toString());
      return t.getTokenType() == TT_WORD; 
    } 
    //--------------------------------------------------------------------------
    /**
     * hasThisWord return true if the compiler has any of this. 
     * @param word
     * @return 
     */
    boolean hasThisWord( String word )
    { 
         Token t = removeNextToken(); 
         pushTokenBackToHead( t );
         if(  t.getTokenType() != TT_WORD ) return false ; 
         assert t instanceof WordToken :"NON instanceof"; 
         WordToken wt = (WordToken)t; 
         return wt.getText().equalsIgnoreCase(word);   
    }
    //--------------------------------------------------------------------------
    /**
     * check to see if the next token is any of these words. 
     * @param words
     * @return 
     */
    boolean hasAnyOfTheseWords( String words[])
    { 
        Token t = removeNextToken(); 
        pushTokenBackToHead( t );
        if(  t.getTokenType() != TT_WORD ) return false ; 
        assert t instanceof WordToken :"NON instanceof"; 
        WordToken wt = (WordToken)t; 
        for( String word : words)
        { 
            if( wt.getText().equalsIgnoreCase(word)) return true  ; 
        } 
        return false ; 
    }
    //--------------------------------------------------------------------------
    /**
     * Tests to see 
     * @return 
     */
    boolean hasANumber()
    { 
        Token t = removeNextToken(); 
        pushTokenBackToHead( t );
        //System.out.println("$$$" + t.toString());
        return t.getTokenType() == TT_NUMBER; 
    }
    //--------------------------------------------------------------------------
    /**
     * hasThisSymbol checks to see if token is the one asked for. 
     * @return 
     */
    boolean hasThisSymbol( int symbolCodePoint)
    { 
         Token t = removeNextToken(); 
         pushTokenBackToHead( t );
         if(  t.getTokenType() ==  TT_SYMBOL )
         { 
            SingleSymbol s = (SingleSymbol)t; 
            return s.getSymbol()==symbolCodePoint;
         }
         return false ; 
    }
    //--------------------------------------------------------------------------
    /**
     *  isSkipingWhiteSpace - 
     * @return true if skipping white space.
     */
    public boolean isSkipingWhiteSpace()
    {
        return skipWhiteSpace;
    }
    //--------------------------------------------------------------------------
    /**
     *  set setSkipWhiteSpace -- 
     * @param skipWhiteSpace 
     */
    public boolean setSkipWhiteSpace(boolean skipWhiteSpace)
    {
        boolean old =  this.skipWhiteSpace; 
        this.skipWhiteSpace = skipWhiteSpace;
        
        if( skipWhiteSpace == true ) 
        { 
            while( !tokenQue.isEmpty() && 
                            tokenQue.peekFirst().getTokenType()==TT_WHITESPACE )
            { 
                tokenQue.pollFirst(); // remove 
            }
        }
        return old ; 
    }
    //--------------------------------------------------------------------------
    public static  boolean symbolInList( int symbol , String list )
    { 
        int indx = list.indexOf(symbol); 
        if( indx >= 0 && indx <= list.length())return true ; 
        return false ; 
    }
    /*
    * 
    */
    //--------------------------------------------------------------------------
    static boolean testRemoveNextToken()
    { 
        String whiteSpace = "    (Helloü\u00f1939431333"; 
        BufferedReader in = new BufferedReader(new StringReader(whiteSpace));
        Lexer  lex = new Lexer(in ); 
        lex.setSkipWhiteSpace(false);
        Token t = lex.removeNextToken(); 
        assert t.getTokenType() == TT_WHITESPACE :"NOT WHITE SPACE " ; 
       
        WhiteSpace ws = (WhiteSpace)t;  
        assert ws.count == 4; 
        
        t = lex.removeNextToken();
        assert t.getTokenType() == TT_SYMBOL :"NOT SYMBOL";
        
        t = lex.removeNextToken();
        assert t.getTokenType() == TT_WORD :"NOT WORD";
        WordToken wtk = (WordToken)t; 
        
        System.out.println("Word  = '" +  wtk.getText() +"'");
        
        t = lex.removeNextToken();
        assert t.getTokenType() == TT_NUMBER : "NOT NUMBER";
        NumberToken ntk = (NumberToken)t;
        
        System.out.println("number  = '" +  ntk.getText() +"'");
        
        whiteSpace = " 1213222"; 
        in = new BufferedReader(new StringReader(whiteSpace));
        lex = new Lexer(in ); lex.setSkipWhiteSpace(true);
        assert lex.hasANumber() == true :" hasNumber fail "; 
        assert lex.hasANumber() == true :" hasNumber fail 2 ";
        NumberToken nt=  (NumberToken)lex.removeNextToken(); 
        if(nt.getNumberAsText().equals("1213222")==false ) 
            System.out.println("|"+nt.getNumberAsText()+"|");
        assert( nt.getNumberAsText().equals("1213222")); 
        
        //hasQuotedStringLiteral
        
        in = new BufferedReader(new StringReader("  “message” hello "));
        lex = new Lexer(in ); lex.setSkipWhiteSpace(true);
        assert lex.hasQuoteStringAvailable() == true ; 
        StringToken st = lex.removeNextTokenAsLiteralString(); 
        assert st != null ; 
        assert st.getText().equals("message"); 
                
        return true ; 
    }
     
    //--------------------------------------------------------------------------
    public static void runLexerTests()
    { 
        int sym = '*'; 
        assert( symbolInList(sym , "()$%£@!@£)$(*" )== true ) ; 
        sym = '9'; 
        assert( symbolInList(sym , "()$%£@!@£)$(*" )== false ) ; 
        System.out.printf("SELF TEST %b \n",   testRemoveNextToken());
    }
    public static void main(String[] args)
    {
       
       runLexerTests(); 
        
    }
   
}