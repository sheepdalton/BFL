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

import static blfexperiment.BFLExpressionParser.debugLastExpression;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.lang.ArithmeticException; 

/**
 * This is a Minimially viable product. This is an interactive 
 *  calculator which tests BFL's expressive capablieis.
 * 
 * Notes
 * 
 * The lanauge has it which is the result of the previous values. 
 * 
 * The langage has paste which puts in the value from the clipboard ( assuming it's text).
 * 
 * @author Sheep Dalton
 */
public class BLFExperiment
{
    /**
     */
    public BLFExperiment( )
    {
        
    }
          
    /**
     *
     * @param args
     */
    public static void main(String[] args) 
    {
        Lexer.runLexerTests(); 
        boolean  slf = BFLParser.BFLParserSelfTest(); 
        System.out.printf("BLFExperiment::%s\n",(slf==true?"Successful ":"Failure")); 
        
        if( slf )
        { 
          Scanner sc = new Scanner(System.in); 
          System.out.println("---------------BFL WELCOME-----------------------");
          System.out.println("TRY ME with 2 * 2 \nQUIT on a line to finish ");
          String name ; 
          while( ((name = sc.nextLine())!= null) && ! name.equalsIgnoreCase("QUIT"))
          {
              if( name.equalsIgnoreCase("paste"))
              { 
                  System.out.println("Paste from clipboard into it (to come)." ) ; 
                  continue ; 
              }
              if( name.startsWith("?"))
              { 
                if( debugLastExpression == null )   
                    System.out.println("No last expression" ) ; 
                else
                    System.out.println(debugLastExpression.toString()) ; 
                continue ; 
              }
              
              if( name.equalsIgnoreCase("help"))
              { 
                   System.out.println("type HELP formula or Help Paste or QUIT to stop"); 
                   continue ; 
              }

              if( name.length()== 0 )continue ; 
              try 
              {
                  BFLExpressionParser.runSimpleExpression(name);
              } catch (ParseError ex)
              {
               System.out.println("I didn't  understand (sorry) "+ name+ "\n"+ex);
               System.out.println("type QUIT to stop.");
              }
              catch ( java.lang.ArithmeticException ae)
              { 
                 System.out.println("Sorry Dave I can't do that "+ name+ "\n"+ae);
              }
           }
           System.out.println("END OF LINE - complete."); 
        } 
    }
    
  /*  publci 
            P 
             
        String theCurrency = "$100,000,000"; 
        
        BufferedReader in = new BufferedReader(new StringReader(theCurrency));
        Lexer  lex = new Lexer(in ); 
        //Lexer.Token t = lex.removeNextToken(); 
        if( lex.hasThisSymbol('$'))
        { 
            Lexer.Token dollar = lex.removeNextToken(); 
            if( lex.hasANumber()) 
            { 
                Lexer.Token num1 = lex.removeNextToken();
            }
        }else { assert false  : "NO DOLLAR "; } 
        
        theCurrency = "£200,000.00"; 

        String unicodeSource; 
        Charset uncode =  Charset.forName( "UTF-16");
        unicodeSource = 
                new String( "h\u00eallo  \u00ea world how  are you" );
        System.out.println(unicodeSource ); 
        for( Character c :unicodeSource.toCharArray() )
        { 
           // System.out.printf(" %c %d \n", c , (int)c ); 
        }
        System.out.printf("------------------------------------------------%n");
    */
}
