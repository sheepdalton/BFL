/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment.expressions;


import blfexperiment.BFLExpressionParser;
import blfexperiment.GeneralTypes.*; 
import java.math.BigDecimal;

/**
 * Expressions are the parse Tree - the high level description of the result of 
 * parsing the BFL source code. 
 * The expression system can be used to 
 *  CHECK TYPE - make sure the program is semantically correct. 
 *  Provide a means to interpret the BFL code.
 *  Provide a means to generate BFL code. 
 * 
 * @author Sheep Dalton
 */
public interface Expression
{
    // This predefines all the basic expression Types 
    
    /** 
     * THIS IS A DEBUG TIHING
     * @return  string 
     */
    default String toHumanString()
    { 
        return toString(); 
    }
    //-------------------------------------------------------------------------- 

    /**
     *
     * @return
     */
    default String converToSource()
    { 
        return " <--NO SOURCE -->" ; 
    } 
    //--------------------------------------------------------------------------

    /**
     *
     * @return
     */
     String getType();
   // {  return "void";  }
    //default BigNumber evaluateCalculation(){      return }
    //--------------------------------------------------------------------------
    /** 
     * doIt is the most general end of the parse tree. By defaul trys to compute
     * faster version ( number,boolean,list,) . 
     * @return 
     */
    default GeneralObject doIt()
    { 
        if( isANumber())
        { 
            BigDecimal val = evaluateCalculation();
            return new GeneralNumber( val); 
        } 
        if( purelyLogic() )
        { 
            boolean val = evaluateLogic(); 
            return new GeneralBoolean( val ); 
        }
        // if string 
        // if list.
        assert false ;
        return null ; 
    }
    /**
     *  this is a more specalised version of doIT for calcuations. This speeds up
     * computaion because we know the result of a number computation is another 
     * number. 
     * @return 
     */
    //--------------------------------------------------------------------------
    default  BigDecimal evaluateCalculation( )
    { 
       assert false ; return null ; 
    }
    /**
     *  if isANumber is true then better to use  evaluateCalculation rather than do it
     * @return 
     */
     default boolean isANumber(){ return false ; } 
    /**
     * More specialised version of evaluate which only computes logical outcomes. 
     * zero is false any non zero thing is true. 
     * Only call this if purelyLogic returns true.
     * @return 
     */
    default boolean evaluateLogic()
    { 
        BigDecimal b =  evaluateCalculation(); 
        if( b.compareTo( BigDecimal.ZERO  )!= 0 )return  true  ; 
       
        return false ;
    }
    /**
     *  Is the parse tree/expression a purely logical one. To save passing 
     * things in complex object referances we can call evaluateLogic. The 
     * tree times below this one also ALL are logic based. 
     * if purelyLogic returns YES it's OK to call evaluateLogic 
     * @return 
     */
    default boolean purelyLogic(){ return false ; }
 
    /**
     *  check if the type of this expression is a question. 
     * @return
     */
    default boolean isQuestion() { return   getType().equals(BFLExpressionParser.typeQuestion); } 
     
    //--------------------------------------------------------------------------
    /** 
     *  isCompatable tests to see if the object is compatable ( roughtly equal ) 
     * to this one. Useful for debugging and develoment checks.
     * @param other
     * @return 
     */
    default boolean isCompatable( Expression other )
    { 
        if( this.getClass() != other.getClass())
        { 
            System.out.println(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! CLASS " + 
                    this.getClass() + " " +other.getClass());
            return false  ;
        } 
       
        if(  ! this.getType().equalsIgnoreCase(other.getType()) )
        { 
            System.out.println(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TYPE " + 
                    this.getType() + " " + other.getType()); 
            return false  ;
        } 
        return true ; 
    }
    //--------------------------------------------------------------------------
}
