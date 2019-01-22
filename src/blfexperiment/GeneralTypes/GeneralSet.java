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

import java.util.*;

/**
 * Implements a set like system. Can be for numbers or strings or anything 
 * which declares equals
 * @author Sheep Dalton
 */
public class GeneralSet  implements GeneralContainer  
{
    Set<GeneralObject> info ; 
    
@Override 
    public boolean isSet(){ return true  ; }
@Override 
    public String getType()
    { 
        return "Set of things"; 
    }
}
