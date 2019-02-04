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
 *  holds literal expression like 
 *  [ 1 ; 2 ; 3 ; 4; 5; 6 ; 7 ] 
 * @author Sheep Dalton
 */
public class LiteralListExpression implements Expression 
{
    List<Expression> listOfExpressions ; 
    public LiteralListExpression()
    { 
     listOfExpressions = new ArrayList<>(); 
    }
    public void add( Expression e )
    { 
      // assert (listOfExpressions! = null : " null listOfExpressions "); 
       listOfExpressions.add(e); 
    }
    @Override
    public String toHumanString()
    {
        return Expression.super.toHumanString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String converToSource()
    {
        return Expression.super.converToSource(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getType()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GeneralObject doIt()
    {
        return Expression.super.doIt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BigDecimal evaluateCalculation()
    {
        return Expression.super.evaluateCalculation(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isANumber()
    {
        return false;  //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean evaluateLogic()
    {
        return Expression.super.evaluateLogic(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean purelyLogic()
    {
        return  false ;  //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isQuestion()
    {
        return  false ;  //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCompatable(Expression other)
    {
        return Expression.super.isCompatable(other); //To change body of generated methods, choose Tools | Templates.
    }

}