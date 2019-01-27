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
public class LiteralNumberExpression implements NumericExpression
{
    String numberAsText = "0"; 
    String type = BFLExpressionParser.typeInt;
    //--------------------------------------------------------------------------

    /**
     *
     * @param number
     */
    public LiteralNumberExpression( String number )
    {
       this.numberAsText = number;  
       if( number.contains("."))type = BFLExpressionParser.typeFloat;
    }
    //--------------------------------------------------------------------------

    /**
     *
     * @param number
     * @param type
     */
    public LiteralNumberExpression( String number , String type )
    {
       assert number != null ; 
       this.numberAsText = number;  
       assert type.intern() != null ; 
       this.type = type.intern();
    }
    //--------------------------------------------------------------------------
    /**
     *  returns the raw typeNumber  eg 000_000_000.00_000 
     * @return 
     */
    public String getNumberAsText()
    {
        return numberAsText;
    }
    //--------------------------------------------------------------------------
    /**
     * Get the type e.g. INTEGER, FLOAT 
     * @return 
     */
    @Override 
    public    String getType()
    { 
        return  type; 
    }
    //--------------------------------------------------------------------------
    /**
     * Use this to set the type eg Integer , Dollar etc 
     * @param type 
     */
    public void setType(String type)
    {
        this.type = type;
    }
    //--------------------------------------------------------------------------
    /**
     *  Literal returns the typeNumber
     */
    @Override 
    public BigDecimal evaluateCalculation()
    { 
      return  new BigDecimal( numberAsText ) ; 
    }
    //--------------------------------------------------------------------------
    @Override 
    public String toString()
    { 
        return numberAsText + " " + type ; 
    }
    @Override 
    public String toHumanString()
    { 
        return numberAsText; 
    }
    //--------------------------------------------------------------------------
    
   
}
