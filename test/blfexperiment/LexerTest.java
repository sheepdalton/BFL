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

import static blfexperiment.Lexer.*;
import java.io.BufferedReader;
import java.io.StringReader;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Sheep Dalton
 */
public class LexerTest
{
    
    public LexerTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }
//
//    /**
//     * Test of getLineNumber method, of class Lexer.
//     */
//    @Test
//    public void testGetLineNumber()
//    {
//        System.out.println("getLineNumber");
//        Lexer instance = null;
//        int expResult = 0;
//        int result = instance.getLineNumber();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of pushBackToStream method, of class Lexer.
//     */
//    @Test
//    public void testPushBackToStream()
//    {
//        System.out.println("pushBackToStream");
//        int letter = 0;
//        Lexer instance = null;
//        instance.pushBackToStream(letter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getNextCharacter method, of class Lexer.
//     */
//    @Test
//    public void testGetNextCharacter()
//    {
//        System.out.println("getNextCharacter");
//        Lexer instance = null;
//        int expResult = 0;
//        int result = instance.getNextCharacter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of showCurrentPosition method, of class Lexer.
//     */
//    @Test
//    public void testShowCurrentPosition()
//    {
//        System.out.println("showCurrentPosition");
//        Lexer instance = null;
//        String expResult = "";
//        String result = instance.showCurrentPosition();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeNextToken method, of class Lexer.
//     */
//    @Test
//    public void testRemoveNextToken()
//    {
//        System.out.println("removeNextToken");
//        Lexer instance = null;
//        Lexer.Token expResult = null;
//        Lexer.Token result = instance.removeNextToken();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of pushTokenBackToHead method, of class Lexer.
//     */
//    @Test
//    public void testPushTokenBackToHead()
//    {
//        System.out.println("pushTokenBackToHead");
//        Lexer.Token t = null;
//        Lexer instance = null;
//        instance.pushTokenBackToHead(t);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeNextTokenAsSymbol method, of class Lexer.
//     */
//    @Test
//    public void testRemoveNextTokenAsSymbol()
//    {
//        System.out.println("removeNextTokenAsSymbol");
//        Lexer instance = null;
//        Lexer.SingleSymbol expResult = null;
//        Lexer.SingleSymbol result = instance.removeNextTokenAsSymbol();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeNextTokenAsWord method, of class Lexer.
//     */
//    @Test
//    public void testRemoveNextTokenAsWord()
//    {
//        System.out.println("removeNextTokenAsWord");
//        Lexer instance = null;
//        Lexer.WordToken expResult = null;
//        Lexer.WordToken result = instance.removeNextTokenAsWord();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hasThisWord method, of class Lexer.
//     */
//    @Test
//    public void testHasWord()
//    {
//        System.out.println("hasThisWord");
//        String word = "";
//        Lexer instance = null;
//        boolean expResult = false;
//        boolean result = instance.hasThisWord(word);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hasAnyOfTheseWords method, of class Lexer.
//     */
//    @Test
//    public void testHasWords()
//    {
//        System.out.println("hasAnyOfTheseWords");
//        String[] words = null;
//        Lexer instance = null;
//        boolean expResult = false;
//        boolean result = instance.hasAnyOfTheseWords(words);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hasANumber method, of class Lexer.
//     */
//    @Test
//    public void testHasNumber()
//    {
//        System.out.println("hasANumber");
//        Lexer instance = null;
//        boolean expResult = false;
//        boolean result = instance.hasANumber();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hasThisSymbol method, of class Lexer.
//     */
//    @Test
//    public void testHasThisSymbol()
//    {
//        System.out.println("hasThisSymbol");
//        int symbolCodePoint = 0;
//        Lexer instance = null;
//        boolean expResult = false;
//        boolean result = instance.hasThisSymbol(symbolCodePoint);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of isSkipingWhiteSpace method, of class Lexer.
     */
    @Test
    public void testRQUITemoveNextTokenAsWord()
    {
        
        System.out.println("removeNextTokenAsWord");
        String whiteSpace = " the sin of 4"; 
        BufferedReader in = new BufferedReader(new StringReader(whiteSpace));
        Lexer  lex = new Lexer(in ); 
        lex.setSkipWhiteSpace(true);
        assertEquals(true,  lex.hasThisWord("the"));
        Lexer.WordToken the = lex.removeNextTokenAsWord();
        assert the!=null ; 
        assertEquals(true,  lex.hasThisWord("sin"));
        Lexer.WordToken sine = lex.removeNextTokenAsWord();
        assertEquals(true,  lex.hasThisWord("of"));
        Lexer.WordToken of = lex.removeNextTokenAsWord();
        assertEquals(true, of.getText().equalsIgnoreCase("of"));
       
    }
    /*
    if( tokenStream.hasThisWord("of"))
         { 
             // WHY THIS IS NOT WORKING IS BEYOND ME ??? 
            Lexer.WordToken of = tokenStream.removeNextTokenAsWord();assert of != null; 
            assert of.getText().equalsIgnoreCase("of");
            Lexer.Token t = tokenStream.removeNextToken();  //tokenStream.pushTokenBackToHead(t);
            //System.out.println("found OF getting expression  " + t + " HAND " + of);
            assert tokenStream.hasThisWord("of") == false  ; 
    */
//
//    /**
//     * Test of setSkipWhiteSpace method, of class Lexer.
//     */
    @Test
    public void testremoveNextTokenAsLiteralString()
    {
        System.out.println("removeNextTokenAsLiteralString");
        
        
        BufferedReader in = new BufferedReader(new StringReader("  “message” hello "));
        Lexer lex = new Lexer(in ); lex.setSkipWhiteSpace(true);
        assert lex.hasQuoteStringAvailable() == true ; 
        Lexer.StringToken st = lex.removeNextTokenAsLiteralString(); 
        assert st != null ; 
        assertEquals("message", st.getText());

    }

//    /**
//     * Test of testRemoveNextToken method, of class Lexer.
//     */
    @Test
    public void testTestRemoveNextToken()
    {
        System.out.println("testRemoveNextToken");
        String whiteSpace = "    (Helloü\u00f1939431333"; 
        BufferedReader in = new BufferedReader(new StringReader(whiteSpace));
        Lexer  lex = new Lexer(in ); 
        lex.setSkipWhiteSpace(false);
        Lexer.Token t = lex.removeNextToken(); 
        assert t.getTokenType() == TT_WHITESPACE :"NOT WHITE SPACE " ; 
        assertEquals( t.getTokenType() , TT_WHITESPACE ); 
    
        Lexer.WhiteSpace ws = (Lexer.WhiteSpace)t;  
        assert ws.count == 4; 
        
        t = lex.removeNextToken();
        assert t.getTokenType() == TT_SYMBOL :"NOT SYMBOL";
        
        t = lex.removeNextToken();
        assert t.getTokenType() == TT_WORD :"NOT WORD";
        Lexer.WordToken wtk = (Lexer.WordToken)t; 
        
        System.out.println("Word  = '" +  wtk.getText() +"'");
        
        t = lex.removeNextToken();
        assert t.getTokenType() == TT_NUMBER : "NOT NUMBER";
        Lexer.NumberToken ntk = (Lexer.NumberToken)t;
        
        System.out.println("number  = '" +  ntk.getText() +"'");
        
        whiteSpace = " 1213222"; 
        in = new BufferedReader(new StringReader(whiteSpace));
        lex = new Lexer(in ); lex.setSkipWhiteSpace(true);
        assert lex.hasANumber() == true :" hasNumber fail "; 
        assert lex.hasANumber() == true :" hasNumber fail 2 ";
        Lexer.NumberToken nt=  (Lexer.NumberToken)lex.removeNextToken(); 
        if(nt.getNumberAsText().equals("1213222")==false ) 
            System.out.println("|"+nt.getNumberAsText()+"|");
        assert( nt.getNumberAsText().equals("1213222")); 
    
    }
    @Test 
    public void symbolInListTests()
    { 
        System.out.println("testRemoveNextToken");
        int sym = '*'; 
        assert( Lexer.symbolInList(sym , "()$%£@!@£)$(*" )== true ) ; 
        sym = '9'; 
        assert( Lexer.symbolInList(sym , "()$%£@!@£)$(*" )== false ) ; 
        
    }
//
//    /**
//     * Test of runLexerTests method, of class Lexer.
//     */
//    @Test
//    public void testRunLexerTests()
//    {
//        System.out.println("runLexerTests");
//        Lexer.runLexerTests();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of main method, of class Lexer.
//     */
//    @Test
//    public void testMain()
//    {
//        System.out.println("main");
//        String[] args = null;
//        Lexer.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
