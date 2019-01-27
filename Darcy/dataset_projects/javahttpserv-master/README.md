# Java 10 Simple HTTP Server

- Main goal is to use Java 9/10 to create a microservice
- Second goaln is to update the code to replace System.out.println(HelloWorld) into a web server
- Third goal: Integrate with Travis-CI

## Check java version

~~~
java --version

java 10.0.1 2018-04-17
Java(TM) SE Runtime Environment 18.3 (build 10.0.1+10)
Java HotSpot(TM) 64-Bit Server VM 18.3 (build 10.0.1+10, mixed mode)
~~~

~~~
javac --version

javac 10.0.1
~~~

## Simplistic HelloWorld project using Classical mode

First step is to understand module, jlink...to be able to build an App 
suitable for microservices, IOT and Kubernetes

Get into the simplistic application directory
~~~
cd app1
~~~

Verify the presence of the source code
~~~
ls src/helloworld

HelloWorld.java
~~~

Compile the source java code
~~~
javac -d classes src/helloworld/*.java
~~~

Check javac call worked
~~~
ls classes/helloworld/

HelloWorld.class
~~~

Let's run the simplistic application using the classical way
~~~
java --class-path classes helloworld.HelloWorld

Hello World
~~~

## Simplistic HelloWorld project using classical Jar files

Let's package the code as a jar file
~~~
mkdir jars
cd classes
jar cvf ../jars/helloworld.jar helloworld/
cd ..
~~~

~~~
java --class-path jars/helloworld.jar helloworld.HelloWorld
~~~

## Simplistic HelloWorld project using Java 9 modules

Get into the simplistic application directory
~~~
cd app1
~~~

Compile the source java code
~~~
javac -d mods/helloworld/ src/module-info.java src/helloworld/HelloWorld.java
~~~

Let's run the simplistic application using the java-module way
~~~
java --module-path mods -m helloworld/helloworld.HelloWorld
~~~

## Simplistic HelloWorld project using modules for microservices purpose (embeded JRE)

### Build the microservice on the local machine 

~~~
mkdir microservices
jlink --module-path mods --add-modules helloworld,java.base --output microservices/helloworld
~~~

### Test the microservice on the local machine 

Go to the microservice helloworld bin directory. Ensure you are using `./java` not `java`

~~~
cd microservices/helloworld/bin/
./java -m helloworld/helloworld.HelloWorld
Hello World
~~~

### Test the microservice on a remote machine with no java installed

Let's tar up the microservice folder
~~~
cd microservices
tar -czvf helloworld.tar.gz helloworld/
~~~

Transfer microservice to machine without Java. I happen have a coreos based VM:
~~~
scp helloworld.tar.gz coreos@192.168.122.10:/home/coreos
Password:
helloworld.tar.gz                                                                                                                               100%   16MB  16.0MB/s   00:00
~~~

~~~
ssh coreos@192.168.122.10
Password:
Last login: Sun Jul  1 18:34:37 UTC 2018 from 192.168.122.7 on ssh
Container Linux by CoreOS stable (1745.7.0)
Update Strategy: No Reboots

java
-bash: java: command not found
~~~

~~~
mkdir microservices
mv helloworld.tar.gz microservices/
cd microservices/
tar -xf helloworld.tar.gz
cd helloworld/bin/
~~~

~~~
ls

java  keytool
~~~

~~~
./java -m helloworld/helloworld.HelloWorld

Hello World
~~~

## Simple HelloWorld Server

Compile the server
~~~
cd app2
javac -d mods/simpleserver/ src/module-info.java src/simpleserver/*.java
~~~

Start in background and test
~~~
java --module-path mods -m simpleserver/simpleserver.Main &
curl -i http://localhost:4250/helloworld
~~~

Stop the server
~~~
fg
CTRL C
~~~

Package as a microservice
~~~
jlink --module-path mods --add-modules simpleserver,java.base --output microservices/simpleserver
cd microservices
tar -czvf simpleserver.tar.gz simpleserver/
~~~

## Simple AppServer

Compile the server
~~~
cd app3
javac -d mods/simplewebsrv/ src/module-info.java src/simplewebsrv/*.java
~~~

Start in background and test
~~~
java --module-path mods -m simplewebsrv/simplewebsrv.SimpleWebSrv &

curl -i http://localhost:9000/
curl -i http://localhost:9000/echoHeader
curl -i http://localhost:9000/echoGet
curl -i http://localhost:9000/echoGet?foo=bar
~~~

Stop the server
~~~
fg
CTRL C
~~~

Package as a microservice
~~~
jlink --module-path mods --add-modules simplewebsrv,java.base --output microservices/simplewebsrv
cd microservices
tar -czvf simplewebsrv.tar.gz simplewebsrv/
~~~

## Links

- [app1](http://openjdk.java.net/projects/jigsaw/quick-start)
- [app2](http://www.adam-bien.com/roller/abien/entry/a_built_in_java_httpserver)
- [app3](https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server)
