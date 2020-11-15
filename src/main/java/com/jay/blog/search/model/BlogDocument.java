package com.jay.blog.search.model;

import com.jay.blog.entity.Tag;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Es 全文搜索对象,
 * @Author: xiejie
 * @Date: 2020/11/10 17:27
 */
@Data
public class BlogDocument {

    private Long id;
    /*标题*/
    private String title;

    /*首图*/
    private String firstPicture;

    /*介绍描述*/
    private String description;

    /* Makedown 格式的内容*/
    private String contentMd;

    /*查看人数*/
    private Integer views;

    private Type blogType;

    private User user;
    /* 博客和标签*/
    private List<Tag> tags = new ArrayList<>();

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return "BlogDocument{" +
                "title='" + title + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", description='" + description + '\'' +
                ", contentMd='" + contentMd.substring(0,100) + '\'' +
                ", views=" + views +
                ", type=" + blogType +
                ", user=" + user +
                ", tags=" + tags +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
