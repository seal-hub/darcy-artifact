import java.util.ArrayList;

public class Dependency {

    private String packageName;
    private String moduleName;
    private ArrayList<String> dependentModules;
    private ArrayList<String> dependentJDKModules;
    private ArrayList<String> dependentPackages;

    public Dependency(String packageName, String moduleName){
        this.packageName = packageName;
        this.moduleName = moduleName;
        this.dependentModules =  new ArrayList<String>();
        this.dependentJDKModules =  new ArrayList<String>();
        this.dependentPackages = new ArrayList<String>();
    }

    public void addDependentModule(String depModule){
        this.dependentModules.add(depModule);
    }

    public void addDependentJDKModule(String depModule){
        this.dependentJDKModules.add(depModule);
    }


    public void addDependentPackage(String depPackage){
        this.dependentPackages.add(depPackage);
    }

    public ArrayList<String> getDependentModules() {
        return dependentModules;
    }

    public ArrayList<String> getDependentJDKModules() {
        return dependentJDKModules;
    }


    public ArrayList<String> getDependentPackages() {
        return dependentPackages;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getPackageName() {
        return packageName;
    }


    public String toString() {
        return "package: "+packageName+", in module: "+moduleName+"\n dependent modules: "+dependentModules+"\n dependent packages: "+dependentPackages;
    }

}
