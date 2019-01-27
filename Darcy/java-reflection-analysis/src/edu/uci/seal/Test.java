package edu.uci.seal;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException{

        HashMap<String, Set<String>> openPaths = new HashMap<String, Set<String>>();
        ArrayList<Pair<String, String>> modToPath = new ArrayList<>();


        ArrayList<String> processDirs = new ArrayList<String>();



        File pathFile = new File("paths_for_open_analysis.txt");
        FileReader fileReader = new FileReader(pathFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");
            modToPath.add(new Pair(split[0],split[1]));
            Set<String> pckgs = new HashSet<String>();
            String[] pckgSplit = split[2].split("#");
            for (int i = 0; i <pckgSplit.length ; i++) {
                pckgs.add(pckgSplit[i]);
            }

            openPaths.put(split[1],pckgs);

        }

        try {
            ReflectUsageIdentifier rui = new ReflectUsageIdentifier(openPaths, modToPath);

            rui.run();
            System.out.println("Number of reflections found: " + rui.getTotalReflectInvokeCount());
            rui.printError();
        }catch(Exception e){
            System.out.println("===============================");
            System.out.println("No reflection usage found!");
            System.out.println("===============================");

        }


    }
}
