package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jay.blog.entity.BlogAndTag;
import com.jay.blog.entity.Tag;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogAndTagDao extends BaseMapper<BlogAndTag> {

}
