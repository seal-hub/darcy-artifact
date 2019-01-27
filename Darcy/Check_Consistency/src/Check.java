import java.io.*;
import java.util.ArrayList;

public class Check {
    public static void main(String[] args) throws IOException{

        DependenciesChecker check = new DependenciesChecker();
        check.buildModuleDependencies();
        ArrayList<Dependency> dep_pckg = check.buildPackageDependencies();
        check.buildProvideUseDep();
        ArrayList<String> error = check.evaluateDependencies();
        if(error.size()>0) {
            System.out.println("FOUND INCONSISTENCIES:");
            for (int i = 0; i < error.size() ; i++) {
                System.out.println(error.get(i));
            }
        }else
            System.out.println("ALL TRUE");
    }
}
