/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment;

/**
 * This is the simple error which records any syntax errors found.
 * @author Sheep Dalton
 */
public class ParseError extends Exception 
{
    int linenumber ;
    
    /**
     * 
     * @param message  what to tell the user. 
     * @param line  line number 
     */
    public ParseError(String message, int line ) 
    {
        super(message);
        this.linenumber = line ; 
    }
    //--------------------------------------------------------------------------
    /**
     * Convert to string.
     * @return 
     */
    @Override 
    public String toString()
    { 
        return super.toString() + " line "+ this.linenumber;  
    }
    //--------------------------------------------------------------------------
}
