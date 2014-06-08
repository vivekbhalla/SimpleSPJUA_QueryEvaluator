SimpleSPJUA_QueryEvaluator
==========================

A simple Query Evaluator for Select, Project, Join, Union, and Aggregate operations

In this project, we have implemented a simple SQL query evaluator with support for
Select, Project, Join, Union, and Aggregate operations. That is, the code will receive a
SQL ﬁle with a set of CREATE TABLE statements deﬁning a schema for the data, and one
or more SELECT statements.

The program will evaluate the SELECT statements on provided data, and produce
output in a standardized form.

Parser
A parser converts a human-readable string into a structured representation of the program (or query)
that the string describes. An open-source SQL parser ([JSQLParser](http://jsqlparser.sourceforge.net)
is being used for this project. Documentation on how to use this parser is available at the above link.

Example invocation

  $> ls data
  R.dat
  S.dat
  T.dat
  $> cat R.dat
  1|1|5
  1|2|6
  2|3|7
  $> cat query.sql
  CREATE TABLE R(A int, B int)
  SELECT B, C FROM R WHERE A = 1
  $> java -cp build:jsqlparser.jar edu.buffalo.cse562.Main --data data query.sql
  1|5
  2|6


Testing done on - TPC-H

TPC-H is an industry standard benchmark for relational decision support queries. The
benchmark specification is available from [here](http://www.tpc.org/tpch/)

A version of the TPC-H data generator is included with the project in the folder dbgen. A UNIX
platform (e.g., Linux, OSX, Cygwin) is required to compile it. Default parameters have
been set that should work on most platforms: Compile dbgen using make.

The JSQL Parser is vailable in the external libraries folder.
