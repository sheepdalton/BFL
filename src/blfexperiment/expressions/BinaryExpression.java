/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blfexperiment.expressions;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Binary expressions - these both describe and apply in fix operators. 
 * @author Sheep Dalton
 */
public class BinaryExpression implements NumericExpression 
{
    NumericExpression before,after ;
    int operator  ;
    
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
    public boolean isRawNumberType( String s )
    { 
        return   s.equals(this.typeInt) ||  s.equals(this.typeFloat) ; 
    }
    //--------------------------------------------------------------------------
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
       
        if( isQuestion() ) return typeQuestion ; // "Question";
        // System.out.println("QUESTION  ****" +   "≠<=>".indexOf(operator));
        
        if(  before != null )myType = before.getType();
        
        if(  after != null )
        { 
            String rightType = after.getType();
            
            //System.out.printf( "COMPARE %s to %s \n", myType , rightType);
            
             if( Expression.isACurrency( myType ) &&  
                                            isRawNumberType( rightType ))
             {
                 return myType; 
             }
            if( Expression.isACurrency(rightType ) &&  
                                            isRawNumberType(myType ))
            {
                 return rightType; 
            }
            if( operator == '*' || operator == '\u00D7' || 
                operator == '/' ||  operator  == '÷'   || 
                    operator == '^') 
            { 
                if( Expression.isACurrency(myType ) && 
                    Expression.isACurrency(rightType ) )
                { 
                    return "Error: Multiplying or dividing "+ myType+" by " +  rightType+ " does not make sense lifeform"; 
                }
                
                /// IF THE OTHER TYPE IS ARRAY/VECTOR or LIST the
                //  AND the other operator is single number then 
                //  we construct a new list with every item 
                // 
                // IF other is list of same size then inividual multiply. 
                // 
                // IF the other is Matrix then multiply matrix 
                // 
                // Not sure what power would do 
            }
            
           
            if( rightType.equals(myType))return myType; // int == int , float == float
            
            if(  myType.equals(this.typeInt) && rightType.equals(this.typeFloat) )// type promtion to more general. 
            { 
                return this.typeFloat;
            }
            if(  myType.equals(this.typeFloat ) && rightType.equals(this.typeInt) )
            { 
                return this.typeFloat;
            }
            if(  myType.equals(this.typeInt) &&   Expression.isAUnit( rightType )) return rightType; 
            if(  myType.equals(this.typeFloat) &&   Expression.isAUnit( rightType )) return rightType; 
           
            if(  rightType.equals(this.typeInt) &&   Expression.isAUnit( myType )) return rightType; 
            if(  rightType.equals(this.typeFloat) &&   Expression.isAUnit( myType )) return rightType;
                   
            if(  Expression.isAUnit( rightType ) &&  Expression.isAUnit( myType ) )// don't need rightType.not equal to myType
            { 
                return "Error: " + myType + " does not match " + rightType ; 
            }
            
        }
        return myType ; 
    }
    //==========================================================================
    public BinaryExpression( int operator )
    { 
        this.operator  = operator; 
        if( false )System.out.println(" new BinaryExpression  operator " + 
                (char)( operator ) + " is ? " + isQuestion());
        
    }
    //==========================================================================
    public BinaryExpression( int operator , NumericExpression left  , NumericExpression right )
    { 
        this.operator  = operator; 
        this.before = left ; 
        this.after = right ; 
    }
    /**
     *  isANumber helps type check statements. Also used to optimise by 
     *  accelerating the evalaution. 
    */
    @Override 
    public boolean isANumber()
    { 
        //@@@ TODO - long term check for objects and strings. 
        if(this.isQuestion()==true )return false ; 
        return  true ; 
     
    }
    
    /**
     * getLeft 
     * @return 
     */
    public NumericExpression getBefore()
    {
        return before;
    }
    /**
     * set the before.
     * @param before 
     */
    public void setBefore(NumericExpression before)
    {
        this.before = before;
    }
    /**
     *  get after 
     * @return 
     */
    public NumericExpression getAfter()
    {
        return after;
    }
    //--------------------------------------------------------------------------
    /**
     * setRight 
     * @param after expression 
     */
    public void setAfter(NumericExpression after)
    {
        this.after = after;
    }
    protected boolean convertToBoolean( BigDecimal it )
    { 
        if( it.compareTo(BigDecimal.ZERO) == 0 )return false; 
        return true ; 
    }
    //--------------------------------------------------------------------------
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
     
    // System.out.println(".... " + before.toString() + "].." );
    // System.out.println("r... [" + after.toString() + "]..");
     
     switch (  operator )
     { 
         case '+' : {  BigDecimal result = beforeVal.add(rigthVal); return result ;} 
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
        // Almost equal to for numbers is within 1% of the original number 
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
