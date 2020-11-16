package com.jay.blog.converter;

import com.jay.blog.entity.Blog;
import com.jay.blog.entity.BlogContent;
import com.jay.blog.entity.Type;
import com.jay.blog.entity.User;
import com.jay.blog.search.model.BlogDocument;
import com.jay.blog.vo.BlogVO;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 17:33
 **/

public class BlogVOConverter {

    public static BlogVO blogToBlogVoExceptContent(Blog blog){
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        blogVO.setType(new Type(blog.getTypeId(), null));
        User user = new User();
        user.setId(blog.getUserId());
        blogVO.setUser(user);
        return blogVO;
    }

    public  static BlogVO blogToBlogVo(Blog blog, BlogContent blogContent){
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        blogVO.setContentMd(blogContent.getContentMd());
        blogVO.setContentHtml(blogContent.getContentHtml());
        blogVO.setType(new Type(blog.getTypeId(), null));
        User user = new User();
        user.setId(blog.getUserId());
        blogVO.setUser(user);
        return blogVO;
    }

    public static List<BlogVO> blogToBlogVO(List<Blog> blogs, List<BlogContent> blogContents){
        Map<Long, BlogContent> map = new HashMap<>();
        for (BlogContent blogContent : blogContents)
            map.put(blogContent.getBlogId(), blogContent);
        List<BlogVO> blogVOS = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            blogVO.setUser(new User(blog.getUserId()));
            blogVO.setType(new Type(blog.getTypeId()));
            BlogContent blogContent = map.get(blog.getId());
            blogVO.setContentMd(blogContent.getContentMd());
            blogVO.setContentHtml(blogContent.getContentHtml());
            blogVOS.add(blogVO);
        }
        return blogVOS;
    }


    public static Blog blogVOToBlog(BlogVO blogVO){
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogVO, blog);
        blog.setTypeId(blogVO.getType().getId());
        blog.setUserId(blogVO.getUser().getId());
        return blog;
    }

    public  static BlogContent blogVOToBlogContent(BlogVO blogVO){
        BlogContent blogContent = new BlogContent();
        blogContent.setBlogId(blogVO.getId());
        blogContent.setContentHtml(blogVO.getContentHtml());
        blogContent.setContentMd(blogVO.getContentMd());
        blogContent.setTitle(blogVO.getTitle());
        return blogContent;
    }

    public static BlogDocument blogVOToBlogDocument(BlogVO blogVO){
        BlogDocument blogDocument = new BlogDocument();
        BeanUtils.copyProperties(blogVO, blogDocument);
        return blogDocument;
    }

    public static BlogVO blogDocumentToBlogVO(BlogDocument blogDocument){
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blogDocument, blogVO);
        return blogVO;
    }

}
