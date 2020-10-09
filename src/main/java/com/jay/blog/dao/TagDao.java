package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Tag;
import com.jay.blog.vo.TagVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TagDao extends BaseMapper<Tag> {

    @Results(id = "tagVO", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "blogNum", column="blogNum")
    })
    @Select("select tag.id id, tag.name name, count(bt.blog_id) blogNum from b_tag as tag left join b_blog_tag as bt on tag.id = bt.tag_id group by tag.id having count(bt.blog_id)>0 order by count(bt.blog_id) desc limit #{size}")
    public List<TagVO> listTagIdTop(Integer size);

    @Select(" select * from b_tag as t where id in(select bt.tag_id from b_blog_tag as bt where bt.blog_id = #{blog_id})")
    public List<Tag> listTag(Long blog_id);
}


