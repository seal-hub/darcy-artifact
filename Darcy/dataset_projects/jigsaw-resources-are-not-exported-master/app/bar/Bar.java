package bar;

import foo.Foo;
import java.lang.Module;

public class Bar {
    public static void main(String[] args) throws Exception {
        Module lib = Foo.class.getModule();
        System.out.println(
                "lib.getResourceAsStream(\"foo/Foo.java\") = " +
                lib.getResourceAsStream("foo/Foo.java")
        );
    }
}
