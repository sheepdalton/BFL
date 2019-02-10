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
import java.math.MathContext;
import java.util.*;

/**
 *
 * @author Sheep Dalton
 */
public class GeneralList  implements GeneralContainer 
{
    List<GeneralObject> contents ; 
    String myTypeContends =   null;// null means list of objects 
    
    //--------------------------------------------------------------------------
    public GeneralList()
    { 
       contents =  new ArrayList<>(); 
    }
    //--------------------------------------------------------------------------
    public int count()
    { 
        return contents.size();
    }
    //--------------------------------------------------------------------------
    public  void add( GeneralObject go )
    { 
        if(  myTypeContends == null )
        { 
            myTypeContends = go.getType(); // will return list of 
        }
        ///@@@ TODO add check
        contents.add(go); 
    }
    //--------------------------------------------------------------------------
    public GeneralList apply( int operator , BigDecimal n )
    { 
        GeneralList l = new GeneralList(); 
        for(  GeneralObject item:  contents )
        { 
            if( item.isNumber())
            { 
               assert item instanceof  GeneralNumber ; 
               GeneralNumber other = (GeneralNumber) item; 
               switch( operator )
               { 
                   case  '+': 
                       {  
                          BigDecimal result = n.add(other); 
                          GeneralNumber thing = new GeneralNumber( result ); 
                          l.add( thing );
                       }break; 
                   case '*' : case '\u00D7': 
                       {  //
                          BigDecimal result = n.multiply(other); 
                          GeneralNumber thing = new GeneralNumber( result ); 
                          l.add( thing );
                       }break;
                   case  '-': 
                       {  
                          BigDecimal result = other.subtract(n); 
                          GeneralNumber thing = new GeneralNumber( result ); 
                          l.add( thing );
                       }break;
                   case  '/': 
                       {  
                          BigDecimal result = other.divide(n, MathContext.DECIMAL128);
                          GeneralNumber thing = new GeneralNumber( result ); 
                          l.add( thing );
                       }break;
                        
                   //@@@ TODO handle the others
                       default: assert false ; 
               }
            }else 
            { 
                // @@@ TODO if other is isntalce of list apply to list
                l.add(item);
            }
        }
        return l ; 
    }
    //--------------------------------------------------------------------------
    /* 
     * for vectors - we get a number of items.
     *   sum -> returns the sum of items 
     *   count -> number of items 
     *   average 
    */
    @Override 
   public   GeneralObject getAttribute( String attributeName)
    { 
       switch( attributeName )
       { 
           case "count" : assert false ; break ; 
           case "sum": assert false ; break ; 
           case "average" : assert false ; break ; 
           case "mean" : assert false ; break ; 
           case "geomean" : assert false ; break ;
           case "mode" : assert false ; break ; 
           case "median" : assert false ; break ; 
           case "min" : assert false ; break ; 
           case "max" : assert false ; break ; 
           case "stdev" : assert false ; break ; 
           case "sumsq" : assert false ; break ; 
       }
       return GeneralContainer.super.getAttribute(attributeName ); 
    }
    //--------------------------------------------------------------------------
    @Override
    public String toString()
    {
        String s = null ; 
        for( GeneralObject go: contents )
        { 
            if(  s == null )
                s = "[ "+ go.toString(); 
            else 
                s = s + " ; "+ go.toString();
        }
        return s + "]"; 
    }
    //--------------------------------------------------------------------------
    /**
     *
     * @return
     */
    @Override 
    public  String getType()
    { 
        if( myTypeContends == null )return "listOf Things"; 
        return "listOf " + myTypeContends; 
    }
    @Override 
    public  boolean isNumber(){ return false  ; } 
    @Override 
    public  boolean isList(){ return true ; }

}
