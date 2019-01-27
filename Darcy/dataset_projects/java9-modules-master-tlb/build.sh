#~/bin/sh


#
# High-tech clean task
#
rm -rf counting-logger/build
rm -rf simple-logger/build
rm -rf main-module/build
rm -rf content-1/build
rm -rf content-2/build

mkdir -p counting-logger/build/classes
mkdir -p simple-logger/build/classes
mkdir -p main-module/build/classes
mkdir -p content-1/build/classes
mkdir -p content-2/build/classes

rm mlibs/*.jar

#
# Next-level javac abstraction
#
cd counting-logger
javac -d build/classes $(find src/main/java -name "*.java")
jar cvf ../mlibs/counting-logger.jar -C build/classes/ .

cd ../simple-logger
javac -d build/classes $(find src/main/java -name "*.java")
jar cvf ../mlibs/simple-logger.jar -C build/classes/ .

cd ../content-1
javac -mp ../mlibs -d build/classes $(find src/main/java -name "*.java")
jar cvf ../mlibs/content-1.jar -C build/classes/ .

cd ../content-2
javac -mp ../mlibs -d build/classes $(find src/main/java -name "*.java")
jar cvf ../mlibs/content-2.jar -C build/classes/ .

cd ../main-module
javac -mp ../mlibs -d build/classes $(find src/main/java -name "*.java")
jar cvf ../mlibs/main.jar -C build/classes/ .


#
# Running modular Java code like a boss
#
cd ..
java -mp mlibs -m com.timberglund.poetry/com.timberglund.poetry.PoetryEmitter Ascetic
