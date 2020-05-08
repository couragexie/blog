package com.jay.blog.converter;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.jay.blog.entity.Comment;
import com.jay.blog.vo.CommentVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-07 11:38
 **/

public class CommentConverter {

    public static CommentVO commentToCommentVO(Comment comment){
        CommentVO  commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        Long parent_id = comment.getParentCommentId() == null ? null : comment.getParentCommentId();
        commentVO.setParentComment(new CommentVO(parent_id));
        return commentVO;
    }

}
