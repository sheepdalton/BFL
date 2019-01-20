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
import blfexperiment.expressions.LiteralNumberExpression;
import blfexperiment.expressions.NumericExpression;
import blfexperiment.expressions.Statement;
import blfexperiment.expressions.UnariyExpression;
import blfexperiment.expressions.Variable;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sheep Dalton
 */
public class BFLExpressionParserTest
{
    
    public BFLExpressionParserTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }

    static BFLParser fromSource( String expr ) throws ParseError
   { 
       BufferedReader in = new BufferedReader(new StringReader(expr));
       BFLParser  bfl = BFLParser.make( in ) ;
       return bfl ; 
   }
    /**
     * Test of make method, of class BFLExpressionParser.
     */
    @Test
    public void testparseBlock()
    { 
        System.out.println("testparseBlock");
        String s=   "put 3*3 into result \n" +
                    "put  result + 1 into output \n";
        try
        { 
            
       
        BFLParser  bfl = fromSource(s); 
        Statement stm =   bfl.parseBlock(0); 
       // @@@ TODO - add in execute to. 
        } catch(  ParseError e )
        { 
            System.err.println("TEST testparseBlock: PARSE ERROR->"+ e);
            fail("Parse Error ->" + e ) ; 
        } 
    }

    @Test
    public void testUnits()
    { 
       assert   BFLExpressionParser.isAUnit( "dollar" ) == false ;
       assert   BFLExpressionParser.isAUnit( "poundSterling" ) == false ; 
       
       assert BFLExpressionParser.isACurrency("dollar") == true ; 
       assert BFLExpressionParser.isACurrency("poundSterling") == true ;
       assert BFLExpressionParser.isACurrency("euro") == true ;
       
    }

    /**
     * Test of parseLongNumber method, of class BFLExpressionParser.
     */
    @Test
    public void testParseLongNumber() 
    {
        System.out.println("parseLongNumber");
        System.out.print("TEST Number "); 
        
        try 
        { 
            
            BFLExpressionParser  bfl = fromSource("100,000ft"); 
            LiteralNumberExpression ex = bfl.parseLiteralNumber();
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            LiteralNumberExpression cmp = new LiteralNumberExpression( "100000", "ft"); 
            assertEquals ( true, cmp.isCompatable(ex) ) ; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "100000");
            
            bfl = fromSource("34feet"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
            
            bfl = fromSource("34.000Yard"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34.000");
            
            bfl = fromSource("111,000,34Inch"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "11100034");
            
            bfl = fromSource("34Meters"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
            
            bfl = fromSource("34mm"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
            
            bfl = fromSource("12cm"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "12");
            
            bfl = fromSource("45km"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText()); 
            assertEquals(ex.getNumberAsText(), "45");
        }
        catch(  ParseError e )
        { 
            System.err.println("TEST EXPRESSION: PARSE ERROR->"+ e);
            fail("Parse Error ->" + e ) ; 
        } 
    }
    //--------------------------------------------------------------------------
        /**
     * Test of parseCurrency method, of class BFLExpressionParser.
     */
    @Test
    public void testParseCurrency() throws Exception
    {
        System.out.println("parseCurrency");
        
        try 
        { 
        BFLExpressionParser  bfl = fromSource("$100,000"); 
            LiteralNumberExpression ex = bfl.parseLiteralNumber();
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "100000");
            
            bfl = fromSource("£34"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
            
             bfl = fromSource("€34"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
         } 
        catch(  ParseError e )
        { 
            System.err.println("TEST testParseCurrency: PARSE ERROR->"+ e);
            fail("Parse Error ->" + e ) ;
        } 
    }
    //--------------------------------------------------------------------------
        /**
     * Test of parseTerm method, of class BFLExpressionParser.
     */
    @Test
    public void testParseTerm() throws Exception
    {
        System.out.print("TEST Term "); 
       try 
        { 
            BFLExpressionParser bl=  fromSource( "100 + 200 " ) ;
            assertNotNull("Expression not returned ",bl );
            NumericExpression e = bl.parseTerm(); assertNotNull("Expression not returned ",e);
            BigDecimal d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
            assertEquals(d.toPlainString(), "300");
            
            bl=  fromSource( "200 - 100 " ) ;
            assertNotNull("Expression not returned ",bl );
            e = bl.parseTerm(); assertNotNull("Expression not returned ",e);
            d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
            assertEquals(d.toPlainString(), "100");
        
        }
        catch(  ParseError e )
        { 
            System.out.println("TEST Term: PARSE ERROR->"+ e);
             fail("TEST Term: PARSE ERROR->"+e);
        }
    }
    
    @Test
    public void testparseLogicExpression()
    { 
        try 
        { 
                runSimpleExpression(" 564 = 436" ); // test compile 
        }
            catch(  ParseError e )
        { 
            System.out.println("TEST testparseLogicExpression: PARSE ERROR->"+ e);
            fail("testparseLogicExpression .");
        }
    }
    @Test
    public void testSimpleExpression()
    { 
        
       
        System.out.println("TEST SimpleExpression "); 
       try 
        {  
            runSimpleExpression(" 564 + 436" ); // test compile 
            assertEquals ( runSimpleExpression(" 200 ÷ 20 " ), "10") ;
            // test long numbers 
            runSimpleExpression("20000000000000000000000000.00 ÷ 2000000000000000000000000 " );
            runSimpleExpression(" 564.9 + 436" ); 
            
            assertEquals( runSimpleExpression(" 2 + 3" ),("5"));
            assertEquals( runSimpleExpression("9  + 2020 / ( 2020/1000 )"), 
                                          "1004.455445544554455445544554455446"); 
            // operator precidance 
            
            assertEquals( runSimpleExpression(" 1 + 2 * 3" ), ("9") ); 
            System.out.println( "[[[R]]== "+ runSimpleExpression("$892.9 + $436,000" ) ) ; 
            assertEquals( runSimpleExpression("$892.9 + $436,000" ),("436892.9"));
            
            assertEquals( runSimpleExpression("2^8" ),("256"));
            
            
        } 
       catch(  ParseError e )
        { 
            System.out.println("TEST SimpleExpression: PARSE ERROR->"+ e);
            fail("testSimpleExpression .");
        }
       try 
        { 
            System.out.println(" TEST FOR FAIL €100 * £200 ");
            BFLExpressionParser bl=  fromSource( " €100 * £200 " ) ;
            NumericExpression e = bl.simpleMathExpression();
            
            BigDecimal d = e.evaluateCalculation();
            System.out.printf(" EXP=  %s\n",e.toString());
            System.out.printf( "Result =  %s \n", d.toPlainString());
            assert bl != null ;
            assert false : " €100 * £200 should fail  "; 
            fail("testSimpleExpression €100 * £200 should fail"); 
        }
       catch(  ParseError e )
        { 
             System.out.println("TEST FOR €100 * £200 wrong passed "+ e );
        }
       System.out.println("TEST SimpleExpression passed");
    }
    //--------------------------------------------------------------------------
    @Test 
    public void testFactor() 
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
           
            String r ; 
            r=runSimpleExpression(" ( 90 - 100 )* 8  " ); // test compile 
            r=runSimpleExpression(" | 90 - 100 | " ); // test does it compile 
            assertEquals(r, "10");
            r=runSimpleExpression("30^3");
            assertEquals(r, "27000");
            
            r=runSimpleExpression("true");
            assertEquals(r, "YES"); 
            r=runSimpleExpression("false");
            assertEquals(r, "NO"); 
            
            r=runSimpleExpression("YES");
            assertEquals(r, "YES"); 
            r=runSimpleExpression("NO");
            assertEquals(r, "NO");
            
            
            r=runSimpleExpression("¬ YES"); assertEquals(r, "NO"); 
            r=runSimpleExpression("¬ NO");
            assertEquals(r, "YES");
            
            r=runSimpleExpression("NOT YES");
            assertEquals(r, "NO"); 
            r=runSimpleExpression("NOT NO");
            assertEquals(r, "YES");
            
            r=runSimpleExpression("¬ TRUE");
            assertEquals(r, "NO"); 
            r=runSimpleExpression("¬ FALSE");
            assertEquals(r, "YES");
            
            r=runSimpleExpression("NOT TRUE");
            assertEquals(r, "NO"); 
            r=runSimpleExpression("NOT FALSE");
            assertEquals(r, "YES");

            r=runSimpleExpression("√16");
            assertEquals(r, "4");
            
            //System.out.println("30 ^ 333 ");
            r=runSimpleExpression("30 ^ 3");
            assertEquals(r, "27000");
            
            parser =  BFLExpressionParser.fromSource( "YES" ); assert parser != null ;
            e = parser.parseFactor(); assert e != null ;
            assertEquals( true , e.evaluateLogic()); 
            
            parser =  BFLExpressionParser.fromSource( "NO" ); assert parser != null ;
            e = parser.parseFactor(); assert e != null ;
            assertEquals( false , e.evaluateLogic()); 
            
            parser =  BFLExpressionParser.fromSource( "TRUE" ); assert parser != null ;
            e = parser.parseFactor(); assert e != null ;
            assertEquals( true , e.evaluateLogic()); 
            
            parser =  BFLExpressionParser.fromSource( "FALSE" ); assert parser != null ;
            e = parser.parseFactor(); assert e != null ;
            assertEquals( false , e.evaluateLogic()); 
            
             
        } 
        catch(  ParseError e )
        { 
            System.err.println("TEST Factor: PARSE ERROR->"+ e);
            fail("TEST testFactor : PARSE ERROR->"+e);
        }
    }
    @Test 
    public void testparseLogicExpressionTest() 
    { 
        System.out.print("TEST parseLogicExpression "); 
        
        try 
        { 
            String source = " 4 = 4 " ; 
            
            BFLExpressionParser parser =  BFLExpressionParser.fromSource( source );
            assert parser != null ;
            NumericExpression e = parser.parseComparisonExpression(); assert e != null ; 
           
            String rslt = runSimpleExpression(" 5 > 6 " ); // test compile 
            assertEquals( "NO", rslt);
            
            rslt = runSimpleExpression(" 6 > 5  " ); // test compile 
             assertEquals(rslt, "YES");
             
            rslt = runSimpleExpression(" 6 is more than 5  " ); // test compile 
            assertEquals(rslt, "YES");
            
             rslt = runSimpleExpression(" 6 more than 5  " ); // test compile 
            assertEquals(rslt, "YES");
            
            rslt = runSimpleExpression(" 6 greater than 5  " ); // test compile 
            assertEquals(rslt, "YES");

            rslt = runSimpleExpression(" 6 more  5  " ); // test compile 
            assertEquals(rslt, "YES");
             
            rslt = runSimpleExpression(" 6 < 5  " ); // test compile 
             assertEquals(rslt, "NO");
            rslt = runSimpleExpression(" 5 < 6  " ); // test compile 
            assertEquals(rslt, "YES");
             
            rslt = runSimpleExpression(" 6 is less than 5  " ); // test compile 
            assertEquals(rslt, "NO");
            
             rslt = runSimpleExpression(" 6 less than 5  " ); // test compile 
            assertEquals(rslt, "NO");
            
            rslt = runSimpleExpression(" 6 less  5  " ); // test compile 
            assertEquals(rslt, "NO");
            
            rslt = runSimpleExpression(" 6 is less  5  " ); // test compile 
            assertEquals(rslt, "NO");
            
             
            String s = runSimpleExpression(" 6 = 5  " ); // test compile 
            assertEquals(s, "NO");
            
             s = runSimpleExpression(" 6 = 6  " ); assertEquals(s, "YES");
            
            s = runSimpleExpression(" 6 is 6  " );  assertEquals(s, "YES");
            s = runSimpleExpression(" 6 ≤ 6  " );   assertEquals( "YES",s);
            s = runSimpleExpression(" 6 ≤ 9  " );   assertEquals(s, "YES");
            s = runSimpleExpression(" 6 ≤ 5  " );   assertEquals(s, "NO");
            s = runSimpleExpression(" 6 <= 5  " );  assertEquals(s, "NO");
            s = runSimpleExpression(" 6 <= 7  " );  assertEquals(s, "YES");
            
            
            s=runSimpleExpression(" “hello he said” " ); //parse literal string with fancy quotes;
            
        } 
        catch(  ParseError e )
        { 
            System.err.println("TEST Factor: PARSE ERROR->"+ e);
            fail("TEST testFactor : PARSE ERROR->"+e);
        }
         System.out.println(" CORRECT  "); 
    }
    @Test
    public void testParseLogicExpression() 
    { 
        System.out.print("TEST testParseLogicExpression "); 
        BFLExpressionParser parser; 
      NumericExpression e ; 
        try
        { 
            parser =  BFLExpressionParser.fromSource( "YES" ); assert parser != null ;
            e = parser.parseLogicExpression(); assert e != null ;
            assertEquals( true , e.evaluateLogic());
            
            parser =  BFLExpressionParser.fromSource( "YES and NO" ); assert parser != null ;
            e = parser.parseLogicExpression(); assert e != null ;
            assertEquals( false , e.evaluateLogic());
            
            parser =  BFLExpressionParser.fromSource( "NO and YES" ); assert parser != null ;
            e = parser.parseLogicExpression(); assert e != null ;
            assertEquals( false , e.evaluateLogic());
            
            parser =  BFLExpressionParser.fromSource( "NO and NO" ); assert parser != null ;
            e = parser.parseLogicExpression(); assert e != null ;
            assertEquals( false , e.evaluateLogic());
            
            parser =  BFLExpressionParser.fromSource( "YES and YES" ); assert parser != null ;
            e = parser.parseLogicExpression(); assert e != null ;
            assertEquals( true , e.evaluateLogic());
            
             parser =  BFLExpressionParser.fromSource( "YES and NO" ); assert parser != null ;
            e = parser.parseExpression(); assert e != null ;
            assertEquals( BigDecimal.ZERO , e.evaluateCalculation());
            
        } catch(  ParseError err )
        { 
            System.err.println("TEST Factor: PARSE ERROR->"+ err);
            fail("TEST testFactor : PARSE ERROR->"+err);
        }
         System.out.print("TEST testParseLogicExpression ");   
    }
    /**
     *  Generate random expression - 4 * 4 + 2  etc 
     *   then test it 
     */
    
    @Test 
    public void testRandomExpression() 
    { 
        System.out.print("TEST testRandomExpression "); 
      
        LiteralNumberExpression lit = new LiteralNumberExpression("3");
        LiteralNumberExpression litafter = new LiteralNumberExpression("3");
        BinaryExpression ex = new BinaryExpression('+', lit, litafter ) ; 
        
    }
    
}
