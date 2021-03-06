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
public class ArrayIndex extends UnariyExpression
{
    String newVariableName; 
   Variable linkToVariable; 

    /**
     *
     * @param newVariableName
     * @param linkToVariable
     * @param e
     */
    public ArrayIndex(String newVariableName, Variable linkToVariable,
                                GeneralExpression e)
    {
        super('[', e);
        this.newVariableName = newVariableName;
        this.linkToVariable = linkToVariable;
    }
    //--------------------------------------------------------------------------
      public BigDecimal evaluateCalculation()
    {
        assert linkToVariable != null;
        return linkToVariable.getNumericValue();
    }
    //--------------------------------------------------------------------------

    /**
     *
     * @return
     */
    public Variable getVariable()
    {
        return linkToVariable;
    }
   
            
}
