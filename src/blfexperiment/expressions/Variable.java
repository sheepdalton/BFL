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
import java.util.Objects;

/**
 *
 * @author Sheep Dalton
 */
   //---------------------------------------------------------------------------
   public class Variable
   { 
       String normailsedform ; 
       String type ; //
       BigDecimal value ; // should really be object.  
       //-----------------------------------------------------------------------
       public Variable(String name)
        {
            this.normailsedform = name.intern();// canoncical versoin save space
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }
        
        public String getVarName(){ return normailsedform; }  
    /**
     *  This is mostly for debugging perposes. This holds the actual 
     *  numeric value should be a GeneralObject which can be either 
     *  object , text or number this is convenciance function. 
     * @return 
     */
    public BigDecimal getValue()
    {
        return value;
    }

    public void setValue(BigDecimal value)
    {
        this.value = value;
    }
        
      

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.normailsedform);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Variable other = (Variable) obj;
        if (!Objects.equals(this.normailsedform, other.normailsedform))
        {
            return false;
        }
        return true;
    }
       
   }
