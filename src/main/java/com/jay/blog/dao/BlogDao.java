package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Blog;
import com.jay.blog.vo.BlogVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BlogDao extends BaseMapper<Blog> {

    @Select("select * from b_blog where recommend=1 order by create_time desc limit #{size}")
    public List<Blog> selectRecommendTop(Integer size);

    @Update("update b_blog set views = views+1 where id = #{id}")
    public int updateViews(Long id);

    /*获取所有博客按年份分组的年份，例如：2018、2019、2020*/
    @Select("select function('date_format',create_time, '%Y') as year from b_blog group by function('date_format', create_time, '%Y')")
    public List<String> selectGroupYear();


    @Select("select * from b_blog where id in(select blog_id from b_blog_tag where tag_id=#{tagId})")
    public Page<Blog> selectPageByTagId(Long tagId, Page<Blog> page);
//    @Results(id = "blogVOMap",value = {
//            @Result(property = "user.nickname", "nicknick")
//    })
//    @Select("select * from b_blog blog left join b_type type on blog.type_id = type.id left join b_user user on blog.user_id = user.id ")
//    public Page<BlogVO> selectBlogVO(Page<BlogVO> page);

    @Select("select * from b_blog order by create_time desc")
    public Page<Blog> selectPageOrderByCreateTime(Page<Blog> page);
}
