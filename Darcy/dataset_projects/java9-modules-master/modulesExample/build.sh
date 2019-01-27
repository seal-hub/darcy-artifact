#!/bin/bash

rm -r build jars 2> /dev/null

mkdir -p jars

mkdir -p build/classes/com.timeteller.clock/
javac -d build/classes/com.timeteller.clock/ com.timeteller.clock/module-info.java com.timeteller.clock/com/timeteller/clock/SpeakingClock.java
jar -c -f jars/clock.jar -C build/classes/com.timeteller.clock/ .

mkdir -p build/classes/com.timeteller.main
javac -d build/classes/com.timeteller.main/ --module-path=jars com.timeteller.main/module-info.java com.timeteller.main/com/timeteller/main/Main.java
jar -c -f jars/timeTeller.jar --main-class com.timeteller.main.Main -C build/classes/com.timeteller.main .
