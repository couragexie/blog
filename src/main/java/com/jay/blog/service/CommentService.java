package com.jay.blog.service;

import com.jay.blog.vo.CommentVO;

import java.util.List;

public interface CommentService {

    public List<CommentVO> listCommentByBlogId(Long blogId);

}
