package main.antlr4;
import javafx.util.Pair;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException{

        File moduleFile = new File("module-infos.txt");
        FileReader fileReader = new FileReader(moduleFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] moduleSplit = line.split("#");
            for (int i = 0; i < moduleSplit.length; i++) {
                File file = new File(moduleSplit[i] + "/module-info.java");
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String moduleInfo = "";
                while ((line = bufferedReader.readLine()) != null) {
                    moduleInfo += line;
                }

                ArrayList<Pair<String, String>> excessDirectives = new ArrayList<Pair<String, String>>();
                file = new File("excessDirectives.txt");
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split("#");
                    excessDirectives.add(new Pair<String, String>(split[0], split[1]));
                }

                file = new File("excessOpenDirectives.txt");
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split("#");
                    excessDirectives.add(new Pair<String, String>(split[0], split[1]));
                }

                //added:
                file = new File("excessUsesDirectives.txt");
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split("#");
                    excessDirectives.add(new Pair<String, String>(split[0], split[1]));
                }


                InputStreamReader isr = new InputStreamReader(System.in);
                ANTLRInputStream inputStream = new ANTLRInputStream(moduleInfo);
                Java9Lexer java9Lexer = new Java9Lexer(inputStream);
                CommonTokenStream commonTokenStream = new CommonTokenStream(java9Lexer);
                Java9Parser java9Parser = new Java9Parser(commonTokenStream);
                Java9Parser.CompilationUnitContext cuContext = java9Parser.compilationUnit();
                Java9Visitor visitor = new Java9Visitor(excessDirectives);
                ParseTreeWalker.DEFAULT.walk(visitor, cuContext);
                if(visitor.getTransformedModuleInfo().length()>0) {
                    String filename = moduleSplit[i] + "/module-info.java";
                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.print(visitor.getTransformedModuleInfo());
                    writer.close();
                }
            }
        }
    }
}
