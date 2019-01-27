#!/bin/bash

jlink --module-path $JAVA_HOME/jmods:mlib:jars --add-modules com.timeteller.main --output timeteller-runtime
