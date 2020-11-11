package com.jay.blog.vo;

import com.jay.blog.entity.Tag;
import com.jay.blog.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 18:13
 */
@Data
public class BlogSearchVO {

    /*标题*/
    private String title;

    /*首图*/
    private String firstPicture;

    /*介绍描述*/
    private String description;

    /*查看人数*/
    private Integer views;

    private User user;
    /* 博客和标签*/
    private List<Tag> tags = new ArrayList<>();

    private Date createTime;

    private Date updateTime;
}
