package it4u.site.java;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * try语句的改进
 */
public class TryTest {

    /**
     * JDK7之前写法
     */
    @Test
    public void testTry1() {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(System.in);
            // 读取数据过程
            reader.read();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 资源关闭
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * JDK7之后写法
     * 要求资源对象的实例化必须放在try的（）内完成
     */
    @Test
    public void testTry2() {
        try(InputStreamReader reader = new InputStreamReader(System.in)) {
            // 此时的reader是final的，不可再被赋值
            reader.read();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * jdk9中可以在try（）中调用已经实例化的资源对象，在jdk8中无法编译通过
     */
    @Test
    public void testTry3() {
        InputStreamReader reader = new InputStreamReader(System.in);
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        try(reader;writer) {
            // 此时的reader是final的，不可再被赋值
            reader.read();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
