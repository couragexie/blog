package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jay.blog.entity.Comment;
import org.springframework.stereotype.Repository;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-01 17:35
 **/

@Repository
public interface CommentDao extends BaseMapper<Comment> {
}
