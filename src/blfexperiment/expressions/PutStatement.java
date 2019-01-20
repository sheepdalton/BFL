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

import java.math.BigDecimal;

/**
 *
 * @author Sheep Dalton
 */
public class PutStatement implements Statement 
{
    public enum HowToPut  
    {
       NON,  INTO, AFTER, BEFORE 
    };
    NumericExpression e  ; 
    HowToPut   how  = HowToPut.NON ; 
    VariableSetExpression variable; // if null means print out. 
    
    public PutStatement(  NumericExpression doThis ) 
    { 
        assert doThis != null ; 
        e = doThis; 
    }
    //--------------------------------------------------------------------------
    public PutStatement(  NumericExpression doThis ,  HowToPut how ,
                VariableSetExpression target)
    { 
       assert doThis != null ; 
       e = doThis;
       this.how = how ; 
       this.variable = target ; // Target can be null if just put
    }
    //--------------------------------------------------------------------------     
    /**
     * 
     * @return 
     */
    @Override
    public GeneralObject evaluteStatement()
    {
      BigDecimal d = e.evaluateCalculation();
      if( variable == null  )
      { 
          System.out.println( "#### PUT " + d.toPlainString());
      } 
      else
      { 
         System.out.println( "###### Setting  " + 
                 variable.getVariable().getVarName() + 
                 " -> " + d.toPlainString());
         
         variable.getVariable().setValue( d ); 
      }
      return null ; 
    }
    //--------------------------------------------------------------------------
}
