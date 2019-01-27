package it4u.site.java;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 钻石操作符改进
 */
public class DiamondOperatorTest {

    @Test
    public void testDiamondOperator() {
        diamondOperator();
    }

    @Test
    public void diamondOperator2() {
        diamondOperatorForJDK9();
    }

    public void diamondOperator() {
        Set<String> set = new HashSet<>();
        set.add("GG");
        set.add("MM");
        for (String s : set) {
            System.out.println(s);
        }
    }

    public void diamondOperatorForJDK9() {
        // 创建一个继承于HashSet的匿名子类的对象
        Set<String> set = new HashSet<>(){
            // 扩展功能
        };
        set.add("GG");
        set.add("MM");
        for (String s : set) {
            System.out.println(s);
        }
    }
}
