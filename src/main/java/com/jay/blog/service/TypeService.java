package com.jay.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.entity.Type;
import com.jay.blog.vo.TypeVO;

import java.util.List;

public interface TypeService {


    public Page<Type> listType(Page<Type> page);

    public List<Type> listType();

    public int saveOne(Type type);

    public Type getOneByName(String name);

    public Type getOneById(Long typeId);

    public int delete(Long id);

    public int update(Type type);


    List<TypeVO> listTypeTop(Integer size);
}
