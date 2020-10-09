package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.cache.RedisCache;
import com.jay.blog.cache.RedisCacheRemove;
import com.jay.blog.dao.TypeDao;
import com.jay.blog.entity.Tag;
import com.jay.blog.entity.Type;
import com.jay.blog.service.TypeService;
import com.jay.blog.vo.TagVO;
import com.jay.blog.vo.TypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-03 18:02
 **/
@Service
public class TypeServiceImp implements TypeService {
    @Autowired
    private TypeDao typeDao;

    @Override
    public Page<Type> listType(Page<Type> page) {

        return  typeDao.selectPage(page, null);
    }


    @Override
    @RedisCache(cacheNames = "type")
    public List<Type> listType() {
        return typeDao.selectList(null);
    }


    @Override
    @RedisCacheRemove(cacheName = "type")
    public int saveOne(Type type) {
        return typeDao.insert(type);
    }

    @Override
    public Type getOneByName(String name) {
       return typeDao.selectOne(new QueryWrapper<Type>().eq("name", name));
    }

    @Override
    public Type getOneById(Long typeId) {
        return typeDao.selectById(typeId);
    }

    @Override
    @RedisCacheRemove(cacheName = "type")
    public int delete(Long id) {
        return typeDao.deleteById(id);
    }

    @Override
    @RedisCacheRemove(cacheName = "type")
    public int update(Type type) {
        return typeDao.updateById(type);
    }

    @Override
    public List<TypeVO> listTypeTop(Integer size) {
        List<TypeVO> typeVOs = typeDao.listTypeIdTop(size);
        List<Long> typeIds = typeVOs.stream().map(e->e.getId()).collect(Collectors.toList());
        List<Type> types = typeDao.selectBatchIds(typeIds);
        for (TypeVO typeVO : typeVOs) {
            for (Type type : types) {
                if(typeVO.getId() == type.getId()) {
                    typeVO.setName(type.getName());
                    break;
                }
            }
        }

        return typeVOs;
    }
}
