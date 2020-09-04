package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.BlogAndBlogVOConverter;
import com.jay.blog.converter.TagConverter;
import com.jay.blog.dao.BlogAndTagDao;
import com.jay.blog.dao.BlogDao;
import com.jay.blog.entity.Blog;
import com.jay.blog.entity.BlogAndTag;
import com.jay.blog.service.BlogService;
import com.jay.blog.utils.MarkdownUtils;
import com.jay.blog.vo.BlogQuery;
import com.jay.blog.vo.BlogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 15:52
 **/
@Slf4j
@Service
public class BlogServiceImp implements BlogService {
    public static final ArrayList<Blog> VALUE = new ArrayList<>();
    @Autowired
    private BlogDao blogDao;


    @Autowired
    private BlogAndTagDao blogAndTagDao;

    /* 获取一页博客*/
    @Override
    public Page<BlogVO> listBlog(Page<Blog> page) {
        page =  blogDao.selectPageOrderByCreateTime(page);
        Page<BlogVO> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);
        //System.out.println(resultPage);
        // 将查询到的结果 Blog 转换成blog;
        resultPage.setRecords(page.getRecords().stream()
                            .map(e->BlogAndBlogVOConverter.blogToBlogVo(e))
                            .collect(Collectors.toList()));
        return resultPage;
    }

    /* 根据标签id 获取所有的博客*/
    @Override
    public Page<BlogVO> listBlogByTagId(Long tagId, Page<Blog> page) {
         page = blogDao.selectPageByTagId(tagId, page);
         Page<BlogVO> resultPage = new Page<>();
         BeanUtils.copyProperties(page, resultPage);

         resultPage.setRecords(page.getRecords().stream()
                                   .map(e->BlogAndBlogVOConverter.blogToBlogVo(e))
                                   .collect(Collectors.toList()));
        return resultPage;
    }

    /* 根据typeId 获取博客*/
    public Page<BlogVO> listBlogByTypeId(Long typeId, Page<Blog> page){
        page = blogDao.selectPage(page, new QueryWrapper<Blog>().eq("type_id", typeId));
        Page<BlogVO> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);

        resultPage.setRecords(page.getRecords().stream()
                .map(e->BlogAndBlogVOConverter.blogToBlogVo(e))
                .collect(Collectors.toList()));
        return resultPage;

    }

    /* 根据查询条件获取所有的博客, 该接口只能用于搜索*/
    @Override
    public Page<BlogVO> listBlog(Page<Blog> page, BlogQuery blogQuery) {
        // 构建查询条件
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        if (blogQuery != null) {
            Map<String, Object> condition = new LinkedHashMap<>();
            if (blogQuery.getTitle() != null && !blogQuery.getTitle().equals(""))
                condition.put("title", blogQuery.getTitle());
            if (blogQuery.getTypeId() != null)
                condition.put("type_id", blogQuery.getTypeId());

            condition.put("recommend", blogQuery.isRecommend());
            queryWrapper.allEq(condition);
        }

        blogDao.selectPage(page, queryWrapper);
        Page<BlogVO> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);
        //System.out.println(resultPage);
        // 将查询到的结果 Blog 转换成blog;
        resultPage.setRecords(page.getRecords().stream()
                .map(e->BlogAndBlogVOConverter.blogToBlogVo(e))
                .collect(Collectors.toList()));
        return resultPage;

    }

    @Override
    public Page<BlogVO> listBlog(String query, Page<Blog> page) {

        blogDao.listBlogByQuery(query,page);

        Page<BlogVO> resultPage = new Page<>();
        // 将 page 的值赋值给 resultPage
        // 指定字段不复制
        List<String> excludeFiled = new ArrayList<>();
        excludeFiled.add("records");
        BeanUtils.copyProperties(page, resultPage,excludeFiled.toArray(new String[excludeFiled.size()]));
        // 转换 page 中records，转成 BlogVO
        List<BlogVO> blogVOS = page.getRecords().stream()
                                .map(e->BlogAndBlogVOConverter.blogToBlogVo(e))
                                .collect(Collectors.toList());
        resultPage.setRecords(blogVOS);

        return resultPage;
    }

    @Override
    public Blog getOneById(Long id){
        return blogDao.selectById(id);
    }


    @Override
    @Cacheable(cacheNames = "blog", key = "#id")
    public BlogVO getAndConvertById(Long id) throws NotFoundException {
        Blog blog = blogDao.selectById(id);
        if(blog == null)
            throw  new NotFoundException("该博客不存在！");
        // md 格式转 html
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
        blogDao.updateViews(id);
        blog.setViews(blog.getViews()+1);
        BlogVO blogVO = BlogAndBlogVOConverter.blogToBlogVo(blog);
        log.info("获取id为 {} 的博客", id);
        return blogVO;
    }


    /* 根据标签获取 id， 按照更新时间排序*/
    @Override
    public List<Blog> listBlog(Long tagId) {
        return blogDao.selectList(new QueryWrapper<Blog>()
                                        .eq("tag_id", tagId)
                                        .orderByDesc("create_time"));
    }

    @Override
    public List<Blog> listBlog() {
        return blogDao.selectList(null);
    }

    @Override
    public List<BlogVO> listRecommendBlogTop(Integer size) {

        return blogDao.selectRecommendTop(size)
                                     .stream()
                                     .map(e-> BlogAndBlogVOConverter.blogToBlogVo(e))
                                     .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Blog>> archiveBlogs() {
        Map<String, List<Blog>> archiveBlog = new HashMap<>();
        List<Blog> blogs = listBlog();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        for (Blog blog : blogs){
            String year = dateFormat.format(blog.getCreateTime());
            if (!archiveBlog.containsKey(year)) {
                archiveBlog.put(year, new ArrayList<>());
                archiveBlog.get(year).add(blog);
            }else{
                archiveBlog.get(year).add(blog);
            }
        }

        return archiveBlog;
    }

    @Override
    public Integer countBlog() {
        return blogDao.selectCount(null);
    }

    @Override
    @Transactional
    public int saveOne(BlogVO blogVO) {
        Blog blog = BlogAndBlogVOConverter.blogVOToBlog(blogVO);
        System.out.println("blogId" + blog.getId());
        int ok = blogDao.insert(blog);
        // 将 blogVO 中一个包含所有标签的String值转成List<Long>,
        // 然后再通过 Lambada 转成 List<BlogAndTag>
        System.out.println("blogId" + blog.getId());
        List<BlogAndTag> blogAndTags = TagConverter.StringTagIdsToListTag(blogVO.getTagIds()).stream()
                                                .map(e-> new BlogAndTag(blog.getId(), e))
                                                .collect(Collectors.toList());

        for (BlogAndTag blogAndTag : blogAndTags )
            blogAndTagDao.insert(blogAndTag);

        return ok;
    }

    @Override
    @Transactional
    public int updateOne(BlogVO blogVO) throws NotFoundException {
        Blog blog = blogDao.selectById(blogVO.getId());
        if (blog == null)
            throw new NotFoundException("该博客不存在");
        // 删除 blog_tag 关系表中，有关该博客的标签关系
        blogAndTagDao.delete(new QueryWrapper<BlogAndTag>().eq("blog_id", blogVO.getId()));

        // 更新blog
        blog = BlogAndBlogVOConverter.blogVOToBlog(blogVO);
        // 将 blogVO 中一个包含所有标签的String值转成List<Long>,
        // 然后再通过 Lambada 转成 List<BlogAndTag>
        List<BlogAndTag> blogAndTags = TagConverter.StringTagIdsToListTag(blogVO.getTagIds()).stream()
                .map(e-> new BlogAndTag(blogVO.getId(), e))
                .collect(Collectors.toList());
        blog.setUpdateTime(new Date());
        int ok = blogDao.updateById(blog);
        // 新增 blo_tag 关系表
        for(BlogAndTag blogAndTag : blogAndTags)
            blogAndTagDao.insert(blogAndTag);

        return ok;
    }

    @Override
    @Transactional
    public int deleteOne(Long id) {
        int ok = blogDao.deleteById(id);
        blogAndTagDao.delete(new QueryWrapper<BlogAndTag>().eq("blog_id", id));
        return ok;
    }
}
