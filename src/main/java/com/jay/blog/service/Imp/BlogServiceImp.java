package com.jay.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.cache.RedisCache;
import com.jay.blog.cache.RedisCacheRemove;
import com.jay.blog.converter.BlogVOConverter;
import com.jay.blog.converter.TagConverter;
import com.jay.blog.dao.*;
import com.jay.blog.entity.*;
import com.jay.blog.service.BlogService;
import com.jay.blog.utils.CollectionsUtil;
import com.jay.blog.utils.PageUtils;
import com.jay.blog.vo.BlogQuery;
import com.jay.blog.vo.BlogVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
@AllArgsConstructor
public class BlogServiceImp implements BlogService {
    private final static Logger logger = LoggerFactory.getLogger(BlogServiceImp.class);

    private BlogDao blogDao;
    private BlogContentMapper blogContentMapper;
    private BlogAndTagDao blogAndTagDao;
    private UserDao userDao;
    private TypeDao typeDao;
    private TagDao tagDao;

    // TODO 测试
    /* 获取一页博客*/
    @Override
    public Page<BlogVO> listBlog(Page<Blog> page) {
        page =  blogDao.selectPageOrderByCreateTime(page);
        Page<BlogVO> resultPage = PageUtils.copyPage(page);
        //System.out.println(resultPage);
        // 将查询到的结果 Blog 转换成blog;
        resultPage.setRecords(page.getRecords().stream()
                            .map(e-> BlogVOConverter.blogToBlogVoExceptContent(e))
                            .collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    public List<Long> listBlogId(){
        return blogDao.listBlogId();
    }

    // TODO 测试
    /* 根据标签id 获取所有的博客*/
    @Override
    public Page<BlogVO> listBlogByTagId(Long tagId, Page<Blog> page) {
         page = blogDao.selectPageByTagId(tagId, page);
         Page<BlogVO> resultPage = PageUtils.copyPage(page);

         resultPage.setRecords(page.getRecords().stream()
                                   .map(e-> BlogVOConverter.blogToBlogVoExceptContent(e))
                                   .collect(Collectors.toList()));
        return resultPage;
    }


    // TODO 测试
    /* 根据typeId 获取博客*/
    //@RedisCache(cacheNames = "blog&type", key = "#typeId", expire = 1, unit = TimeUnit.HOURS)
    @Override
    public Page<BlogVO> listBlogByTypeId(Long typeId, Page<Blog> page){
        page = blogDao.selectPage(page, new QueryWrapper<Blog>().eq("type_id", typeId));
        Page<BlogVO> resultPage = PageUtils.copyPage(page);

        resultPage.setRecords(page.getRecords().stream()
                .map(e-> BlogVOConverter.blogToBlogVoExceptContent(e))
                .collect(Collectors.toList()));
        return resultPage;

    }

    // TODO 测试
    /* 后台搜索接口，根据查询条件获取指定类型的博客, 该接口只能用于搜索*/
    @Override
    public Page<BlogVO> listBlog(Page<Blog> page, BlogQuery blogQuery) {
        // 构建查询条件
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();

        if (blogQuery != null) {
            Map<String, Object> condition = new LinkedHashMap<>();
            if (blogQuery.getTitle() != null && !blogQuery.getTitle().equals("")) {
                condition.put("title", blogQuery.getTitle());
            }
            if (blogQuery.getTypeId() != null) {
                condition.put("type_id", blogQuery.getTypeId());
            }

            condition.put("recommend", blogQuery.isRecommend());
            queryWrapper.allEq(condition);
        }

        blogDao.selectPage(page, queryWrapper);
        Page<BlogVO> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);
        //System.out.println(resultPage);
        // 将查询到的结果 blog 转换成blogVO,不包含内容;
        resultPage.setRecords(page.getRecords().stream()
                .map(blog-> BlogVOConverter.blogToBlogVoExceptContent(blog))
                .collect(Collectors.toList()));
        return resultPage;

    }

    /**
     * 搜索模块：按照关键字搜索
     * */
    @Override
    public Page<BlogVO> searchListBlog(String query, int pageNo) {
        logger.info(" >>>>> mysql 全文搜索， pageNo={}", pageNo);
        // 构建 page 查询，查询 blog 内容
        Page<BlogVO> resultPage = PageUtils.generatePage(pageNo);
        // 全文搜索，搜索字段 content 和 title
        resultPage.setRecords(blogContentMapper.listBlogContentByQuery(query,resultPage));
        // 搜索关键字失败
        if (CollectionsUtil.checkListIsNull(resultPage.getRecords())) {
            logger.info(">>>>> mysql 全文搜索为空...");
            return null;
        }
        return resultPage;
    }


    @Override
    public BlogVO getBlogVOById(Long blogId){
        Blog blog =  blogDao.selectById(blogId);
        BlogContent blogContent = blogContentMapper.selectOneByBlogId(blogId);
        User user = userDao.selectById(blog.getUserId());
        user.setPassword(null);
        Type type = typeDao.selectById(blog.getTypeId());
        BlogVO blogVO = BlogVOConverter.blogToBlogVo(blog, blogContent);

        List<Tag> tags = tagDao.listTag(blogId);

        blogVO.setUser(user);
        blogVO.setType(type);
        blogVO.setTags(tags);
        return blogVO;
    }



//    @Override
//    @RedisCache(cacheNames = "blog", key = "#id", expire = 60*60*12)
//    public BlogVO getAndConvertById(Long id) throws NotFoundException {
//        Blog blog = blogDao.selectById(id);
//        if(blog == null)
//            throw  new NotFoundException("该博客不存在！");
//        // md 格式转 html
//        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
//        blogDao.updateViews(id);
//        blog.setViews(blog.getViews()+1);
//        BlogVO blogVO = BlogVOConverter.blogToBlogVo(blog);
//        log.info("获取id为 {} 的博客", id);
//        return blogVO;
//    }
    // TODO 测试
    @RedisCache(cacheNames = "blog", key = "#blogId", expire = 60*60*12)
    public BlogVO getBlogVObyIdToView(Long blogId){
        Blog blog = blogDao.selectById(blogId);
        String contentHtml = blogContentMapper.selectContentHtml(blogId);
        BlogVO blogVO = BlogVOConverter.blogToBlogVoExceptContent(blog);
        blogVO.setContentHtml(contentHtml);
        blog.setViews(blog.getViews()+1);
        blogDao.updateViews(blog.getId()); // 更新观看人数，同步到数据库中
        log.info("获取id为 {} 的博客", blogId);
        return blogVO;
    }


    /* 根据标签获取 id， 按照更新时间排序*/
    @Override
    @RedisCache(cacheNames = "blog&tagId", key = "#tagId" ,expire = 60*60)
    public List<Blog> listBlog(Long tagId) {
        return blogDao.selectList(new QueryWrapper<Blog>()
                                        .eq("tag_id", tagId)
                                        .orderByDesc("create_time"));
    }

    @Override
    public List<Blog> listBlog() {
        return blogDao.selectList(null);
    }

    //TODO 测试
    /* 推荐列表，不返回 blog 的内容 */
    @Override
    @RedisCache(cacheNames = "recommendBlog")
    public List<BlogVO> listRecommendBlogTop(Integer size) {
        List<Blog> listRecommendBlogs = blogDao.selectRecommendTop(size);
        return listRecommendBlogs.stream()
                    .map(blog -> BlogVOConverter.blogToBlogVoExceptContent(blog))
                    .collect(Collectors.toList());
    }

    /* 归档，不返回 blog 内容*/
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

    // TODO 测试
    @Override
    @RedisCacheRemove(cacheName = "recommendBlog")
    @Transactional
    public int saveOne(BlogVO blogVO) {
        Blog blog = BlogVOConverter.blogVOToBlog(blogVO);
        BlogContent blogContent = BlogVOConverter.blogVOToBlogContent(blogVO);
        // 插入 blog 和 blogContent
        // blog 插入后，mybatis 会将 id 赋值到 blog 实例中
        blogDao.insert(blog);
        blogVO.setId(blog.getId());
        blogContent.setBlogId(blog.getId());
        blogContentMapper.insert(blogContent);
        // 将 blogVO 中一个包含所有标签的String值转成List<Long>,
        // 然后再通过 Lambada 转成 List<BlogAndTag>
        //System.out.println("blogId" + blog.getId());
        List<BlogAndTag> blogAndTags = TagConverter.StringTagIdsToListTag(blogVO.getTagIds())
                                                .stream()
                                                .map(e-> new BlogAndTag(blog.getId(), e))
                                                .collect(Collectors.toList());
        // 将 blogAndTag 插入到数据库
        for (BlogAndTag blogAndTag : blogAndTags ) {
            blogAndTagDao.insert(blogAndTag);
        }

        log.info("添加博客成功！blogId={}, title={}", blog.getId(), blog.getTitle());
        int ok = 1;
        return ok;
    }

    // TODO 测试
    @Override
    @RedisCacheRemove(cacheName = "blog", key = "#blogVO.getId()")
    @Transactional
    public int updateOne(BlogVO blogVO) throws NotFoundException {
        Blog blog = blogDao.selectById(blogVO.getId());
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        // 删除 blog_tag 关系表中，有关该博客的标签关系
        blogAndTagDao.delete(new QueryWrapper<BlogAndTag>().eq("blog_id", blogVO.getId()));
        // 更新 blog 和 BlogContent
        blog = BlogVOConverter.blogVOToBlog(blogVO);
        BlogContent blogContent = BlogVOConverter.blogVOToBlogContent(blogVO);
        // 将 blogVO 中一个包含所有标签的String值转成List<Long>,
        // 然后再通过 Lambada 转成 List<BlogAndTag>
        List<BlogAndTag> blogAndTags = TagConverter.StringTagIdsToListTag(blogVO.getTagIds()).stream()
                .map(e-> new BlogAndTag(blogVO.getId(), e))
                .collect(Collectors.toList());
        blog.setUpdateTime(new Date());
        blogDao.updateById(blog);
        // 新增 blo_tag 关系表
        for(BlogAndTag blogAndTag : blogAndTags) {
            blogAndTagDao.insert(blogAndTag);
        }
        // 修改内容
        int ok = blogContentMapper.update(blogContent, new QueryWrapper<BlogContent>().eq("blog_id", blogContent.getBlogId()));
        log.info("修改博客成功！blogId={}, title={}", blog.getId(), blog.getTitle());
        return ok;
    }

    // TODO 测试
    @Override
    @RedisCacheRemove(cacheName = "blog", key = "#blogId")
    @Transactional
    public int deleteOne(Long blogId) {
        blogDao.deleteById(blogId);
        blogAndTagDao.delete(new QueryWrapper<BlogAndTag>().eq("blog_id", blogId));
        int ok =  blogContentMapper.delete(blogId);
        return ok;
    }


}
