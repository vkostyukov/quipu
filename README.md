Quipu Programming Language
==========================

[Quipu](http://esolangs.org/wiki/Quipu) - is an esoteric programming language inspired by [Quipus](http://en.wikipedia.org/wiki/Quipu)
(also known as "talking knots") - recording devices historically used by [Incs](http://en.wikipedia.org/wiki/Inca_Empire). 

Code Samples
------------

Prints "Hello World!" (without quotes)

    'H
    'e
    'l
    'l
    'o
    ' 
    'W
    'o
    'r
    'l
    'd
    '!
    \n
    /\

Calculates the factorial of given number 

    "0  1  2  3  4  5  6"

    \/ 2& 1& 4& 1& 1& 1&
    6& [] -- [] ^^ [] /\
    == ** 0& 4& -- /\    
       1& [] == 1& :: 
       -- -- 2& >>        
       4& 4& []           
       [] [] 1&           
       ** ** >>          
       1& 0&
       ++ []
          ++

Prints the first N members of Fibonacci sequence

    "0  1  2  3  4  5  6  7  8"

    \/ 1& 3& 4& 3& 7& 3& 1& 7&
       -- [] [] [] [] [] ^^ []
       0&    1& 2& 6& /\ -- 7&
       []    -- [] == 7& 1& >>
       --    7& ++ ', [] >> '.
       7&    []    /\ 1&    /\
       []    **    '  >>
       **    1&    /\
       0&    ++
       []
       ++
       8&
       ==

How to compile and run
----------------------

Cmpile the sources:

    mvn assembly:single
    
And run Quipu porgram:

    java -jar target/quipu-0.0.1-jar-with-dependencies.jar programm.qp

----
by [Vladimir Kostyukov](http://vkostyukov.ru), 2013
