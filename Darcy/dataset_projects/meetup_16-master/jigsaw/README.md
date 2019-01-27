# Java 9 / Jigsaw + jlink + Gradle + Docker

```
export JAVA_HOME=/opt/java/jdk-9
export PATH=$JAVA_HOME/bin:$PATH
```

## Assemble and run

```
./gradlew -q clean assemble

./gradlew -q clean :by.jprof.app:run

```

## jdeps introspections

```
jdeps by.jprof.api/build/libs/by.jprof.api.jar

by.jprof.api
 [file:///home/madhead/Projects/playgrounds/jigsaw/by.jprof.api/build/libs/by.jprof.api.jar]
   requires mandated java.base (@9)
by.jprof.api -> java.base
   by.jprof.api
```
```
jdeps --module-path by.jprof.api/build/libs/by.jprof.api.jar:by.jprof.impl.en/build/libs/by.jprof.impl.en.jar:by.jprof.impl.be/build/libs/by.jprof.impl.be.jar:by.jprof.impl.chr/build/libs/by.jprof.impl.chr.jar by.jprof.app/build/libs/by.jprof.app.jar

jar by.jprof.app/build/libs/by.jprof.app.jar
by.jprof.app
 [file:///home/madhead/Projects/meetup_16/jigsaw/by.jprof.app/build/libs/by.jprof.app.jar]
   requires by.jprof.api
   requires by.jprof.impl.be
   requires by.jprof.impl.chr
   requires by.jprof.impl.en
   requires mandated java.base (@9)
   requires java.xml (@9)
by.jprof.app -> by.jprof.api
by.jprof.app -> java.base
by.jprof.app -> java.xml
   by.jprof.app                                       -> by.jprof.api                                       by.jprof.api
   by.jprof.app                                       -> java.io                                            java.base
   by.jprof.app                                       -> java.lang                                          java.base
   by.jprof.app                                       -> java.util                                          java.base
   by.jprof.app                                       -> javax.xml.parsers                                  java.xml
   by.jprof.app                                       -> javax.xml.transform                                java.xml
   by.jprof.app                                       -> javax.xml.transform.dom                            java.xml
   by.jprof.app                                       -> javax.xml.transform.stream                         java.xml
   by.jprof.app                                       -> org.w3c.dom                                        java.xml
```

## jlink

```
jlink --module-path ${JAVA_HOME}/jmods:by.jprof.api/build/libs/by.jprof.api.jar:by.jprof.impl.en/build/libs/by.jprof.impl.en.jar:by.jprof.impl.be/build/libs/by.jprof.impl.be.jar:by.jprof.impl.chr/build/libs/by.jprof.impl.chr.jar:by.jprof.app/build/libs/by.jprof.app.jar --add-modules by.jprof.app --output build --launcher app=by.jprof.app/by.jprof.app.Main

build/bin/app
Hello, world!
Вітаем, world!
ᎣᏏᏲ, world!
```

There is a Gradle task for jlink package:

```
./gradlew -q dist

build/bin/app
Hello, world!
Вітаем, world!
ᎣᏏᏲ, world!
```

## Docker
```
docker build -t jprof .

docker run jprof
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<greetings>
    <greeting>Hello, world!</greeting>
    <greeting>Вітаем, world!</greeting>
    <greeting>ᎣᏏᏲ, world!</greeting>
</greetings>
```
