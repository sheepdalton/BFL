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
import java.lang.ArithmeticException; 

/**
 *  UnariyExpression 
 *  performs unary expressions like 
 *  | absolute value 
 *  - negate 
 * @author Sheep Dalton
 */
public class UnariyExpression  implements NumericExpression
{
    NumericExpression left  ;
    int operator  ;// ¬ is possible 
    
    //--------------------------------------------------------------------------

    /**
     *
     * @param operator
     * @param e
     */
    public UnariyExpression( int operator   ,  NumericExpression e )
    { 
        this.operator = operator;
        left = e ; 
    }
    //--------------------------------------------------------------------------
    @Override 
    public String toString()
    { 
       return  " " + (char)this.operator + " "+ left.toString()  ; 
    }
    //--------------------------------------------------------------------------
    /* 
     * depending upon the type we get more complex calcuations for exmaple 
       if unit is Meter 2 ( meters squared ) then Square root gives meters 
       The type system is waiting for version 2. When we know what units Meters 0.5 means. 
    */

    /**
     *
     * @return
     */

    @Override 
    public String getType() 
    { 
       // if operator = ¬ then return question 
        if( operator == '√' && left.isANumber()) return BFLExpressionParser.number;  
        
        if( isQuestion() )
        { 
  
        return BFLExpressionParser.typeQuestion ; // "Question";
        } 
        
        return left.getType();
    }
    //--------------------------------------------------------------------------

    /**
     *
     * @return
     */
    @Override public boolean isQuestion( ) 
    { 
        if( operator == '¬' )
        {   
           return true ; 
        } 
        return false ; 
    }
    //--------------------------------------------------------------------------
    /**
     * Computes doIt 
     * @return 
     */
    @Override 
    public BigDecimal evaluateCalculation()
    { 
     assert   left != null :"No left";
     BigDecimal leftVal = left.evaluateCalculation();
     if( operator == '|') return leftVal.abs();
     if(  operator == '-') return leftVal.negate();
     if( operator == '√')
     { 
         if( leftVal.signum()== -1 )throw new java.lang.ArithmeticException("cannot find square root of negative number");
         return  new  BigDecimal( Math.sqrt( leftVal.doubleValue()) );
     }
     if(  operator == '¬' )
     { 
         if( left.evaluateLogic()) return BigDecimal.ZERO; 
         return BigDecimal.ONE; 
     }
     assert false  ; // unreachable 
      
     return leftVal; 
    }
    //--------------------------------------------------------------------------
    public  boolean evaluateLogic()
    { 
        return ! evaluateLogic(); 
    }
    //--------------------------------------------------------------------------   
}
