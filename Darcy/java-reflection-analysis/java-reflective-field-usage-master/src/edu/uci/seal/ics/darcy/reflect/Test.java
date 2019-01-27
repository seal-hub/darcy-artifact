package edu.uci.seal.ics.darcy.reflect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws IOException{

        ArrayList<String> processDirs = new ArrayList<>();

        File openPath = new File("../paths_for_open_analysis.txt");
        FileReader fileReader = new FileReader(openPath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            String[] incons = line.split("#");
            for (int i = 0; i < incons.length ; i++) {
                String[] split = incons[i].split(":");
                processDirs.add(split[1]);
            }
        }
            ReflectFieldUsageTransformer t = new ReflectFieldUsageTransformer(processDirs);
        t.run();

        System.out.println("Number of reflections found: "+t.getTotalReflectInvokeCount());
    }
}
