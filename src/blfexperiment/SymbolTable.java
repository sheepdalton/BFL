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

package blfexperiment;

import blfexperiment.expressions.Variable;
import java.util.*;
import org.apache.commons.codec.language.*;

//============================================================================== 
/**
 * SymbolTable holds names of variables ( and later types and classes )
 * What makes this special is it uses an ensable of sound mapping systems which 
 * forbids variables which 'read'/look to close. @see  {@link blfexperiment.expressions.Variable} OK 
 * @version  0.9 
 * @author Sheep Dalton
 */
//---------------------------------------------------------------------------
public class SymbolTable
{ 
   Map<String,Variable> table ; 
   SymbolTable outerTable ; // can be null; 
   RefinedSoundex comparor;
   Caverphone2 cavp2 = new Caverphone2(); 
   Map<String,String> cavp2Map = new HashMap<>(); 
   Metaphone mphon = new Metaphone();
   Map<String,String> mphonMap = new HashMap<>();
   
   /**
    * Tables an have a 'parent'. OK to pass null. 
    * @param parentTable 
    */
   SymbolTable( SymbolTable parentTable )
   { 
       table = new HashMap<>(); 
       this.outerTable = parentTable ;  
       comparor = new RefinedSoundex(); 
   }
   SymbolTable getParent(){ return outerTable; } 
   //---------------------------------------------------------------------------
   /**
    * Not sure if this is good idea. assumes name
    * @param normliaseVarName
    * @return 
    */
   Variable getOrMakeIfNull(   String normliaseVarName )
   { 
       normliaseVarName = normliaseVarName.toLowerCase();
       if( table.containsKey(normliaseVarName) )return table.get(normliaseVarName); 
       return add(  normliaseVarName ); 
   }
   //---------------------------------------------------------------------------
   /**
    * getVariable
    * @param normliaseVarName
    * @return 
    */
   Variable  getVariable(  String normliaseVarName )
   { 
       normliaseVarName = normliaseVarName.toLowerCase();
       return table.get(normliaseVarName); 
   } 
   //---------------------------------------------------------------------------
   /**
    * add this this name. Notice we add.
    * @param normliaseVarname
    * @return 
    */
   Variable add( String normliasedVarname )
   { 
       Variable v = new Variable( normliasedVarname); 
       table.put( normliasedVarname.toLowerCase() , v); 
       
       cavp2Map.put( cavp2.encode(normliasedVarname), normliasedVarname ); 
       mphonMap.put( mphon.encode(normliasedVarname), normliasedVarname ) ;
   
       return v; 
   }
   //---------------------------------------------------------------------------
   /** 
    * @@@ TODO - 
    * @param s
    * @return  the String which is convered to 'universal' or normal for.
    */
   String normalise( String s ) { return s; } 
   //---------------------------------------------------------------------------
   /**
    * Strictly test if this name exists literally. Checks if exists in all 
    * tables 
    * @param normliaseVarname
    * @return 
    */
   boolean contains( String normliaseVarname )
   { 
       normliaseVarname = normliaseVarname.toLowerCase();
       if(  table.containsKey(normliaseVarname)== true )  return true ; 
       if( outerTable == null) return false; 
       return outerTable.contains(normliaseVarname); 
   }
   //---------------------------------------------------------------------------
   /**
    *  Returns first name in this context  which is to close to this variabel name. 
    *  many rules apply. 
    *       1. if name ends with number - thats just an array def ok 
    *       2. if the name is predeclared as a var this is Also OK -- it's deliberate 
    *       3
    * @param normliaseVarname
    * @return 
    */
   protected String similarLocalName( String normliaseVarname )
   { 
       normliaseVarname = normliaseVarname.toLowerCase();
       
       if( cavp2Map.containsKey(cavp2.encode(normliaseVarname))) 
       { 
           return cavp2Map.get(cavp2.encode(normliaseVarname)); 
       }
       if(  mphonMap.containsKey( mphon.encode(normliaseVarname) ))
       {
           return mphonMap.get(mphon.encode(normliaseVarname));
       }
        
       return null ; 
   }
   //---------------------------------------------------------------------------
  protected  boolean thisIsStartOfOneOfYourStrings( String wordNormalised )
   { 
       wordNormalised = wordNormalised.toLowerCase();
       for( String s : table.keySet())
       { 
           if( s.startsWith(wordNormalised)) return true ; 
       }
       return false ; 
   }
   //---------------------------------------------------------------------------
    /**
     * TESTS the code for the lexer.  
     * @param args
     */
    public static void main(String[] args) 
    {
        DaitchMokotoffSoundex comparor = new DaitchMokotoffSoundex(); 
      // try 
       {
           //System.out.println(comparor.difference("their", "there"));
           //System.out.println(comparor.difference("there", "there"));
           //System.out.println(comparor.difference("thedrin", "thedrine"));
           
           System.out.println(comparor.soundex("there"));
           System.out.println(comparor.soundex("their"));
           System.out.println(comparor.soundex("thedrin"));
           System.out.println(comparor.soundex("theedryn"));
           System.out.println(comparor.soundex("cristos"));
           System.out.println(comparor.soundex("crystus"));
           { 
           DoubleMetaphone cmp = new DoubleMetaphone(); 
           
           System.out.println(cmp.encode("there"));
           System.out.println(cmp.encode("their"));
           System.out.println(cmp.encode("thedrin"));
           System.out.println(cmp.encode("theedryn"));
           System.out.println(cmp.encode("cristos"));
           System.out.println(cmp.encode("crystus"));
           } 
           System.out.println("--- MatchRatingApproachEncoder "); 
           { // not so gun. 
            MatchRatingApproachEncoder cmp = new MatchRatingApproachEncoder(); 
             System.out.println(cmp.encode("there"));
            System.out.println(cmp.encode("their"));
            System.out.println(cmp.encode("thedrin"));
            System.out.println(cmp.encode("theedryn"));
            System.out.println(cmp.encode("cristos"));
            System.out.println(cmp.encode("crystus"));
           } 
            System.out.println("--- Metaphone"); 
           { 
            Metaphone cmp = new Metaphone(); 
            System.out.println(cmp.encode("there"));
            System.out.println(cmp.encode("their"));
            System.out.println(cmp.encode("thedrin"));
            System.out.println(cmp.encode("theedryn"));
            System.out.println(cmp.encode("cristos"));
            System.out.println(cmp.encode("crystus"));
           }
            System.out.println("--- Nysiis "); // good. 
           { 
            Nysiis cmp = new Nysiis(); 
            System.out.println(cmp.encode("there"));
            System.out.println(cmp.encode("their"));
            System.out.println(cmp.encode("thedrin"));
            System.out.println(cmp.encode("theedryn"));
            System.out.println(cmp.encode("cristos"));
            System.out.println(cmp.encode("crystus"));
           }
           System.out.println("---"); // good. 
           { 
            Caverphone2 cmp = new Caverphone2(); 
            System.out.println(cmp.encode("there"));
            System.out.println(cmp.encode("their"));
            System.out.println(cmp.encode("thedrin"));
            System.out.println(cmp.encode("theedryn"));
            System.out.println(cmp.encode("cristos"));
            System.out.println(cmp.encode("crystus"));
           }
       } //catch (EncoderException ex)
       {
           //System.out.println("ERROR " + ex);
       }
    }
   // Variable get 
}
//------------------------------------------------------------------------------