#!/bin/bash
: "${JAVA9_BIN:?Env variable JAVA9_BIN not set, set with 'export JAVA9_BIN=<PATH_TO_JDK9_BIN>'}"

echo "Compiling interview sources..."
$JAVA9_BIN/javac --module-path classes/. -d classes/se.omegapoint.interview $(find se.omegapoint.interview -name "*.java") && \
echo "Packaging into bin/interview.jar..." && \
mkdir -p bin && \
$JAVA9_BIN/jar --create --file=bin/interview.jar --module-version=1.0 --main-class=se.omegapoint.interview.PersonNumberValidator -C classes/se.omegapoint.interview .
