package it4u.site.java;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalTest {

    /**
     * Optional类中提供了转换为Stream的方法
     */
    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("tom");
        list.add("jerry");
        list.add("tim");
        Optional<List<String>> optional = Optional.ofNullable(list);
        Stream<String> stringStream = optional.stream().flatMap(x -> x.stream());
        System.out.println(stringStream.count());
    }
}
