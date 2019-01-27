package edu.uci.seal;

import edu.uci.seal.ReflectUsageIdentifier;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        ReflectUsageIdentifier rui = new ReflectUsageIdentifier();
        List<String> processDirs = new ArrayList<String>();
        processDirs.add("/Users/negar/Documents/University/UCI/Research/Java9/test_projects/number-to-text_jdk9/number_provider/target/classes");

        rui.internalTransform(processDirs);

        System.out.println("Number of reflections found: "+rui.getTotalReflectInvokeCount());

    }
}
