package com.jay.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Tag;
import com.jay.blog.vo.TagVO;

import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 14:48
 **/

public interface TagService {
    /**/
    public Page<Tag> listTag(Page<Tag> page);

    public List<Tag> listTag();

    public List<Tag> listTag(Long blogId);

    public String getTagIds(Long blogId);

    public Tag getOneByName(String name);

    public Tag getOneById(Long id);

    public int saveOne(Tag tag);

    public int updateOne(Tag tag);

    public int deleteOne(Long id);

    List<TagVO> listTagTop(Integer size);

}
