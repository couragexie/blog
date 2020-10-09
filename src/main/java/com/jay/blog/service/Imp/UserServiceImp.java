package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jay.blog.dao.UserDao;
import com.jay.blog.entity.User;
import com.jay.blog.service.UserService;
import com.jay.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-02 21:55
 **/
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    public User checkUser(User user){

        return userDao.selectOne(new QueryWrapper<User>()
                                            .eq("username", user.getUsername())
                                            .eq("password", MD5Utils.code(user.getPassword())));
    }

    @Override
    public User getOneById(Long id){
        return userDao.selectById(id);
    }



}
