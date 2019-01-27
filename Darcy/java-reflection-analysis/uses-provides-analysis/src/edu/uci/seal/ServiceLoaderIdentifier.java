package edu.uci.seal;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import javafx.util.Pair;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.options.Options;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.SimpleLocalDefs;

import java.io.*;
import java.util.*;

public class ServiceLoaderIdentifier extends SceneTransformer {

    private static int totalServiceLoaderCount = 0;
    private static int nonStringConstantMethodNameCount = 0;
    private static int methodNameWithoutClassNameCount = 0;
    private static int fullMethodNameCount = 0;
    private static Map<String, Integer> fullMethodCounts = new LinkedHashMap<String, Integer>();
    private static Map<String, Integer> partMethodCounts = new LinkedHashMap<String, Integer>();

    private HashMap<String, Set<String>> paths;
    private ArrayList<Pair<String, String>> modToPath;

    private HashMap<String,String> packageToModule = new HashMap<>();

    Set<String> error;
    HashMap<String,Set<String>> serviceLoaderUsage;


    public ServiceLoaderIdentifier(HashMap<String, Set<String>> paths, ArrayList<Pair<String, String>> modToPath) {
        this.modToPath = modToPath;
        this.paths = paths;
        error = new HashSet<String>();
        serviceLoaderUsage = new HashMap<>();
    }

    @Override
    protected void internalTransform (String s, Map<String, String> map ) {

        Set<String> services = new HashSet<String>();
        List<String> processDirs = new ArrayList<String>();
        String modName = "";
        for (String path : paths.keySet()) {
            processDirs.add(path);
            for(String srv : paths.get(path)){
                services.add(srv);
            }
            for(Pair<String,String> pair:modToPath){
                if(pair.getValue().equals(path)){
                    modName = pair.getKey();
                }
            }

        }

        //added:
        try {
            FileReader pkgToModFile = new FileReader("package_to_module.txt");
            BufferedReader bufferedReader = new BufferedReader(pkgToModFile);
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                packageToModule.put(split[0],split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Set<SootMethod> methods = getMethods(processDirs);

        int currMethodCount = 1;
        System.out.println("total number of possible methods to analyze: " + methods.size());
        for (SootMethod method : methods) {
            System.out.println("Checking if I should analyze method: " + method);
            if (Utils.isApplicationMethod(method)) {
                if (method.isConcrete()) {

                    Body body = method.retrieveActiveBody();
                    PackManager.v().getPack("jtp").apply(body);
                    if (Options.v().validate()) {
                        body.validate();
                    }

                }
                if (method.hasActiveBody()) {
                    doAnalysisOnMethod(method, services);
                } else {
                    System.out.println("method " + method + " has no active body, so it's won't be analyzed.");
                }

                if (Thread.interrupted()) {
                    try {
                        throw new InterruptedException();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        return;
                    }
                }

            }
            System.out.println("Finished loop analysis on method: " + method);
            System.out.println("Number of methods analyzed: " + currMethodCount);
            currMethodCount++;
        }

        //found excess uses directive:
        if(totalServiceLoaderCount == 0){
            for(String srv : services) {
                error.add(modName + ":uses#" + srv);
            }
        }



    }

    private Set<SootMethod> getMethods(List<String> processDirs){

        Options.v().set_whole_program(true);
        Options.v().set_debug(true);
        Options.v().set_allow_phantom_refs(true);

        Options.v().set_process_dir(processDirs);

        Options.v().no_writeout_body_releasing();

        Scene.v().loadNecessaryClasses();

        Set<SootMethod> methods = new LinkedHashSet<SootMethod>();

        for (SootClass clazz : Scene.v().getClasses()) {

            if(!(clazz.getName().startsWith("java.") || clazz.getName().startsWith("sun.") || clazz.getName().startsWith("jdk.") || clazz.getName().startsWith("javax.") || clazz.getName().startsWith("com.sun."))) {
                System.out.println(clazz.getName());
                clazz.setApplicationClass();
                methods.addAll(clazz.getMethods());
            }
        }
        return methods;
    }

    private void doAnalysisOnMethod(SootMethod method, Set<String> services) {
        System.out.println("Analyzing method " + method.getSignature());

        int serviceLoaderUsageCount = 0;
        if (method.hasActiveBody()) {
            Body body = method.getActiveBody();
            PatchingChain<Unit> units = body.getUnits();

            UnitGraph unitGraph = new BriefUnitGraph(body);
            SimpleLocalDefs defs = new SimpleLocalDefs(unitGraph);

            for (Unit unit : units) {
                if (unit instanceof JAssignStmt) {
                    JAssignStmt assignStmt = (JAssignStmt)unit;
                    if (assignStmt.getRightOp() instanceof StaticInvokeExpr) {
                        StaticInvokeExpr invokeExpr = (StaticInvokeExpr)assignStmt.getRightOp();
                        serviceLoaderUsageCount = identifyServiceLoaderUsage(method, serviceLoaderUsageCount, defs, assignStmt, invokeExpr, services);
                    }
                }
                else if (unit instanceof JInvokeStmt) {
                    JInvokeStmt invokeStmt = (JInvokeStmt)unit;
                    if ( checkForServiceLoaderUsage(invokeStmt.getInvokeExpr()) ) {
                        if (invokeStmt.getInvokeExpr() instanceof StaticInvokeExpr) {
                            StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr)invokeStmt.getInvokeExpr();
                            serviceLoaderUsageCount = identifyServiceLoaderUsage(method, serviceLoaderUsageCount, defs, invokeStmt, staticInvokeExpr, services);
                        }
                    }
                }
            }
        }
        totalServiceLoaderCount += serviceLoaderUsageCount;
    }

    public int identifyServiceLoaderUsage(SootMethod method, int serviceLoaderUsageCount, SimpleLocalDefs defs, Stmt inStmt, StaticInvokeExpr invokeExpr, Set<String> services) {
        if ( checkForServiceLoaderUsage(invokeExpr) ) {
            if (invokeExpr.getMethod().getName().equals("load")) {
                for (String srv : services) {
                    if (invokeExpr.getArgBox(0).getValue().toString().replace("/", ".").contains(srv)) {
                        serviceLoaderUsageCount++;

                        //added:
                        if(packageToModule.containsKey(method.getDeclaringClass().getPackageName())){
                            String usingMod = packageToModule.get(method.getDeclaringClass().getPackageName());

                            if(serviceLoaderUsage.keySet().contains(usingMod)){
                                serviceLoaderUsage.get(usingMod).add(srv);
                            }else{
                                Set<String> usingSrvs = new HashSet<String>();
                                usingSrvs.add(srv);
                                serviceLoaderUsage.put(usingMod,usingSrvs);
                            }
                        }

//                        if(serviceLoaderUsage.keySet().contains(method.getDeclaringClass().getPackageName())){
//                            serviceLoaderUsage.get(method.getDeclaringClass().getPackageName()).add(srv);
//                        }else{
//                            Set<String> usingSrvs = new HashSet<String>();
//                            usingSrvs.add(srv);
//                            serviceLoaderUsage.put(method.getDeclaringClass().getPackageName(),usingSrvs);
//                        }
                    }
                }
            }
        }

        return serviceLoaderUsageCount;
    }

    private boolean checkForServiceLoaderUsage(InvokeExpr invokeExpr) {
        return invokeExpr.getMethodRef().declaringClass().getName().startsWith("java.util.ServiceLoader");
    }

    public void run() {
        PackManager.v().getPack("wjtp").remove("wjtp.ru");
        Options.v().set_whole_program(true);
        Options.v().set_output_format(Options.v().output_format_none);
        PackManager.v().getPack("wjtp")
                .add(new Transform("wjtp.ru", this));
        PackManager.v().getPack("wjtp").apply();
    }


    public int getTotalServiceLoaderCount(){
        return totalServiceLoaderCount;
    }

    public void printError(HashMap<String,Set<String>> useMap) throws IOException {


        PrintWriter writer = new PrintWriter("excessUsesDirectives.txt", "UTF-8");

        System.out.println("=========================Excess uses========================");
        for(String usingModule: useMap.keySet()){
            boolean found = false;
            for(String key : serviceLoaderUsage.keySet()) {
                if (key.contains(usingModule)) {
                    found = true;
                    for (String specifiedUses : useMap.get(usingModule)) {
                        if (!(serviceLoaderUsage.get(key).contains(specifiedUses))) {
                            System.out.println(usingModule + ": uses " + specifiedUses);
                            writer.println(usingModule+":" +"uses#"+specifiedUses);
                        }
                    }
                }

            }
            if(!found){
                for (String excessUses : useMap.get(usingModule)) {
                    System.out.println(usingModule + ": uses " + excessUses);
                    writer.println(usingModule + ":" + "uses#" + excessUses);
                }
                }

        }
        System.out.println("=================================================");
        writer.close();

//        System.out.println("=================================================");
//        for (String err : error){
//            System.out.println(err);
//        }
//        System.out.println("=================================================");
//
//        System.out.println("============= Service Loader Usage =============");
//        for(String usage : serviceLoaderUsage)
//            System.out.println(usage);
//        System.out.println("=================================================");

    }


}