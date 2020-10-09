package com.jay.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Blog;
import com.jay.blog.vo.BlogQuery;
import com.jay.blog.vo.BlogVO;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;
import java.util.Map;

public interface BlogService {

    /*获取一页*/
    public Page<BlogVO> listBlog(Page<Blog> page);

    /* 根据标签来获取*/
    public Page<BlogVO> listBlogByTagId(Long tagId, Page<Blog> page);

    /* 根据分类来获取*/
    public Page<BlogVO> listBlogByTypeId(Long typeId, Page<Blog> page);

    /* 根据分类、标题、是否推荐等条件来查询来获取*/
    public Page<BlogVO> listBlog(Page<Blog> page, BlogQuery blogQuery);

    /* 根据搜索模糊查询来获取*/
    public Page<BlogVO> searchListBlog(String query, int pageNo);

    /* 根据 id 获取*/
    public BlogVO getOneById(Long blogIsd);

    /* 根据id来获取， 并将 md 格式转为 html 格式*/
   // public BlogVO getAndConvertById(Long id) throws NotFoundException;

    /*根据标签来获取*/
    public List<Blog> listBlog(Long tagId);


    /*获取所有的博客*/
    public List<Blog> listBlog();

    /* 获取最新推荐的blog*/
    public List<BlogVO> listRecommendBlogTop(Integer size);

    /* 获取根据年份分类的 blog*/
    public Map<String, List<Blog>> archiveBlogs();

    /* 获取博客的数量*/
    public Integer countBlog();

    /* 新增*/
    public int saveOne(BlogVO blog);

    /* 更新*/
    public int updateOne(BlogVO blog) throws NotFoundException;

    public int deleteOne(Long blogId);

}
