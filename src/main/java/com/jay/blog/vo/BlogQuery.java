package com.jay.blog.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: blog
 * @description: 查询条件
 * @author: Jay
 * @create: 2020-04-05 12:49
 **/
@Data
public class BlogQuery implements Serializable {

    private String title;

    private Long typeId;

    private boolean recommend;

}
