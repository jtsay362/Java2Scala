# Java2Scala

[![Build Status](https://travis-ci.org/jtsay362/Java2Scala.png?branch=master)](https://travis-ci.org/jtsay362/Java2Scala)

This command-line tool converts Java code to Scala code. Instead of compiling the Java code and producing an AST, the project uses regular expressions to look for patterns and converts those patterns to Scala. The disadvantage of doing this is that the Java syntax is not really understood, so comments in unexpected places and some hairy expressions may mess up conversion. The advantages of using regexes are:

<ul>
<li>Much simpler to code (no Java parser to produce an AST is required)</li>
<li>Comments and formatting are preserved exactly</li>
<li> More immune to Java language changes</li>
</ul>

Right now, this project uses dependencies from earlier work I did (com.taco.*) that are not publicly available, but I'll work on making them available soon.

This code was used convert the existing Java code in my website to Scala. Now the website is written 100% in Scala (with some Java library dependencies of course).

