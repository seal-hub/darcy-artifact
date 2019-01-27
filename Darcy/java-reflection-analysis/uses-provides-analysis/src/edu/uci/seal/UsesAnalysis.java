package edu.uci.seal;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UsesAnalysis {

    public static void main(String[] args) throws IOException{

        ArrayList<Pair<String, String>> usesModToPath = new ArrayList<>();
        HashMap<String, Set<String>> usesPaths = new HashMap<String, Set<String>>();

        HashMap<String,Set<String>> useMap = new HashMap<>();

        File provUsePath = new File("paths_for_prov_use_analysis.txt");
        FileReader fileReader = new FileReader(provUsePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");

            if(useMap.keySet().contains(split[0])){
                useMap.get(split[0]).add(split[2]);
            }else{
                Set<String> usingSrvs = new HashSet<String>();
                usingSrvs.add(split[2]);
                useMap.put(split[0],usingSrvs);
            }

            usesModToPath.add(new Pair(split[0],split[1]));
            Set<String> services = new HashSet<String>();
            services.add(split[2]);


            if(usesPaths.containsKey(split[1])){
                for(String s : services)
                    usesPaths.get(split[1]).add(s);
            }else{
                usesPaths.put(split[1],services);
            }

        }

        try {
            ServiceLoaderIdentifier sli = new ServiceLoaderIdentifier(usesPaths, usesModToPath);
            sli.run();
            sli.printError(useMap);
            System.out.println("Number of Service Loader Usage found: " + sli.getTotalServiceLoaderCount());
        }catch (Exception e){
            System.out.println("===============================");
            System.out.println("No Service Loader Usage found!");
            System.out.println("===============================");

        }

}
}
