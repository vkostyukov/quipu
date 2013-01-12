Quipu Programming Language
==========================

Quipu - is an esoteric programming language inspired by [Quipus](http://en.wikipedia.org/wiki/Quipu)
(also known as "talking knots") - recording devices historically used by [Incs](http://en.wikipedia.org/wiki/Inca_Empire)).

Overview
--------

Code Samples
------------

Prints "Hello World!" (without quotes)

    a:  a.
    'H  <<
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

Prints numbers from 0 to 99 to console

    p.  i.  c.  e.

    $i  1&  $i
    <<  ++  1%
    \n      --
    <<      =e
    ?i      ?p

Calculates the sum of numbers from 0 to 99 and prints the result to console

    s.  i.  c.  e.

    $i  1&  $i  $s
    ++  ++  1%  <<
            --
            =e
            ?s

Calculates the factorial of number 

    a:  a.  b:  b.   e.

    >>  $a  1&  $a   $b
        =e      1&   <<
        1&      ++
        --      $b
        =e      **
                ?a

Prints the Fibonacci numbers

    s.  f.  d.  a:  b:  a.  b.  x:  i.  t.  o.  e.

    $x  $x  ',  1&  1&  $b  $f  >>  1&  1&  1&  '.
    =e  2&  '               ?i      ++  <<  <<  <<
    1&  --  <<                      ?f  ',  ?e
    --  $i  $f                          '
    =o  --  <<                          ?o
    1&  =e
    --  $a
    =t  $b
    1&  ++
    <<
    ',
    '
    <<
    1&
    <<

How to compile and run
----------------------

Cmpile the sources:

    mvn assembly:single
    
And run Quipu porgramm:

    java -jar target/quipu-0.0.1-jar-with-dependencies.jar programm.qp

----
by [Vladimir Kostyukov](http://vkostyukov.ru), 2013
