package it4u.site.java;

import org.junit.Test;

import java.util.*;

/**
 * 只读集合测试
 */
public class CollectionMapTest {

    // 创建一个只读特点的集合list(jdk9之前）
    @Test
    public void test1() {
       List<String> list = new ArrayList<>();
       list.add("tom");
       list.add("jerry");
       list.add("steven");
       // 将list变为只读的
       List<String> newList = Collections.unmodifiableList(list);
       // 不可以做下面的操作
//       newList.add("dd");
       newList.forEach(System.out::println);
    }

    // 创建一个只读特点的集合list(jdk9之前）
    @Test
    public void test2() {
        List<Integer> integers = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
        // 不可以做下面的操作
//        integers.add(5);
    }

    // 创建一个只读特点的集合set(jdk9之前）
    @Test
    public void test3() {
        Set<Integer> integers = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(1, 2, 3)));
        integers.forEach(System.out::println);
        // 不可以做下面的操作
//        integers.add(4);
    }

    // 创建一个只读特点的集合map(jdk9之前）
    @Test
    public void test4() {
        Map<Object, Object> objectObjectMap = Collections.unmodifiableMap(new HashMap<>() {
            {
                put("tom", 78);
                put("jerry", 68);
                put("steven", 100);
            }
        });
        objectObjectMap.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    // 创建一个只读特点的集合list(jdk9）
    @Test
    public void test5() {
        // 创建只读的list
        List<String> names = List.of("tom", "jerry", "steven");
        names.forEach(System.out::println);
    }

    // 创建一个只读特点的集合set(jdk9）
    @Test
    public void test6() {
        // 创建只读的set
        Set<String> names = Set.of("tom", "jerry", "steven");
        names.forEach(System.out::println);
    }

    // 创建一个只读特点的集合map(jdk9）
    @Test
    public void test7() {
        // 创建只读的map方法1
        Map<String, Integer> nameValuePairs = Map.of("tom", 11, "jerry", 22, "steven", 30);
        nameValuePairs.forEach((k, v) -> System.out.println(k + ":" + v));
        // 创建只读的map方法2
        Map<String, Integer> stringIntegerMap = Map.ofEntries(Map.entry("tom", 11), Map.entry("jerry", 4));
        stringIntegerMap.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
