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

99-bottles-of-beer

    "0 1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21"

    'n 'N '  '  's '  '  ', '. 'T 'G 1& 1% 1@ 1@ 9& 2& 9& 1& 9& 1& 1@
    'o 'o 'm 'b    'o 'o '  \n 'a 'o ++ ;; 2& 2& [] /\ [] /\ [] [] []
          'o 'o    'f 'n       'k '     1@ [] [] /\ 3& /\ 3& /\ /\ /\
          'r 't    '  '        'e 't    1& 2& /\ 7& [] 7& [] 7& 2& 7&
          'e 't    'b 't       '  'o    [] -- 3& [] /\ [] /\ [] [] []
             'l    'e 'h       'o '     -- 1@ [] /\ 4& /\ 5& /\ /\ /\
             'e    'e 'e       'n 't       6& /\ 1@ [] 1& [] 0& 3& 9@
                   'r '        'e 'h       == 4& 2& /\ /\ /\ [] [] 9&
                      'w       '  'e          [] [] 5& 3& 6& /\ /\ /\
                      'a       'd '           /\ 1& [] [] [] 2& 4& 3&
                      'l       'o 's          5& -- /\ /\ /\ [] [] []
                      'l       'w 't          [] /\ 6& 5& 7& /\ /\ /\
                               'n 'o          /\ 3& [] [] [] 3& 5& 4&
                               '  'r          6& [] /\ /\ /\ [] [] []
                               'a 'e          [] /\ 7& 6& 1& /\ /\ /\
                               'n '           /\ 4& [] [] /\ 4& 6& 5&
                               'd 'a          7& [] /\ /\ 3& [] [] []
                               '  'n          [] /\ 2& 8& [] /\ /\ /\
                               'p 'd          /\ 5& /\ [] /\ 5& 7& 6&
                               'a '           1@ [] 3& /\ 5& [] [] []
                               's 'b          2& /\ [] \n [] /\ /\ /\
                               's 'u          [] 6& /\ /\ /\ 6& 0& 8&
                               '  'y          /\ [] 4&    8& [] [] []
                               'i '           3& /\ []    [] /\ /\ /\
                               't 's          [] 8& /\    /\ 8& 2&
                               '  'o          /\ [] 5&       [] []
                               'a 'm          4& /\ []       /\ /\
                               'r 'e          [] \n /\       \n 3&
                               'r '           /\ /\ 8&       /\ []
                               'o 'm          5& 1@ []          /\
                               'u 'o          [] 1& /\          4&
                               'n 'r          /\ ??             []
                               'd 'e          8&                /\
                                              []                5&
                                              /\                []
                                                                /\
                                                                8&
                                                                []
                                                                /\

How to compile and run
----------------------

Cmpile the sources:

    mvn assembly:single
    
And run Quipu porgram:

    java -jar target/quipu-0.0.1-jar-with-dependencies.jar programm.qp

----
by [Vladimir Kostyukov](http://vkostyukov.ru), 2013
