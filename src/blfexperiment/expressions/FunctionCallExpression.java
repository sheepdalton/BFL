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

import blfexperiment.BFLExpressionParser;
import blfexperiment.GeneralTypes.GeneralObject;
import java.math.BigDecimal;
import java.util.*;

/**
 * This is the place a function is CALLED. Currently can only handle built in
 * functions. ( Future versions will have pointer to Expression tree of functions).
 * 
 * @author Sheep Dalton
 */
public class FunctionCallExpression implements NumericExpression
{
    static class FunctionInfo
    { 
        String name ; 
        int expectedNumberOfArguments; 
        String argumentTypes[]; 
        String returnType ; // 
        
        public FunctionInfo( String name,int argCount , String argTypes[], String returnType )
        { 
            this.name = name ; 
            this.expectedNumberOfArguments = argCount ; 
            this.argumentTypes = argTypes; 
            this.returnType = returnType; 
        }
    }
    String fullName; // name possibly with spaces. 
    int howManyArguments =1 ; 
    int howManyResults   =1; // in the future I can return list of results.
    List<Expression> arguments = new ArrayList<Expression>(4); // default to 4 argument
    
    static Map<String,FunctionInfo> functionInfo = null; 
    static FunctionInfo getFunctionInfo( String name )
    { 
        if( functionInfo==null)
        { 
            functionInfo = new HashMap<String,FunctionInfo>()
            {{ 
                 put("sin", new FunctionInfo( "sin", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("cos", new FunctionInfo( "cos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("log", new FunctionInfo( "log", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
            }};
        }
        
        return functionInfo.getOrDefault(name, null); 
    }
    static Map<String,String> builtInMessageSignatures  = null ; 
    static Map<String,String> getBuiltInMessageSignatures()
    { 
        if(builtInMessageSignatures !=null) return builtInMessageSignatures; 
        builtInMessageSignatures   =  new HashMap<String,String>() 
      {{
        put("sin", BFLExpressionParser.typeNumber);
        put("cos", BFLExpressionParser.typeNumber);
        put("log", BFLExpressionParser.typeNumber);
        put("random", BFLExpressionParser.typeNumber);
        //@@@ TODO more methods - need to change type. 
       }};
        return null;
    }
    //  "sine" 1 arg , "radians" 
    //  "cos" , 1 arg , "radians" 
    //  "Atan2" , 2 arg , "typeNumber" , "typeNumber" 
    
    public FunctionCallExpression( String fullNameWithSpaces)
    { 
        fullName = fullNameWithSpaces ; 
    }
    public void addArgument( Expression e)
    { 
        arguments.add(e);
    }
    @Override
    public GeneralObject doIt()
    {
        return NumericExpression.super.doIt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isANumber()
    {
        assert fullName != null ;
        if(  getBuiltInMessageSignatures().
                 getOrDefault(fullName, BFLExpressionParser.typeNumber).
                   equalsIgnoreCase(BFLExpressionParser.typeNumber))
            return true ;              
       return  false ; 
    }

    @Override
    public String toHumanString()
    {
        return NumericExpression.super.toHumanString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String converToSource()
    {
        return NumericExpression.super.converToSource(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getType()
    {
        return BFLExpressionParser.typeFloat ;
    }

    @Override
    public BigDecimal evaluateCalculation()
    {
        System.out.println("function call " +  fullName);
        BigDecimal d= BigDecimal.ZERO;
        for( Expression e:  arguments)
        { 
            d = e.evaluateCalculation();
        }
        switch( fullName )
        { 
            case "_sin" : return  new  BigDecimal( Math.sin( d.doubleValue() ) ); 
            // _sin radians 
            // _sin degrees 
            case "_cos" : return  new  BigDecimal( Math.cos( d.doubleValue() ) );
            case "_cosh": return  new  BigDecimal( Math.cosh(d.doubleValue() ) );
            case "_acos": return  new  BigDecimal( Math.sinh(d.doubleValue() ) );
            case "_tan" : return  new  BigDecimal( Math.tan( d.doubleValue() ) );
            case "_random":return  new  BigDecimal( Math.random( ) ).multiply(d );
            case "_exp":return  new  BigDecimal(  Math.exp(d.doubleValue() ));
            case "_log":return  new  BigDecimal(  Math.log(d.doubleValue() ));
            case "_log_base_e":return  new  BigDecimal(  Math.log(d.doubleValue() ));
            case "_log10":return  new  BigDecimal(  Math.log10(d.doubleValue() ));
            case "_log_base_ten":return  new  BigDecimal(  Math.log10(d.doubleValue() ));
           
            default : assert false ; 
        }
       
       assert false ; 
       return null; 
    }
    //--------------------------------------------------------------------------
    /** Checks the args. IF all OK sends out null. If not then sends out 
     * error message. 
     * @return 
     */
    public String endAndCheckTypeAndArguments() 
    { 
         if( this.arguments.size()<= 0 )return "Not enough arguments"; 
         for( Expression e: arguments )
         { 
             if( e.isANumber()==false )return "Argument 1 needs to result in a number"; 
         }
         return null; // all is OK.
    }
    @Override
    public boolean evaluateLogic()
    {
        return NumericExpression.super.evaluateLogic(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean purelyLogic()
    {
        return NumericExpression.super.purelyLogic(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isQuestion()
    {
        return NumericExpression.super.isQuestion(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCompatable(Expression other)
    {
        return NumericExpression.super.isCompatable(other); //To change body of generated methods, choose Tools | Templates.
    }

}
