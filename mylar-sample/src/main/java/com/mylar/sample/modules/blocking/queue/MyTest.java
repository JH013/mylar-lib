package com.mylar.sample.modules.blocking.queue;

/**
 * @author wangz
 * @date 2022/3/14 0014 21:23
 */
public class MyTest {

    public static void main(String[] args) {

        Person person = new Person();
        person.setName("456");

        test(person);

        System.out.println(person);
    }

    private static void test(Person person) {
        person = new Person();
        person.setName("123");
    }
}
