package com.jay.blog.utils;

public class StringUtils {

    public static boolean checkIsEmpty(String str){
        if (str == null || str.length() <= 0 || str.equals("")) {
            return true;
        }
        return false;
    }

    public static String subStringIfNotNull(String str, int start, int end){
        if (checkIsEmpty(str)) {
            return str;
        }
        return str.substring(start, end);
    }
}
