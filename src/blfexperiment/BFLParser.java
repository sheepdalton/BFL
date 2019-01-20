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

import blfexperiment.expressions.GeneralObject;
import blfexperiment.expressions.NewVariableNameExpression;
import blfexperiment.expressions.NumericExpression;
import blfexperiment.expressions.PutStatement;
import blfexperiment.expressions.Statement;
import blfexperiment.expressions.StatementBlock;
import blfexperiment.expressions.Variable;
import blfexperiment.expressions.VariableSetExpression;
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;

/**
 * Project now on github.
 * @author Sheep Dalton
 */ 
public class BFLParser extends BFLExpressionParser 
{
    public BFLParser (  BufferedReader source ) 
    { 
        super( source); 
    }
    //---------------------------------------------------------------------------
   protected  BFLParser(  BufferedReader source, SymbolTable s  )
   { 
      super(source , s );  assert source != null:"No null source";
   }

    /*
     * put [ 1,2,3,4,5,6,7] into x as array
    put
    1
    2
    3
    4
    5
    into a as array -- keeps reading if rest of line is empty on put.
    add 4 to x
    put 5 into x[2]
    put 5 into x2
    put [ { 'bob',2}, {2,2}] into x as hash dictionary
    put x['bob'] into y
    put { 'bob' , 4, } after x
    put [ 1 ; 2 ;3 ;4 5,6,7] into x as hashset array
    put  hello this is bob. into x as string
    put hello, world
    put tan( 43 ; 23 )
     */
    //---------------------------------------------------------------------------
    /**
     *  Get accesses information into super variabel called it.
     *
     * <pre>
     *   get line 5 of var
     *   get file "bobo"
     *   get line 2 of file "bob
     *   get page "www.xxxx.com://page" ...
     * </pre>
     */
    //---------------------------------------------------------------------------
    /**  is a
     * Put - is a general purpose operation including the creation of vars
     * <pre>
     *
     * Handles statements of type
     *                put 8^2 ➯ => var possibly declearing it.
     *                Put 5 * 3 <b>into</b> <i>variable</i>
     *                put 5 * 3 after variable
     *                put 5 + 4                    <i>-- print this out</i>
     *                put it  as time -- prin out with conversion
     *                put 5 * 3 after file <i>variable</i>
     *                put 5 * 3 before file <i>variable</i>
     *                put 5 ^ 3 into <b>clipboard</b>  --
     *                put 5 <b>squared into</b> <i>my long var name here ok</i>
     *                put 2 into the height of coordinate
     *                put 2 into the coordinate.height  -- element access
     *                put 45,330 into stuff[454]      --  array access
     *                put $303,000 into my array#454    -- array access
     *                       put put 89 into my array#index -- array access via
     *                       put Bob. into the key of my dicionary -- map access
     *                       put 993.2 * 0.1 into jamjar as integer -- type setting
     *                       put 3-4 into the "costa" of  jamjar as dictionary
     *                       put Hello. into word 4 of jamjar
     *                       put Hello. after word 3 of line of thetext
     *                       put Hello. after letter 4 of word 2 of  line 3 of sentance 2 of paragaph 4
     *                       put Hello. into item 5 of jamjar [as list ]
     *                       put "40043" as integer into jamjar -- conversion
     *                       put 0322033 as letter into jamjar as letter -conversion and type
     *                       put $20300 as number into value  -- casting
     *                       put 3Km * 1000 as meter into length -- casting
     *                       put  x from coordinate -- access object member
     *                       put coordinate/x  -- \ dividion / is elemnt
     *                       put coordinate's x -- x is
     *                       put coordinate's length( arg1 arg2 arg )
     *                       put student's name <b>With</b> option long form  -- p
     *                       put coordinate's
     *                       put  -- blank multi line string indented
     *                           hello world
     *                           how are you today
     *                       into the text -- into at start of text starts.
     *                       put screens's height
     *                       put xml/head/div/div/4 i into x -- XML access.
     *                       put coordinate.x  --
     *                       put height from Screen --
     *                       put height of Screen
     *                       put 56 * 344 into locked length -- create constant
     *                       put <b>hash</b> of "bob" into 23
     * Advanced operators
     *                       put list  4 4 3 2 3 5 3 4 3 2 into nums as list
     *                       put list
     *                               4  -- multi line list  notice indent
     *                               6
     *                       into numbers as list
     *                       put table
     *                               4 6 4 5  "bob"
     *                               4 3 2 3  "henry"
     *                       into my table with header "x,y,z,name' -- sets type to tabel
     *                       put matrix
     *                               4 3 4 3
     *                               3 3 4 3
     *                               2 3 2 3
     *                       into M as matrix
     *                       put J dot k into l as vector  -- Num pie operatino
     *                       put dictionary
     *                           A. 23
     *                           Charline. 332
     *                       into my address book [ as dicitionary ]
     *
     *                       local bob bob2 -- use when you want to FORCE similar names
     *                       global bob -- use if you want access to globlas
     *                       local eternal bob -- has same value as last time.
     *                  put characters 2 through 7 of steve into bill
     *
     *
     * FUTURE speculative
     *                       put the list a,b,c,d,e,f,g into things as list
     *                       expression -> variable -- put expression into var
     *                       put message into Twitter  -- access bot assum config
     *                       put last message from Twitter -- if configured
     *                       put last message from Discord -- if configured
     *                       put last message from Slack -- if configured
     *                       put last message from SMS -- if configured
     *                       put header( text ) into page v.html
     *  line 20 ; 30 ; 30 ; 100 into page "x.svg" acts like plot lib.
     *  line 20 ; 30 ; 30 ; 100 into PDF "bob.pdf" -- acts line plot lib.
     *
     *
     * variable
     *       slot of object
     *       array[ expression]
     *       array#expression
     *       expression-number of array
     *       expression  of array
     *       name AS type
     * </pre>
     * @return Statement level object
     * @throws ParseError
     */
    public Statement parsePutStatement() throws ParseError
    {
        boolean isJustPut = true;
        tokenStream.setSkipWhiteSpace(true);
        if (!tokenStream.hasWord("put"))
        {
            return null; // not a put statement
        }
        Lexer.WordToken t = (Lexer.WordToken) tokenStream.removeNextToken();
        NumericExpression exp = parseComparisonExpression();
        VariableSetExpression varExp = null;
        if (tokenStream.hasWord("into"))
        {
            //@@@TODO handle put as in into
            t = (Lexer.WordToken) tokenStream.removeNextToken();
            //@@@ check for file and clipboard
            String variableName = "";
            Lexer.Token tkn;
            do
            {
                tkn = tokenStream.removeNextToken();
                //System.out.println("tkeb  " +  tkn );
                if (tkn.getTokenType() == Lexer.TT_WORD)
                {
                    variableName = variableName + ((Lexer.WordToken) tkn).getText() + "_";
                }
            } while (tkn.getTokenType() == Lexer.TT_WORD);
            if (!variableName.isEmpty())
            {
                if (this.currentSymTable.contains(variableName))
                {
                    Variable v = currentSymTable.getOrMakeIfNull(variableName);
                    varExp = new VariableSetExpression(variableName, v);
                } else
                {
                    String sim = this.currentSymTable.similarLocalName(variableName);
                    if (sim != null)
                    {
                        parseErrorStop(" Your new variable '" + variableName + "' is to " + " close to previsou var ' " + sim + " This could be a miss spelling. " + " if you MUST have this name then use local statement " + " I would  use much more diffrent name my self.");
                    }
                    Variable v = currentSymTable.getOrMakeIfNull(variableName);
                    varExp = new NewVariableNameExpression(variableName, v);
                }
            }
            System.out.println("Variable " + variableName);
            //@@@ TODO HANDLE [ EXPRESSION ]
            //@@@ TODO HANDLE AS [ TYPE ]
            if (this.tokenStream.hasWord("As"))
            {
                Lexer.WordToken as = tokenStream.removeNextTokenAsWord();
                assert as.getText().equalsIgnoreCase("as");
                if (!tokenStream.hasWordAvilable())
                {
                    parseErrorStop(" Put " + variableName + "  As.. what (Number,Kg) ?");
                }
                Lexer.Token tx = tokenStream.removeNextToken();
                if (!(tx.getTokenType() == tokenStream.TT_EOL || tx.getTokenType() == tokenStream.TT_EOF))
                {
                    parseErrorStop("  Expected new line or end of file but got " + tx.toString());
                }
            }
            tkn = tokenStream.removeNextToken();
            System.out.println("next  " + tkn);
            return new PutStatement(exp, PutStatement.HowToPut.INTO, varExp);
        }
        /// put 6 * 3 to std out
        return new PutStatement(exp, PutStatement.HowToPut.NON, null);
    }

    //---------------------------------------------------------------------------
    /**
     *  This parses any statement
     *   put
     *   add <exprt> to object
     *   get expression
     *   subtract, multiply, divide, +, sum
     * set propertyName to propertyValue
     * the sum of factor
    sum(expression)
     *      convert container to format
     * create new objectType
     *   send {<expr> | <token>} [ to <object> ] |
     *   do
     *
     * @return
     * @throws ParseError
     */
    public Statement parseStatement() throws ParseError
    {
        if (tokenStream.hasWord("Put"))
        {
            return parsePutStatement();
        }
        // if( tokenStream.hasWord("add") )  // add x to y
        return null;
    }

    //---------------------------------------------------------------------------
    /**
     * Parses a block ( equally indented )
     * @return
     * @throws ParseError
     */
    StatementBlock parseBlock(int currentIndent) throws ParseError
    {
        //System.out.println(  "parseBlock indent " + currentIndent);
        StatementBlock block = new StatementBlock();
        this.tokenStream.setSkipWhiteSpace(false);
        int i = 0;
        do // keep going while indent of white space == currentIndent
        {
            if (currentIndent > 0 && tokenStream.hasWhiteSpaceAvailable())
            {
                Lexer.WhiteSpace w = tokenStream.removeNextTokenAsWhiteSpace();
                //System.out.println( w  + " " + currentIndent);
                if (w.getCount() < currentIndent)
                {
                    tokenStream.pushTokenBackToHead(w);
                    return block;
                }
                if (w.getCount() > currentIndent)
                {
                    parseErrorStop("INDENTATION ERROR Block was more indented than expected");
                }
            }
            if (tokenStream.hasWord("Put"))
            {
                Statement s = parseStatement();
                block.add(s);
            } else
            {
                break;
            }
            this.tokenStream.setSkipWhiteSpace(false);
        } while (!tokenStream.hasEOFAvilable());
        return block;
    }

    //---------------------------------------------------------------------------
    /**
     *   put Hello world.
     *   parameter
     *        indentifier [as type] [ check expression ] |
     *                    [ check not empty ] | default expression
     *         option indentifier [as type] called idenifier default expression
     */
    //---------------------------------------------------------------------------
    /**
     *    function -- FUNCTION name [[WITH]|[ PARAMETER(s)] {PARAMDEF LIST}] EOL
     *             -- function name ( paremeters ... )
     *    To --- method name----
     *    on --- event name----- -----------------|
     *    {DOUBLE INDENT}                |---- parameterlist---|
     *
     *    {PARAMDEF LIST}
     *           IDENIFIER { AS TYPE } [  CHECK EXPRESSION  ] [ , ] PARAMDEF
     *
     *
     *
     *   paramter list
     *   param[eter] parameter name
     *   option option name
     */
    StatementBlock parseProcedure() throws ParseError
    {
        boolean oldWS = tokenStream.setSkipWhiteSpace(true);
        if (tokenStream.hasWord("function"))
        {
            Lexer.WordToken w = tokenStream.removeNextTokenAsWord();
            assert w.getText().equalsIgnoreCase("function");
            String functioname = getIdentifier();
            assert functioname != null;
            // CREATE A NEW LOCAL VARS.
            SymbolTable locals = this.pushSymbolTable();
            String[] prms = {"with", "param", "parameters", "Argument", "option"};
            if (tokenStream.hasWords(prms))
            {
                //@@@ TODO PARAMETER LIST
                assert false;
            }
            Lexer.Token t = tokenStream.removeNextToken();
            if (t.getTokenType() != Lexer.TT_EOL)
            {
                this.parseErrorStop("Expected NEW LINE BUT FOUND " + t.toString());
                // PARSE BIG PARAMETERS ..
            }
            // PARSE A BLOCK with indent + 4 of current indent
            this.popSymbolTable(); // go back to global table
            return null;
        }
        tokenStream.setSkipWhiteSpace(oldWS);
        return null;
    }

    //---------------------------------------------------------------------------
    /**
     * parse
     * function    Source -
     * @return
     * @throws ParseError
     */
    Statement parseSource() throws ParseError
    {
        return parseProcedure();
    }
    
    
     //---------------------------------------------------------------------------
    static boolean testBlock()
    {
        System.out.println(" -------------- BLOCK ----------------");
        try
        {
            BFLParser bl = BFLParser.fromSource("put 7 * 3\nput 6 * 5 into bob");
            StatementBlock stm = bl.parseBlock(0);
            assert stm != null;
            GeneralObject d = stm.evaluteStatement();
            bl = BFLParser.fromSource("    put 7 * 3\n\tput 6 * 5 into bob");
            stm = bl.parseBlock(4);
            assert stm != null;
        } catch (ParseError e)
        {
            System.out.println("TEST testParsePutStatement: PARSE ERROR->" + e);
            return false;
        }
        try
        {
            System.out.println("TEST ERROR OF INDENT");
            BFLParser bl = fromSource("    put 7 * 3\n");
            StatementBlock stm = bl.parseBlock(0);/// @@@ TODO - fix.
            assert stm != null;
            System.out.println("TEST testParsePutStatement: PARSE INTDENT ERROR DID NOT THROW");
            return false;
        } catch (ParseError e)
        {
            // this should be called
        }
        System.out.println(" -------------- END ----------------");
        return true;
    }

    //---------------------------------------------------------------------------
    static boolean testForPutStatement()
    {
        System.out.println(" -------------- PUT ----------------");
        try
        {
            // TEST 1.
            BFLParser bl = BFLParser.fromSource("   put 6 * 2 into student name \n");
            assert bl != null;
            Statement e = bl.parsePutStatement();
            assert e != null;
            GeneralObject gObj = e.evaluteStatement();
            // TEST 2.
            String source = "put  student name + 1  into  result \n";
            BufferedReader in = new BufferedReader(new StringReader(source));
            SymbolTable sym = new SymbolTable(null);
            Variable var = sym.getOrMakeIfNull("_student_name");
            var.setValue(BigDecimal.ONE);
            var.setType("number");
            BFLParser parser = new BFLParser(in, sym);
            assert parser != null;
            assert parser.isLocalVariableDebug("_student_name") == true : "No such var";
            e = parser.parsePutStatement();
            assert e != null;
            gObj = e.evaluteStatement();
            // TEST 3.
            bl = BFLParser.fromSource("   put 6 * 2,000  \n");
            assert bl != null;
            e = bl.parsePutStatement();
            assert e != null;
            gObj = e.evaluteStatement();
            //System.out.printf( "Result =  %s (%s) \n", d.toPlainString(), e.getType());
        } catch (ParseError e)
        {
            System.out.println("TEST SimpleExpression: PARSE ERROR->" + e);
            return false;
        }
        System.out.println(" -------------- ----------------");
        return true;
    }
    
    //---------------------------------------------------------------------------
   static BFLParser make(  BufferedReader source )
   { 
       BFLParser p = new BFLParser( source ); 
       return p ; 
   }
     //---------------------------------------------------------------------------
   /*
    * make a small parser for this scourse code. 
    */
   static BFLParser fromSource( String expr ) throws ParseError
   { 
       BufferedReader in = new BufferedReader(new StringReader(expr));
       BFLParser  bfl = BFLParser.make( in ) ;
       return bfl ; 
   }
   
   static boolean BFLParserSelfTest()
    { 
       return  BFLExpressionParser.BFLParserSelfTest() && 
               testForPutStatement(); 
       // &&   testBlock() ; 
    }
   
            
}

