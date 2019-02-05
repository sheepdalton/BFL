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

import static blfexperiment.BFLExpressionParser.fromSource;
import static blfexperiment.BFLExpressionParser.runSimpleExpression;
import blfexperiment.expressions.BinaryExpression;
import blfexperiment.expressions.Expression;
import blfexperiment.GeneralTypes.GeneralObject;
import blfexperiment.expressions.LiteralNumberExpression;
import blfexperiment.expressions.Statement;
import blfexperiment.expressions.StatementBlock;
import blfexperiment.expressions.Variable;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import blfexperiment.expressions.GeneralExpression;

/**
 *
 * @author Sheep Dalton
 */
public class BFLParserTest
{
    public BFLParserTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }
     //---------------------------------------------------------------------------
   /*
    * make a small parser for this scourse code. 
    */
   static BFLParser fromSource( String expr ) throws ParseError
   { 
       BufferedReader in = new BufferedReader(new StringReader(expr));
       BFLParser  bfl = BFLParser.make( in ) ;
       return bfl ; 
   }
    //---------------------------------------------------------------------------       
   static String runSimpleExpression( String exp )throws ParseError 
   { 
       BFLParser bl=  fromSource( exp ) ;  assert bl != null ;
       GeneralExpression e = bl.simpleMathExpression();  assert e != null ;
       System.out.printf(" EXP=  %s\n",e.toString());
       BigDecimal d = e.evaluateCalculation();  assert d != null ; 
       System.out.printf( "Result =  %s \n", d.toPlainString());

       return  d.toPlainString();
   }
   
   
    //--------------------------------------------------------------------------
     @Test
    public void testParsePutStatement()
    { 
       System.out.print("TEST testParsePutStatement "); 
       try 
        { 
            BFLParser bl=  fromSource( "put 7 * 3\n" ) ;
            assertNotNull("Expression not returned ",bl );
            Statement stm = bl.parsePutStatement(); assertNotNull("statem not parse", stm);
          
            GeneralObject d = stm.evaluteStatement();
            
            bl=  fromSource( "put 7 * 3 into bob\n" ) ;
            assertNotNull("Expression not returned ",bl );
            stm = bl.parsePutStatement(); assertNotNull("statem not parse", stm);
          
            d = stm.evaluteStatement();
            //assertEquals(d.toPlainString(), "300");
        }
        catch(  ParseError e )
        { 
            System.out.println("TEST testParsePutStatement: PARSE ERROR->"+ e);
            fail("TEST testParsePutStatement : PARSE ERROR->"+e);
        }
       try 
        { 
            BFLParser bl=  fromSource( "put 8 * novar into bob\n" ) ;
            Statement stm = bl.parsePutStatement();
            fail("TEST testParsePutStatement : non var not stopped");
        }
        catch(  ParseError e )
        { 
            System.out.println(" CORRECT"+ e); 
        }
      /// @@@ TODO check that put cannot use two similar names  
    }
    //--------------------------------------------------------------------------
     @Test
    public void testParseBlock()
    { 
       System.out.print("TEST testParsePutStatement "); 
       try 
        { 
            BFLParser bl=  fromSource( "put 7 * 3\nput 6 * 5 into bob" ) ;
            assertNotNull("Expression not returned ",bl );
            StatementBlock stm = bl.parseBlock(0); 
            assertNotNull("statem not parse", stm);
          
            GeneralObject d = stm.evaluteStatement();
        }
        catch(  ParseError e )
        { 
            System.out.println("TEST testParsePutStatement: PARSE ERROR->"+ e);
             fail("TEST testParsePutStatement : PARSE ERROR->"+e);
        } 
    }
    //--------------------------------------------------------------------------    //--------------------------------------------------------------------------
    /**
     * Test of makeLiteralNumberExpression method, of class BFLExpressionParser.
     */
//    @Test
//    public void testMakeLiteralNumberExpression()
//    {
//        System.out.println("makeLiteralNumberExpression");
//        String typeNumber = "";
//        BFLExpressionParser instance = null;
//        LiteralNumberExpression expResult = null;
//        LiteralNumberExpression result = instance.makeLiteralNumberExpression(typeNumber);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//

//
//    /**
//     * Test of parseUnits method, of class BFLExpressionParser.
//     */
//    @Test
//    public void testParseUnits() throws Exception
//    {
//        System.out.println("parseUnits");
//        LiteralNumberExpression ex = null;
//        BFLExpressionParser instance = null;
//        LiteralNumberExpression expResult = null;
//        LiteralNumberExpression result = instance.parseUnits(ex);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of parseLiteralNumber method, of class BFLExpressionParser.
//     */
//    @Test
//    public void testParseLiteralNumber() throws Exception
//    {
//        System.out.println("parseLiteralNumber");
//        BFLExpressionParser instance = null;
//        LiteralNumberExpression expResult = null;
//        LiteralNumberExpression result = instance.parseLiteralNumber();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of parseFactor method, of class BFLExpressionParser.
//     */
//    @Test
//    public void testParseFactor() throws Exception
//    {
//        System.out.println("parseFactor");
//        BFLExpressionParser instance = null;
//        Expression expResult = null;
//        Expression result = instance.parseFactor();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of makeBinaryExpression method, of class BFLExpressionParser.
//     */
//    @Test
//    public void testMakeBinaryExpression()
//    {
//        System.out.println("makeBinaryExpression");
//        int what = 0;
//        BFLExpressionParser instance = null;
//        BinaryExpression expResult = null;
//        BinaryExpression result = instance.makeBinaryExpression(what);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//

//
//    /**
//     * Test of fromSource method, of class BFLExpressionParser.
//     */
//    @Test
//    public void testFromSource() throws Exception
//    {
//        System.out.println("fromSource");
//        String expr = "";
//        BFLExpressionParser expResult = null;
//        BFLExpressionParser result = BFLExpressionParser.fromSource(expr);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//
//    /**
//     * Test of testNumber method, of class BFLExpressionParser.
//     */
    @Test
    public void testTestNumber()
    {
        System.out.println("testNumber");
        try 
        { 
            String source = "100000" ; 
            BufferedReader in = new BufferedReader(new StringReader(source));
            BFLExpressionParser parser = new BFLExpressionParser( in   );
            assert parser != null ;
            LiteralNumberExpression e = parser.parseLiteralNumber();
            assert e != null ; 
           
        } 
        catch(  ParseError e )
        { 
            System.err.println("TEST Factor: PARSE ERROR->"+ e);
            fail("TEST testFactor : PARSE ERROR->"+e);
        }
    }
//
//    /**
//     * Test of testCurrency method, of class BFLExpressionParser.
//     */
//    @Test
//    public void testTestCurrency()
//    {
//        System.out.println("testCurrency");
//        boolean expResult = false;
//        boolean result = BFLExpressionParser.testCurrency();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }


    
    
}
