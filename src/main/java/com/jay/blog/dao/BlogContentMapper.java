package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.BlogContent;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import com.jay.blog.vo.BlogVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BlogContentMapper extends BaseMapper<BlogContent> {
    /* 返回 html 格式的内容*/
    @Select("SELECT content_html from b_blog_content where blog_id = #{blog_id}")
    public String selectContentHtml(long blog_id);

    /* 返回 makedown 格式的内容*/
    @Select("SELECT content_md from b_blog_content where blog_id = #{blog_id}")
    public String selectContentMd(long blog_id);

    @Select("SELECT * from b_blog_content where blog_id = #{blogId}")
    public BlogContent selectOneByBlogId(long blogId);


    /* 根据关键字进行全文搜索，多表联查*/
    // 要点!! 分页返回的对象与传入的对象是同一个
    public List<BlogVO> listBlogContentByQuery(@Param("query") String query, Page<BlogVO> page);


    @Delete("delete from b_blog_content where blog_id = #{blogId}")
    public int delete(long blogId);

    @Select("<script> "
            + "select * from b_blog_content where blog_id in"
            + "<foreach item='item' index='index' type='blogIds' open='(' separator=',' close=')' >"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    public List<BlogContent> listBlogContentByBlogIds(List<Long> blogIds);

    @Select({"<script> "
            + "select id, blog_id, content_html from b_blog_content where blog_id in"
            + "<foreach item='item' index='index' type='blogIds' open='(' separator=',' close=')' >"
            + "#{item}"
            + "</foreach>"
            + "</script>"})
    public List<BlogContent> listBlogContentHtmlByBlogIds(List<Long> blogIds);
}
