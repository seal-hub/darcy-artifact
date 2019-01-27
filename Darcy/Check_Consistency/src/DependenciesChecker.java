
import java.io.*;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DependenciesChecker {

    private ArrayList<String> modules;
    private ArrayList<String> services;
    private HashMap<String, Set<String>> packageToClasses;
    Set<String> packages;
    private ArrayList<String> classPaths;
    private HashMap<String, Set<String>> pathsToCheckOpen;
    private String stat;
    private int directivesNum;
    private ArrayList<ModuleReference> moduleRefs;
    private ArrayList<Dependency> deps;
    private ArrayList<String> JDKModules;

    private HashMap<String,ArrayList<String>> modProvides;
    private HashMap<String,ArrayList<String>> modUses;


    public DependenciesChecker(){
        this.modules = new ArrayList<String>();
        this.services = new ArrayList<String>();

        this.deps = new ArrayList<Dependency>();
        this.moduleRefs = new ArrayList<ModuleReference>();
        this.packageToClasses = new HashMap<String, Set<String>>();
        this.packages = new HashSet<String>();
        this.classPaths = new ArrayList<String>();
        this.pathsToCheckOpen = new HashMap<String, Set<String>>();
        this.stat = "";
        JDKModules = new ArrayList<String>();
        this.modProvides = new HashMap<>();
        this.modUses = new HashMap<>();

    }


    public void buildModuleDependencies() throws IOException{
        File moduleFile = new File("modules.txt");
        FileReader fileReader = new FileReader(moduleFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while((line = bufferedReader.readLine()) != null){
            String[] split = line.split("#");
            stat += "# of Modules: "+split.length+"\n";
            for (int i = 0; i < split.length ; i++) {
                Path dir = Paths.get(split[i]);
                classPaths.add(dir.toString());
                ModuleFinder finder = ModuleFinder.of(dir);
                moduleRefs.addAll(finder.findAll());
            }
        }

        //added:
        PrintWriter pckgToModuleWriter = new PrintWriter("package_to_module.txt", "UTF-8");

        for(ModuleReference mod : moduleRefs){
            modules.add(mod.descriptor().name());
            //added:
            for(String pkg : mod.descriptor().packages()){
                pckgToModuleWriter.println(pkg+":"+mod.descriptor().name());
            }
        }





        //debug
        ModuleFinder finder = null;
        finder = ModuleFinder.ofSystem();
        if (finder != null) {
            ModuleReference base = finder.find("java.base").orElse(null);
            if (base == null)
                throw new RuntimeException("java.base not found");
            Set<ModuleReference> allModules = finder.findAll();
            if (!allModules.contains(base))
                throw new RuntimeException("java.base not in all modules");
        }


        //added:
        pckgToModuleWriter.close();
    }


    private int containPackage(String pckgName){
        for (int i = 0; i < deps.size(); i++) {
            if (deps.get(i).getPackageName().equals(pckgName))
                return i;
        }
        return -1;
    }
    public ArrayList<Dependency> buildPackageDependencies() throws IOException {
        File file = new File("pkg_graph.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");
            packages.add(split[0]);
            //building package-module and package-package dependencies
            for (int i = 0; i < moduleRefs.size(); i++) {
                if (moduleRefs.get(i).descriptor().packages().contains(split[0])) { //found the module

                    String dependentModule = new String();
                    for (int k = 0; k < moduleRefs.size(); k++) {
                        if (moduleRefs.get(k).descriptor().packages().contains(split[1]) && (k!=i))
                            dependentModule = moduleRefs.get(k).descriptor().name();
                    }

                    int index = containPackage(split[0]);


                    if(dependentModule.length()>0) {
                        if(index >= 0) {
                            if(!(deps.get(index).getDependentModules().contains(dependentModule)))
                                deps.get(index).addDependentModule(dependentModule);
                            if(!(deps.get(index).getDependentPackages().contains(split[1])))
                                deps.get(index).addDependentPackage(split[1]);
                        }else{
                            Dependency dependency = new Dependency(split[0], moduleRefs.get(i).descriptor().name());
                            dependency.addDependentModule(dependentModule);
                            dependency.addDependentPackage(split[1]);
                            deps.add(dependency);
                        }
                    }
                    else{
                        if(index >= 0) {
                            if(!(deps.get(index).getDependentJDKModules().contains(split[1])))
                                deps.get(index).addDependentJDKModule(split[1]);
                        }else{
                            Dependency dependency = new Dependency(split[0], moduleRefs.get(i).descriptor().name());
                            dependency.addDependentJDKModule(split[1]);
                            deps.add(dependency);
                        }
                    }

                }
            }
        }
        fileReader.close();
        buildClassList();
        buildServiceList();
        return deps;
    }

    private void buildClassList()throws IOException{
        File file = new File("class_pkg_graph.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");
            if(packageToClasses.containsKey(split[0])){
                packageToClasses.get(split[0]).add(split[1]);
            }else{
                Set<String> classes = new HashSet<String>();
                classes.add(split[1]);
                packageToClasses.put(split[0],classes);
            }
        }
    }

    private void buildServiceList()throws IOException{
        File file = new File("interface_abstract_classes.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");
            services.add(split[0]);
        }
    }

    public ArrayList<String> evaluateDependencies() throws IOException{
        PrintWriter writer = new PrintWriter("excessDirectives.txt", "UTF-8");
        PrintWriter pathWriter = new PrintWriter("paths_for_open_analysis.txt", "UTF-8");
        PrintWriter memoryWriter = new PrintWriter("memory-usage.txt", "UTF-8");
        PrintWriter UsesPathWriter = new PrintWriter("paths_for_prov_use_analysis.txt", "UTF-8");

        ArrayList<String> error = new ArrayList<String>();

        int reqNum = 0;
        int expNum = 0;
        int useNum = 0;
        int provNum = 0;
        int openNum = 0;

        for(ModuleReference mod : moduleRefs){

            reqNum += mod.descriptor().requires().size();
            expNum += mod.descriptor().exports().size();
            useNum += mod.descriptor().uses().size();
            provNum += mod.descriptor().provides().size();
            openNum += mod.descriptor().opens().size();

            //open modules ???
            if(mod.descriptor().isOpen()){

            }
            //checking requires:
            for(ModuleDescriptor.Requires modReq : mod.descriptor().requires()){

                boolean reqError = true;
                if(modules.contains(modReq.name())) {

                    for (String pckg : mod.descriptor().packages()) {
                        for (Dependency dep : deps) {
                            if (dep.getPackageName().equals(pckg)) { //found the package in deps
                                if (dep.getDependentModules().contains(modReq.name())) {
                                    reqError = false;
                                }
                            }
                        }
                    }

                    if (reqError) {
                        error.add("Requires- " + mod.descriptor().name() + " : " + modReq.name());
                        writer.println(mod.descriptor().name() + ":" +"requires#"+modReq.name());
                    }

                }else{
                    JDKModules.add(modReq.name());
                    if(!modReq.name().equals("java.base")){ //JDK requires
                        //debug
                        for (String pckg : mod.descriptor().packages()) {
                            for (Dependency dep : deps) {
                                if (dep.getPackageName().equals(pckg)) { //found the package in deps

                                    if (dep.getDependentJDKModules().contains(modReq.name())) {
                                        reqError = false;
                                    }else{
                                        ModuleFinder finder = null;
                                        finder = ModuleFinder.ofSystem();
                                        if (finder != null) {
                                            ModuleReference jdkModule = finder.find(modReq.name()).orElse(null);
                                            if(jdkModule != null){
                                                //check all exported packages in JDK Modules
                                                for(ModuleDescriptor.Exports jdkExportedPckg : jdkModule.descriptor().exports()){
                                                    if(dep.getDependentJDKModules().contains(jdkExportedPckg.toString()))
                                                        reqError = false;
                                                }
                                            }
                                            //limitation: if can't find the module in JDK or the system itself
                                            Set<ModuleReference> allModules = finder.findAll();
                                            if (jdkModule ==null || !allModules.contains(jdkModule))
                                                reqError = false;

                                        }
                                    }
                                }
                            }
                        }

                        if (reqError) {
                            error.add("Requires-JDK- " + mod.descriptor().name() + " : " + modReq.name());
                            writer.println(mod.descriptor().name() + ":" +"requires#"+modReq.name());
                        }
                    }
                }

                //check requires "transitive"
                if(!reqError){
                    if(modReq.toString().contains("transitive")){
                        boolean transitiveReqError = true;

                        for (Dependency dep : deps) {
                            if (dep.getDependentModules().contains(mod) && dep.getDependentModules().contains(modReq)) {
                                transitiveReqError = false;
                            }
                        }
                        if(transitiveReqError){
                            error.add("Requires-transitive- " + mod.descriptor().name() + " : " + modReq.name());
                            writer.println(mod.descriptor().name() + ":" +"requirestransitive#"+modReq.name());
                        }

                    }


                }





            }

            //checking if the Provides is necessary
            if(mod.descriptor().provides().size()>0){
                for(ModuleDescriptor.Provides modProv : mod.descriptor().provides()){
                    if((!services.contains(modProv.service())) && (!modProv.service().startsWith("java"))) {
                        error.add("Provides- " + mod.descriptor().name() + " : " + modProv.service());
                        writer.println(mod.descriptor().name() + ":" +"provides#" + modProv.service());
                    }

                    //checking if any other module is using this service
                    boolean found = false;
                    for (String usingModule : modUses.keySet()) {
                        if(modUses.get(usingModule).contains(modProv.service())){
                            found = true;
                            for(String classpath : classPaths)
                                UsesPathWriter.println(usingModule+":"+classpath+":"+modProv.service());
                        }
                    }
                    if(!found){
                        error.add("Provides- " + mod.descriptor().name() + " : " + modProv.service());
                        writer.println(mod.descriptor().name() + ":" +"provides#" + modProv.service());
                    }
                }
            }
            //checking if the Uses is necessary
            if(mod.descriptor().uses().size()>0){
                for(String modUse : mod.descriptor().uses()){
                    if(!services.contains(modUse)) {
                        error.add("Uses- " + mod.descriptor().name() + " : " + modUse);
                        writer.println(mod.descriptor().name() + ":" +"uses#" + modUse);
                    }

                }
            }

            //checking exports
            for (ModuleDescriptor.Exports pckg : mod.descriptor().exports()) {
                boolean found = false;
                if (pckg.isQualified()) { // exports ... to ...
                    for (ModuleReference targetMod : moduleRefs) {
                        if (pckg.targets().contains(targetMod.descriptor().name())) {
                            for (String targetPckg : targetMod.descriptor().packages()) {
                                int index = containPackage(targetPckg);
                                if (index > 0) {
                                    if (deps.get(index).getDependentPackages().contains(pckg.toString()))
                                        found = true;
                                }
                            }
                        }
                    }
                } else {
                    for (Dependency dep : deps) {
                        if (dep.getDependentPackages().contains(pckg.toString()))
                            found = true;
                    }
                }
                if (!found && !pckg.isQualified()) {
                    error.add("Exports- " + mod.descriptor().name() + ", " + pckg.toString());
                    writer.println(mod.descriptor().name() + ":" +"exports#" + pckg.toString());
                }
                if (!found && pckg.isQualified()) {
                    error.add("Exports to- " + mod.descriptor().name() + ", " + pckg.toString());
                    writer.println(mod.descriptor().name() + ":" +"exportsto#" + pckg.toString());
                }
            }

            //checking opens
            if(mod.descriptor().opens().size()>0) {
                for (ModuleDescriptor.Opens openPckg : mod.descriptor().opens()) {
                    if (openPckg.isQualified()) { //opens to
                        boolean foundMod = false;
                        for (ModuleReference targetMod : moduleRefs) {
                            if (openPckg.targets().contains(targetMod.descriptor().name())) {
                                foundMod = true;
                                String targetPath = mod.location().toString().substring(16, mod.location().toString().length() - 2);
                                if(pathsToCheckOpen.containsKey(mod.descriptor().name()+":"+targetPath)){
                                    pathsToCheckOpen.get(mod.descriptor().name()+":"+targetPath).add(openPckg.source());
                                }else{
                                    Set<String> pckgSet = new HashSet<String>();
                                    pckgSet.add(openPckg.source());
                                    pathsToCheckOpen.put(mod.descriptor().name()+":"+targetPath,pckgSet);
                                }
                            }
                        }
                        if(!foundMod){ // package is opened to a JDK module
                            error.add("Opens to- " + mod.descriptor().name() + ", " + openPckg.toString());
                            writer.println(mod.descriptor().name() + ":" +"opensto#" + openPckg.toString());
                        }
                    } else { //opens
                        for (String path : classPaths) {
                            if (!(path.equals(mod.location().toString().substring(16, mod.location().toString().length() - 2)))) { //found path of the class files of the package
                                if(pathsToCheckOpen.containsKey(mod.descriptor().name()+":"+path)){
                                    pathsToCheckOpen.get(mod.descriptor().name()+":"+path).add(openPckg.source());
                                }else{
                                    Set<String> pckgSet = new HashSet<String>();
                                    pckgSet.add(openPckg.source());
                                    pathsToCheckOpen.put(mod.descriptor().name()+":"+path,pckgSet);
                                }
                            }
                        }
                    }
                }
            }
        }

        stat += "# of requires: "+reqNum+"\n";
        stat += "# of exports: "+expNum+"\n";
        stat += "# of opens: "+openNum+"\n";
        stat += "# of uses: "+useNum+"\n";
        stat += "# of provides: "+provNum+"\n";
        directivesNum = reqNum+expNum+openNum+useNum+provNum;
        stat += "# of total directives: "+directivesNum;




        for(String path : pathsToCheckOpen.keySet()){
            pathWriter.print(path+":");
            for(String pckg : pathsToCheckOpen.get(path)){
                pathWriter.print(pckg+"#");
            }
            pathWriter.println();
        }

        System.out.println("::::: STATS ::::");
        System.out.println(stat);

        for (int i = 0; i < JDKModules.size() ; i++) {
            memoryWriter.println(JDKModules.get(i));
        }
        memoryWriter.println("-------------------------------------------");

        writer.close();
        pathWriter.close();
        memoryWriter.close();
        UsesPathWriter.close();
        return error;
    }
    public void buildProvideUseDep(){
        for(ModuleReference mod : moduleRefs){
            if(mod.descriptor().provides().size()>0) {
                ArrayList<String> provides = new ArrayList<>();
                for (ModuleDescriptor.Provides modProv : mod.descriptor().provides()) {
                    provides.add(modProv.toString());
                }
                modProvides.put(mod.descriptor().name(),provides);
            }

            if(mod.descriptor().uses().size()>0){
                ArrayList<String> uses = new ArrayList<>();
                for(String moduse: mod.descriptor().uses()){
                    uses.add(moduse);
                }
                modUses.put(mod.descriptor().name(),uses);
            }
        }
    }


}
