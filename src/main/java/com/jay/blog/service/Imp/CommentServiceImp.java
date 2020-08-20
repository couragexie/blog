package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jay.blog.converter.CommentConverter;
import com.jay.blog.dao.CommentDao;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Comment;
import com.jay.blog.service.CommentService;
import com.jay.blog.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-07 11:34
 **/
@Service
public class CommentServiceImp implements CommentService {
    @Autowired
    private CommentDao commentDao;

    public int saveComment(Comment comment){
        if(comment.getParentCommentId() == -1)
            comment.setParentCommentId(null);
        return commentDao.insert(comment);
    }

    /* 获取comments*/
    @Override
    public List<CommentVO> listCommentByBlogId(Long blogId) {
        // 获取所有的评论
        List<Comment> comments = commentDao.selectList(new QueryWrapper<Comment>().eq("blog_id",blogId));
        List<CommentVO> commentVOS = comments.stream()
                    .map(e -> CommentConverter.commentToCommentVO(e))
                    .collect(Collectors.toList());
        // 将无序的 comment，整理成层级。
        return sortComment(commentVOS);
    }

    /*将无序的评论，整理成层级*/
    private List<CommentVO> sortComment(List<CommentVO> commentVOS){
        List<CommentVO> result = new ArrayList<>();
        for (CommentVO commentVO : commentVOS){
            List<CommentVO> replyComments = new ArrayList<>();
            for (CommentVO commentVO1 : commentVOS){
                if (commentVO1.getParentComment().getId() == null)
                    continue;
                if (commentVO1.getParentComment().getId().equals(commentVO.getId())) {
                    commentVO1.getParentComment().setNickname(commentVO.getNickname());
                    replyComments.add(commentVO1);
                }
            }
            commentVO.setReplyComments(replyComments);
            if (commentVO.getParentComment().getId() == null)
                result.add(commentVO);
        }
        combineComments(result);
        return result;
    }

    private void combineComments(List<CommentVO> commentVOS){
        // 遍历每一个顶级的评论
        for (CommentVO commentVO : commentVOS){
            // 获取顶级评论的子评论
            List<CommentVO> replyComments = commentVO.getReplyComments();
            // 循环每个子评论
            for ( CommentVO reply : replyComments){
                // 深度遍历，递归寻找，将子类的所有子类放到 tempComments
                recursive(reply);
            }
            // 修改顶级节点的 replyComments 集合，改为递归处理后，扁平化为一级的集合
            commentVO.setReplyComments(tempComments);
            // 清空临时区
            tempComments = new ArrayList<>();
        }
    }
    /* 临时区*/
    List<CommentVO> tempComments = new ArrayList<>();
    /* 深度遍历，将遍历到的子评论 加入到 tempComments*/
    private void recursive(CommentVO commentVO){
        tempComments.add(commentVO);
        // 判断当前 comment 是否有子类
        if (commentVO.getReplyComments().size() > 0){
            List<CommentVO> replys = commentVO.getReplyComments();
            for (CommentVO reply : replys)
                recursive(reply);
        }
    }


}
