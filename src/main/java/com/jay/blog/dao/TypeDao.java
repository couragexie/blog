package com.jay.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jay.blog.entity.Type;
import com.jay.blog.vo.TypeVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeDao extends BaseMapper<Type> {

    @Results(id = "typeVOResult", value={
            @Result(property = "id", column = "typeId"),
            @Result(property = "name", column = "typeName"),
            @Result(property = "blogNum", column = "blogNum")
    })
    @Select("select type.id typeId, type.name typeName ,count(blog.id) blogNum from b_blog blog right join b_type type on type.id = blog.type_id group by type.id having count(blog.id)>0  order by count(blog.id) desc limit #{size}")
    public List<TypeVO> listTypeIdTop(Integer size);

}
