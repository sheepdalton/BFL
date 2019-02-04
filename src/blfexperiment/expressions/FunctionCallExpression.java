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
    static interface basicMathFunction
    { 
       public  float doCalculation( float argument ) ; 
    }
    // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 
    static class FunctionInfo
    { 
        String name ; 
        int expectedNumberOfArguments; 
        String argumentTypes[]; 
        String returnType ; //
        basicMathFunction doThis ; 
        
        public FunctionInfo( String name,int argCount , String argTypes[],
                                                             String returnType )
        { 
            this.name = name ; 
            this.expectedNumberOfArguments = argCount ; 
            this.argumentTypes = argTypes; 
            this.returnType = returnType; 
            this.doThis = null; 
            assert false ; 
        }
        //----------------------------------------------------------------------
        public FunctionInfo( String name,int argCount , String argTypes[],
                                  String returnType , basicMathFunction action  )
        { 
            this.name = name ; 
            this.expectedNumberOfArguments = argCount ; 
            this.argumentTypes = argTypes; 
            this.returnType = returnType; 
            this.doThis = action; 
        }
        //----------------------------------------------------------------------
        public boolean isNumber()
        { 
            return returnType.equalsIgnoreCase(BFLExpressionParser.typeNumber); 
        } 
        //----------------------------------------------------------------------
        public BigDecimal computeSimpleBuitIn( BigDecimal singleArg)
        { 
            assert  this.doThis != null ; 
            assert singleArg != null ; 
            double d =  this.doThis.doCalculation(singleArg.floatValue()); 
            return new BigDecimal(d);
        }
        //----------------------------------------------------------------------
        public String  getName(){ return  name ;  } 
        //----------------------------------------------------------------------
    }
    // = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 
    public  static boolean hasFunctionCalled(String name ) 
    { 
        FunctionInfo  fin = getFunctionInfo( name ); 
        if( fin==null)return false; 
        return true; 
    }
    static Map<String,FunctionInfo> functionInfo = null; 
    static FunctionInfo getFunctionInfo( String name )
    { 
        /*
        basicMathFunction bx =  new  basicMathFunction()
         {  
             @Override 
             public float doCalculation(float f){ return (float) Math.sin(f);} 
         };
        
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
        */
        if( functionInfo==null)
        { 
            functionInfo = new HashMap<String,FunctionInfo>()
            {{ 
                put("_sin", new FunctionInfo( "_sin", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.sin(f);}  }  ));
                put("_cos", new FunctionInfo( "_cos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.cos(f);}  }  ));
                put("_log", new FunctionInfo( "_log", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.log(f);}  }  ));
                put("_tan", new FunctionInfo( "_log", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.tan(f);}  }  ));
                put("_exp", new FunctionInfo( "_log", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.exp(f);}  }  ));
                put("_random", new FunctionInfo( "_random", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) (Math.random( )*f);}  }  ));
                put("_log_base_e", new FunctionInfo( "_log_base_e", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.log(f);}  }  ));
                put("_log_base_ten", new FunctionInfo( "_log_base_e", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.log10(f);}  }  ));
                put("_acos", new FunctionInfo( "_acos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.acos(f);}  }  ));
                put("_asin", new FunctionInfo( "_acos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.asin(f);}  }  ));
                put("_cosh", new FunctionInfo( "_cosh", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber, 
                         new  basicMathFunction(){   @Override   public float doCalculation(float f){ return (float) Math.asin(f);}  }  ));
              /*    
                 put("cos", new FunctionInfo( "cos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("log", new FunctionInfo( "log", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("cosh", new FunctionInfo( "cosh", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("random", new FunctionInfo( "random", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("cos", new FunctionInfo( "cos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("tan", new FunctionInfo( "tan", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
                 put("acos", new FunctionInfo( "acos", 1 , new String[]{ BFLExpressionParser.typeNumber } , BFLExpressionParser.typeNumber));
              */    
            }};
        }
        return functionInfo.getOrDefault(name, null); 
    }
    /*static Map<String,String> builtInMessageSignatures  = null ; 
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
    }*/
    //  "sine" 1 arg , "radians" 
    //  "cos" , 1 arg , "radians" 
    //  "Atan2" , 2 arg , "typeNumber" , "typeNumber" 
   //  INSTANCE VARS 
    String fullName; // name possibly with spaces. 
    int howManyArguments =1 ; 
    int howManyResults   =1; // in the future I can return list of results.
    List<Expression> arguments = new ArrayList<Expression>(4); // default to 4 argument
    FunctionInfo myBuiltInFunction = null ; // if not null is builtin. 
    
    //--------------------------------------------------------------------------
    public FunctionCallExpression( String fullNameWithSpaces)
    { 
        fullName = fullNameWithSpaces ; 
        myBuiltInFunction =  getFunctionInfo(fullNameWithSpaces);  
        if( myBuiltInFunction != null )
        { 
            System.out.println(" @@@@@  GOT FUNCTION- "+ fullNameWithSpaces );
        }else
        { 
            System.out.println(" @@@@@ WRONG not found - "+ fullNameWithSpaces );
        }
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param e 
     */
    public void addArgument( Expression e)
    { 
        arguments.add(e);
    }
    //--------------------------------------------------------------------------
    @Override
    public GeneralObject doIt()
    {
        return NumericExpression.super.doIt(); //To change body of generated methods, choose Tools | Templates.
    }
    //--------------------------------------------------------------------------
    @Override
    public boolean isANumber()
    {
        if(myBuiltInFunction != null )// NEW CLEAN WAY 
        { 
            //System.out.println("NEW METHOD isANumber "); 
           return myBuiltInFunction.isNumber(); 
        }
        assert fullName != null ;
        return false ; 
        /*
        assert getBuiltInMessageSignatures() != null ; 
        Map<String,String> sigs = getBuiltInMessageSignatures(); 
     
        if( ! sigs.containsKey(fullName) ) return false ; 
            
        if( sigs.getOrDefault(fullName, BFLExpressionParser.typeNumber).
                   equalsIgnoreCase(BFLExpressionParser.typeNumber))
            return true ;              
       return  false ; 
         */
    }
    //--------------------------------------------------------------------------
    /**
     *  returns human readable string 
     * @return 
     */
    @Override
    public String toHumanString()
    {
        return NumericExpression.super.toHumanString(); //To change body of generated methods, choose Tools | Templates.
    }
    //--------------------------------------------------------------------------
    /**
     * convert To Source 
     * @return 
     */
    @Override
    public String converToSource()
    {
        return NumericExpression.super.converToSource(); //To change body of generated methods, choose Tools | Templates.
    }
    //--------------------------------------------------------------------------
    /**
     * what is the type of the float.
     * @return 
     */
    @Override
    public String getType()
    {
        return BFLExpressionParser.typeFloat ;
    }
    //--------------------------------------------------------------------------
    /**
     * 
     * @param e BigDecimal which is single argument cannot be null; 
     * @return 
     */
     public BigDecimal computeSingleArgBuiltInFunction( BigDecimal e )
     { 
         assert( myBuiltInFunction!= null ); 
         
         return this.myBuiltInFunction.computeSimpleBuitIn(e);
     }
    //--------------------------------------------------------------------------
    /**
     *  evaluateCalculation - 
     * @return 
     */
    @Override
    public BigDecimal evaluateCalculation()
    {
        /// System.out.println("Function call " +  fullName);
        BigDecimal d= BigDecimal.ZERO;
        for( Expression e:  arguments)
        { 
            d = e.evaluateCalculation();
        }
       if(  myBuiltInFunction != null )
       { 
           //System.out.println(" %%%*** compute " + myBuiltInFunction.getName() );
           return myBuiltInFunction.computeSimpleBuitIn(d);
       }
        switch( fullName )
        { 
            case "_sin" : assert false ; break ; //  return  new  BigDecimal( Math.sin( d.doubleValue() ) ); 
            // _sin radians 
            // _sin degrees 
            case "_cos" :  assert false ; break ; //  return  new  BigDecimal( Math.cos( d.doubleValue() ) );
            case "_cosh": assert false ; break ; //  return  new  BigDecimal( Math.cosh(d.doubleValue() ) );
            case "_acos": assert false ; break ; //  return  new  BigDecimal( Math.sinh(d.doubleValue() ) );
            case "_tan" : assert false ; break ; //  return  new  BigDecimal( Math.tan( d.doubleValue() ) );
            case "_random": assert false ; break ; //  return  new  BigDecimal( Math.random( ) ).multiply(d );
            case "_exp": assert false ; break ; //  return  new  BigDecimal(  Math.exp(d.doubleValue() ));
            case "_log": assert false ; break ; //  return  new  BigDecimal(  Math.log(d.doubleValue() ));
            case "_log_base_e":  assert false ; break ; //  return  new  BigDecimal(  Math.log(d.doubleValue() ));
            case "_log10":assert false ; break ; // return  new  BigDecimal(  Math.log10(d.doubleValue() ));
            case "_log_base_ten":assert false ; break ; // return  new  BigDecimal(  Math.log10(d.doubleValue() ));
           
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
