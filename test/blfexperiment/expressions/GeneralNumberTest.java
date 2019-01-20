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
package blfexperiment.expressions;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sheep Dalton
 */
public class GeneralNumberTest
{
    
    public GeneralNumberTest()
    {
        
    }
    
    @Before
    public void setUp()
    {
    }
    //--------------------------------------------------------------------------
    @Test 
    public void testAddition()
    { 
        System.out.println("Addition"); 
        GeneralNumber instance = new GeneralNumber("10000") ;
        GeneralNumber instance2 = new GeneralNumber("10000") ;
        //GeneralNumber instance3  = instance.add(instance2); 
    }
    //--------------------------------------------------------------------------
    /**
     * Test of getType method, of class GeneralNumber.
     */
    @Test
    public void testGetType()
    {
        System.out.println("getType");
        GeneralNumber instance = new GeneralNumber("10000") ;
        String expResult = "number";
        String result = instance.getType();
        assertEquals(expResult, result); 
    }
    //--------------------------------------------------------------------------
    /**
     * Test of isNumber method, of class GeneralNumber.
     */
    @Test
    public void testIsNumber()
    {
        System.out.println("isNumber");
        GeneralNumber instance = new GeneralNumber("10000") ;
        boolean expResult = true;
        boolean result = instance.isNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
