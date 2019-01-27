package it4u.site.java;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {

    // jdk9中针对Stream增加了4个方法
    @Test
    public void test1() {
        List<Integer> list = Arrays.asList(12, 3, 83, 90, 56);
        Stream<Integer> stream = list.stream();
        stream.takeWhile(x -> x < 70).forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<Integer> list = Arrays.asList(1, 45, 2, 3, 4, 5, 6);
        Stream<Integer> stream = list.stream();
        stream.dropWhile(x -> x < 5).forEach(System.out::println);
    }

    @Test
    public void test3() {
        Stream<Integer> stream1 = Stream.of(1, 2, 3, null);
        stream1.forEach(System.out::println);
        System.out.println();
        // 如果只有单个元素，此元素不能为null，否则报异常
//        Stream<Object> stream2 = Stream.of(null);
        // jdk9中新增的ofNullable（）允许存放单个null
        Stream<String> stream3 = Stream.ofNullable("tom");
        System.out.println(stream3.count());
        Stream<String> stream4 = Stream.ofNullable(null);
        System.out.println(stream4.count());
    }

    @Test
    public void test4() {
        Stream.iterate(0, x -> x + 1).limit(100).forEach(System.out::println);
        // jdk9 中新增如下方式
        Stream.iterate(0, x -> x < 10, x -> x + 1).forEach(System.out::println);
    }
}
