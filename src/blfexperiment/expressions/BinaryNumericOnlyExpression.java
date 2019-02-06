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
import java.math.MathContext;

/**
 *
 * @author Sheep Dalton
 */
public class BinaryNumericOnlyExpression implements NumericExpression 
{
    NumericExpression before;
    NumericExpression after;
    int operator  ;
    
    //--------------------------------------------------------------------------
    public BinaryNumericOnlyExpression( int operator , 
                            NumericExpression left  , NumericExpression right )
    { 
        assert isBinaryOperator( operator )== true ;
        this.operator  = operator; 
        this.before = left ; 
        this.after = right ; 
    }
    //--------------------------------------------------------------------------
    public static boolean isBinaryOperator( int operator )
    { 
        switch (  operator )
        { 
            case '+' :return true ; 
            case '-': return true ;  
            case '*' : case '\u00D7': return true ;  
            case '/':case '÷': return true ; 

           case '^':  return true ; 
           default:  return false ; 
       }
    } 
    //--------------------------------------------------------------------------  
    /**
     *  isANumber is true by defintion 
     * @return true 
     */
    @Override
    public boolean isANumber()
    {
        return true ; 
    }
    //--------------------------------------------------------------------------
    /**
     *  This accelerates calcuations when both sides are numeric. 
     * @return 
     */
    @Override
    public BigDecimal evaluateCalculation()
    { 
     assert   before != null :"No left";
     assert after != null : "No right"; 
     
     BigDecimal  beforeVal = before.evaluateCalculation();
     BigDecimal rigthVal = after.evaluateCalculation();
     
    
    // System.out.println(".... " + before.toString() + "].." );
    // System.out.println("r... [" + after.toString() + "]..");
     
        switch (  operator )
        { 
            case '+' : {  BigDecimal result = beforeVal.add(rigthVal); return result ;} 
            case '-': { BigDecimal result = beforeVal.subtract(rigthVal); return result;} 
            case '*' : case '\u00D7': { BigDecimal result = beforeVal.multiply(rigthVal); return result;} 
            case '/':case '÷': 
            { 
                return  beforeVal.divide(rigthVal, MathContext.DECIMAL128);
            } 
           case '^': 
           { 
                int power = rigthVal.intValue(); 
                BigDecimal result = beforeVal.pow(power); 
                return result ; 
           }
           default:  assert false ; 

       }
     assert false ; 
     return null ; 
    }
    
    @Override 
    public String getType() 
    { 
        String myType = "void";
   
        assert isBinaryOperator(operator  )== true ; 
        
        if(  before != null )myType = before.getType();
        
        if(  after != null )
        { 
            String rightType = after.getType();
            
            //System.out.printf( "COMPARE '%s' to '%s' \n", myType , rightType);
            //System.out.printf( " Units %b %b\n" , BFLExpressionParser.isAUnit( rightType ) ,  BFLExpressionParser.isAUnit( myType )); 
             if( BFLExpressionParser.isACurrency( myType ) &&
                     BFLExpressionParser.isACurrency(rightType) && 
                           rightType.equalsIgnoreCase(myType)== false   )
             { 
                return "Error:  Cannot combine types "+ myType+" does not match  "
                        +  rightType+ ". Use conversion with an exchange rate.  ";  
             }
                     
             if( BFLExpressionParser.isACurrency( myType ) &&  
                                            isRawNumberType( rightType ))
             {
                 return myType; 
             }
            if( BFLExpressionParser.isACurrency(rightType ) &&  
                                            isRawNumberType(myType ))
            {
                 return rightType; 
            }
            if( operator == '*' || operator == '\u00D7' || 
                operator == '/' ||  operator  == '÷'   || 
                    operator == '^') 
            { 
                if( BFLExpressionParser.isACurrency(myType ) && 
                    BFLExpressionParser.isACurrency(rightType ) )
                { 
                    return "Error: Multiplying or dividing "+ myType+" by " +  rightType+ " does not make sense lifeform"; 
                }
                
                /// IF THE OTHER TYPE IS ARRAY/VECTOR or LIST the
                //  AND the other operator is single typeNumber then 
                //  we construct a new list with every item 
                // 
                // IF other is list of same size then inividual multiply. 
                // 
                // IF the other is Matrix then multiply matrix 
                // 
                // Not sure what power would do 
            }
            
           
            if( rightType.equals(myType))return myType; // int == int , float == float
            
            if(  myType.equals(BFLExpressionParser.typeInt) && rightType.equals(BFLExpressionParser.typeFloat) )// type promtion to more general. 
            { 
                return BFLExpressionParser.typeFloat;
            }
            if(  myType.equals(BFLExpressionParser.typeFloat ) && rightType.equals(BFLExpressionParser.typeInt) )
            { 
                return BFLExpressionParser.typeFloat;
            }
            if(  myType.equals(BFLExpressionParser.typeInt) &&   BFLExpressionParser.isAUnit( rightType )) return rightType; 
            if(  myType.equals(BFLExpressionParser.typeFloat) &&   BFLExpressionParser.isAUnit( rightType )) return rightType; 
           
            if(  rightType.equals(BFLExpressionParser.typeInt) &&   BFLExpressionParser.isAUnit( myType )) return rightType; 
            if(  rightType.equals(BFLExpressionParser.typeFloat) &&   BFLExpressionParser.isAUnit( myType )) return rightType;
                   
            if(  BFLExpressionParser.isAUnit( rightType ) &&  BFLExpressionParser.isAUnit( myType ) )// don't need rightType.not equal to myType
            { 
                return "Error: " + myType + " does not match " + rightType ; 
            }
            
        }
        return myType ; 
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param s - string of type
     * @return 
     */
    public boolean isRawNumberType( String s )
    { 
        return   s.equals(BFLExpressionParser.typeInt) ||  s.equals(BFLExpressionParser.typeFloat) ; 
    }
    
}
