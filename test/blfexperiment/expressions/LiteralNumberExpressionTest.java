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

import blfexperiment.BFLExpressionParser;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sheep Dalton
 */
public class LiteralNumberExpressionTest
{
    public LiteralNumberExpressionTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }

    /**
     * Test of getNumberAsText method, of class LiteralNumberExpression.
     */
    @Test
    public void testGetNumberAsText()
    {
        System.out.println("getNumberAsText");
        LiteralNumberExpression instance = new LiteralNumberExpression("1.0");
        String expResult = "1.0";
        String result = instance.getNumberAsText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class LiteralNumberExpression.
     */
    @Test
    public void testGetType()
    {
        System.out.println("getType");
        LiteralNumberExpression instance =  new LiteralNumberExpression("1.0");
        String expResult = BFLExpressionParser.typeFloat;
        String result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setType method, of class LiteralNumberExpression.
     */
    @Test
    public void testSetType()
    {
        System.out.println("setType");
        String type = "";
        LiteralNumberExpression instance =  new LiteralNumberExpression("1.0");
        instance.setType(BFLExpressionParser.typeEuro);
        // TODO review the generated test code and remove the default call to fail.
         String result = instance.getType();
        assertEquals(BFLExpressionParser.typeEuro, result);
    }

    /**
     * Test of evaluateCalculation method, of class LiteralNumberExpression.
     */
    @Test
    public void testEvaluateCalculation()
    {
        System.out.println("evaluateCalculation");
        LiteralNumberExpression instance = new LiteralNumberExpression("1");
        BigDecimal expResult = BigDecimal.ONE;
        BigDecimal result = instance.evaluateCalculation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
}