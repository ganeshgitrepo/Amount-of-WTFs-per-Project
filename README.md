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
annotation and scans all classes/interfaces under the package for WTFs occurances. The runner uses a test class 
internally to assert whether the code is still infested with WTFs. 

If assertion fails (WTFs found present in the code), the test class generates metrics about how many WTFs are there 
and where. These metrics are part of the assertion failure message. For example, the following is the example of 
the custom JUnit runner:

	@RunWith(WTFsPerProject.class) 
	@ScanPackage("wtf.per.project")
	public final class WTFsPerProjectRunner {
	
	}

I have few POJOs marked with WTF annoation, so the following is the produced output after running the above class:

	junit.framework.AssertionFailedError:
	Dude.. WTF!? Sources in package [wtf.per.project] are infested with [15] WTFs:
	wtf.per.project.model.DummyPojo
	wtf.per.project.model.DummyPojo.someInterfaceMethod()
	wtf.per.project.model.DummyPojoChild.<init>(java.lang.String)
	wtf.per.project.model.DummyPojoChild.someAbstractMethod()
	wtf.per.project.model.DummyPojoChild.thisIsPrivateStaticMethod()
	wtf.per.project.model.DummyPojoImpl
	wtf.per.project.model.DummyPojoImpl.<init>()
	wtf.per.project.model.DummyPojoImpl.<init>(java.lang.Integer)
	wtf.per.project.model.DummyPojoImpl.<init>(java.lang.String) 
	wtf.per.project.model.DummyPojoImpl.SOME_CONSTANT
	wtf.per.project.model.DummyPojoImpl.getName()
	wtf.per.project.model.DummyPojoImpl.name
	wtf.per.project.model.DummyPojoImpl.setName(java.lang.String)
	wtf.per.project.model.DummyPojoImpl.someAbstractMethod()
	wtf.per.project.model.DummyPojoImpl.somePrivateMethod() 
	expected:<0> but was:<15>

Disclaimer
----------
I created this library for fun. Nothing more. If someone actually decides to use it - great.

Dependencies
------------
* Java 1.6 (or higher). This will not run on 1.5 and lesser.
* Reflections project for Java runtime metadata analysis (a fork from Scannotations). It seems that it is too much 
overhead for what I am trying to do so perhaps in the near future I will implement my own meta data analysis. The
Reflections library is really great, but because of this size the wtf-per-project-x.x-jar-with-dependencies.jar is more 
than 3.6 MB. 

WARNING
-------
In my implementation I am using a service provider implementation which is identified by placing a 
provider-configuration file in the resource directory META-INF/services. As of 20 of May 2012, I am having trouble to
compile the code via command line using Maven, unlike when I am using IntelliJ which succesfully compiles the code.

The error is:

	Bad service configuration file, or exception thrown while constructing Processor object: 
	javax.annotation.processing.Processor: Provider wtf.per.project.annotation.processing.WTFProcessor 
	not found

Maven probably needs to be told where to find annotation processor WTFProcessor class. I need to look into that. Having 
said that, when I compile using IDE, I dont experience compilation errors.

How to add support into your application
----------------------------------------
Just add the wtf-per-project-x.x-jar-with-dependencies.jar to your classpath and start annotating your source code away.

