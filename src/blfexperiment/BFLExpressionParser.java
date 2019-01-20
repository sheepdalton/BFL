/*
 * Copyright (C) 2019 Sheep Dalton.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package blfexperiment;

import blfexperiment.expressions.Variable;
import static blfexperiment.Lexer.TT_WORD;
import static blfexperiment.Lexer.TT_EOL;
import static blfexperiment.Lexer.TT_WHITESPACE;
import blfexperiment.expressions.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Sheep Dalton
 */
public class BFLExpressionParser
{
    private boolean debugTrace= false; 
    Lexer tokenStream ; 
    SymbolTable currentSymTable  ; 
    String keyWords[] = { "into", 
                          "put" ,
                          "as" , 
                          "with",
                          "and",
                          "or", 
                          "to", 
                          "of",
                          "by", 
                          "funtion", 
                          "parameter", 
                          "param" , 
                          "plus", 
                          "times", 
                          "minus", 
                          "divided",
                          "multiplied", 
                          "greater", 
                          "more", 
                          "less",
                          "than", 
                          "equal", 
                          "equals", 
                          "almost", 
                          "power" , 
                          "it", 
                          "and", 
                          "or"
            
    } ; 
   //--------------------------------------------------------------------------- 
   public  BFLExpressionParser(  BufferedReader source )
   { 
       assert source != null:"No null source";
       this.tokenStream = new Lexer( source );
       currentSymTable = new SymbolTable(null);// gobal table 
   }
   //---------------------------------------------------------------------------
   protected BFLExpressionParser(  BufferedReader source, SymbolTable s  )
   { 
       assert source != null:"No null source";
       this.tokenStream = new Lexer( source );
       currentSymTable = s;// gobal table 
   }
    
   
   //---------------------------------------------------------------------------
   protected SymbolTable pushSymbolTable()
   { 
      SymbolTable  sym = new SymbolTable( currentSymTable );
      currentSymTable = sym ;
      return sym ; 
   }
   //---------------------------------------------------------------------------
   protected void popSymbolTable()
   {
       assert currentSymTable.getParent() != null : 
                              "Attempted to popSymbolTable past Global table  "; 
       currentSymTable = currentSymTable.getParent(); 
   }
   //---------------------------------------------------------------------------
   public boolean isLocalVariableDebug( String normalisedName  ) 
   { 
       return currentSymTable.contains(normalisedName);
   }
   //---------------------------------------------------------------------------
   static BFLExpressionParser make(  BufferedReader source )
   { 
       BFLExpressionParser p = new BFLExpressionParser( source ); 
       return p ; 
   }
   //---------------------------------------------------------------------------
   static BFLExpressionParser make( BufferedReader source , SymbolTable t )
   { 
       BFLExpressionParser p = new BFLExpressionParser( source, t ); 
       return p ; 
   }
   //---------------------------------------------------------------------------
   public boolean  isKeyWord( String s )
   { 
       for( String it: keyWords ){ if( s.equalsIgnoreCase(it))return true; } 
       
       return false ; 
   }
   //---------------------------------------------------------------------------
   /** 
    *   science format 
    *   
    */
   //---------------------------------------------------------------------------
   /** 
    *  Hexadecimal number 
    *   #xxxxxxxxx 
    */
   void parseErrorStop( String message  )throws ParseError 
   { 
        throw new ParseError( message + " @"+ tokenStream.charNumber , 
                                                   tokenStream.getLineNumber()); 
   }
   //---------------------------------------------------------------------------
   /**
    * 
    * long number  - for currency  - can be seperated by commas e.g.
    * <pre>
    *    -longNumber-----------------------------------------
    *              |                               |
    *              |-----Number,longNumber---------|
    *              |                               |
    *              |-----longNumber-Million--------|
    *              |                               |
    *              |-----longNumber-Thousand-------|
    *              |                               |
    *              |-----longNumber.longNumber-----|
    *              |                               |
    *              |-----digit*--------------------|
    *              |                               |
    *              |-One,Two,Three...Nine,Ten,-----|
    * 
    *  </pre>
    * 
    * <pre>
    *       digit xxx,xxx,xxx.xx 
    *       digit xxx,xxx,xxx. 
    *  must begin with non zero? 
    *      digit  xxx,xxx,xxx  ( no decimal places ) 
    *  </pre> 
    *  question - return numbers ? 
    * @return 
    */
   LiteralNumberExpression parseLongNumber() throws ParseError 
   {
    tokenStream.setSkipWhiteSpace(true);// Skip read uo to number 
    if( ! tokenStream.hasNumber()) parseErrorStop("Expected number here" );
    
    Lexer.NumberToken num1 = (Lexer.NumberToken)tokenStream.removeNextToken();// get number 
    tokenStream.setSkipWhiteSpace(false);
    String number = num1.getNumberAsText(); 
    //System.out.println("READ ø " + num1 );
    while( tokenStream.hasThisSymbol(','))
    { 
        Lexer.SingleSymbol  commaSymbol = tokenStream.removeNextTokenAsSymbol(); 
        assert commaSymbol.getSymbol() == ','  :  "hasThisSymbol failure";
        if( ! tokenStream.hasNumber()) parseErrorStop("Expected number here 2");
        num1 = (Lexer.NumberToken) tokenStream.removeNextToken();
       
        number = number + num1.getNumberAsText();
    }
   
   if( tokenStream.hasThisSymbol('.')) // decimal point.
   { 
       Lexer.SingleSymbol  decimalPointSymbol = tokenStream.removeNextTokenAsSymbol();
       number = number + "."; 
       /*
           handle .xxx,xxx,xxx 
       */
       if(tokenStream.hasNumber() ) 
       { 
        num1 = (Lexer.NumberToken) tokenStream.removeNextToken();
        number = number + num1.getNumberAsText();
        while(  tokenStream.hasThisSymbol(',') )
        { 
            Lexer.SingleSymbol  commaSymbol = tokenStream.removeNextTokenAsSymbol(); 
            assert commaSymbol.getSymbol() == ','  :  "hasThisSymbol failure";
            if( ! tokenStream.hasNumber()) parseErrorStop("Expected number here");
            num1 = (Lexer.NumberToken) tokenStream.removeNextToken();
            number = number + num1.getNumberAsText(); 
        }// end while.
       }
   } // end if decim point 
   //@@@TODO -handle exponet scientific notation  
   
   //@@@ TODO handle Million, , Thousand , Billion
    LiteralNumberExpression literal = makeLiteralNumberExpression(number); 
             
    return literal ;    
   }
   //---------------------------------------------------------------------------
   LiteralNumberExpression makeLiteralNumberExpression( String number )
   { 
       return  new LiteralNumberExpression(number); 
   }
   //---------------------------------------------------------------------------
   /**
    * Currency is in the form 
    *     $ or £ or € [ Long number ] ) 
    * @return 
    */
   LiteralNumberExpression parseCurrency() throws ParseError
   { 
        if( tokenStream.hasThisSymbol('$'))
        { 
            Lexer.Token dollar = tokenStream.removeNextToken(); 
            LiteralNumberExpression lit =  parseLongNumber(); assert  lit != null ; 
            lit.setType(typeDollar);
            return  lit ; 
        }
        if(  tokenStream.hasThisSymbol('£') )
        { 
            Lexer.Token dollar = tokenStream.removeNextToken(); 
            LiteralNumberExpression lit =  parseLongNumber(); assert  lit != null ; 
            lit.setType(typePoundSterling);
            return  lit ; 
        }
        if(  tokenStream.hasThisSymbol('€') )
        { 
            Lexer.Token dollar = tokenStream.removeNextToken(); 
            LiteralNumberExpression lit =  parseLongNumber(); assert  lit != null ; 
            lit.setType(typeEuro);
            return  lit ; 
        }
    assert false ; 
    return null ; 
   }
   //---------------------------------------------------------------------------
   /** 
    * Parse Units handles 
    *    10Millionft 
    *    10ft , 30,000feet 
    *    0.1ft 
    *    10inches , 10inch, 10"
    *    10yards  , 2yards 
    *    20meters, 
    *    -20mm 
    *    20cm 
    *    30000.1Km 
    *    2AU
    *    4LightYears
    *    4Parsec 
    *    4pixel // screen 
    *    1em    // printing 
    *    
    *    1kg 
    *    1gram 
    *    1lb 
    *    30.22Ton
    *    
    * @param  -LiteralNumberExpression the values 
    * @return
    * @throws ParseError 
    */
   LiteralNumberExpression parseUnits( LiteralNumberExpression ex ) throws ParseError
   { 
    Lexer.Token t = tokenStream.removeNextToken();
    if(  t.getTokenType() != TT_WORD ) 
    { 
        tokenStream.pushTokenBackToHead(t);
        return ex ; 
    }
   
   Lexer.WordToken unitWord = (Lexer.WordToken)t;  
   String it = unitWord.getText(); 

   for( String ut: allunits ) 
   { 
       if( ut.equalsIgnoreCase(it))
       { 
           System.out.println("Setting type to '"+ ut + "' from '" + it+"'");
           ex.setType(ut);
           return ex ; 
       }
   }
   tokenStream.pushTokenBackToHead(t);
   return ex ; 
   }
   //---------------------------------------------------------------------------
    public static String typeInt = "integer"; 
    public static String typeFloat = "real";
    public static String number = "number"; 
    public static String typeDollar = "dollar"; 
    public static String typePoundSterling = "poundSterling"; 
    public static String typeEuro = "euro";
    public static String typeText = "text";
    
    public static String typeKilogram = "kg";
    public static String typePound = "lb";
    public static String typeMilimeter = "mm";
    public static String typeMeter = "meter";
    public static String typeCentiMeter = "meter";
    public static String typeKilometer = "km";
    public static String typeMile = "mile";
    public static String typeYard = "yard";
    public static String typeFoot = "ft";
    public static String typeInches = "inches";
    public static String typeQuestion = "Question"; 
    
    // subset of types all stinrgs of names.
    public static String allunits[] = 
    { 
        typeKilogram , typePound, 
        typeMilimeter , typeMeter, typeYard , typeFoot, typeInches,typeCentiMeter,
        typeYard, 
        typeCentiMeter, typeKilometer, typeMile
    };
     /***
    * Post fixes of all units recognised. 
    */
   /*String units[] =
   { 
      "feet", "ft","inch", "yard", "meter", "mm", "cm", 
      "Km", "mile", 
      "AU" , "LY", "LightYear", "Parsec", "pixel",
      
      "em", 
      "kg", "gram", "lb","ton" 
   };*/
    //--------------------------------------------------------------------------
    static public boolean isAUnit( String item)
    { assert item != null ; 
        for( String s : allunits)
        { 
            //@@@ TODO - or equals ignore case.
            if( item.equalsIgnoreCase(s))return true ; 
        }
        return false ; 
    }
    //--------------------------------------------------------------------------
    public static String allCurrency[] = 
    { 
        typeDollar, typePoundSterling , typeEuro 
    };
    //--------------------------------------------------------------------------
    static public boolean isACurrency( String item)
    { assert item != null ; 
        for( String s : allCurrency)
        { 
            if( item.equals(s))return true ; 
        }
        return false ; 
    } 
  
   //---------------------------------------------------------------------------
   /** 
    * <pre>
    *    -number-----------------------------------------
    *              |                               |
    *              |-------Currency.Number---------|
    *              |                               |
    *              |--------Number.Unit-------------|
    *              |                               |
    *              |-------- number-------------|

    *  </pre>
    *  parse a number can return null if not number 
    * @return
    * @throws ParseError 
    */
   LiteralNumberExpression parseLiteralNumber( )throws ParseError
   { 
       LiteralNumberExpression result= null ;
      /* System.out.println("parseLiteralNumber 1  " + tokenStream.hasNumber() 
                + " " + tokenStream.hasThisSymbol('£') +  " " +  tokenStream.hasNumber()  );
       
       System.out.println("parseLiteralNumber -> 2 " + 
                                                        tokenStream.hasNumber());
       */
       if(     tokenStream.hasThisSymbol('$') || 
               tokenStream.hasThisSymbol('£')  || 
               tokenStream.hasThisSymbol('€') ) 
       { 
           result = parseCurrency(); 
       }
       else 
       {  
           //System.out.println("parseLiteralNumber -> parseLongNumber " +   tokenStream.hasNumber());
           boolean old = tokenStream.setSkipWhiteSpace(true);
           result =   parseLongNumber() ; 
           
           tokenStream.setSkipWhiteSpace(old);
           
           if( tokenStream.hasWords(allunits ))
           { 
             result = parseUnits( result ); 
           }
       }
       return result ; 
   }
//------------------------------------------------------------------------------
   /* 
   . o O { thought } 
    ≡ identical to  
≠ not equal  ≒ 
≈ almost equal to  ( sounds like ) 
≉ not almost equal to 
≍ eqivalna t to    ( for strings ignores the case ) 

⊃ super set of 
⊅ not super set of 
⊆ subset or equal 
⊂ subset of 

∈ element of  4 ∈ { 1 4 5. 6 6 7 } boolean 


∧ logical and 
∨ logical or
¬ not sign

∞ infinity 

⋅ dot operator . Full stop 
⊚ dot operator 
⨼ inter product 

➯ put or into  
👎 wrong 

 ℮
∉ 
∅ empty set 

   */ 
   //---------------------------------------------------------------------------
   String parseIdentifier() throws ParseError 
   { 
     if( tokenStream.hasWords(keyWords)) this.parseErrorStop("CANNOT Start Identifier with key word ");
     Lexer.Token word =  tokenStream.removeNextToken() ;
     String IDENIFYIER = ""; 
    
     while( word.getTokenType()== Lexer.TT_WORD) // 
     { 
         Lexer.WordToken w  = (Lexer.WordToken)word ; 
         if(  isKeyWord( w.getText() ))
         { 
             tokenStream.pushTokenBackToHead(w);
             break ; 
         }
         IDENIFYIER = IDENIFYIER + "_" + w.getText() ; 
         //System.out.println(" PARSING VARNAME= " + varName );
        
         word =  tokenStream.removeNextToken() ;
    }
     tokenStream.pushTokenBackToHead(word);
     return IDENIFYIER; 
   }
   //---------------------------------------------------------------------------
   /** 
    *   Capital word word word. (<-- full stop ) 
    *   THE word word word of argument 
    *   word word word -- variable 
    * <pre>
    *    -FACTOR-----------------------------------------
    *              |                                |
    *              |--------     - FACTOR    -------|don
    *              |                                |
    *              |--------     + FACTOR    -------|done
    *              |                                |
    *              |---   NOT_SYMOBL FACTOR    -----|done
    *              |                                |
    *              |--------   NOT FACTOR    -------|done 
    *              |                                |
    *              |---   LITERAL_BOOLEAN (YES) ----|done
    *              |                                |
    *              |-------(    EXPRESSION ) -------|done
    *              |                                |
    *              |-----| ABS MATHEXPRESSION |-----|done
    *              |                                |
    *              |-------LITERAL NUKBER(42)-------|done
    *              |                                | 
    *              |---------- IDENTIFIER ----------|done
    *              |                                |
    *              |---IDENTIFIER's  IDENTIFIER-----|done
    *              |                                |
    *              |-------- IDENTIFIER#EXPR--------|
    *              |                                |
    *              |-------   ARRAY[ EXPRE ]--------|
    *              |                                |
    *              |---- ITEM <EXPR> OF IDENT ------| 
    *              |                                |
    *              |------ ∆ DEFINED-VARIABLE-------| ∆ = increment  
    *              |                                |
    *              |-------   Function ( EXP )------|
    *              |                                |
    *              |-----the Function of Expr-------|
    *              |                                |
    *              |---------- √ EXP ---------------| done
    *              |                                |
    *              |----------  EXP !  -------------| // exponent of number
    *              |                                |
    *              |---------- LITERL-DATE ---------|
    *              |                                |
    *              |---------- "Literal String" ----|done and smart string.
    *              |                                |
    *              |-------[ literal list ] --------|
    *              |                                |
    *              |-------{ literal set } ---------|
    * 
    *  </pre>
    * parseFactor's can be null ( no addition ) 
    *  This will get quite big. 
    * NOTUCE ABSOLITE | X | cannot be nested with out brackets
    */
   //---------------------------------------------------------------------------
   boolean parsingAnAbsolute = false ; 
   NumericExpression parseFactor() throws ParseError 
   { 
     if( debugTrace) { System.out.println("     PARASE START FACTOR"); } 
     NumericExpression result = null ;
     // = null ; 
     Boolean addNegateOperator = false ; 
     boolean oldSetting =  tokenStream.setSkipWhiteSpace(true);
     if( tokenStream.hasThisSymbol('-')) // negates the next factor.
     { 
        Lexer.SingleSymbol neg = tokenStream.removeNextTokenAsSymbol();assert neg.getSymbol() == '-';
        NumericExpression num = parseFactor(); 
        result = makeNewUnariyExpression( '-', num ); 
        return result; 
     }// NOT 
     if( tokenStream.hasThisSymbol('+')) // do nothing . 
     { 
        Lexer.SingleSymbol pos = tokenStream.removeNextTokenAsSymbol(); assert pos.getSymbol() == '+';
        NumericExpression num = parseFactor(); 
        //result = makeNewUnariyExpression( '-', num ); 
        return result; 
     }// NOT 
     
     if( tokenStream.hasWord("NOT") ||  tokenStream.hasThisSymbol('¬') ) // THIS IS LOGICAL NOT SYMBOL
     { 
        Lexer.Token not = tokenStream.removeNextToken();// what ever it is. 
        NumericExpression num = parseFactor(); 
        result = makeNewUnariyExpression( '¬', num ); 
        return result; 
     }
     if( tokenStream.hasThisSymbol('√'))
     { 
        Lexer.SingleSymbol pos = tokenStream.removeNextTokenAsSymbol();
        result =   parseExpression(); 
        return result=  makeNewUnariyExpression( '√', result ); 
     }
    if( tokenStream.hasQuoteStringAvailable())
    { 
        Lexer.StringToken str = tokenStream.removeNextTokenAsLiteralString(); 
        return new LiteralStringExpression(str.getText()); 
    }

     String booleanliteralWords[] = { "YES", "NO", "TRUE", "FALSE" } ; 
     if( tokenStream.hasWords( booleanliteralWords))
     { 
         Lexer.WordToken wt = tokenStream.removeNextTokenAsWord();
         return new LiteralBoolean( wt.getText()); 
     }
     
     if( tokenStream.hasThisSymbol('|') && parsingAnAbsolute == false ) // take the absolute value of. Introduced vagaries 
     { 
        Lexer.SingleSymbol s =  tokenStream.removeNextTokenAsSymbol(); 
        assert s.getSymbol() == '|'; 
        parsingAnAbsolute = true ; 
        result =   parseExpression();  
        parsingAnAbsolute = false ; 
        if( tokenStream.hasThisSymbol('|')==false ) parseErrorStop("Expected a |");
        s =  tokenStream.removeNextTokenAsSymbol(); 
        result =   makeNewUnariyExpression( '|'   , result ); 
        return result ; 
     }
     if( tokenStream.hasThisSymbol('(')) 
     {  
        Lexer.Token openBracket =  tokenStream.removeNextToken( ) ;
        if( openBracket.getTokenType() == Lexer.TT_SYMBOL  )
        {
            result =   parseExpression();  
        }
        if(  tokenStream.hasThisSymbol(')')==false ) parseErrorStop("Expected a )");
        Lexer.Token closeBracket =  tokenStream.removeNextToken( ) ;
        return result ; 
     }
     if( debugTrace) 
     { 
         System.out.println("     PARASE CHECK NUMBER "+ tokenStream.hasNumber()) ; 
         Lexer.Token t = tokenStream.removeNextToken();
         System.out.println("     PARASE CHECK NUMBER "+ t.toString()) ; 
         tokenStream.pushTokenBackToHead(t);  
     } 
     
     if(  tokenStream.hasNumber() ||  tokenStream.hasThisSymbol('$')
             || tokenStream.hasThisSymbol('£') ||  tokenStream.hasThisSymbol('€')) 
     { 
        if( debugTrace) { System.out.println("      PARASE parseLiteralNumber"); } 
      LiteralNumberExpression first = parseLiteralNumber();
       if( debugTrace) { System.out.println("      ENDD parseLiteralNumber " + first); }
      //System.out.println(" literal number '" + first.getNumberAsText()+"'" ); 
      tokenStream.setSkipWhiteSpace(oldSetting);
      if( debugTrace) { System.out.println("     END PARASE  FACTOR"); } 
      // @@@ TODO CHECK FOR Literl number access like 45's type 
      return first ; 
     } 
    if( debugTrace) { System.out.println("     PARASE NOT A  NUMBER"); } 
     
    if( tokenStream.hasWords(keyWords)) this.parseErrorStop("CANNOT Start variable with key word ");
    String IDENIFYIER = ""; 
    if( tokenStream.hasWordAvilable() ) 
    { 
        IDENIFYIER = this.parseIdentifier();
    }
    Lexer.Token  word =  tokenStream.removeNextToken() ; // ????
    // THE NEXT WORD COULD BE INTO 
    tokenStream.pushTokenBackToHead(word);
   
    // IF IS IDENTIFER THEN CAN BE AT END OF IDENTIFER 
    if(tokenStream.hasThisSymbol('\'') ) // 's 
    { 
        // check types although even numbers have messages like 's type 's units
        // also have methods like 45's sin   45's log 
        word =  tokenStream.removeNextToken() ;
        if( tokenStream.hasWord("s"))
        { 
            Lexer.WordToken s  = tokenStream.removeNextTokenAsWord();
            // get an identifier 
            assert false ; // reutrn new attribute access 
        }else this.parseErrorStop("EXPECTED s as in 's for attrbite access");
    }
    // put array[ 3 ; 4] 
    if( tokenStream.hasThisSymbol('['))   
    { 
        // check this refers to an array 
        word =  tokenStream.removeNextToken() ;
        NumericExpression ne =   parseExpression(); 
        if(!  tokenStream.hasThisSymbol(']') )
                this.parseErrorStop("EXPECTED ] for array access");
        // can have ';' in array acce 
    }
    if(tokenStream.hasThisSymbol('#') ) // ARRAY ACCESSS 
    { 
        word =  tokenStream.removeNextToken() ;
        // @@@ TODO can either be followed by number 
        // @@@ TO 
        assert false ; 
        //NumericExpression ne =   parseComparisonExpression();  
        //result =  new ArrayIndex(IDENIFYIER , v , ne ); 
    }
     // It's not 
     //System.out.println(" PARSING VARNAME= " + varName );
     Variable v = null ; 
     if(IDENIFYIER.length()>0 && this.currentSymTable.contains(IDENIFYIER) ) // @@@ TODO ALSO LOOK FOR MISS SPELLED VERSION OF THIS
     { 
        //System.out.println(" CREATE VARIABLE "+ varName);
        v = currentSymTable.getVariable( IDENIFYIER );
        tokenStream.setSkipWhiteSpace(oldSetting);
        result =  new VariableGetExpression(IDENIFYIER , v); 
     }else 
           this.parseErrorStop(" I don't know any container called '" + IDENIFYIER+"'");

    tokenStream.setSkipWhiteSpace(oldSetting);
    
        
    return result ;  
   }
   //---------------------------------------------------------------------------
   protected UnariyExpression makeNewUnariyExpression( int operator , NumericExpression e )
   { 
       return  new UnariyExpression( operator, e ); 
   }
   //---------------------------------------------------------------------------
   BinaryExpression makeBinaryExpression(  int what )
   { 
       return new BinaryExpression( what ); 
   }
   //---------------------------------------------------------------------------
   BinaryExpression makeBinaryExpression(  int what , NumericExpression before, 
                                                        NumericExpression after )
   { 
       return new BinaryExpression( what , before , after ); 
   }
   //---------------------------------------------------------------------------
   BinaryExpression makeComparisionExpression(  int what  , NumericExpression before, 
                                                        NumericExpression after )
   { 
       return new LogicBinaryExpression( what  , before , after ); 
   }
   //---------------------------------------------------------------------------
   UnariyExpression makeUnariyExpression( int what , NumericExpression e )
   { 
       assert e != null ; 
       return new UnariyExpression( what , e);
   }
   //---------------------------------------------------------------------------
   /** 
    * <pre>
    *    -POWEROPERATOR----------------------------------------->
    *              |                                        ^
    *              |                                        |
    *              |-> FACTOR TO THE POWER OF POWEROPERATOR-|
    *              |                                        |
    *              |->------FACTOR ^ POWEROPERATOR----------|
    *              |                                        |
    *              |->------FACTOR # POWEROPERATOR----------|
    *              |                                        |
    *  </pre>
    * parseFactor's can be null ( no addition ) 
    */
    NumericExpression parsePowerOperator() throws ParseError 
    { 
      if( debugTrace) { System.out.println("    PARASE FACTOR from"); } 
      NumericExpression factor = parseFactor();
      if( debugTrace) { System.out.println("    END PARASE FACTOR (fro power"); } 
      boolean oldSetting =  tokenStream.setSkipWhiteSpace(true);
      boolean parsePower = false ; 
      Lexer.WordToken w =null; 
      //if(  tokenStream.hasWord("to") ) w = tokenStream.removeNextTokenAsWord();
      //if(  tokenStream.hasWord("the ") ) w = tokenStream.removeNextTokenAsWord();
      if( tokenStream.hasWord("power"))
      { 
           w = tokenStream.removeNextTokenAsWord();
           assert w.getText().equalsIgnoreCase("power"); 
           parsePower = true ; 
          System.out.println("-----------PARSE POWER 1 -------------" + parsePower  );
      }else  if(  tokenStream.hasThisSymbol('^'))
      { 
          parsePower = true ; 
          Lexer.SingleSymbol sym = tokenStream.removeNextTokenAsSymbol(); assert sym instanceof Lexer.SingleSymbol ; 
          assert( sym.getSymbol() == '^'  ); 
      }//else  //if( w != null && parsePower == false ) this.parseErrorStop("Expected 'power' at this point");
       
      if( true == parsePower )
      { 
          System.out.println("-----------PARSE POWER-------------");
          BinaryExpression bx = null ; 
  
              bx  = makeBinaryExpression( '^' );assert bx != null ; 
              bx.setBefore(factor);
              
              NumericExpression power = parseFactor();
              System.out.println( "power = "  + power.toString());
              
              
              if( !power.isANumber())
                  parseErrorStop(" cannot raise to non integer types (Sorry)" ); 
              bx.setAfter(power);
              
              tokenStream.setSkipWhiteSpace(oldSetting);
              
              System.out.println( " RETURNING "  + bx );
              
              return bx; 
          
      }
      tokenStream.setSkipWhiteSpace(oldSetting);
      return factor ;  
    }
   //---------------------------------------------------------------------------
   /** 
    * <pre>
    *    -TERM------------------------------------------------------------
    *              |                                        |
    *              |-------POWEROPERATOR + TERM-------------|
    *              |                                        |
    *              |-------POWEROPERATOR PLUS TERM----------|
    *              |                                        |
    *              |-------POWEROPERATOR - TERM-------------|
    *              |                                        | 
    *              |--POWEROPERATOR '...' POWEROPERATOR-----| literal range 
    *              |                                        |
 

    *  </pre>
    * parseFactor's can be null ( no addition ) 
    */
   NumericExpression parseTerm() throws ParseError 
   { 
      boolean oldSetting =  tokenStream.setSkipWhiteSpace(true);
      if( debugTrace) { System.out.println("   PARASE parsePowerOperator"); } 
      NumericExpression factor = parsePowerOperator();// AKA FACTOR
      if( debugTrace) { System.out.println("   END PARASED parsePowerOperator"); } 
      
      
      while( tokenStream.hasThisSymbol('+') || tokenStream.hasThisSymbol('-')  )// || 
             // tokenStream.hasWord("plus") ||  tokenStream.hasWord("minus")
      { 
          BinaryExpression bx = null ; 
          
          Lexer.SingleSymbol opertor = tokenStream.removeNextTokenAsSymbol();
          
          bx  = makeBinaryExpression( opertor.getSymbol() );
          
          bx.setBefore(factor);
          assert bx!= null ; 

          tokenStream.setSkipWhiteSpace(true);
          if( debugTrace) { System.out.println("   PARASE parsePowerOperator 2"); } 
          NumericExpression  right = parsePowerOperator(); 
          if( debugTrace) { System.out.println("   END PARASE parsePowerOperator 2"); } 
          bx.setAfter(right);
          
          String theCombinedType = bx.getType(); 
          if( theCombinedType.contains("Error") )parseErrorStop(" Cannot combine types " + theCombinedType ); 
         factor =  bx ;
      } 
       if( debugTrace) { System.out.println("   TERM END LOOP OVER "); } 
      tokenStream.setSkipWhiteSpace(oldSetting);
      return factor ;  
   }
   //---------------------------------------------------------------------------
     /** 
    * <pre>
    *    -SimpleExpression-----------------------------------------
    *              |                              |
    *              |------------- TERM  ----------|
    *              |                              |
    *              |------------- TERM  as TYPE --|
    *              |                              |
    *              |--------- TERM x TERM --------|
    *              |                              |
    *              |--------- TERM ÷ TERM --------|
    *              |                              |
    *              |--- TERM MULTIPLY TERM -------|
    *              |                              |
    *              |--- TERM DIVIDED BY TERM -----|
    *              |                              |
    *              |------- TERM DIV BY TERM -----|
    *              |                              |
    *              |-------- TERM MOD  TERM ------|
    *              |                              |
    *              |-------- TERM ROL  TERM ------|
    *              |                              |
    *              |-- TERM ROTATE LEFT  TERM ----|
    *              |                              |
    *              |-------- TERM ROR  TERM ------|
    *              |                              |
    *              |-- TERM ROTATE RIGTH  TERM ---|
    *              |                              |
    *              |---- TERM BIT AND  TERM ------|
    *              |                              |
    *              |------ TERM  BIT OR  TERM ----|
  *                |                              |
    *              |----- TERM  BIT XOR  TERM ----|
    *  </pre>
    *  
    */
   NumericExpression simpleMathExpression() throws ParseError
   { 
      boolean oldSetting =  tokenStream.setSkipWhiteSpace(true);
      if( debugTrace) { System.out.println(" PARASE TERM"); } 
      NumericExpression term = parseTerm();
      if( debugTrace) { System.out.println(" END PARA  TERM 1 in simpleMathExpression "); } 
      // AS 
       //System.out.println( "***"+tokenStream.showCurrentPosition());
      
      if( tokenStream.hasThisSymbol('*') || tokenStream.hasThisSymbol('/') 
              ||  tokenStream.hasThisSymbol('\u00D7') //  x 
              || tokenStream.hasThisSymbol('÷')   )
      { 
          BinaryExpression bx = null ; 
          Lexer.Token operator =  tokenStream.removeNextToken( ) ; 
          if( operator.getTokenType() == Lexer.TT_SYMBOL )
          { assert operator instanceof Lexer.SingleSymbol ; 
            Lexer.SingleSymbol sym = (Lexer.SingleSymbol)operator; 
            bx  = makeBinaryExpression( sym.getSymbol() );
            bx.setBefore(term);
            term = bx ; 
            assert bx!= null ; 
            
            if( debugTrace) { System.out.println(" PARASE TERM 2"); } 
            NumericExpression  right = parseTerm(); 
             if( debugTrace) { System.out.println(" END PARASE TERM 2"); } 
            tokenStream.setSkipWhiteSpace(oldSetting);
            bx.setAfter(right);
            
            String theCombinedType = bx.getType(); 
            if( theCombinedType.contains("Error") )parseErrorStop(" Cannot combine types " + theCombinedType ); 
            return term ; 
          }
      } 
       if( debugTrace) { System.out.println(" END PARASE simpleMathExpression 2"); } 
      /// @@@ TODO HANDLE MOD 
      /// @@@ TOOD handle DIV ( integer divde ) 
      
      tokenStream.setSkipWhiteSpace(oldSetting);
      return term ; 
   }
   
   //---------------------------------------------------------------------------
  /** 
    * <pre>
    parseComparisonExpression----------------------------------------------------
         |                                                  ^
         |                                                  |
         |-------------SIMPLE Expression--------------------| 
         |                                                  |
         |------SimpleExpression >   SimpleExpression    ---|
    *         |                                                  |
    *         |----SimpleExpression more than  SimpleExpression ---|
    *         |                                                  |
    *         |----SimpleExpression greater than  SimpleExpression ---|
    *         |                                                  |
    *         |----SimpleExpression =   SimpleExpression---------|
    *         |                                                  |
    *         |---MathExpression nearly MathExpression -----|
    *         |                                                  |
    *         |---GeneralExpresion element of ListExpression ----| 
    *         |                                                  |
    *         |------------------   EXPR..EXPR   ----------------| // generate range object
    *     
    *   listExpression 
    *         -> Constant list 
    *         -> variabel with list name. 
    *         -> List operation  (untion ) 
    *         -> function returning a list 
    *   
    *  </pre>
    */
   public NumericExpression parseComparisonExpression() throws ParseError
   { 
      boolean oldSetting =  tokenStream.setSkipWhiteSpace(true);
      if( debugTrace )System.out.println("simpleMathExpression in logic before");
      NumericExpression ex =  simpleMathExpression();
       if( debugTrace )System.out.println("end simpleMathExpression in logic beofe");
     //@@@TODO SWITCH OFF SKIP WHITE SPACE for binary operands. 
// EQUALS. 
      if( tokenStream.hasThisSymbol('=')   ) // equals 
      { 
          Lexer.SingleSymbol sym =  tokenStream.removeNextTokenAsSymbol() ;
          assert ( sym.getSymbol() == '=' )  ; 
          NumericExpression ex2 =  simpleMathExpression();
          BinaryExpression bx  = makeBinaryExpression( '=', ex, ex2 ); assert bx != null;
          tokenStream.setSkipWhiteSpace(oldSetting);
          return bx; 
      }
// NEARLY EQUAL TO  ( NEW IN BFL ) 
      if( tokenStream.hasThisSymbol('~') || tokenStream.hasThisSymbol('≈')) // Almost equal.
      {
          Lexer.SingleSymbol sym =  tokenStream.removeNextTokenAsSymbol() ;
          NumericExpression ex2 =  simpleMathExpression();
          BinaryExpression bx  = makeBinaryExpression( '~' , ex,ex2); assert bx != null;
          tokenStream.setSkipWhiteSpace(oldSetting);
          return  bx;  
      }
// LESS THAN OR EQUAL TO       
      if(  tokenStream.hasThisSymbol('≤')) 
      { 
        Lexer.SingleSymbol sym =  tokenStream.removeNextTokenAsSymbol() ;
        NumericExpression ex2 =  simpleMathExpression();
        BinaryExpression bx  = makeBinaryExpression( '≤' , ex, ex2  );
        System.out.println(" PARSING  < <<<<="+ bx);
        tokenStream.setSkipWhiteSpace(oldSetting);
        return bx ;
      }
// LESS  or NOT EQUAL OR LESS THAN OR EQUAL TO 
      if( tokenStream.hasThisSymbol('<')   )
      { 
        Lexer.SingleSymbol sym =  tokenStream.removeNextTokenAsSymbol() ;
        assert sym.getSymbol() == '<' ; 
        
        if( tokenStream.hasThisSymbol('=') )
        { 
            Lexer.SingleSymbol equal =  tokenStream.removeNextTokenAsSymbol() ;
            NumericExpression ex2 =  simpleMathExpression();
            BinaryExpression bx  = makeBinaryExpression( '≤' , ex, ex2); 
            tokenStream.setSkipWhiteSpace(oldSetting);
            return bx ;
        }
        
        if(  tokenStream.hasThisSymbol('>') ) // <> not equal to 
        { 
            NumericExpression ex2 =  simpleMathExpression();
            BinaryExpression bx  = makeBinaryExpression( '≠' );
            bx.setBefore(ex);
            bx.setAfter(ex2); tokenStream.setSkipWhiteSpace(oldSetting);
            return bx ; 
        }
        NumericExpression ex2 =  simpleMathExpression();
        BinaryExpression bx  = makeBinaryExpression( '<' , ex, ex2 );
        ex = bx ;  tokenStream.setSkipWhiteSpace(oldSetting);
        return bx; 
      }
      if( tokenStream.hasThisSymbol('\u2265')   ) // >= 
      { 
        Lexer.SingleSymbol sym =  tokenStream.removeNextTokenAsSymbol() ;
        BinaryExpression bx  = this.makeBinaryExpression('\u2265' );
        NumericExpression ex2 =  simpleMathExpression();
        bx.setBefore(ex);
        bx.setAfter(ex2); tokenStream.setSkipWhiteSpace(oldSetting);
        ex = bx ;
         
      }
      if( tokenStream.hasThisSymbol('>')   )
      { 
        int symbol = '>';
        Lexer.SingleSymbol sym =  tokenStream.removeNextTokenAsSymbol() ;
        if( tokenStream.hasThisSymbol('=') )
        {  
            sym =  tokenStream.removeNextTokenAsSymbol() ;
            symbol = '\u2265';  // > or eqal to  
        }
        if( this.debugTrace ) System.out.println(".... found > ....");
        /*BinaryExpression bx  = this.makeBinaryExpression(symbol );
         NumericExpression ex2 =  simpleMathExpression();
        bx.setBefore(ex);
        bx.setAfter(ex2);
        ex = bx ; tokenStream.setSkipWhiteSpace(oldSetting);
        return ex  ; */
        
         NumericExpression ex2 =  simpleMathExpression();
        tokenStream.setSkipWhiteSpace(oldSetting);
        return makeComparisionExpression( symbol , ex, ex2 );
      }
      
      Lexer.WordToken is  = null ;
      boolean foundIs = false ; 
      if( tokenStream.hasWord("is") )  // this could be a filler word. 
      { 
          is = tokenStream.removeNextTokenAsWord(); // optional 
          foundIs = true ; 
      }
      if( tokenStream.hasWord("more") ||  tokenStream.hasWord("greater"))
      { 
        Lexer.WordToken more = tokenStream.removeNextTokenAsWord(); 
        if( tokenStream.hasWord("than")) // filler words OPTIONAL 
        { 
            Lexer.WordToken than = tokenStream.removeNextTokenAsWord(); 
        }
          //@@@ TODO 'OR EQUAL [TO]' ...
       
        BinaryExpression bx  = this.makeBinaryExpression('>' );
       
         NumericExpression ex2 =  simpleMathExpression();
         if( debugTrace )System.out.println("END OF  simpleMathExpression in logic");
        bx.setBefore(ex);
        bx.setAfter(ex2);
        ex = bx ;   tokenStream.setSkipWhiteSpace(oldSetting);
        return ex; 
      }
      if( tokenStream.hasWord("less"))
      { 
        Lexer.WordToken more = tokenStream.removeNextTokenAsWord(); 
        if( tokenStream.hasWord("than"))
        { 
              Lexer.WordToken than = tokenStream.removeNextTokenAsWord(); // optinal 
        }
          //@@@ TODO 'OR EQUAL [TO]' ...
        
        BinaryExpression bx  = this.makeBinaryExpression('<' );
        NumericExpression ex2 =  simpleMathExpression();
       
        
        bx.setBefore(ex);
        bx.setAfter(ex2);
        ex = bx ;   tokenStream.setSkipWhiteSpace(oldSetting);
        return ex ; 
      }  
      // if is != null then we HAVE found IS  OR
      
      if( tokenStream.hasWord("nearly") ) 
      { 
        Lexer.WordToken nearly = tokenStream.removeNextTokenAsWord();// ignore optional.
        if( tokenStream.hasWord("equal")) // Just remove it.
        {
           Lexer.WordToken equal = tokenStream.removeNextTokenAsWord();// ignore optional.
        }
          
        if( tokenStream.hasWord("to")) // just remove it.
        { 
              Lexer.WordToken than = tokenStream.removeNextTokenAsWord(); // ignore optional.  
        } 
         
        NumericExpression ex2 =  simpleMathExpression();
        BinaryExpression bx  = this.makeBinaryExpression('~' , ex, ex2);
        ex = bx ;  tokenStream.setSkipWhiteSpace(oldSetting);
        return ex;
      }
      if( tokenStream.hasWord("equal") || foundIs )
      {   if( debugTrace )System.out.println("tokenStream.hasWord(\"equal\") || is != null ");
          if( tokenStream.hasWord("equal")) // Just remove it.
          {
            Lexer.WordToken equal = tokenStream.removeNextTokenAsWord();
          }
          
          if( tokenStream.hasWord("to")) // just remove it.
          { 
              Lexer.WordToken than = tokenStream.removeNextTokenAsWord(); // optinal 
          } 
          // test for 'if it is then' ... special case
          // SO FAR WE HAVE 
       // System.out.println("PICLING UP NEXT MATH EXPRESSION");
        BinaryExpression bx  = this.makeBinaryExpression('=' );
        //debugTrace= true ; 
        NumericExpression ex2 =  simpleMathExpression();
        //System.out.println("END PICLING UP NEXT MATH EXPRESSION");
        
        bx.setBefore(ex);
        bx.setAfter(ex2);
        ex = bx ;  tokenStream.setSkipWhiteSpace(oldSetting);
        return ex; 
      }
      // element of  - for numbers returns if all the on bits of a are in b. 
      // treats numbers of 
      
        tokenStream.setSkipWhiteSpace(oldSetting);
      return ex; 
   }
   //---------------------------------------------------------------------------
   public NumericExpression parseLogicExpression() throws ParseError
   {
      boolean oldSetting =  tokenStream.setSkipWhiteSpace(true);
      if( debugTrace )System.out.println("simpleMathExpression in logic before");
      NumericExpression ex =  this.parseComparisonExpression();
      if( debugTrace )System.out.println("end simpleMathExpression in logic beofe");
     
      if( tokenStream.hasWord("and")) // 
      { 
          Lexer.WordToken and = this.tokenStream.removeNextTokenAsWord();
          if( this.debugTrace) System.out.println("PARSING 'AND' " + and + " GOT factor = " + ex);
          NumericExpression  after = this.parseComparisonExpression();  
          //if( ! factor.isQuestion()) parseErrorSto p(" AND operation only works on logic expression. Had  " + factor ); 
        
          LogicBinaryExpression bx = new LogicBinaryExpression('&'); 
          bx.setBefore(ex);
          bx.setAfter(after);
          if( this.debugTrace) System.out.println("PARSING AND " + and + " GOT factor = " + bx);
          ex = bx;
           
         // if(  ! after.isQuestion() )  parseErrorStop(" AND (2) operation only works on logic expression. Had  " + after ); 
      }else 
          if( tokenStream.hasWord("or")) // 
            { 
                Lexer.WordToken and = this.tokenStream.removeNextTokenAsWord();
                if( this.debugTrace) System.out.println("PARSING 'OK' " + and + " GOT factor = " + ex);
                NumericExpression  after = this.parseComparisonExpression();  
                //if( ! factor.isQuestion()) parseErrorSto p(" AND operation only works on logic expression. Had  " + factor ); 

                LogicBinaryExpression bx = new LogicBinaryExpression('|'); 
                bx.setBefore(ex);
                bx.setAfter(after);
                if( this.debugTrace) System.out.println("PARSING AND " + and + " GOT factor = " + bx);
                ex = bx;

               // if(  ! after.isQuestion() )  parseErrorStop(" AND (2) operation only works on logic expression. Had  " + after ); 
            }
       
      tokenStream.setSkipWhiteSpace(oldSetting);
      return ex;
   }
   //-------
   public NumericExpression parseExpression( )  throws ParseError
   { 
      return  parseLogicExpression(); 
   }
   //---------------------------------------------------------------------------
   /**
    *  An indentifer has any string of words ( but no key words ) 
    * @return
    * @throws ParseError 
    */
   String getIdentifier() throws ParseError 
   { 
      if( tokenStream.hasWordAvilable() == false )this.parseErrorStop("Expected Idenifier");
      String identifier = ""; 
      Lexer.Token tkn ; 
      do{ 
            if( tokenStream.hasWords(keyWords) ) break ; 
           
            tkn = tokenStream.removeNextToken();
            System.out.println("tkeb  " +  tkn ); 
            if( tkn.getTokenType() == TT_WORD ) 
            { 
                identifier = identifier + ((Lexer.WordToken)tkn).getText() + "_";
            }
      } while( ( tokenStream.hasWordAvilable()) );
      return identifier; 
   }
  
   //---------------------------------------------------------------------------
   static boolean  testComparisonExpression() 
   { 
       System.out.print("\n\n--------------------------------------------------\n"
               + "TEST testComparisonExpression "); 
       try 
        {  
            BFLExpressionParser bl=  fromSource(" ( 3Kg + 2Kg ) * 5,000 " ) ; assert bl != null ;
            NumericExpression e = bl.parseComparisonExpression();assert e != null ; 
            BigDecimal d = e.evaluateCalculation();assert d != null ; 
            System.out.printf(" EXP=  %s\n",e.toString());
            System.out.printf( "Result =  %s (%s) \n", d.toPlainString(), e.getType());
            
            bl=  fromSource("  3 > 2  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ; 
            
            bl=  fromSource("  3 < 2  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == false ; 
            
           
            bl=  fromSource("  3 = 3  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ; 
             
            bl=  fromSource("  3 is 3  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ; 
            
            bl=  fromSource("  3 is equal 3  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ; 
            
            bl=  fromSource("  3 is equal 2  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == false ; 

            
            bl=  fromSource("  3 is equal to 3  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ; 
            
            bl=  fromSource("  3*4 is equal to 4*3  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ;
            
            bl=  fromSource("  3*4 is more than 2*1  " ) ; assert bl != null ;
            e = bl.parseComparisonExpression();assert e != null ; 
            assert e.evaluateLogic() == true ;
            
            System.out.println("\n\n--------------------------------------------------\n"
               + "TEST COMPARE 3 \n"); 
            
            bl=  fromSource( "  3  > 1 and 5 > 8  " ) ;
            bl.debugTrace  = true ; 
            e = bl.parseLogicExpression();
            System.out.println("@@@@@\n"+ e.toString());
            System.out.println("-->>>>>\n"+ e.toString());
            assert e.evaluateLogic() == false; 
            
             
            d = e.evaluateCalculation();assert d != null ; 
           
            System.out.printf(" EXP=  %s\n",e.toString());
            System.out.printf( "Result =  %s (%s) \n", d.toPlainString(), e.getType());
            assert d.compareTo(BigDecimal.ZERO )== 0 ; 
            
        }catch(  ParseError e )
        { 
            System.out.println("1 TEST SimpleExpression: PARSE ERROR->"+ e);
            return false ; 
        }
       
       /*try 
        { 
           System.out.println("@^@ 3  > 1 and 5 > 8");
            BFLExpressionParser bl=  fromSource( " 3  > 1 and 5 > 8" ) ;
            NumericExpression e = bl.parseComparisonExpression();
            System.out.printf(" EXPl=  %s\n",e.toString());
            assert bl != null ;
            assert false : " 3  > 1 and 5 > 8  should fail  "; 
            3 

        }
       catch(  ParseError e )
        { 
             System.out.println("TEST FOR 3  > 1 and 5 > 8  wrong passed OK "+ e );
        }
        */
       
       System.out.println("OK "); 
       return true ; 
   }
   
   //---------------------------------------------------------------------------
   static boolean testSimpleExpression()
   {       
       System.out.print("TEST SimpleExpression "); 
       try 
        {  
            assert runSimpleExpression(" 200 ≤ 50000 " ).equals("YES");// compiled
             
            runSimpleExpression(" 200 ≤ 50000 " );
            runSimpleExpression("$892.9 + $436,000" ); 
            runSimpleExpression(" 564 + 436" ); 
            runSimpleExpression(" 200 ÷ 20 " );
            // test long numbers 
            runSimpleExpression("20000000000000000000000000.00 ÷ 2000000000000000000000000 " );
            runSimpleExpression(" 564.9 + 436" ); 
            
            assert runSimpleExpression(" 2 + 3" ).equalsIgnoreCase("5"):"2 + 3 fail";
            // operator precidance 
            assert runSimpleExpression(" 1 + 2 * 3" ).equals("9"); 
            assert runSimpleExpression("$892.9 + $436,000" ).equals("436892.9");
        } 
       catch(  ParseError e )
        { 
            System.out.println("TEST SimpleExpression: PARSE ERROR->"+ e);
            return false ; 
        }
       
       try 
        { 
            System.out.println(" TEST FOR FAIL €100 * £300 ");
            BFLExpressionParser bl=  fromSource( " €100 * £300 " ) ;
            NumericExpression e = bl.simpleMathExpression();
            
            BigDecimal d = e.evaluateCalculation();
            System.out.printf(" EXP=  %s\n",e.toString());
            System.out.printf( "Result =  %s \n", d.toPlainString());
            assert bl != null ;
            assert false : " €100 * £300 should fail  "; 
        }
       catch(  ParseError e )
        { 
             System.out.println("**TEST FOR €100 * £300 wrong passed** \n"+ e );
        }
       
       
       
       
       System.out.println("TEST SimpleExpression passed");
       
       return true ;
   }       
   //---------------------------------------------------------------------------
   static boolean testTerm()
   { 
       System.out.print("TEST Term "); 
       try 
        { 
            BFLExpressionParser bl=  fromSource( "100 + 200 " ) ;
            NumericExpression e = bl.parseTerm();
            BigDecimal d = e.evaluateCalculation();
            System.out.printf(" EXP=  %s\n",e.toString());
            System.out.printf( "Result =  %s \n", d.toPlainString());
            assert bl != null ; 
            
            bl=  fromSource( "300,000 - 500" ) ;
            e = bl.parseTerm(); 
            assert bl != null ; 
            
            bl=  fromSource( "300,000 plus 500,000 " ) ;
            e = bl.parseTerm(); 
            assert bl != null ;
            
            bl=  fromSource( "111 + 222 - 444 " ) ;
            e = bl.parseTerm();
            d = e.evaluateCalculation();
            System.out.printf(" EXPx Term=  %s\n",e.toString());
            System.out.printf( "Result =  %s \n", d.toPlainString());
            assert bl != null ;  
        } 
        catch(  ParseError e )
        { 
            System.out.println("TEST Term: PARSE ERROR->"+ e);
            return false ; 
        }
       // TEST FOR failure of mixing currencies 
       try 
        { 
            System.out.println(" TEST FOR FAIL $100 + £200 ");
            BFLExpressionParser bl=  fromSource( " $100 + £200 " ) ;
            NumericExpression e = bl.parseTerm();
            //isCompatable 
            
            assert e != null ; 
            BigDecimal d = e.evaluateCalculation();
            System.out.printf(" EXP=  %s\n",e.toString());
            System.out.printf("Result =  %s \n", d.toPlainString());
            assert bl != null ;
            assert false : " $100 + £200 should fail  "; 
        }
        catch(  ParseError e )
        { 
             System.out.println("TEST FOR FAIL passed");
        }
       
       System.out.println("TEST Term passed");
       
       return true ; 
   }
   //---------------------------------------------------------------------------
   static boolean testFactor()
   { 
        System.out.print("TEST Factor "); 
        
        try 
        { 
            String source = " student name " ; 
            BufferedReader in = new BufferedReader(new StringReader(source));
            SymbolTable sym = new SymbolTable(null);
            Variable var = sym.getOrMakeIfNull("_student_name"); 
            var.setValue(BigDecimal.ONE);
            BFLExpressionParser parser = new BFLExpressionParser( in, sym  );
            assert parser != null ;
            NumericExpression e = parser.parseFactor(); assert e != null ; 
            
        // NEGATIVE     
            parser = fromSource(" -34"); 
            e = parser.parseFactor(); 
            assert e!= null ; 
          
                      
// NOW DO POWER OPERATOR            
            //System.out.println("30^333");
            /*BFLExpressionParser  bfl = fromSource("30^333");
            NumericExpression ex = bfl.parsePowerOperator();
            assert ex != null ;   
            assert 0 == ex.evaluateCalculation().compareTo(
            new BigDecimal( "529144398052420314716929933900838757437386767361 "))
            " POWER NOT WORKING"; 
            
            //System.out.println("30 ^ 333 ");
            bfl = fromSource("31 ^ 32");
            ex = bfl.parsePowerOperator();
            
            assert ex != null ;*/ 
        } 
        catch(  ParseError e )
        { 
            System.err.println("TEST Factor: PARSE ERROR -> "+ e);
            return false ; 
        }
        return true ;
   }
   //---------------------------------------------------------------------------
   static boolean testNumber()
   { 
       System.out.print("TEST Number "); 
        
        try 
        { 
            String expr = "100,000ft";
            System.out.println("\nTrying '"+ expr+"'"); 
            BFLExpressionParser  bfl = fromSource(expr); 
            LiteralNumberExpression ex = bfl.parseLiteralNumber();
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            LiteralNumberExpression cmp = new LiteralNumberExpression( "100000","ft");
            assert ( cmp.getType().equalsIgnoreCase("ft")); 
            if( cmp.isCompatable(ex) == false ) System.out.println(cmp + " " + ex);
            assert cmp.isCompatable(ex) == true  ; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("34feet"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("34.000Yard"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("111,000,34Inch"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("34Meters"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("34mm"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("12cm"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            
            bfl = fromSource("45km"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText()); 
        } 
        catch(  ParseError e )
        { 
            System.err.println("TEST EXPRESSION: PARSE ERROR->"+ e);
            return false ; 
        }
        
        System.out.println(" TEST Number OK "); 
        return true ; 
   }
   //--------------------------------------------------------------------------- 
    static boolean testCurrency()
    { 
        System.out.println("TEST Currency"); 
        
        try 
        { 
            String expr = "$100,000";
            BufferedReader in = new BufferedReader(new StringReader(expr));
            BFLExpressionParser  bfl = BFLExpressionParser.make( in ) ;
            LiteralNumberExpression ex = bfl.parseCurrency(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            assert ex.getType().equals( BFLExpressionParser.typeDollar ):" WRONG TYPE"; 
            //System.out.println( ex.getNumberAsText());
            
            expr = "£100,000.00";
            in = new BufferedReader(new StringReader(expr));
            bfl = BFLExpressionParser.make( in ) ;
            ex = bfl.parseCurrency(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            assert ex.getType().equals( BFLExpressionParser.typePoundSterling ):" WRONG TYPE 2"; 
            // System.out.println( ex.getNumberAsText());
             
            expr = "€100,000.0";
            in = new BufferedReader(new StringReader(expr));
            bfl = BFLExpressionParser.make( in ) ;
            ex = bfl.parseCurrency(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal";
            assert ex.getType().equals( BFLExpressionParser.typeEuro ):" WRONG TYPE 3"; 
           // System.out.println( ex.getNumberAsText());
        } 
        catch(  ParseError e )
        { 
            System.out.println("PARSE ERROR->"+ e);
            return false ; 
        }
        
        return true ; 
    }
    //---------------------------------------------------------------------------
   /*
    * make a small parser for this scourse code. 
    */
   static SymbolTable testSymTable = null ; 
   static BFLExpressionParser fromSource( String expr ) throws ParseError
   { 
       BufferedReader in = new BufferedReader(new StringReader(expr));
       BFLExpressionParser  bfl = null ; 
       if( testSymTable == null ){  bfl = make( in ) ;} 
       else {  bfl = make( in ,testSymTable ) ;  } 
       testSymTable = bfl.currentSymTable; 
       return bfl ; 
   }
   //---------------------------------------------------------------------------
   /** 
    *  runSimpleExpression - this converts an expression and prints.
    *    woudl be good if we could keep 'IT' variable between calcuations 
    * @param exp
    * @return
    * @throws ParseError 
    */
   static  NumericExpression debugLastExpression  = null ; 
   static String runSimpleExpression( String exp )throws ParseError 
   { 
       BFLExpressionParser bl =  fromSource( exp ) ;  assert bl != null ;
       NumericExpression e = bl.parseExpression();  assert e != null ;
       debugLastExpression = e; 
       BigDecimal d = BigDecimal.ZERO; 
       //System.out.printf(" EXP=  %s\n",e.toString());
       Variable it= null ; 
       if( e.isANumber())
       { 
          d = e.evaluateCalculation();
           assert d != null ;
           it = testSymTable.getOrMakeIfNull("_it"); 
           it.setValue(d);
           it.setType(e.getType());
           
           String t = e.getType(); 
           
           String front = "" ; 
           if( t.equalsIgnoreCase("dollar"))
           { 
              front = "$" ; t= ""; 
           }else if( t.equalsIgnoreCase("pound"))
           { 
               front = "£"; t ="" ; 
           }else if( t.equalsIgnoreCase("euro"))
           { 
               front = "€"; t = "" ; 
           }else if ( t.equals("integer"))
           { 
               t = " (int)"; 
           }else if( t.equals("float" ))
           { 
               t = ""; 
           }if( t.equalsIgnoreCase("Question"))
           { 
            t  = "?"; 
            String s = "NOT SURE" ;  
            if( d.compareTo(BigDecimal.ZERO)==0 ) s = "NO";
            if( d.compareTo(BigDecimal.ONE)==0 ) s = "YES";
            System.out.printf( "Answer to your question  %s  \n", s ) ; // d.compareTo(BigDecimal.ONE));
            return s ; 
           }
           System.out.printf( "Answer =  %s%s%s \n", front,  d.toPlainString(),t);
       } else if( e.isQuestion() )
       { 
        boolean b = e.evaluateLogic(); 
        //e.toHumanString(); 
        String s = "NOT SURE" ;  
        if( b ==false ) s = "NO"; else s = "YES";
        
        System.out.printf( "Answer 3 to your (question)  %s  \n", s ) ; // d.compareTo(BigDecimal.ONE));
        return s ; 
       }
       else 
       {
           System.out.println( "Answer2 = "+ e.doIt().toString() ); 
           return e.doIt().toString()  ; 
       }
       
       ///System.out.println( " TYPE of IT + " +  it.getType()); 
       
       
       return  d.toPlainString();
   }
    //--------------------------------------------------------------------------
    static boolean BFLParserSelfTest()
    { 
       return  testFactor() && 
               testNumber() &&  
               testCurrency() && 
               testTerm()  && 
               testSimpleExpression()&&
               testComparisonExpression(); 
             
    }
    public static NumericExpression makeRandomExpression()
    {
       final double range = 10000.0; 
       double  f = (Math.random() * range) ; 
       double f2 = (Math.random() * range) ; 
       NumericExpression lit = new LiteralNumberExpression(""+f);
       NumericExpression litafter = new LiteralNumberExpression(""+f2);
       int r = (int) (Math.random() * 5); 
       System.out.print(r+ " ("  +( "+*-÷/R".charAt(r)) + " ) "); 
       String s = "" ; 
       BinaryExpression ex= null; 
       
       if((Math.random() > 0.8 ) )litafter =  makeRandomExpression(); 
       if((Math.random() > 0.8 ) )lit =  makeRandomExpression(); 
           
       switch( r ) 
       { 
           case 0 :  { ex = new BinaryExpression('+', lit, litafter ) ;  s = s+ (f+2) ;  }  break ;
           case 1 :  ex = new BinaryExpression('*', lit, litafter ) ; break ; 
           case 2 :  ex = new BinaryExpression('-', lit, litafter ) ; break ; 
           case 3 :  ex = new BinaryExpression('/', lit, litafter ) ; break ; 
           case 4 :  ex = new BinaryExpression('/', lit, litafter ) ; break ; 
           
           
           default:  assert false ; 
       }
       
       return ex ; 
    }
    public static void testRandomExpression() 
    { 
        System.out.println("TEST testRandomExpression "); 
        for( int i = 0 ; i < 1000 ; i++ )
        { 
            Expression ex = makeRandomExpression(); 
            System.out.print(ex.toHumanString() ) ;
             BFLExpressionParser bl ;
            try
            {
                bl = fromSource( ex.toHumanString() );
                assert bl != null ;
            NumericExpression compiledExp = bl.parseExpression();  
            assert compiledExp != null ;


           BigDecimal d = compiledExp.evaluateCalculation();  assert d != null ; 
           System.out.printf(" =  %s\n",d.toString());
           assert d.compareTo(ex.evaluateCalculation()) == 0 : "CALCUATION"; 
           assert compiledExp.isCompatable(ex); 
           
            } catch (ParseError ex1)
            {
                assert false; 
            }
        } 
       
    }
    
    /**
     * Interative system tests at the command line. 
     * @param args 
     */
    public static void main(String[] args) 
    {
        
        testRandomExpression(); 
    }
    //--------------------------------------------------------------------------
}
