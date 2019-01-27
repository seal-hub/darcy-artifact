package it4u.site.java;

import org.junit.Test;
import site.it4u.bean.Person;

import java.util.logging.Logger;

/**
 * 测试java9的模块化特性
 */
public class ModuleTest {

    private static final Logger LOGGER = Logger.getLogger("it4u");

    public static void main(String[] args) {
        Person p = new Person("tom", 22);
        System.out.println(p);
        LOGGER.info("aaaaa");
    }

    @Test
    public void test1() {
    }
}
