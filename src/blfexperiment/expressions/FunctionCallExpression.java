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
    public void addArgument( Expression e)
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
        return BFLExpressionParser.typeFloat ;
    }

    @Override
    public BigDecimal evaluateCalculation()
    {
        System.out.println("function call " +  fullName);
        BigDecimal d= BigDecimal.ZERO;
        for( Expression e:  arguments)
        { 
            d = e.evaluateCalculation();
        }
        switch( fullName )
        { 
            case "_sin" : return  new  BigDecimal( Math.sin( d.doubleValue() ) ); 
            case "_cos" : return  new  BigDecimal( Math.cos( d.doubleValue() ) );
            case "_cosh": return  new  BigDecimal( Math.cosh(d.doubleValue() ) );
            case "_acos": return  new  BigDecimal( Math.sinh(d.doubleValue() ) );
            case "_tan" : return  new  BigDecimal( Math.tan( d.doubleValue() ) );
            case "_random":return  new  BigDecimal( Math.random( ) ).multiply(d );
            default : assert false ; 
        }
       
       assert false ; 
       return null; 
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
