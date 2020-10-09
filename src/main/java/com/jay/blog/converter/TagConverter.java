package com.jay.blog.converter;

import com.jay.blog.entity.Tag;
import com.jay.blog.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-05 10:44
 **/

public class TagConverter {
    /* List  转成 String*/
    public static String listTagToStringTagIds(List<Long> tagIds){
        if (tagIds == null)
            return null;

        StringBuffer result = new StringBuffer();
        boolean flag = false;
        for (Long tagId: tagIds){
            if (flag)
                result.append(",");
            flag = true;
            result.append(tagId);
        }
        return result.toString();
    }

    public static List<Long> StringTagIdsToListTag(String tagIds){
        // tagIds 为空是，返回一个空 List，防止后续遍历失败
        if(StringUtils.checkIsEmpty(tagIds))
            return new ArrayList<>();

        String[] arrayTagIds = tagIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String tagId : arrayTagIds)
            ids.add(Long.parseLong(tagId));

        return ids;
    }


}
