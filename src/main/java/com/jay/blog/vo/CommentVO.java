package com.jay.blog.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jay.blog.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-07 11:34
 **/
@NoArgsConstructor
@ToString
@Data
public class CommentVO implements Serializable {
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
    private CommentVO parentComment;
    /* 是否是管理员的评论*/
    private boolean adminComment;

    private List<CommentVO> replyComments = new ArrayList<>();

    private Date createTime;

    public CommentVO(Long id){
        this.id = id;
    }

}
