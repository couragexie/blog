package com.jay.blog.utils;

import java.util.List;

public class CollectionsUtil {
    public static boolean checkListIsNull(List list){
        if (list == null || list.size() == 0)
            return true;
        return false;
    }

}
