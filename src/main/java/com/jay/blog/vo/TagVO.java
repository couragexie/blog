package com.jay.blog.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-05 17:19
 **/
@ToString
@Data
public class TagVO implements Serializable {
    private Long id;
    private String name;

    private Integer blogNum;
}
