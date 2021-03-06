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

import static blfexperiment.BFLExpressionParser.runSimpleExpression;
import blfexperiment.GeneralTypes.GeneralObject;
import blfexperiment.expressions.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author Sheep Dalton
 */
public class BFLExpressionParserTest
{
    
    public BFLExpressionParserTest()
    {
        
    }
    //--------------------------------------------------------------------------
    @Before
    public void setUp()
    {
    }
    //--------------------------------------------------------------------------
    static BFLParser fromSource( String expr ) throws ParseError
   { 
       BufferedReader in = new BufferedReader(new StringReader(expr));
       BFLParser  bfl = BFLParser.make( in ) ;
       return bfl ; 
   }
    //--------------------------------------------------------------------------
   @Test
   public void testLiteraList()
   { 
       System.out.print("TEST Lists "); 
       BFLExpressionParser  bfl;  Expression ex;
       
        try 
        { 
            bfl = fromSource("[ 1 ; 2; 3 ; 4 ; 5] "); 
            ex = bfl.parseFactor();
            assert ex!= null ; 
            assert ex instanceof LiteralListExpression : "NOT literal"; 
            LiteralListExpression lx = new LiteralListExpression(); 
            lx.add(new LiteralNumberExpression( "1" ));
            lx.add(new LiteralNumberExpression( "2" ));
            lx.add(new LiteralNumberExpression( "3" ));
            lx.add(new LiteralNumberExpression( "4" ));
            lx.add(new LiteralNumberExpression( "5" ));
            assert  lx.isCompatable(ex);
            
            BFLExpressionParser bl =  fromSource( "[ 1 ; 2; 3 ; 4 ; 5]*2");
            GeneralExpression e = bl.parseExpression(); 
            GeneralObject go = e.doIt();
            assert go.toString().equals( "[ 2 ; 4 ; 6 ; 8 ; 10]" );
            
            bl =  fromSource( "[ 1 ; 2; 3 ; 4 ; 5] -1 ");
            e = bl.parseExpression(); 
            go = e.doIt(); 
            assert go.toString().equals( "[ 0 ; 1 ; 2 ; 3 ; 4]" );
            
            bl =  fromSource( "[ 2 ; 4 ; 6 ; 8 ; 10] / 2 ");
            e = bl.parseExpression();  go = e.doIt(); 
            assert go.toString().equals( "[ 1 ; 2 ; 3 ; 4 ; 5]" );
        }
        catch(  ParseError e )
        { 
            System.err.println("TEST EXPRESSION: PARSE ERROR->"+ e + "\n" );
            e.printStackTrace();
            fail("LiteraList vars ok");
        }
         System.out.println(" OK "); 
   }
   //---------------------------------------------------------------------------
  @Test
   public  void testNumber()
   { 
       System.out.print("TEST Number/Units "); 
       BFLExpressionParser  bfl; 
       LiteralNumberExpression ex;
       
        try 
        { 
            String expr = "100,000ft";
            System.out.println("\nTrying '"+ expr+"'"); 
            bfl = fromSource(expr); 
            ex = bfl.parseLiteralNumber();
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            LiteralNumberExpression cmp = new LiteralNumberExpression( "100000","ft");
            assert ( cmp.getType().equalsIgnoreCase("ft")); 
            if( cmp.isCompatable(ex) == false ) System.out.println(cmp + " " + ex);
            assert cmp.isCompatable(ex) == true  ; 
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
            
            bfl = fromSource("34Meter");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ex = bfl.parseLiteralNumber(); 
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
            System.err.println("TEST EXPRESSION: PARSE ERROR->"+ e + "\n" );
            e.printStackTrace();
            fail("Ligit vars ok");
        }
        
        try 
        { 
            bfl = fromSource("34feet"); // FEET IS NOT A UNIT
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText());
            fail(" should not work.");
        }
            catch(  ParseError e )
        { 
            System.out.println(" TEST Number OK "); 
            
        }
            
        System.out.println(" TEST Number OK "); 
      
   }
   //--------------------------------------------------------------------------
   @Test 
   public void testBinaryExpressionNumeric()
    { 
        System.out.println("testBinaryExpressionNumeric");
        try
        {
            BFLExpressionParser bl =  fromSource( "2*2" ) ;  assert bl != null ;
            GeneralExpression e = bl.parseExpression();  assert e != null ;
            assert e instanceof BinaryNumericOnlyExpression ;
           
            bl =  fromSource( "2÷2" ) ;  assert bl != null ;
            e = bl.parseExpression();  assert e != null ;
            assertTrue( e instanceof BinaryNumericOnlyExpression );
            
            bl =  fromSource( "2/2" ) ;  assert bl != null ;
            e = bl.parseExpression();  assert e != null ;
            assertTrue( e instanceof BinaryNumericOnlyExpression );
            
            bl =  fromSource( "2+2" ) ;  assert bl != null ;
            e = bl.parseExpression();  assert e != null ;
            assert e instanceof BinaryNumericOnlyExpression ;
            
            bl =  fromSource( "2-2" ) ;  assert bl != null ;
            e = bl.parseExpression();  assert e != null ;
            assert e instanceof BinaryNumericOnlyExpression ;
            
             bl =  fromSource( "2^2" ) ;  assert bl != null ;
            e = bl.parseExpression();  assert e != null ;
            assert e instanceof BinaryNumericOnlyExpression ;
        // THIS IS NEGATIVE -- 2= nos not binary numeric     
             bl =  fromSource( "2=2" ) ;  assert bl != null ;
            e = bl.parseExpression();  assert e != null ;
            assert  !(e instanceof BinaryNumericOnlyExpression) ;
        } catch (ParseError ex)
        {
            fail(" 2 * 2 caused exception - not good.");
        }
    }
    /** 
     * recycle internal test.
     */
    @Test
    public void testSimpleExpression2()
    { 
        assert BFLExpressionParser.testSimpleExpression() == true; 
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
      
        } catch(  ParseError e )
        { 
            System.err.println("TEST testparseBlock: PARSE ERROR->"+ e);
            fail("Parse Error ->" + e ) ; 
        } 
    }
    //--------------------------------------------------------------------------
    @Test
    public void testUnits()
    { 
       System.out.println("testUnits");
       assert   BFLExpressionParser.isAUnit( "dollar" ) == false ;
       assert   BFLExpressionParser.isAUnit( "poundSterling" ) == false ; 
       assert   BFLExpressionParser.isAUnit( "fish" ) == false ; 
        
       assert BFLExpressionParser.isACurrency("dollar") == true ; 
       assert BFLExpressionParser.isACurrency("poundSterling") == true ;
       assert BFLExpressionParser.isACurrency("euro") == true ;
       
       assert   BFLExpressionParser.isAUnit( "Kg" ) == true ; 
       assert   BFLExpressionParser.isAUnit( "Lb" ) == true ;
       assert   BFLExpressionParser.isAUnit( "mm" ) == true ;
       assert   BFLExpressionParser.isAUnit( "meter" ) == true ;
       assert   BFLExpressionParser.isAUnit( "ft" ) == true ;
       assert   BFLExpressionParser.isAUnit( "inch" ) == true ;
       assert   BFLExpressionParser.isAUnit( "cm" ) == true ;
       assert   BFLExpressionParser.isAUnit( "yard" ) == true ;
       assert   BFLExpressionParser.isAUnit( "mile" ) == true ;
       assert   BFLExpressionParser.isAUnit( "Km" ) == true ;
       assert   BFLExpressionParser.isAUnit( "yards" ) == true ;
       assert   BFLExpressionParser.isAUnit( "inchs" ) == true ;
       assert   BFLExpressionParser.isAUnit( "foot" ) == true ;
       assert   BFLExpressionParser.isAUnit( "feet" ) == true ;
       assert   BFLExpressionParser.isAUnit( "radians" ) == true ;
       assert   BFLExpressionParser.isAUnit( "degrees" ) == true ;
       
       assert   BFLExpressionParser.isAUnit( "thong" ) == false ;
       assert   BFLExpressionParser.isAUnit( "" ) == false ;
       assert   BFLExpressionParser.isAUnit( "     " ) == false ;
    }
    //--------------------------------------------------------------------------
    @Test
    public void testParseIdentifer()
    { 
       System.out.println("testParseIdentifer");
       try 
        { 
            BFLExpressionParser  bfl = fromSource(" hello bob of "); 
            String s =  bfl.parseIdentifier();
            assertEquals("_hello_bob",s);
            bfl = fromSource(" hello bob ( "); 
            s =  bfl.parseIdentifier();
            assertEquals("_hello_bob",s);
            
            bfl = fromSource(" hello    bob  how  ( "); 
            s =  bfl.parseIdentifier();
            assertEquals("_hello_bob_how",s);
        }catch(  ParseError e )
        { 
            System.err.println("TEST EXPRESSION: PARSE ERROR->"+ e);
            fail("Parse Error ->" + e ) ; 
        }
    }
    //--------------------------------------------------------------------------
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
            
            bfl = fromSource("34ft"); 
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
            
            bfl = fromSource("34Meter"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println("######"+ ex.getType()+"##");
            assertEquals( "meter",ex.getType());
            assertEquals("34",ex.getNumberAsText());
            
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
            
            if( true )
            { 
                bfl = fromSource("45°C "); 
                ex = bfl.parseLiteralNumber(); 
                assert ex!= null ; 
                assert ex instanceof LiteralNumberExpression : "NOT literal"; 
                assertEquals( BFLExpressionParser.typeCentigrade  ,  ex.getType() ); 
                assertEquals(ex.getNumberAsText(), "45");
                
                bfl = fromSource("45°Centigrade "); 
                ex = bfl.parseLiteralNumber(); 
                assert ex!= null ; 
                assert ex instanceof LiteralNumberExpression : "NOT literal"; 
                assertEquals( BFLExpressionParser.typeCentigrade  ,  ex.getType() ); 
                assertEquals(ex.getNumberAsText(), "45");
                
                bfl = fromSource("45°F "); 
                ex = bfl.parseLiteralNumber(); 
                assert ex!= null ; 
                assert ex instanceof LiteralNumberExpression : "NOT literal"; 
                System.out.println( ex.getNumberAsText()); 
                assertEquals( BFLExpressionParser.typeFahrenheit  ,  ex.getType() ); 
                assertEquals(ex.getNumberAsText(), "45");
                
                bfl = fromSource("45°Fahrenheit "); 
                ex = bfl.parseLiteralNumber(); 
                assert ex!= null ; 
                assert ex instanceof LiteralNumberExpression : "NOT literal"; 
                System.out.println( ex.getNumberAsText()); 
                assertEquals( BFLExpressionParser.typeFahrenheit  ,  ex.getType() ); 
                assertEquals(ex.getNumberAsText(), "45"); 
            }
            
          /*  
             bfl = fromSource("34foot"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
            
            bfl = fromSource("34feet"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            //System.out.println( ex.getNumberAsText());
            assertEquals(ex.getNumberAsText(), "34");
            
            bfl = fromSource("45radians"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText()); 
            assertEquals(ex.getNumberAsText(), "45");
            
            bfl = fromSource("45degree"); 
            ex = bfl.parseLiteralNumber(); 
            assert ex!= null ; 
            assert ex instanceof LiteralNumberExpression : "NOT literal"; 
            System.out.println( ex.getNumberAsText()); 
            assertEquals(ex.getNumberAsText(), "45"); 
    */
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
    public void testParseFunction() throws Exception
    {
        System.out.print("TEST function "); 
       try 
        { 
            BFLExpressionParser bl=  fromSource( " the sin of 3.14159265359/2.0 " ) ;
            assertNotNull("Expression not returned ",bl );
            GeneralExpression e = bl.parseFactor() ;assertNotNull("Expression not returned ",e);
            BigDecimal d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
            assertEquals(d.toPlainString(), "1");
            
            bl=  fromSource( "sin ( 3.14159265359/2.0 )  " ) ;
            assertNotNull("Expression not returned ",bl );
            e = bl.parseFactor(); assertNotNull("Expression not returned ",e);
            d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
           assertEquals(d.toPlainString(), "1");
           
           bl=  fromSource( "exp ( 30 )  " ) ;
            assertNotNull("Expression not returned ",bl );
            e = bl.parseFactor(); assertNotNull("Expression not returned ",e);
            d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
           assertEquals(d.toPlainString(), "10686474581524.462890625");
           
            bl=  fromSource( "exp( 30 )  " ) ; assertNotNull("Expression not returned ",bl );
            e = bl.parseFactor(); assertNotNull("Expression not returned ",e);
            d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
           assertEquals(d.toPlainString(), "10686474581524.462890625");
           
            bl=  fromSource( "log base ten ( 100 )  " ) ; assertNotNull("Expression not returned ",bl );
            e = bl.parseFactor(); assertNotNull("Expression not returned ",e);
            d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
           assertEquals(d.toPlainString(), "2");
           bl=  fromSource( "log base ten(100)  " ) ; assertNotNull("Expression not returned ",bl );
            e = bl.parseFactor(); assertNotNull("Expression not returned ",e);
            d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
           assertEquals(d.toPlainString(), "2");
           
           bl=  fromSource( "random(100)  " ) ; assertNotNull("Expression not returned ",bl );
           e = bl.parseFactor(); assertNotNull("Expression not returned ",e);
           d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
        }
        catch(  ParseError e )
        { 
           System.out.println("TEST faction of inbuilt function: PARSE ERROR->"+ e);
           fail("TEST faction of inbuilt function : PARSE ERROR->"+e);
        }
       try 
        { 
            BFLExpressionParser bl=  fromSource( " the bob of 3.14159265359/2.0 " ) ;
            assertNotNull("Expression not returned ",bl );
            GeneralExpression e = bl.parseFactor() ;assertNotNull("Expression not returned ",e);
            fail("TEST faction parsed function which did not exist");

            BigDecimal d = e.evaluateCalculation();assertNotNull("Expression not returned ",d);
            assertEquals(d.toPlainString(), "1");
        }
       catch(  ParseError e )
        { 
           System.out.println("Inbuilt function test passed.");
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
            GeneralExpression e = bl.parseTerm(); assertNotNull("Expression not returned ",e);
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
    //--------------------------------------------------------------------------
    @Test
    public void testparsePowerExpression()
    { 
        System.out.print("testparsePowerExpression:");
        try 
        { 
             runSimpleExpression("4 ^ 2 + 1 " ).equals("17");
             runSimpleExpression("3 * 2^2 " ).equals("12"); // make sure operator precidance works OK.
             runSimpleExpression("4 squared + 1" ).equals("17");// 
             runSimpleExpression("2 cubed + 1" ).equals("17");
             runSimpleExpression("4 to the power of 2" ).equals("16");
             runSimpleExpression("4 to the power 2" ).equals("16");
             runSimpleExpression("4 to power 2" ).equals("16");
             runSimpleExpression("4 to power of 2" ).equals("16");
             runSimpleExpression("4 power 2" ).equals("16");
             runSimpleExpression("4 the power 2" ).equals("16");
        }catch(  ParseError e )
        { 
            System.out.println("TEST testparsePowerExpression: PARSE ERROR->"+ e);
            fail("testparseLogicExpression .");
        }
        
        try 
        { 
            runSimpleExpression("4 the to power 2" ).equals("16");
            fail("testparseLogicExpression .");
        }catch(  ParseError e )
        { 
            System.out.println("TEST testparseLogicExpression: Caught miss compile OK");
            // It is good it reah 
        }
    }
    //--------------------------------------------------------------------------
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
    //--------------------------------------------------------------------------
    /**
     *
     * @return true if tests passed. 
     */
    @Test
    public void  testSimpleExpression()
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
            GeneralExpression e = bl.simpleMathExpression();
            
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
            GeneralExpression e = parser.parseFactor(); assert e != null ; 
           
            String r ; 
            r=runSimpleExpression(" ( 90 - 100 )* 8  " ); // test compile 
            r=runSimpleExpression("2+2" ); // test compile 
            
            assertEquals( "4",r);
            r=runSimpleExpression(" | 90 - 100 | " ); // test does it compile 
            assertEquals(r, "10");
            r=runSimpleExpression("30^3");
            assertEquals( "27000",r);
            
            r=runSimpleExpression("30 ^ 3");// possibly caused by something in parse units.
            assertEquals( "27000",r);
            
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
            GeneralExpression e = parser.parseComparisonExpression(); assert e != null ; 
           
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
    //--------------------------------------------------------------------------
    @Test
    public void testParseLogicExpression() 
    { 
        System.out.print("TEST testParseLogicExpression "); 
        BFLExpressionParser parser; 
      GeneralExpression e ; 
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
