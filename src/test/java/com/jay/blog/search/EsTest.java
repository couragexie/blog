package com.jay.blog.search;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.BlogVOConverter;
import com.jay.blog.search.es.EsHandler;
import com.jay.blog.search.model.BlogDocument;
import com.jay.blog.service.BlogService;
import com.jay.blog.utils.PageUtils;
import com.jay.blog.vo.BlogVO;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: xiejie
 * @Date: 2020/11/11 16:18
 */
@SpringBootTest
public class EsTest {
    @Autowired
    EsHandler esHandler;

    @Autowired
    BlogService blogService;

    @Test
    public void createBlogMapping() throws IOException {
        String blogMapping = blogMapping();
        //System.out.println(blogMapping);
        String index = "blog";
        esHandler.createIndexMapping(index);
    }

    @Test
    public void putBlogMapping() throws IOException {
        String blogMapping = blogMapping();
        String index = "blog";
        esHandler.putIndexMapping(index, blogMapping);
    }

    @Test
    public void deleteIndex() throws IOException {
        String index = "blog";
        esHandler.deleteIndex(index);
    }

    @Test
    public void createDoc() throws IOException {
        Long blogId = 2L;
        BlogVO blogVO = blogService.getBlogVOById(blogId);
        //System.out.println(blogVO);
        BlogDocument blogDocument = BlogVOConverter.blogVOToBlogDocument(blogVO);
        String documentJson = JSONObject.toJSONString(blogDocument);
//        System.out.println(blogDocument);
        System.out.println(documentJson);
        esHandler.createDoc("blog", blogId.toString() , documentJson);
    }

    /**
     * 批量插入文档
     * @author xiejie
     * @since 2020/11/23
     * @param
     */
    @Test
    public void bulkDoc() throws IOException {
        List<Long> blogsId = blogService.listBlogId();
        for (Long blogId : blogsId){
            BlogVO blogVO = blogService.getBlogVOById(blogId);
            BlogDocument blogDocument = BlogVOConverter.blogVOToBlogDocument(blogVO);
            String documentJson = JSONObject.toJSONString(blogDocument);
            esHandler.createDoc("blog", blogId.toString(), documentJson);
        }

    }

    /**
     * 测试全文搜索
     * @author xiejie
     * @since 2020/11/23
     * @param
     */
    @Test
    public void fullSearch() throws IOException {
        List<String> searchFields = new ArrayList<>();
        searchFields.add("contentMd");
        searchFields.add("title");
        String query="java";
        Page<BlogDocument> page = new Page<>();
        String[] fields = searchFields.toArray(new String[searchFields.size()]);
        esHandler.fullSearch("blog", fields,query, page, BlogDocument.class);
        for (BlogDocument blogDocument : page.getRecords()){
            System.out.println(blogDocument.getTitle());
        }
        System.out.println(page.getTotal());

    }

    /**
     * 测试 Es 的分页情况
     * @author xiejie
     * @since 2020/11/23
     * @param
     */
    @Test
    public void testFullSearchPaging() throws IOException {
        int pageSize = 6;
        int pageNo = 0;

        List<String> searchFields = new ArrayList<>();
        searchFields.add("contentMd");
        searchFields.add("title");
        String query="java";
        String[] fields = searchFields.toArray(new String[searchFields.size()]);

        List<String> set = new ArrayList<>();
        for (int i = 1; i <= 42 / 6; i ++){
            Page<BlogDocument> page = PageUtils.generatePage(i);
            esHandler.fullSearch("blog", fields,query, page, BlogDocument.class);

            for (BlogDocument blogDocument : page.getRecords()){
                if (set.contains(blogDocument.getTitle())){
                    System.out.println("重复..." + blogDocument.getTitle());
                    continue;
                }
                set.add(blogDocument.getTitle());
            }
        }
        System.out.println("set size : " + set.size());

        for (String title : set){
            System.out.println(title);
        }
    }

    @Test
    public void testPage(){
        int pageNo = 0;
        int pageSize = 6;
        Page page = new Page(pageNo, pageSize);
        page.setCurrent(0);
        System.out.println(page.getCurrent());
    }

    public static String blogMapping() throws IOException {
        XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
        jsonBuilder.startObject();
        {
            jsonBuilder.startObject("properties");
            {
                // title
                jsonBuilder.startObject("title");
                {
                    jsonBuilder.field("type", "text");
                    jsonBuilder.field("analyzer", "ik_max_word");
                    jsonBuilder.field("search_analyzer", "ik_max_word");
                }
                jsonBuilder.endObject();
                // description
                jsonBuilder.startObject("description");
                {
                    jsonBuilder.field("type", "text");
                    jsonBuilder.field("analyzer", "ik_max_word");
                    jsonBuilder.field("search_analyzer", "ik_max_word");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("contentMd");
                {
                    jsonBuilder.field("type", "text");
                    jsonBuilder.field("analyzer", "ik_max_word");
                    jsonBuilder.field("search_analyzer", "ik_max_word");
                }
                jsonBuilder.endObject();
                // type
                jsonBuilder.startObject("type");
                {
                    jsonBuilder.field("type", "object");
                    jsonBuilder.startObject("properties");
                    {
                        jsonBuilder.startObject("name");
                        {
                            jsonBuilder.field("type", "text");
                            jsonBuilder.field("analyzer", "ik_max_word");
                            jsonBuilder.field("search_analyzer", "ik_max_word");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("id");
                        {
                            jsonBuilder.field("type", "long");
                        }
                        jsonBuilder.endObject();
                    }
                    jsonBuilder.endObject();
                }
                jsonBuilder.endObject();
                // tags
                jsonBuilder.startObject("tags");
                {
                    jsonBuilder.field("type", "object");
                    jsonBuilder.startObject("properties");
                        jsonBuilder.startObject("name");
                        {
                            jsonBuilder.field("type", "text");
                            jsonBuilder.field("analyzer", "ik_max_word");
                            jsonBuilder.field("search_analyzer", "ik_max_word");
                        }
                        jsonBuilder.endObject();
                        jsonBuilder.startObject("id");
                        {
                            jsonBuilder.field("type", "long");
                        }
                        jsonBuilder.endObject();
                    jsonBuilder.endObject();
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("user");
                {
                    jsonBuilder.field("type", "object");
                    jsonBuilder.startObject("properties");
                    {
                        jsonBuilder.startObject("id");
                        {
                            jsonBuilder.field("type", "long");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("nickname");
                        {
                            jsonBuilder.field("type", "text");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("username");
                        {
                            jsonBuilder.field("type", "text");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("password");
                        {
                            jsonBuilder.field("type", "text");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("email");
                        {
                            jsonBuilder.field("type", "text");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("avatar");
                        {
                            jsonBuilder.field("type", "text");
                        }
                        jsonBuilder.endObject();

                        jsonBuilder.startObject("type");
                        {
                            jsonBuilder.field("type", "long");
                        }
                        jsonBuilder.endObject();
                    }
                    jsonBuilder.endObject();
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("firstPicture");
                {
                    jsonBuilder.field("type","text");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("views");
                {
                    jsonBuilder.field("type","integer");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("createTime");
                {
                    jsonBuilder.field("type","long");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("updateTime");
                {
                    jsonBuilder.field("type","long");
                }
                jsonBuilder.endObject();
            }
            jsonBuilder.endObject();
        }
        jsonBuilder.endObject();
        String blogMapping = Strings.toString(jsonBuilder);
        System.out.println(blogMapping);

        return blogMapping;
    }

}
