package com.jay.blog.search;

/**
 * @Author: xiejie
 * @Date: 2020/11/19 18:03
 */
public class Main {

    public static void main(String[] args) {
        String s2 = new String("myString").intern();
        String s1 = "myString";

        System.out.println(s1 == s2);
    }
}
