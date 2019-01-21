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
 *  The General Object is the parent all classes. 
 *  Defines the minimal number of things something must do.
 * @author Sheep Dalton
 */
public interface GeneralObject
{

    /**
     *
     * @return
     */
    default String getType()
    { 
        return "void"; 
    }
   /**
    * Can I take part in calcuations ? 
    * @return 
    */
    
    default boolean isNumber(){ return false ; } 

    /**
     *
     * @return
     */
    default boolean isQuestion(){ return false ; } 

    /**
     *
     * @return
     */
    default boolean isText(){ return false ; }

    /**
     *
     * @return
     */
    default boolean isList(){ return false ; }

    /**
     *
     * @return
     */
    default boolean iDate(){ return false ; }
    /**
     * All objects have attributes even built in objects.
     * attributes can be virtual = that is the result of a formula or calculation.
     * universal attributes are 
     *      object's type 
     *      object's text -- like to String.
     *      object's as ( type ) -- conversion.
     * @param attributeName
     * @return 
     */ 
    default GeneralObject getAttribute( String attributeName)
    { 
        if( attributeName.equalsIgnoreCase("type"))
                        return new GeneralText(getType()) ; 
        return null ; 
    }
    
    
}
