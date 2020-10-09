package com.jay.blog.vo;

import com.jay.blog.entity.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 17:22
 **/
@Data
public class BlogVO implements Serializable{

    private Long id;

    /*标题*/
    @NotBlank(message = "标题不能为空")
    private String title;
    /* Makedown 格式的内容*/
    private String contentMd;
    /* Html 格式的内容 */
    private String contentHtml;
    /*首图*/
    private String firstPicture;
    /*介绍描述*/
    private String description;
    /* 是否标识为原创*/
    private String flag;
    /*查看人数*/
    private Integer views;
    /*开启赞赏*/
    private boolean appreciation;
    /* 开启版权*/
    private boolean copyright;
    /* 开启可评论*/
    private boolean commentabled;
    /* 是否发布状态*/
    private boolean published;
    /*是否推荐*/
    private boolean recommend;

    private Type type;

    private User user;

    /*该博客所有的标签id是 1,2,3*/
    private String tagIds;

    private Date createTime;
    private Date updateTime;

    /* 博客和标签*/
    private List<Tag> tags = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

}
