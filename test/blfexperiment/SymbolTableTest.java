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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sheep Dalton
 */
public class SymbolTableTest
{
    
    public SymbolTableTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }

    /**
     * Test of getParent method, of class SymbolTable.
     */
    @Test
    public void testGetParent()
    {
        System.out.println("getParent");
        SymbolTable parent = new SymbolTable( null); 
        SymbolTable instance = new SymbolTable(  parent);
        SymbolTable expResult = parent;
        SymbolTable result = instance.getParent();
        assertEquals(expResult, result);
      
        
    }

    /**
     * Test of getOrMakeIfNull method, of class SymbolTable.
     */
    @Test
    public void testGetOrMakeIfNull()
    {
        System.out.println("getOrMakeIfNull");
        String normliaseVarName = "this_is_Test_";
        SymbolTable instance = new SymbolTable( null);
        
        Variable result = instance.getOrMakeIfNull(normliaseVarName);
        assertNotNull("Its null",  result ); 
       // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
            @@@TODO - finish symbol table
     * Test of add method, of class SymbolTable.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
        
        String wordNormalised = "hello_this_is_test_";
        SymbolTable instance = new SymbolTable( null);
        Variable result = instance.add(wordNormalised);
        boolean result2 = instance.contains(wordNormalised);
        assertEquals(true, result2);
        boolean result3 = instance.contains("bob_");
        assertEquals(false, result3);
    }
    /**
     *   testSimilarLocalName we check.
     */
    @Test 
    public void testSimilarLocalName()
    { 
        System.out.print("similarLocalName"); 
        SymbolTable instance = new SymbolTable( null);
        String c1 = "_the_current"; 
        String c2 = "_the_curent";
        instance.add(c1); 
        String s = instance.similarLocalName(c2); 
        assert(s!=null);
        assertEquals(s,c1);
        System.out.println("SIMILAR + " + s); 
        
        s = instance.similarLocalName("thething"); 
        System.out.println("SIMILAR + " + s);
        assertEquals(s,null);
    }

//    /**
//     * Test of normalise method, of class SymbolTable.
//     */
//    @Test
//    public void testNormalise()
//    {
//        System.out.println("normalise");
//        String s = "";
//        SymbolTable instance = null;
//        String expResult = "";
//        String result = instance.normalise(s);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of contains method, of class SymbolTable.
//     */
//    @Test
//    public void testContains()
//    {
//        System.out.println("contains");
//        String normliaseVarname = "";
//        SymbolTable instance = null;
//        boolean expResult = false;
//        boolean result = instance.contains(normliaseVarname);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of similarLocalName method, of class SymbolTable.
//     */
//    @Test
//    public void testSimilarLocalName()
//    {
//        System.out.println("similarLocalName");
//        String normliaseVarname = "";
//        SymbolTable instance = null;
//        String expResult = "";
//        String result = instance.similarLocalName(normliaseVarname);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of thisIsStartOfOneOfYourStrings method, of class SymbolTable.
     */
    @Test
    public void testThisIsStartOfOneOfYourStrings()
    {
        System.out.println("thisIsStartOfOneOfYourStrings");
        String wordNormalised = "hello_this_is_test_";
        SymbolTable instance = new SymbolTable( null);
        Variable result = instance.getOrMakeIfNull(wordNormalised);
        boolean present1 = instance.thisIsStartOfOneOfYourStrings("hello_");
        assertEquals(true, present1);
        boolean present2 = instance.thisIsStartOfOneOfYourStrings("bob_");
        assertEquals(false, present2);
        // TODO review the generated test code and remove the default call to fail.
       
    }
    
}
