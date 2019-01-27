#!/bin/bash
: "${JAVA9_BIN:?Env variable JAVA9_BIN not set, set with 'export JAVA9_BIN=<PATH_TO_JDK9_BIN>'}"

echo "Compiling util sources..."
$JAVA9_BIN/javac -d classes/se.omegapoint.utils $(find se.omegapoint.utils -name "*.java") && \
echo "Packaging into bin/utils.jar..."  && \
mkdir -p bin  && \
$JAVA9_BIN/jar --create --file=bin/utils.jar --module-version=1.0 -C classes/se.omegapoint.utils .
