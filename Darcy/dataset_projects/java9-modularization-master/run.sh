#!/bin/bash
: "${JAVA9_BIN:?Env variable JAVA9_BIN not set, set with 'export JAVA9_BIN=<PATH_TO_JDK9_BIN>'}"

echo "Starting application"
$JAVA9_BIN/java -cp "bin/*" se.omegapoint.interview.PersonNumberValidator "$@"
