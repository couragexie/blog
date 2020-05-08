package com.jay.blog.service;

import com.jay.blog.entity.User;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-02 22:28
 **/

public interface UserService {

    public User checkUser(String username, String password);

    public User getOneById(Long id);
}
