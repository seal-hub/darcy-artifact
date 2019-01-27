#!/bin/bash

set -e

cd number_main

mvn exec:exec -Dnumber="$1" -Dlang="$2"
