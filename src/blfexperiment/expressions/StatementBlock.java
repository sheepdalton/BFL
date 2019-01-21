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

import java.util.*;

/**
 *
 * @author Sheep Dalton
 */
public class StatementBlock  implements Statement 
{
    List<Statement>  statements  = new ArrayList<>(); 
    
    /**
     *
     */
    public StatementBlock(){ } 

    /**
     *
     * @param s
     */
    public void add( Statement s  ){ assert s!=null ; statements.add(s); } 
    //--------------------------------------------------------------------------
    /**
     *  Bring your questions see our answers . challange them make better ones. 
     * We now need some context for the variables  
     * @return 
     */
    @Override
    public GeneralObject evaluteStatement()
    {
        GeneralObject result = null ;
        for(  Statement  s : statements ) 
        { 
           result = s.evaluteStatement(); 
        }
        return result ; 
    }

    /**
     *
     * @return
     */
    @Override 
     public String getType() {  return "void";  }

}
