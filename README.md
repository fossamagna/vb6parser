ProLeap ANTLR4-based parser for Visual Basic 6.0
================================================

[![Build](https://img.shields.io/travis/uwol/vb6parser.svg)](https://travis-ci.org/uwol/vb6parser)
[![Coverage](https://coveralls.io/repos/github/uwol/vb6parser/badge.svg?branch=master)](https://coveralls.io/github/uwol/vb6parser?branch=master)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

This is a parser and grammar for Visual Basic 6.0, which generates an
Abstract Syntax Tree (AST) and Abstract Semantic Graph (ASG) for Visual Basic 6.0 code.
The AST represents plain Visual Basic 6.0 source code in a syntax tree structure.
The ASG is generated from the AST by semantic analysis and provides data and control
flow information (e. g. variable access).


Example
-------

### Input: VB6 code

```
Private Sub Command1_Click ()
   Text1.Text = "Hello, world!"
End Sub
```


### Output: Abstract Syntax Tree (AST)

```
(startRule
	(module
		(moduleBody
			(moduleBodyElement
				(subStmt
					(visibility Private) Sub
					(ambiguousIdentifier Command1_Click)
					(argList ( ))
					(block
						(blockStmt
							(letStmt
								(implicitCallStmt_InStmt
									(iCS_S_MembersCall
										(iCS_S_VariableOrProcedureCall
											(ambiguousIdentifier Text1))
										(iCS_S_MemberCall .
											(iCS_S_VariableOrProcedureCall
												(ambiguousIdentifier
													(ambiguousKeyword Text)))))) =
								(valueStmt
									(literal "Hello, world!"))))) End Sub)))) <EOF>)
```


Getting started
---------------

To include the parser in your Maven project edit your `pom.xml` file as follows

```
<repositories>
	<repository>
		<id>maven.proleap.io</id>
		<url>http://maven.proleap.io</url>
	</repository>
</repositories>
```

and add the dependency

```
<dependency>
	<groupId>io.github.uwol</groupId>
	<artifactId>vb6parser</artifactId>
	<version>2.1.0-SNAPSHOT</version>
</dependency>
```

Use the following code as a starting point for developing own code.

### Simple: Generate an Abstract Semantic Graph (ASG) from VB6 code

```java
// generate ASG from plain VB6 code
java.io.File inputFile = new java.io.File("src/test/resources/io/proleap/vb6/asg/HelloWorld.cls");
io.proleap.vb6.asg.metamodel.Program program = new io.proleap.vb6.asg.runner.impl.VbParserRunnerImpl().analyzeFile(inputFile);

// navigate on ASG
io.proleap.vb6.asg.metamodel.Module module = program.getClazzModule("HelloWorld");
io.proleap.vb6.asg.metamodel.Variable variableI = module.getVariable("I");
io.proleap.vb6.asg.metamodel.type.Type typeOfI = variableI.getType();
```

### Complex: Generate an Abstract Semantic Graph (ASG) and traverse the Abstract Syntax Tree (AST)

```java
// generate ASG from plain VB6 code
java.io.File inputFile = new java.io.File("src/test/resources/io/proleap/vb6/asg/HelloWorld.cls");
io.proleap.vb6.asg.metamodel.Program program = new io.proleap.vb6.asg.runner.impl.VbParserRunnerImpl().analyzeFile(inputFile);

// traverse the AST
io.proleap.vb6.VisualBasic6BaseVisitor<Boolean> visitor = new io.proleap.vb6.VisualBasic6BaseVisitor<Boolean>() {
  @Override
  public Boolean visitVariableSubStmt(final io.proleap.vb6.VisualBasic6Parser.VariableSubStmtContext ctx) {
    io.proleap.vb6.asg.metamodel.Variable variable = (io.proleap.vb6.asg.metamodel.Variable) program.getASGElementRegistry().getASGElement(ctx);
    String name = variable.getName();
    io.proleap.vb6.asg.metamodel.type.Type type = variable.getType();

    return visitChildren(ctx);
  }
};

for (final io.proleap.vb6.asg.metamodel.Module module : program.getModules()) {
  visitor.visit(module.getCtx());
}
```


Characteristics
---------------

1. The grammar is line-based and takes into account whitespace, so that
   member calls (e.g. "A.B") are distinguished from contextual object calls
   in WITH statements (e.g. "A .B").

2. Keywords can be used as identifiers depending on the context, enabling
   e.g. "A.Type", but not "Type.B".

3. The ANTLR4 grammar is derived from the Visual Basic 6.0 language reference
   http://msdn.microsoft.com/en-us/library/aa338033%28v=vs.60%29.aspx
   and tested against MSDN VB6 statement examples as well as several Visual
   Basic 6.0 code repositories.

4. For parsing large VB6 source code files, following VM args have to be set: `-Xmx2048m -XX:MaxPermSize=256m`.
   Intellij Plugin for ANTLR 4 has to be provided with those VM args in file `idea.vmoptions`.


Build process
-------------

The build process is based on Maven (version 3 or higher). Building requires a JDK 8 and generates a Maven JAR, which can be used in other Maven projects as a dependency.

* Clone or download the repository.
* In [Eclipse](https://eclipse.org) import the directory as a an `existing Maven project`.
* To build, run:

```
$ mvn clean package
```

* The test suite executes AST and ASG tests against VB6 test code and MSDN statement examples. Unit tests and parse tree files were generated by class `io.proleap.vb6.TestGenerator` from those VB6 test files.
* You should see output like this:

```
[INFO] Scanning for projects...
...
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running io.proleap.vb6.ast.calls.CallsTest
Parsing file Calls.cls.
Comparing parse tree with file Calls.cls.tree.
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.991 sec
Running io.proleap.vb6.ast.calls.Module1Test
...
Results :

Tests run: 215, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

* To install the JAR in your local Maven repository:

```
$ mvn clean install
```

* To use the JAR in your Maven project, add following dependency to the pom.xml of your project:

```
<dependency>
  <groupId>io.github.uwol</groupId>
  <artifactId>vb6parser</artifactId>
  <version>2.0.0</version>
</dependency>
```

* To only run the tests:

```
$ mvn clean test
```


Release process
---------------

* Milestones are published in the [ANTLR grammars repo](https://github.com/antlr/grammars-v4).


License
-------

Licensed under the BSD 3-Clause License. See LICENSE for details.

### And finally...

Patches accepted, or create an issue!
I'd love, if you could send me a note in which context you're using the parser. Thank you!
