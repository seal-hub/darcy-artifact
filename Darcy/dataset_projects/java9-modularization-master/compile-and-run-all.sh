#!/bin/bash
: "${JAVA9_BIN:?Env variable JAVA9_BIN not set, set with 'export JAVA9_BIN=<PATH_TO_JDK9_BIN>'}"

sh ./build-utils.sh && sh ./build-interview.sh && sh run.sh "$@"
