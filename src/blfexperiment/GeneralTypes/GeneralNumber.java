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

package blfexperiment.GeneralTypes;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Sheep Dalton
 */
public class GeneralNumber extends BigDecimal implements GeneralObject 
{

    /**
     *
     * @param other
     */
    public GeneralNumber(BigDecimal other)
    {
        super(other.toString()); // this is slow for a copy. 
        //super(other.unscaledValue() ,  
                //other.val, other.scale, other.prec) ; 
    }
  
    /**
     *
     * @param numberFromThisString
     */
    public GeneralNumber( String numberFromThisString )
    { 
        super( numberFromThisString ); 
    }
    
    /**
     *
     * @return
     */
    @Override 
    public  String getType()
    { 
        return "number"; 
    }
    @Override 
    public  boolean isNumber(){ return true  ; }  

}
