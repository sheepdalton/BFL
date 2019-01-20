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
public interface NumericExpression extends Expression 
{
    //--------------------------------------------------------------------------
    /**
     *  This is more of a convenicance fuction but it makes sure all numeric cals are OK.
     * @return 
     */
    //--------------------------------------------------------------------------
    
    default GeneralObject doIt()
    { 
        return new GeneralNumber( evaluateCalculation()) ; //FYI this is slow
    }
    //--------------------------------------------------------------------------
    default boolean isANumber(){ return true ; } 
    
    //--------------------------------------------------------------------------
}
