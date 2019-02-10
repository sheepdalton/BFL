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

import blfexperiment.GeneralTypes.GeneralList;
import blfexperiment.GeneralTypes.GeneralObject;
import java.math.BigDecimal;
import java.util.*;

/**
 *  holds literal expression like 
 *  [ 1 ; 2 ; 3 ; 4; 5; 6 ; 7 ] 
 * @author Sheep Dalton
 */
public class LiteralListExpression implements GeneralExpression 
{
    List<Expression> listOfExpressions ; 
    public LiteralListExpression()
    { 
     listOfExpressions = new ArrayList<>(); 
    }
    //--------------------------------------------------------------------------
    /**
     * adds an expression e. 
     * @param e 
     */
    public void add( Expression e )
    { 
      // assert (listOfExpressions! = null : " null listOfExpressions "); 
       listOfExpressions.add(e); 
    }
    //--------------------------------------------------------------------------
    @Override
    public String getType()
    {
        if( listOfExpressions.size() == 0 )return "List of Empty";
        return "List of " + listOfExpressions.get( listOfExpressions.size()-1).getType() ;
    }
    //--------------------------------------------------------------------------
    @Override
    public GeneralObject doIt()
    {
        GeneralList gl = new GeneralList(); 
        for( Expression e: listOfExpressions )
        { 
            gl.add( e.doIt()); 
        }
        return gl;    //To change body of generated methods, choose Tools | Templates.
    }
    //--------------------------------------------------------------------------
    @Override public boolean isList(){ return true ; }
    //--------------------------------------------------------------------------
    public BigDecimal evaluateCalculation( )
    { 
        assert false ; 
        if( listOfExpressions.size() == 0 )return null;
        
        return listOfExpressions.get(listOfExpressions.size()-1).evaluateCalculation() ;
    }
    //--------------------------------------------------------------------------
    @Override
    public boolean isCompatable(Expression other)
    {
        if(! (other instanceof LiteralListExpression) )return false ;
        LiteralListExpression le = (LiteralListExpression)other; 
        for( int i = 0 ; i <  listOfExpressions.size() ; i++ )
        { 
          if( !listOfExpressions.get(i).isCompatable( listOfExpressions.get(i)))
              return false ;     
        }
        return true ; //To change body of generated methods, choose Tools | Templates.
    }
    //--------------------------------------------------------------------------
    /*
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

    */

}
