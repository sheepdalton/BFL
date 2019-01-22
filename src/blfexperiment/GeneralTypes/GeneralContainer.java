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

/**
 *<pre>                   GENERAL OBJECT 
 *                              | 
 *                        GENERAL CONTAINER
 *            ------------------|----------------------------------------
 *            |                           |              |              |
 *   GENERAL LIST     GENERAL TEXT    GENERAL SET  GENERAL RANGE     (GEN MAP)
 * 
 * </pre> 
 *  General container has a number of operations which can be applied to it 
 * For example  
 *    Contains (is in)
 *    Itteration ( repeat for each  ) 
 *    primative operations like + applied over the list 
 *    Append 
 *    Remove 
 *    
 *    Union 
 *    Intersection 
 *    Not 
 *    Exclusive Intersection 
 *     - For example You can find intersection of both Set and Range 
 * 
 *   
 * 
 * @author Sheep Dalton
 */
public interface GeneralContainer extends GeneralObject
{
    
}
