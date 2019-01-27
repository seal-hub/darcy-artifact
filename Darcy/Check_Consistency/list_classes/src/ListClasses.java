import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class ListClasses {

    private HashMap<String, String> classToPackage;
    ArrayList<String> packages;


    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        //path = "/tmp/dumb/pckg/alaki";
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        //while(resources.enums.length() > 0){
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }

        return (Class[]) classes.toArray(new Class[classes.size()]);
    }


    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public void listAllClasses() throws IOException{
        File file = new File("/Users/negar/Documents/University/UCI/Research/Java9/all_together/pkg_graph.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        packages = new ArrayList<String>();

        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(":");
            if(!(packages.contains(split[0]))){

            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Class[] allClasses = getClasses("dumb.pckg.alaki");
        System.out.println(allClasses.length);
    }
}
