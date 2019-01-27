package it4u.site.java;

/**
 * 接口声明
 */
interface MyInterface {

    // jdk7 :只能声明全局常量和抽象方法
    void method1();
    public static final String ARGS = "123";


    // jdk8中可以声明静态方法和默认方法
    public static void method2() {
        System.out.println("method2");
    }
    default void method3() {
        System.out.println("method3");
        method4();
    }
    // jdk9中可以声明私有方法
    private void method4() {
        System.out.println("私有方法");
    }
}

class MyInterfaceImpl implements MyInterface {
    @Override
    public void method1() {
        System.out.println("实现接口中的抽象方法method1()");
    }
}

public class MyInterfaceTest {
    public static void main(String[] args) {
        MyInterface myInterface = new MyInterfaceImpl();
        myInterface.method1();
        myInterface.method3();
    }
}
