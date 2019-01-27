#!/bin/bash 

#
# Script used to compile, build jars and run
# For now: You need to put all dependencies(jars) in 'libs' dir
# 
# USAGE:
#  * define basic config and run:
# ./build.sh
#


############### BASIC CONFIG ###################
RUN_TESTS=true
################################################


export JAVA_HOME=/usr/lib/jvm/java-9-oracle

JAVA_BIN=$JAVA_HOME/bin
echo Java: $JAVA_BIN

# clean
echo Cleaning ...
rm -rf build
mkdir build
rm -rf mlib
mkdir mlib
mkdir modules/automatic
mkdir modules/generated
mkdir modules/libs
mkdir modules/test
mkdir modules/test-automatic
mkdir modules/test-libs

# compile
echo Compiling ...
$JAVA_BIN/javac -mp modules/automatic:modules/test-automatic -d build -modulesourcepath src $(find src -name "*.java")


#download dependencies
if [ $RUN_TESTS = true ]; then	
	if [ ! -f "modules/test-automatic/junit-4.12.jar" ]; then 
		wget -cv http://central.maven.org/maven2/junit/junit/4.12/junit-4.12.jar -P modules/test-automatic
	fi
	if [ ! -f "modules/test-libs/hamcrest-core-1.3.jar" ]; then 
		wget -cv http://central.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar -P modules/test-libs
	fi
fi


# modules
echo Creating Modules ...

echo Creating Module: MovieModel
$JAVA_BIN/jar --create --file modules/generated/media.movies.model@1.0.jar --module-version 1.0 -C build/media.movies.model .

echo Creating Module: MovieNFO
$JAVA_BIN/jar --create --file modules/generated/media.movies.nfo@1.0.jar --module-version 1.0 -C build/media.movies.nfo .

if [ $RUN_TESTS = true ]; then
	echo Running MovieNFO tests ...
	cp -Rf src/media.movies.nfo.test/nfo build/media.movies.nfo.test
	$JAVA_BIN/jar --create --file modules/test/media.movies.nfo.test@1.0.jar --module-version 1.0  --main-class br.com.phtcosta.media.movie.nfo.test.TestRunner -C build/media.movies.nfo.test .
	$JAVA_BIN/java -mp modules/generated:modules/test:modules/test-automatic -cp modules/test-libs/hamcrest-core-1.3.jar -m media.movies.nfo.test 
fi
