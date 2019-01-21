/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment.expressions;

import blfexperiment.BFLExpressionParser;
import java.math.BigDecimal;

/**
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
     * doIt is very much from valuateing statements - any result is put into IT. 
     * @return 
     */
    default GeneralObject doIt()
    { 
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
     * @return 
     */
    default boolean evaluateLogic()
    { 
        BigDecimal b =  evaluateCalculation(); 
        if( b.compareTo( BigDecimal.ZERO  )!= 0 )return  true  ; 
       
        return false ;
    }
    /**
     *  prefer to use evaluateLogic. 
     * @return 
     */
     default boolean purelyLogic(){ return false ; }
 
    /**
     *
     * @return
     */
    default boolean isQuestion() { return   getType().equals(BFLExpressionParser.typeQuestion); } 
     
    //--------------------------------------------------------------------------
    /** 
     *  isCompatable tests to see if the object is compatable ( roughtly equal ) 
     * to this one. 
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
