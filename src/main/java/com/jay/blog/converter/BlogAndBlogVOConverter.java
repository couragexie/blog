package com.jay.blog.converter;

import com.jay.blog.entity.Blog;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import com.jay.blog.vo.BlogVO;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.springframework.beans.BeanUtils;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 17:33
 **/

public class BlogAndBlogVOConverter {

    public  static BlogVO blogToBlogVo(Blog blog){
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        blogVO.setType(new Type(blog.getTypeId(), null));
        User user = new User();
        user.setId(blog.getUserId());
        blogVO.setUser(user);
        return blogVO;
    }

    public static Blog blogVOToBlog(BlogVO blogVO){
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogVO, blog);
        blog.setTypeId(blogVO.getType().getId());
        blog.setUserId(blogVO.getUser().getId());
        return blog;
    }

}
