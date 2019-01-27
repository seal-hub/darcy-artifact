package edu.uci.seal.ics.darcy.reflect;

import edu.uci.seal.Utils;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.options.Options;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.SimpleLocalDefs;

import java.util.*;

public class ReflectFieldUsageTransformer extends SceneTransformer {

    private static List<String> processDirs;
    private int nonStringConstantFieldNameCount=0;
    private int fullFieldNameCount=0;
    private int fieldNameWithoutClassNameCount=0;

    private static Map<String,Integer> fullFieldCounts = new LinkedHashMap<String,Integer>();
    private static Map<String,Integer> partFieldCounts = new LinkedHashMap<String,Integer>();
    private int totalReflectFieldUsageCount=0;


    public ReflectFieldUsageTransformer(ArrayList<String> paths){
        super();
        this.processDirs = paths;
    }
//    public static void main(String[] args) {
//	    processDirs = new ArrayList<String>();
//	    processDirs.add("/Users/negar/Downloads/java-reflection-field-example-master-2/out/production/reflective-test");
//
//	    ReflectFieldUsageTransformer t = new ReflectFieldUsageTransformer();
//        t.run();
//
//        System.out.println("Number of reflections found: "+t.getTotalReflectInvokeCount());
//    }

    public int getTotalReflectInvokeCount(){
        return totalReflectFieldUsageCount;
    }

    public void run() {
        Options.v().set_output_format(Options.v().output_format_none);
        PackManager.v().getPack("wjtp")
                .add(new Transform("wjtp.rfu", this));
        PackManager.v().getPack("wjtp").apply();
    }

    private Set<SootMethod> getMethods(List<String> processDirs){
        Options.v().set_whole_program(true);
        Options.v().set_debug(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_process_dir(processDirs);
        //Options.v().no_writeout_body_releasing();

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


    @Override
    protected void internalTransform(String s, Map<String, String> map) {
        int currMethodCount = 1;

        Set<SootMethod> methods = getMethods(processDirs);

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
                    doAnalysisOnMethod(method);
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
    }

    private void doAnalysisOnMethod(SootMethod method) {
        System.out.println("Analyzing method " + method.getSignature());

        int fieldReflectUsageCount = 0;
        if (method.hasActiveBody()) {
            Body body = method.getActiveBody();
            PatchingChain<Unit> units = body.getUnits();

            UnitGraph unitGraph = new BriefUnitGraph(body);
            SimpleLocalDefs defs = new SimpleLocalDefs(unitGraph);

            for (Unit unit : units) {
                if (unit instanceof JAssignStmt) {
                    JAssignStmt assignStmt = (JAssignStmt)unit;
                    if (assignStmt.getRightOp() instanceof VirtualInvokeExpr) {
                        VirtualInvokeExpr invokeExpr = (VirtualInvokeExpr)assignStmt.getRightOp();
                        fieldReflectUsageCount = identifyReflectiveFieldUsage(method,fieldReflectUsageCount,defs,assignStmt,invokeExpr);
                    }
                }
                else if (unit instanceof JInvokeStmt) {
                    JInvokeStmt invokeStmt = (JInvokeStmt)unit;
                    if ( checkForReflectInvocation(invokeStmt.getInvokeExpr()) ) {
                        if (invokeStmt.getInvokeExpr() instanceof VirtualInvokeExpr) {
                            VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr)invokeStmt.getInvokeExpr();
                            fieldReflectUsageCount = identifyReflectiveFieldUsage(method, fieldReflectUsageCount, defs, invokeStmt, virtualInvokeExpr);
                        }
                    }
                }
            }
        }

        totalReflectFieldUsageCount += fieldReflectUsageCount;
    }

    public int identifyReflectiveFieldUsage(SootMethod method, int fieldReflectUsageCount, SimpleLocalDefs defs, Stmt inStmt, VirtualInvokeExpr invokeExpr) {
        if ( checkForReflectInvocation(invokeExpr) ) {
            Hierarchy hierarchy = Scene.v().getActiveHierarchy();
            SootClass classLoaderClass = Utils.getLibraryClass("java.lang.ClassLoader");
            //checks if reflectively invokes the proposed package

            fieldReflectUsageCount++;
            System.out.println(method.getName() + " reflectively invokes " + invokeExpr.getMethod().getDeclaringClass() + "." + invokeExpr.getMethod().getName());
            if (invokeExpr.getMethod().getDeclaringClass().getName().equals("java.lang.reflect.Field")) {
                if (!(invokeExpr.getBase() instanceof Local)) {
                    System.out.println("\tThis reflection API field usage has a callee of a non-local class");
                    return fieldReflectUsageCount;
                }
                Local invokeExprLocal = (Local) invokeExpr.getBase();
                List<Unit> defUnits = defs.getDefsOfAt(invokeExprLocal, inStmt);
                for (Unit defUnit : defUnits) {
                    if (!(defUnit instanceof JAssignStmt)) {
                        continue;
                    }
                    JAssignStmt methodAssignStmt = (JAssignStmt) defUnit;
                    if (methodAssignStmt.getRightOp() instanceof VirtualInvokeExpr) {
                        VirtualInvokeExpr getDeclaredMethodExpr = (VirtualInvokeExpr) methodAssignStmt.getRightOp();
                        if (getDeclaredMethodExpr.getMethod().getDeclaringClass().getName().equals("java.lang.Class") && MethodConstants.reflectiveGetFieldMethodsSet.contains(getDeclaredMethodExpr.getMethod().getName())) {
                                boolean result = handleReflectiveGetMethods(getDeclaredMethodExpr, methodAssignStmt, defs, inStmt, hierarchy, classLoaderClass);
                                if (!result) {
                                    continue;
                                }
                        }
                    }
                    for (ValueBox useBox : methodAssignStmt.getUseBoxes()) {
                        if (useBox.getValue() instanceof FieldRef) {
                            System.out.println("\t\t" + useBox + " is instanceof FieldRef");
                            FieldRef fieldRef = (FieldRef) useBox.getValue();
                            for (Tag tag : fieldRef.getField().getTags()) {
                                System.out.println("\t\t\ttag: " + tag);
                            }
                        }
                    }
                }
            }

        }
        return fieldReflectUsageCount;
    }

    private boolean handleReflectiveGetMethods(VirtualInvokeExpr getDeclaredMethodExpr, JAssignStmt methodAssignStmt, SimpleLocalDefs defs, Stmt inStmt, Hierarchy hierarchy, SootClass classLoaderClass) {
        if (!(getDeclaredMethodExpr.getArg(0) instanceof StringConstant)) {
            System.out.println("Reflective field usage is not a string constant at " + methodAssignStmt);
            nonStringConstantFieldNameCount++;
            return false;
        }
        StringConstant reflectivelyUsedFieldName = (StringConstant)getDeclaredMethodExpr.getArg(0);
        //logger.debug("Found the following method invoked reflectively: " + reflectivelyUsedFieldName);
        if (!(getDeclaredMethodExpr.getBase() instanceof Local)) {
            System.out.println("Reflective field usage receives a non-local method name at " + methodAssignStmt);
            return false;
        }
        Local classLocal = (Local)getDeclaredMethodExpr.getBase();
        List<Unit> classDefUnits = defs.getDefsOfAt(classLocal,inStmt);
        boolean foundClassName = false;
        for (Unit classDefUnit : classDefUnits) {
            if (!(classDefUnit instanceof JAssignStmt)) {
                return false;
            }
            JAssignStmt classAssignStmt = (JAssignStmt) classDefUnit;
            if (classAssignStmt.getRightOp() instanceof InvokeExpr) {
                InvokeExpr classInvokeExpr = (InvokeExpr) classAssignStmt.getRightOp();
                if (classInvokeExpr.getMethod().getDeclaringClass().getName().equals("java.lang.Class") && classInvokeExpr.getMethod().getName().equals("forName")) {
                    if (classInvokeExpr.getArg(0) instanceof StringConstant) {
                        StringConstant classNameConst = (StringConstant) classInvokeExpr.getArg(0); //??
                        String fullMethodName = classNameConst.value + "." + reflectivelyUsedFieldName.value;
                        System.out.println("\twith class: " + classNameConst.value);
                        System.out.println("\tFound reflective invocation of " + fullMethodName);
                        foundClassName = true;
                        incrementFullMethodCounts(fullMethodName);
                        fullFieldNameCount++;
                    }
                } else if (classLoaderClass != null) {
                    if (hierarchy.isClassSubclassOfIncluding(classInvokeExpr.getMethod().getDeclaringClass(), classLoaderClass)) {
                        if (classInvokeExpr.getMethod().getName().equals("loadClass")) {
                            if (classInvokeExpr.getArg(0) instanceof StringConstant) {
                                StringConstant classNameConst = (StringConstant) classInvokeExpr.getArg(0);
                                String fullMethodName = classNameConst.value + "." + reflectivelyUsedFieldName.value;
                                System.out.println("\twith class: " + classNameConst.value);
                                System.out.println("\tFound reflective invocation of " + fullMethodName);
                                foundClassName = true;
                                incrementFullMethodCounts(fullMethodName);
                                fullFieldNameCount++;
                            }
                        }
                    }
                }
                if (classInvokeExpr.getMethod().getName().equals("getClass")) {
                    if (classInvokeExpr instanceof VirtualInvokeExpr) {
                        VirtualInvokeExpr classVirtualInvokeExpr = (VirtualInvokeExpr) classInvokeExpr;
                        if (classVirtualInvokeExpr.getBase() instanceof Local) {
                            Local objLocal = (Local) classVirtualInvokeExpr.getBase();
                            for (Unit objDefUnit : defs.getDefsOfAt(objLocal, classAssignStmt)) {
                                if (objDefUnit instanceof JAssignStmt) {
                                    JAssignStmt objAssignStmt = (JAssignStmt) objDefUnit;
                                    if (objAssignStmt.getRightOp() instanceof Local) {
                                        Local rightOp = (Local)objAssignStmt.getRightOp();
                                        Type type = rightOp.getType();
                                        if (type instanceof RefType) {
                                            RefType refType = (RefType)type;
                                            String fullClassName = refType.getClassName();
                                            System.out.println("\tFound field usage of class " + fullClassName + " for field " + reflectivelyUsedFieldName);
                                            fullFieldNameCount++;
                                            foundClassName = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (classAssignStmt.getRightOp() instanceof ClassConstant) {
                ClassConstant classConstant = (ClassConstant)classAssignStmt.getRightOp();
                String fullMethodName = classConstant.getValue() + "." + reflectivelyUsedFieldName.value;
                System.out.println("\tFound reflective invocation of " + fullMethodName);
                foundClassName = true;
                incrementFullMethodCounts(fullMethodName);
                fullFieldNameCount++;
            }
        }
        if (!foundClassName) {
            System.out.println("\tCould not find class name to match reflective invocation of method: " + reflectivelyUsedFieldName);
            incrementPartFieldCounts(reflectivelyUsedFieldName.value);
            fieldNameWithoutClassNameCount++;
        }
        return true;
    }

    private void incrementFullMethodCounts(String fullFieldName) {
        Integer count = null;
        if (fullFieldCounts.containsKey(fullFieldName)) {
            count = fullFieldCounts.get(fullFieldName);
        } else {
            count = 0;
        }
        count++;
        fullFieldCounts.put(fullFieldName, count);
    }

    private void incrementPartFieldCounts(String methodNameOnly) {
        Integer count = null;
        if (partFieldCounts.containsKey(methodNameOnly)) {
            count = partFieldCounts.get(methodNameOnly);
        } else {
            count = 0;
        }
        count++;
        partFieldCounts.put(methodNameOnly, count);

    }

    private boolean checkForReflectInvocation(InvokeExpr invokeExpr) {
        return invokeExpr.getMethodRef().declaringClass().getPackageName().startsWith("java.lang.reflect");
    }
}
