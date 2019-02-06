/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment.expressions;

import blfexperiment.BFLExpressionParser;
import java.math.BigDecimal;
import java.math.MathContext;
//==============================================================================
/**
 * 
 * Binary expressions - these both describe and apply in fix operators.  Now
 * divided between this class and Binary Numeric Only Expression. @see BinaryNumericOnlyExpression
 * {@link BinaryNumericOnlyExpression}
 * @author Sheep Dalton
 */
public class BinaryExpression implements GeneralExpression 
{
    GeneralExpression before;
    GeneralExpression after;
    int operator  ;
    
    
    //--------------------------------------------------------------------------
    /**
     * 
     * @param operator
     * @param left
     * @param right
     */
    public BinaryExpression( int operator , GeneralExpression left  , GeneralExpression right )
    { 
        this.operator  = operator; 
        this.before = left ; 
        this.after = right ; 
    }
    //--------------------------------------------------------------------------
   //static int validOperators[] = { '=' , '≠' ,  ,'<' , '>' ,'\u2265' , '≤'  , '~' , '&'   ,  '|' } ; 
    @Override 
    public String toString()
    { 
       return  "{" + before.toString() + " " + (char)this.operator + " "+ after.toString()+ " }"  ; 
    }
    public String toHumanString()
    { 
        return  "(" + before.toHumanString() + " " + (char)this.operator + " "+ after.toHumanString()+ " )"  ; 
    }
    //--------------------------------------------------------------------------

    /**
     *
     * @param s
     * @return
     */
    public boolean isRawNumberType( String s )
    { 
        return   s.equals(BFLExpressionParser.typeInt) ||  s.equals(BFLExpressionParser.typeFloat) ; 
    }
    //--------------------------------------------------------------------------

    /**
     *
     * @return
     */
    @Override public boolean isQuestion( ) 
    { 
        if( operator == '=' || operator == '≠' || 
                operator == '<' || operator == '>' || 
                operator == '\u2265'  ||   operator == '≤'  || 
                operator == '~'  || // NEW BFL operator
                operator == '&' ||    operator == '|'  // AND OR.. NOT IS UNARY 
                )   
        {   
           return true ; 
        } 
        return false ; 
    }
    //--------------------------------------------------------------------------
    /** 
     *  @@@ should throw exception on incompatablites  
     *  @@@ binary expresion needs more type checking.
     * @return 
     */
    @Override 
    public String getType() 
    { 
        String myType = "void";
        //if( "≠<=>".indexOf(operator)!= -1 )
       
        if( isQuestion() ) return BFLExpressionParser.typeQuestion ; // "Question";
        // System.out.println("QUESTION  ****" +   "≠<=>".indexOf(operator));
        
        if(  before != null )myType = before.getType();
        
        if(  after != null )
        { 
            String rightType = after.getType();
            
            //System.out.printf( "COMPARE '%s' to '%s' \n", myType , rightType);
            //System.out.printf( " Units %b %b\n" , BFLExpressionParser.isAUnit( rightType ) ,  BFLExpressionParser.isAUnit( myType )); 
             if( BFLExpressionParser.isACurrency( myType ) &&
                     BFLExpressionParser.isACurrency(rightType) && 
                           rightType.equalsIgnoreCase(myType)== false   )
             { 
                return "Error:  Cannot combine types "+ myType+" does not match  "
                        +  rightType+ ". Use conversion with an exchange rate.  ";  
             }
                     
             if( BFLExpressionParser.isACurrency( myType ) &&  
                                            isRawNumberType( rightType ))
             {
                 return myType; 
             }
            if( BFLExpressionParser.isACurrency(rightType ) &&  
                                            isRawNumberType(myType ))
            {
                 return rightType; 
            }
            if( operator == '*' || operator == '\u00D7' || 
                operator == '/' ||  operator  == '÷'   || 
                    operator == '^') 
            { 
                if( BFLExpressionParser.isACurrency(myType ) && 
                    BFLExpressionParser.isACurrency(rightType ) )
                { 
                    return "Error: Multiplying or dividing "+ myType+" by " +  rightType+ " does not make sense lifeform"; 
                }
                
                /// IF THE OTHER TYPE IS ARRAY/VECTOR or LIST the
                //  AND the other operator is single typeNumber then 
                //  we construct a new list with every item 
                // 
                // IF other is list of same size then inividual multiply. 
                // 
                // IF the other is Matrix then multiply matrix 
                // 
                // Not sure what power would do 
            }
            
           
            if( rightType.equals(myType))return myType; // int == int , float == float
            
            if(  myType.equals(BFLExpressionParser.typeInt) && rightType.equals(BFLExpressionParser.typeFloat) )// type promtion to more general. 
            { 
                return BFLExpressionParser.typeFloat;
            }
            if(  myType.equals(BFLExpressionParser.typeFloat ) && rightType.equals(BFLExpressionParser.typeInt) )
            { 
                return BFLExpressionParser.typeFloat;
            }
            if(  myType.equals(BFLExpressionParser.typeInt) &&   BFLExpressionParser.isAUnit( rightType )) return rightType; 
            if(  myType.equals(BFLExpressionParser.typeFloat) &&   BFLExpressionParser.isAUnit( rightType )) return rightType; 
           
            if(  rightType.equals(BFLExpressionParser.typeInt) &&   BFLExpressionParser.isAUnit( myType )) return rightType; 
            if(  rightType.equals(BFLExpressionParser.typeFloat) &&   BFLExpressionParser.isAUnit( myType )) return rightType;
                   
            if(  BFLExpressionParser.isAUnit( rightType ) &&  BFLExpressionParser.isAUnit( myType ) )// don't need rightType.not equal to myType
            { 
                return "Error: " + myType + " does not match " + rightType ; 
            }
            
        }
        return myType ; 
    }
    //==========================================================================
    public enum Operators   
    {
          PLUS   , TIMES, DIVIDE , MINUS , POWER 
    }; 
    /**
     *
     * @param operator
     */
    public BinaryExpression( int operator )
    { 
        this.operator  = operator; 
        if( false )System.out.println(" new BinaryExpression  operator " + 
                (char)( operator ) + " is ? " + isQuestion());
    }
   
    /**
     *  isANumber helps type check statements. Also used to optimise by 
     *  accelerating the evalaution. 
     * @return 
    */
    static  String mathOpertors  = "+-*÷^"; 
    @Override 
    public boolean isANumber()
    { 
        //@@@ TODO - long term check for objects and strings.
        if(this.isQuestion()==true )return false ; 
        return  true ; 
    }
    //--------------------------------------------------------------------------
    /**
     * getLeft 
     * @return 
     */
    public GeneralExpression getBefore()
    {
        return before;
    }
    //--------------------------------------------------------------------------
    /**
     * set the before.
     * @param before 
     */
    public void setBefore(GeneralExpression before)
    {
        this.before = before;
    }
    //--------------------------------------------------------------------------
    /**
     *  get after 
     * @return 
     */
    public GeneralExpression getAfter()
    {
        return after;
    }
    //--------------------------------------------------------------------------
    /**
     * setRight 
     * @param after expression 
     */
    public void setAfter(GeneralExpression after)
    {
        this.after = after;
    }
    //--------------------------------------------------------------------------
    /**
     *
     * @param it
     * @return
     */
    protected boolean convertToBoolean( BigDecimal it )
    { 
        if( it.compareTo(BigDecimal.ZERO) == 0 )return false; 
        return true ; 
    }
    //--------------------------------------------------------------------------
    // System.out.println(".... " + before.toString() + "].." );
    // System.out.println("r... [" + after.toString() + "]..")
    /**
     * doIt - for testing - evaluate expression 
     * @return 
     */
    @Override 
    public  BigDecimal evaluateCalculation( )
    {
     assert   before != null :"No left";
     assert after != null : "No right"; 
     
     BigDecimal  beforeVal = before.evaluateCalculation();
     BigDecimal rigthVal = after.evaluateCalculation();
      
     switch (  operator )
     { 
         case '+' : case '^' :  
         case '-': case '*' : case '/': case '\u00D7':  
         { 
             assert false;  // this is handled by BinaryNumericOnlyExpression now
         }break ; 
        /* case '+' : {  BigDecimal result = beforeVal.add(rigthVal); return result ;} 
         case '-': { BigDecimal result = beforeVal.subtract(rigthVal); return result;} 
         case '*' : case '\u00D7': { BigDecimal result = beforeVal.multiply(rigthVal); return result;} 
         case '/':case '÷': 
         { 
             return  beforeVal.divide(rigthVal, MathContext.DECIMAL128);
         }  
        case '^': 
        { 
             int power = rigthVal.intValue(); 
             BigDecimal result = beforeVal.pow(power); 
             return result ; 
        }
        */ 
        case '=': 
        { 
            System.out.println("= LEfT  " + beforeVal + "  R = "+ (rigthVal)); 
            assert ( BigDecimal.ONE.compareTo(BigDecimal.ONE) == 0 ) ; 
            assert ( BigDecimal.ZERO.compareTo(BigDecimal.ONE ) == -1 ) ; 
            
            System.out.println("= evL @@ " + beforeVal.compareTo(rigthVal)  );
            if( beforeVal.compareTo(rigthVal) == 0 )
            { 
                System.out.println("RETURN 1  " ); 
                return BigDecimal.ONE;
            }
            System.out.println("RETURN 0  " ); 
            return BigDecimal.ZERO; 
        } 
        // Almost equal to for numbers is within 1% of the original typeNumber 
        // useful to compare floating point numbers.
        // for strings they have sound alike .
        case '~': 
        { 
            if( rigthVal.compareTo(BigDecimal.ZERO)==0)// CHECK FOR ZERO
            { 
                if( (rigthVal.abs()).doubleValue() > 0.02 )return BigDecimal.ZERO; 
                return BigDecimal.ONE;  
            } 
            BigDecimal r = beforeVal.abs().divide(rigthVal.abs(), MathContext.DECIMAL128);
            double  d = r.doubleValue(); // APPROXIMATLY 
            System.out.println("= LEfT  " + beforeVal + "  R = "+ (rigthVal)+ " "+ d );
              
            if( d > 0.99 && d < 1.01 )// within one percent.
            { 
                 return BigDecimal.ONE;// false 
            }else 
                return BigDecimal.ZERO;  
        }
        case '>': 
        { 
           if( beforeVal.compareTo(rigthVal) >  0 )
            { 
                //System.out.println("RETURN 1 >   " ); 
                return BigDecimal.ONE;
            } return BigDecimal.ZERO; 
        } 
        case '<': 
        { 
         return ((beforeVal.compareTo(rigthVal) == -1) ? BigDecimal.ONE:BigDecimal.ZERO); 
        } 
        case '≠': 
        { 
         return ((beforeVal.compareTo(rigthVal) != 0) ? BigDecimal.ONE:BigDecimal.ZERO); 
        }
        case '≤': 
        { 
            int r  = beforeVal.compareTo(rigthVal); 
            if( r<= 0 )return BigDecimal.ONE; 
            return BigDecimal.ZERO;
        }
        case '≥': 
        { 
            int r  = beforeVal.compareTo(rigthVal); 
            if( r>= 0 )return BigDecimal.ONE; 
            return BigDecimal.ZERO;
        }
        case '&': 
        { 
           // NOTICE WE EVLUATE BOTH SIDES 
           System.out.println( "DOING AND question b= " +  before.isQuestion() 
                                            + "? r="+ this.after.isQuestion());
           System.out.println(" Doing ANDD " + convertToBoolean(beforeVal) 
                                        + " r = "+ convertToBoolean( rigthVal) );
           if( convertToBoolean(beforeVal) && convertToBoolean( rigthVal))   return BigDecimal.ONE; 
           System.out.println(" return zero");
           return BigDecimal.ZERO;
        } 
         
        case '|': 
        { 
           // NOTICE WE EVLUATE BOTH SIDES - we need Fast and , FAST Or 
           System.out.println( "DOING AND question b= " +  before.isQuestion() 
                                            + "? r="+ this.after.isQuestion());
           System.out.println(" Doing ANDD " + convertToBoolean(beforeVal) 
                                        + " r = "+ convertToBoolean( rigthVal) );
           if( convertToBoolean(beforeVal) || convertToBoolean( rigthVal))   return BigDecimal.ONE; 
           System.out.println(" return zero");
           return BigDecimal.ZERO;
        } 
        
        default: assert false : " " + (char)operator + " not implmented BinaryExpression " + operator ; 
     }
     
     assert false :  " Not sure of operator = "+ this.operator ; 
     return null ; 
    }
    //--------------------------------------------------------------------------
    @Override  
    public boolean isCompatable( Expression other )
    { 
        if( ! (other instanceof  BinaryExpression ) ) return false ; 
        BinaryExpression  b = (BinaryExpression)other ; 
        
        if(   operator  != b.operator )return false ; 
        if( !  before.isCompatable(b.before ))return false ; 
        if( !   after.isCompatable(b.after))return false ;   
        return true ; 
    }
}
