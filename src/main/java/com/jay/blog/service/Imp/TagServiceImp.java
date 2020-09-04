package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.TagConverter;
import com.jay.blog.dao.BlogAndTagDao;
import com.jay.blog.dao.TagDao;
import com.jay.blog.entity.BlogAndTag;
import com.jay.blog.entity.Tag;
import com.jay.blog.service.TagService;
import com.jay.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 14:49
 **/
@Service
public class TagServiceImp implements TagService {
    @Autowired
    private TagDao tagDao;

    @Autowired
    private BlogAndTagDao blogAndTagDao;

    @Override
    public Page<Tag> listTag(Page<Tag> page) {
        return tagDao.selectPage(page, null);
    }

    @Override
    public List<Tag> listTag() {
        return tagDao.selectList(null);
    }

    @Override
    public List<Tag> listTag(Long blogId) {

        return tagDao.listTag(blogId);
    }

    /* 获取博客的所有标签id 并将其转成 String 类型*/
    public String getTagIds(Long blogId){
        // 获取 BlogAndTag 对象集合
        List<BlogAndTag> blogAndTag = blogAndTagDao.selectList(new QueryWrapper<BlogAndTag>()
                .eq("blog_id",blogId));
        // 获取 BlogAndTag 对象集合中所有tagId值，并将其转成 String 类型。
        return TagConverter.listTagToStringTagIds(blogAndTag.stream()
                                            .map(e-> e.getTagId()).collect(Collectors.toList()));
    }

    @Override
    public Tag getOneByName(String name) {
        return tagDao.selectOne(new QueryWrapper<Tag>().eq("name", name));
    }

    @Override
    public Tag getOneById(Long id) {
        return tagDao.selectById(id);
    }

    @Override
    public int saveOne(Tag tag) {
        return tagDao.insert(tag);
    }

    @Override
    public int updateOne(Tag tag) {
        return tagDao.updateById(tag);
    }

    @Override
    public int deleteOne(Long id) {
        return tagDao.deleteById(id);
    }

    @Override
    public List<TagVO> listTagTop(Integer size) {
        return tagDao.listTagIdTop(size);
    }

}
