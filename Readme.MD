# BFL
BFL (Big Falcon Language) or (Beginners Falcon Language) Is a new programming language inspired by HyperTalk and Python. It's designed to be a powerful and correct learner language. This is a work in progress.

## BFL MVP
BFL is intended to be a full programming language - if you see the documentation you will see the developing tutorial. The language began by writing the tutorial not all aspects of the introduction exist or are certain to make it into version 1.0

The MVP ( _Minimal Viable Product_ ) is an interactive command line based calculator. It shows off some of the aspects of BFL philosophy which will be apprent in the final language.

## Language aspects

1. Unicode based - This permits BFL to introduce a number of new ( correct ) symbols for example ÷ for divide ∈ for element of, and √  for square root.
2. English based. Inspired by languages like Hypertalk and to some extent, the sprit of Cobol. BFL uses/permits an English like syntax.
3. English like syntax means BFL is good for beginners
4. English like Syntax means BFL is good for those to only program occasionally. We are committed to makeing the benefits of programming open to all.
5. Most importantly this means that professional code can be reviewed by non experts and occasional users.
6. Dedication to correctness. While BFL is intended to be a very friendly language for casual programming we are seeing more and more errors effecting end users. To this end BFL introduces some aspects to make code more correct. That is, the code should do what the programmer expects and intendeds. BFL is full of aspects which encorage good programming pratices in new and casual users.

## Examples
It is proberbly best to see some of BFL in BFCalc - the MVP we are progressively releasing.
Lets begin in BFCalc **seebelow on how to get it running** When you start you will get this screen.

    `---------------BFL WELCOME-----------------------
    TRY ME with 2 * 2
    QUIT on a line to finish

Let's being with some basic items.

` 2 * 2 `
will give

    `Answer =  4 (int)`

This says that the answer is both 4 and it's of type int( Integer or counting number) More about this later.

#### Numbers

One of the aspects which confuses new users is the limitations of efficent machine sizes. While a long time ago this might have been a problem it seems rediculus to be bound by them. In BFL all numbers are unlimited by default.

Try this

`5924^42`
 The carrot ( up arrow Shift 6 on my keyboard ) is power of operator. The You get the answer back

    `Answer =  28172935025451224288322000331500310127272037669748524
    15669096698921616634134483676223002154815729662648436
    22150497528848050828810216162499526967512795789131776 (int) `

BFL by default uses numbers which can be of any size. Later on you can use a with statement to configure the use of machine efficent numbers like floats and ints ( don't worry about this for now) when you want your computation code to run faster.

In BFL numbers can be seperated by commas like this

`100,000,000 + 300,000.000,0001`

Notice the fullstop/period in the middle this indicates a number of decimal places. Again we can have as many as we like.

    `Answer =  100300000.0000real`

Witness also that the number is of type real.
Every day millions of dollars pounds and yen are lost to what are known as 'fast fingered errors' Where people and programmers get the number of digits wrong.

### Currency

In the old days computers were used for one thing. Mostly computing huge calculations for physics. Modern computing is much more general. So BFL understands that $ means dollar and £ means pounds sterling (money). This shows how BFL can use knowlge of the real world to make sure mistakes don't happen. Try this

`$10,000 + $200.00 `

     `Answer =  $10200.00`
Notice BFL does not have to tell you the 'type' of the calcuation - it's in Dollars . o O { note output formatting is still a work in progress}.
Now try this, adding pounds to dollars.
`$10,000 + £200.00`

You should get

    `blfexperiment.ParseError:  Cannot combine types
     Error: dollar does not match poundSterling
      @20 line 1
type QUIT to stop.
`
That's right BFL undersands that you cannot add one currency to another with out some kind of conversion.

while this is ok.

`$10,000 * 10`

This makes no sense.

`$10,000 * $10`

    `I didn't  understand (sorry) $10,000 * $10
    blfexperiment.ParseError:  Cannot combine types
     Error: Multiplying or dividing dollar by dollar
      does not make sense lifeform @16 line 1
`
Note these are compile time checks. If you do need to multiply currency by currency then you will have to convert the currency to a number first. Which is OK. The type checking is there to pause and make sure you know what your doing.

There is also normal units. Try this.

`100kg + 100kg`

BFL understands most common units. Both metric and British (aka imperial). Again Units cannot mix but they can be converted using the as proposision.

`100mm +  100ft as mm`

for experts the `as` command is the equivalent of `(int)` except it is fully able to automatically convert from one basically one using to another and complaining if it's not possible.

## Truth and lies

At the heart of most languages is the ability to ask logical questions like this  

`45 is more than 34 `

    `Answer to your (question)  YES `

In most computer languages you would write it like this.

`45 > 56`

    `Answer to your (question)  NO `

You can also have unicode symbols such as greater than or equal to.  

`45 ≥ 45`

Notice that the responses are **YES** and **NO**. Yes means yes ( true in most languages )  no means false. This was intended to more natural. For example is the engine on giving true is confusing and YES is more direct and simple.

`Yes and No --  NO`

`Yes or No  -- YES`

You can use true and false if you need to for example

Not true

New to BFL is the ~ ( almost equal) one of the problems with most langages is the comparison of floating point numbers for example

`3  = 3.00000000000001`

    `Answer 3 to your (question)  NO`

BFL has the almost equal operator.

`3 ~ 3.00000000000001`  

    `Answer 3 to your (question)  YES`  

By default ~ gives YES if it is within 1% of the first number.

## BFL Authors
BFL is the product of Flock. We are unappologetic  Dyslexic programmers who believe that programming is for everyone. Even grammar Natzis. We can't seen any spelling errors, if you do then why not take some time to uploading some corrections. We promise not to mind (much).
