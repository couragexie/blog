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
@TableName("b_comment")
public class Comment {
    /* id */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /*评论者昵称*/
    private String nickname;
    /*email*/
    private String email;
    /*内容*/
    private String content;
    /*评论者头像*/
    private String avatar;
    /*所属博客id*/
    private Long blogId;
    /*父级评论 id*/
    private Long parentCommentId;
    /* 是否是管理员的评论*/
    private boolean adminComment;

    private Date createTime;

}
