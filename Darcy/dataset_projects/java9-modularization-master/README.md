Modularization example for Java 9
---------------------------------
This example demonstrates how modularization can work with Java 9
As there are little support for modern IDEs to support Java 9, bash scripts are used.

## Program description
The program verifies swedish person numbers. 

## Structure
The project consists of two modules
* `se.omegapoint.utils`
* `se.omegapoint.interview`

Module naming conforms with standard practise to avoid name clashes (as module names are global)

## Module dependencies
The module definitions can be found in respective module-info.java
`se.omegapoint.interview` requires (depends on) `se.omegapoint.utils`.
`se.omegapoint.utils` exports two packages

## How to run
1. Open a terminal 
2. Set environment variable JAVA9_BIN to the path of the JDK 9 bin folder. E.g. on Mac this could be
   ```sh
   export JAVA9_BIN=/Library/Java/JavaVirtualMachines/jdk-9.jdk/Contents/Home/bin
   ```
   
3. Run
   ```sh
   ./compile-and-run-all.sh 198905117837 198905117839
   ```

## Questions
1. What happens when a class in `interview` project attempts to call methods in se.omegapoint.utils.internal? (e.g. StringUtils...)
2. How does the module linking work when compiling? (check the build scripts)
3. When does the module system come into action? Compile or runtime?
4. If we export `se.omegapoint.utils.calculations`, will we be able to access subpackages as well? E.g. `se.omegapoint.utils.calculations.internal`?
5. How do we include additional dependencies that does not have a module definition?
6. How do we depend on jar depenendencies that have module information?