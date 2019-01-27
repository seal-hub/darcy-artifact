# Java 9th's Module System

(this post is part of our mini Java 9 series - check out other posts!)

Modularity is quite a known concept in software engineering. Writing unique units encapsulating functionality and exposing it through various interfaces is something we try to archieve on many fields nowadays. This attitude has number of advantages. We:
 	
 - make our design cleaner and easier to maintain
 - choose what tools to use in a more concious way
 - make our application smaller because we avoid using unnecessary things
 - possibly make it more scalable and improve performance

Java creators certainly know that. That's why there's [Jigsaw](http://openjdk.java.net/projects/jigsaw/). It is a huge project started at Sun way back in August 2008. Delayed due to technical and non-technical reasons (eg. integrating Sun into Oracle) it's finally there to make Java modular. Encompassing 6 JEPs, it will be a part of Java 9 release which is planned to happen in about 90 days. 

## Breaking The Monolith

As we take a look at Java Platform and the JDK we are probably not satisfied about it's structure. This is not a suprise - over the years Java was designed without mechanisms that enforced modular design. The spirit of always being backwards compatible didn't help either. Every release has just made the it bigger and more tangled. No wonder the primary goal of the Jigsaw project is to cut the Java Platform and the JDK into smaller and more organized modules. Those could be used to build software without the need to include all APIs that are there in Java.

This job is a tough one. After the decision about defering Project Jigsaw from Java 8 to Java 9 in 2012, Mark Reinhold, the Chief Architect of the Java Platform Group at Oracle explained why it was so hard:

>There are two main reasons. The first is that the JDK code base is deeply interconnected at both the API and the implementation levels, having been built over many years primarily in the style of a monolithic software system. We’ve spent considerable effort eliminating or at least simplifying as many API and implementation dependences as possible, so that both the Platform and its implementations can be presented as a coherent set of interdependent modules, but some particularly thorny cases remain.
>
>We want to maintain as much compatibility with prior releases as possible, most especially for existing classpath-based applications but also, to the extent feasible, for applications composed of modules.

_From ["late for the train qa"](http://mreinhold.org/blog/late-for-the-train-qa)_

Now it seems that they succeded in doing the job. All Java 9 modules are located in your `$JAVA_HOME/jmods` directory:

```
$ ls -l $JAVA_HOME/jmods
```

Each module has it's interface that is exposed to other modules. They _require_ each other and communicate only through _exported_ packages. Developers can compile, package, deploy and execute applications that consist only of the selected modules and nothing else. This is a huge change.

## Getting modular

There will be means to make our own code modular too. Let's explain the basics on a simple Java application made of two modules:
	
1. com.timeteller.clock: a module which contains _SpeakingClock_ - simple class for printing current time to the stdout.
2. com.timeteller.main: a module utilizing the functionality offered by com.timeteller.clock module.

The code, building and running instructions are located here 
(TODO: LINK DO REPOZYTORIUM Z KODEM - GDZIE MAM WYSTAWIĆ TEN KOD - pytanie do M.O.?)

The overall project structure looks like this: 

```
$ tree
.
├── com.timeteller.clock
│   ├── com
│   │   └── timeteller
│   │       └── clock
│   │           └── SpeakingClock.java
│   └── module-info.java
└── com.timeteller.main
    ├── com
    │   └── timeteller
    │       └── main
    │           └── Main.java
    └── module-info.java

8 directories, 5 files
```

Classes _Main_ and _SpeakingClock_ are almost irrelevant when it comes to talking about modules. Jigsaw does not make them any different from previous java implementations. All we have to know that the _main()_ method in the _Main_ class uses the _SpeakingClock's_ method from the other module: 

```
public static void main (String[] args) {
    SpeakingClock clock = new SpeakingClock();
    clock.tellTheTime(); // displays the time to stdout.
}
```

The most important part is hidden in the module descriptor files _(module-info.java)_. They contain all the module-related metadata. They're .java files but a little different, like this:

```
module com.timeteller.clock {
    exports com.timeteller.clock;
}

```
or this:

```
module com.timeteller.main {
    requires com.timeteller.clock;
}

```

It is a trivial configuration but you can learn the following from it:

- module descriptor files are placed in the root folder of the module (by convention)	 
- every module has a unique name
- module descriptors define what packages are _exported_ from the module and what modules do they _require_

The last bullet is strictly about the encapsulation I mentioned earlier. If you do not export your packages, they will remain hidden in your module -  unavailable to other modules. Analogically with requiring. If something is exported that doesn't mean that you can use it everywhere. You have to explicitly require it (except with the java.base module - for convinience every module automatically requires it). If you won't do the above, the application won't even compile.

Let's also notice that the _public_ keyword changes it's meaning in java 9. Before modules, it meant that a public code is visible everywhere. Now it means that the code is not visible outside the module if the package is not exported. It's a good thing - this gives the possibility for hiding internal APIs. This is also one of the goals of project Jigsaw described in [JEP 260] (http://openjdk.java.net/jeps/260). It's also a source of fear of many developers - some APIs, eg. from _sun.misc_ package get hidden what may introduce complications while migrating to Java 9.

### Building the app
	
We can create JARs the following way:

```
$ mkdir -p jars

$ mkdir -p build/classes/com.timeteller.clock
$ javac -d build/classes/com.timeteller.clock/ com.timeteller.clock/module-info.java com.timeteller.clock/com/timeteller/clock/SpeakingClock.java
$ jar -c -f jars/clock.jar -C build/classes/com.timeteller.clock/ .

$ mkdir -p build/classes/com.timeteller.main
$ javac -d build/classes/com.timeteller.main/ --module-path=jars com.timeteller.main/module-info.java com.timeteller.main/com/timeteller/main/Main.java
$ jar -c -f jars/timeTeller.jar --main-class com.timeteller.main.Main -C build/classes/com.timeteller.main .

```

To let the compiler know about the modules we must place them on the module path. It's a concept simillar to classpath but for modules. When we build the `com.timeteller.main` module we put a jar with `com.timeteller.clock` on its module path. Otherwise it doesn't find the necessary code and results in a compilation error:

```
$ javac -d build/classes/com.timeteller.main/ com.timeteller.main/module-info.java com.timeteller.main/com/timeteller/main/Main.java
com.timeteller.main/module-info.java:2: error: module not found: com.timeteller.clock
  requires com.timeteller.clock;
                         ^
1 error
```

Notice that the JARs we created here are modular too. They are just like the regular JARs except that they include module-info.class file (compiled module descriptor). This way they can be put both on modulepath and classpath.

## Tailor made run-time images

Project Jigsaw will equip Java 9 in some other useful things. Among them is [The Java Linker](http://openjdk.java.net/jeps/282) - a tool which can be used to take only needed modules and create a custom run-time image. Instead of using the whole java run-time (the 51 MB rt.jar) we can create our own "tailor made" one:

```
$ jlink --module-path $JAVA_HOME/jmods:mlib:jars --add-modules com.timeteller.main --output timeteller-runtime
```

and use it to run our application:

```
$ ./timeteller-runtime/bin/java --module-path jars/ -m com.timeteller.main
Sun Apr 09 18:31:16 CEST 2017

```
The run-time size decreases - now it's 35MB. This optimization is especially useful on devices that run low on memory. We can go further and try to make it even smaller getting to 21MB:

```
$ jlink --module-path $JAVA_HOME/jmods:mlib:jars --add-modules com.timeteller.main --output timeteller-runtime-compressed --compress=2 --strip-debug
```

The `--compress` flag enables compression of resources on three different levels: from no-compression to ZIP level compression. `--strip-debug` flag allows to remove debug information from the runtime which would not be needed in production system. 


## Summary

Java Platform Module System with modularized JDK and the Java platform itself is a long awaited change that is finally going to be there in the upcomming release. In this post we shortly described it and showed _jlink_ tool as a "cherry on the top". 

If you're concerned about any problems that may appear while migrating your software to Java 9, there is a [guide](https://docs.oracle.com/javase/9/migrate/toc.htm#JSMIG-GUID-7744EF96-5899-4FB2-B34E-86D49B2E89B6) straight from Oracle. They strongly encourage developers to try it as soon as possible due to possible incompatibilities. 

We are looking forward to see how the Module System is going to adopt in the world of Java software. 


