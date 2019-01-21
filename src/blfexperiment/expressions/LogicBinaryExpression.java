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
public class LogicBinaryExpression extends BinaryExpression
{
    
    /**
     *
     * @param operator
     */
    public LogicBinaryExpression(int operator)
    {
        super(operator);
    }

    /**
     *
     * @param operator
     * @param b
     * @param after
     */
    public LogicBinaryExpression(int operator , NumericExpression b , NumericExpression after )
    {
        super(operator, b ,after );
    }
    //--------------------------------------------------------------------------
    public boolean purelyLogic(){ return true ; } 

    /**
     *
     * @return
     */
    public boolean isQuestion( ) { return true ; } 
    //--------------------------------------------------------------------------
    /** 
     *  This code is not called as much as we woudl like in the unit tests. 
     * @return 
     */
    protected boolean processLogic()
    { 
        //@@@ TODO call this code un unit tests.
          boolean beforeVal  = before.evaluateLogic(); 
          boolean afterVal   = after.evaluateLogic();
          //System.out.println(">>>>PROCESSING LOGIC!!!");
          
          switch (  operator )
            { 
              
              case '=':  return beforeVal == afterVal ; // same for nearly equal
              case '>': return (beforeVal==true?1:0) > (afterVal==true?1:0) ;
              case '<': return (beforeVal==true?1:0) < (afterVal==true?1:0) ;
              
              case '≠': return beforeVal != afterVal ; // same for nearly equal
              //case <=  
              case '≥': return (beforeVal==true?1:0) >= (afterVal==true?1:0) ;
              case '&': return beforeVal && afterVal;
              case '|': return beforeVal || afterVal;
              //@@@ TODO - restof the logic operators. 
              default : assert false ; 
             }
          assert false ; 
          return false ; 
    }
    //--------------------------------------------------------------------------
    /** 
     *   This code is called when we are a logical parse tree but we have NON 
     * logical sub items ( for example we return true but argyments are numbers.
     * 
     * @return 
     */
    public boolean evaluateLogic()
    { 
        assert  before != null :"No BEfore";
        assert  after != null : "No After"; 
        
        if(  before.purelyLogic() &&   before.purelyLogic() )return processLogic(); 

        BigDecimal leftVal = before.evaluateCalculation();
        BigDecimal rigthVal = after.evaluateCalculation();

       switch (  operator )
       { 
        case '=': 
        { 
            //System.out.println("= LEfT  " + leftVal + "  R = "+ (rigthVal)); 
            assert ( BigDecimal.ONE.compareTo(BigDecimal.ONE) == 0 ) ; 
            assert ( BigDecimal.ZERO.compareTo(BigDecimal.ONE ) == -1 ) ; 
            
            //System.out.println("= evL @@ " + leftVal.compareTo(rigthVal)  );
            if( leftVal.compareTo(rigthVal) == 0 )
            { 
                //System.out.println("RETURN 1  " ); 
                return true ;
            }
            //System.out.println("RETURN 0  " ); 
            return false; 
        } 
        case '>': 
        { 
           if( leftVal.compareTo(rigthVal) >  0 )
            { 
                //System.out.println("RETURN 1 >   " ); 
                return true;
            } return false ; 
        }
        case '≥': 
        { 
           if( leftVal.compareTo(rigthVal) >=  0 )
            { 
                //System.out.println("RETURN 1 >=   " ); 
                return true;
            } return false ; 
        }
        case '<': 
        { 
         return ((leftVal.compareTo(rigthVal) == -1) ? true:false); 
        } 
        case '≠': 
        { 
         return ((leftVal.compareTo(rigthVal) != 0) ? true:false); 
        }
         
       default: assert false : " " + operator + " not implmented BinaryExpression " ; 
     }// end switch 
    return false ; 
    }// end method.  
    
    @Override 
    public String getType() 
    { 
        String myType = "void";
        //assert false ;  // looks like this code is never called.
       
        switch (  operator )
       { 
            case '&': case'|':
        case '~':
        case '=':
        case '>':
        case '≥': 
        case '<': 
        case '≠': 
        {  
         assert before != null ; 
         myType = before.getType();
         assert after != null ; 
         String afterType = after.getType();
        
         if(  myType.equalsIgnoreCase(afterType)) return BFLExpressionParser.typeQuestion; 
         assert false ; 
        } 
       default: assert false : " " + operator + " " + (char)operator+ " not implmented BinaryExpression " ; 
     }// end switch
       
        assert false ; 
        if( isQuestion() ) return BFLExpressionParser.typeQuestion ; // "Question";
        return BFLExpressionParser.typeQuestion ;
    } 
    
}
