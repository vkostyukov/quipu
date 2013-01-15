Quipu Programming Language
==========================

Quipu - is an esoteric programming language inspired by [Quipus](http://en.wikipedia.org/wiki/Quipu)
(also known as "talking knots") - recording devices historically used by [Incs](http://en.wikipedia.org/wiki/Inca_Empire).

The Quipu Language Specifications
-----------------------------

Programm on Quipu

Code Samples
------------

Prints "Hello World!" (without quotes)

    a. 
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
    <<

Prints numbers from 0 to 99 to console

    p.  i.  c.

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

Calculates the factorial of given number 

    n.  a.  b.  c.  q.  f.  o.

    >>  $b  1&  $q  1&  $a  &1
    =o  **  --  =q  $q  <<  <<
        1&  $n  $b  --  ::
        --  --  >a  >a
        $q  $q
        **  **
        1&  $n
        ++  ++

Prints the Fibonacci numbers for given limit

    a.  b.  z.  x.  y.  w.  v.  q.  e.

    >>  1&  $x  $y  $x  $q  $x  1&  $q
        --      1&  $z  =v  <<  $q  >q
        $a      --  ++  ',  $q  --  '.
        --      $q      <<  >b  >b  <<
        $q      **      ' 
        **      1&      <<
        $a      ++
        ++
        =e

How to compile and run
----------------------

Cmpile the sources:

    mvn assembly:single
    
And run Quipu porgramm:

    java -jar target/quipu-0.0.1-jar-with-dependencies.jar programm.qp

----
by [Vladimir Kostyukov](http://vkostyukov.ru), 2013
