Amount of WTFs per Project
==========================
This library is a by-product that derived from what I was doing few days ago:

I was dabbling with Java and decided to take a break. I came across a well know image that 
depicts code review session behind closed doors. The image called "The only valid measurement of code 
quality: WTF/minute". I decided to make an extension to the latter concept.

This library brings you the ability to mark code smells in your source code (Classes, methods, fields etc.) 
with 'WTF' annotation. The WTF annotation accepts an arbitary message, if none provided, the default
message 'Dude.. WTF?!' is used instead. When source compiles, the compiler generates a warning using the message from detected 
annotation(s) and information about marked element type. The following is a sample output from compiling a class 
containing WTF annotations:

	Warning: : In CLASS [wtf.per.project.model.DummyPojoImpl] :
	CLASS level => WTF?! Are you for real?! This naming convention is bad!
	Warning: : In CLASS [wtf.per.project.model.DummyPojoImpl] :
	FIELD 'SOME_CONSTANT' => WTF?! What is this non-descriptive name?
	Warning: : In CLASS [wtf.per.project.model.DummyPojoImpl] :
	CONSTRUCTOR 'DummyPojoImpl(java.lang.String)' => WTF?! Dude.. WTF?!

The library also provides a custom JUnit test runner class. The runner takes a package name through @ScanPackage
annotation and scans all classes/interfaces under the package and its sub-packages for WTFs occurances. The runner uses 
a test class internally to assert whether the code is still infested with WTFs. I implemented my own metadata analysis 
logic in order not to be dependent on third party libraries like Reflections (which is a very good tool btw!)

If assertion fails (WTFs found present in the code), the test class generates metrics about how many WTFs are there 
and where. These metrics are part of the assertion failure message. For example, the following is the example of 
the custom JUnit runner:

	@RunWith(WTFsPerProject.class) 
	@ScanPackage("wtf.per.project")
	public final class WTFsPerProjectRunner {
	
	}

I have few POJOs marked with WTF annoation, so the following is the produced output after running the above class:

	junit.framework.AssertionFailedError: 
	
	Dude.. WTF!? Sources in package [wtf.per.project.model] are infested with [23] WTFs:

	[ANNOTATION] interface wtf.per.project.model.SomeAnnotation
	[CLASS] class wtf.per.project.model.DummyPojoImpl
	[CONSTRUCTOR] private wtf.per.project.model.DummyPojoImpl(java.lang.Integer,java.lang.String)
	[CONSTRUCTOR] public wtf.per.project.model.DummyPojoChild(java.lang.String)
	[CONSTRUCTOR] public wtf.per.project.model.DummyPojoImpl()
	[CONSTRUCTOR] public wtf.per.project.model.DummyPojoImpl(java.lang.Integer)
	[CONSTRUCTOR] public wtf.per.project.model.DummyPojoImpl(java.lang.String)
	[ENUM] class wtf.per.project.model.DummyPojoImpl$Names
	[ENUM] class wtf.per.project.model.Surnames
	[FIELD] private java.lang.String wtf.per.project.model.DummyPojoImpl.name
	[FIELD] private static final java.lang.String wtf.per.project.model.DummyPojoImpl.SOME_CONSTANT
	[FIELD] public static final wtf.per.project.model.Surnames wtf.per.project.model.Surnames.JOHNSON
	[INTERFACE] interface wtf.per.project.model.DummyPojo	
	[METHOD] private void wtf.per.project.model.DummyPojoImpl.somePrivateMethod()
	[PARAMETER] public void wtf.per.project.model.DummyPojoImpl.setName(java.lang.String)

	expected:<0> but was:<15>

Disclaimer
----------
I created this library for fun. Nothing more. If someone actually decides to use it - great.

Dependencies
------------
* None

Requirements                                                                                                            
------------
* Java 1.6 (or higher). This will not run on 1.5 or lesser.

WARNING
-------
In my implementation I am using a service provider implementation which is identified by placing a provider-configuration 
file in the resource directory META-INF/services. With JSR 269, annotation processors are discovered by searching the 
classpath for the latter file (META-INF/services/javax.annotation.processing.Processor). The contents of this file enumerate 
available annotation processors (in other words this file is a provider-configuration file). 

If you try to compile the code using Maven, most probably you are going to get the following error:

	Bad service configuration file, or exception thrown while constructing Processor object: 
	javax.annotation.processing.Processor: Provider wtf.per.project.annotation.processing.WTFProcessor 
	not found

**RESOLUTION**
Apperantly Maven has a bug. The following resource talks about workaround to the problem when Maven cannot find annotation
processor: http://cdivilly.wordpress.com/2010/03/16/maven-and-jsr-269-annotation-processors/

The workaround is to modify the maven build lifecycle to compile the project in two passes. The first pass compiles 
just the annotation processors (with annotation processing disabled), the second compiles the rest of the project 
(with annotation processing enabled). The first pass makes the compiled annotation processors available for 
the second pass. Check my POM file.

How to add support into your application
----------------------------------------
Just add the wtf-per-project-x.x-jar-with-dependencies.jar to your classpath and start annotating your source code away.

