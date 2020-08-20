package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jay.blog.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-01 17:34
 **/
@Repository
public interface UserDao extends BaseMapper<User> {
}
