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

/**
 *
 * @author Sheep Dalton
 */
public class LiteralBoolean extends LiteralNumberExpression
{
    
    boolean myBooleanValue = false ; 
   
   
    //--------------------------------------------------------------------------

    /**
     *
     * @param boolAsText
     */
    public LiteralBoolean( String boolAsText )
    {
       super( (boolAsText.equalsIgnoreCase("YES")|| boolAsText.equalsIgnoreCase("TRUE"))? "1":"0" ); 
       this.type = BFLExpressionParser.typeQuestion;
       
       if( boolAsText.equalsIgnoreCase("YES")|| boolAsText.equalsIgnoreCase("TRUE"))
       {   
           myBooleanValue = true ; 
           return ; 
       }
       if( boolAsText.equalsIgnoreCase("NO")|| boolAsText.equalsIgnoreCase("FALSE") ||
               boolAsText.equalsIgnoreCase("0"))
       { 
           this.numberAsText ="0"; 
           myBooleanValue = false  ; 
           return ; 
       }
       assert false :  " don't undertand boolAsText "+ boolAsText  ; 
    }
    //--------------------------------------------------------------------------

    /**
     *
     * @param boolAsText
     * @param type
     */
    public LiteralBoolean( String boolAsText , String type )
    {
       super( (boolAsText.equalsIgnoreCase("YES")|| boolAsText.equalsIgnoreCase("TRUE"))? "1":"0" ); 
        
       if( boolAsText.equalsIgnoreCase("YES")|| boolAsText.equalsIgnoreCase("TRUE"))
       { 
           myBooleanValue = true ; 
           return ; 
       }
       if( boolAsText.equalsIgnoreCase("NO")|| 
               boolAsText.equalsIgnoreCase("FALSE") ||
               boolAsText.equalsIgnoreCase("0"))
       { 
           myBooleanValue = false  ; 
       }
       this.type = type.intern();
    }
    
    //--------------------------------------------------------------------------
    /**
     *  Literal returns the typeNumber
     * @return 
     */
    @Override 
    public BigDecimal evaluateCalculation()
    { 
        if( myBooleanValue == true ) return BigDecimal.ONE; 
        return BigDecimal.ZERO ; 
    }
     @Override 
    public  boolean evaluateLogic()
    { 
        return myBooleanValue ; 
    }
    /**
     *  prefer to use evaluateLogic. 
     * @return 
     */
     @Override 
      public boolean purelyLogic(){ return true ; }
      
    @Override
    public boolean isANumber()
    {
        return false;  //To change body of generated methods, choose Tools | Templates.
    }
    /**
     *
     * @return
     */
    @Override 
      public boolean isQuestion() 
      { return  getType().equals(BFLExpressionParser.typeQuestion); } 
     
    //--------------------------------------------------------------------------
    @Override 
    public String toString()
    { 
        return "literal boolean " +  (myBooleanValue?"YES":"NO")  + " " + type ; 
    }
    //--------------------------------------------------------------------------
    

}
