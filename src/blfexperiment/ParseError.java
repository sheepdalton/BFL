/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment;

/**
 *
 * @author Sheep Dalton
 */
public class ParseError extends Exception 
{
    int linenumber ;
    
    /**
     *
     * @param message
     * @param line
     */
    public ParseError(String message, int line ) 
    {
        super(message);
        this.linenumber = line ; 
    }
    //--------------------------------------------------------------------------
    @Override 
    public String toString()
    { 
        return super.toString() + " line "+ this.linenumber;  
    }
}
