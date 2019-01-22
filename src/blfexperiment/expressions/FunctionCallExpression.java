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

import blfexperiment.GeneralTypes.GeneralObject;
import java.math.BigDecimal;
import java.util.*;

/**
 * This is the place a function is CALLED. Currently can only handle built in
 * functions. ( Future versions will have pointer to Expression tree of functions).
 * 
 * @author Sheep Dalton
 */
public class FunctionCallExpression implements NumericExpression
{
    String fullName; // name possibly with spaces. 
    int howManyArguments =1 ; 
    int howManyResults   =1; // in the future I can return list of results.
    List<Expression> arguments = new ArrayList<Expression>(4); // default to 4 argument 
    
    public FunctionCallExpression( String fullNameWithSpaces)
    { 
        fullName = fullNameWithSpaces ; 
    }
    void addArgument( Expression e)
    { 
        arguments.add(e);
    }
    @Override
    public GeneralObject doIt()
    {
        return NumericExpression.super.doIt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isANumber()
    {
        return NumericExpression.super.isANumber(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toHumanString()
    {
        return NumericExpression.super.toHumanString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String converToSource()
    {
        return NumericExpression.super.converToSource(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getType()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BigDecimal evaluateCalculation()
    {
        return NumericExpression.super.evaluateCalculation(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean evaluateLogic()
    {
        return NumericExpression.super.evaluateLogic(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean purelyLogic()
    {
        return NumericExpression.super.purelyLogic(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isQuestion()
    {
        return NumericExpression.super.isQuestion(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCompatable(Expression other)
    {
        return NumericExpression.super.isCompatable(other); //To change body of generated methods, choose Tools | Templates.
    }

}
