package com.jay.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-01 17:01
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Component
@TableName("b_blog")
public class Blog {
    /*id*/
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /*标题*/
    private String title;
    /*内容*/
    private String content;
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

    private Long typeId;
    private Long userId;

    private Date createTime;
    private Date updateTime;


}
