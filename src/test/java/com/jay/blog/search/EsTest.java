package com.jay.blog.search;

import com.jay.blog.converter.BlogVOConverter;
import com.jay.blog.entity.Blog;
import com.jay.blog.search.model.BlogDocument;
import com.jay.blog.service.BlogService;
import com.jay.blog.vo.BlogVO;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

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
        String index = "blog";
        esHandler.createIndexMapping(index, blogMapping);

    }

    @Test
    public void createIndex(){
        Long blogId = 2L;
        BlogVO blogVO = blogService.getOneById(blogId);
        System.out.println(blogVO);
        BlogDocument blogDocument = BlogVOConverter.blogVOToBlogDocument(blogVO);
        System.out.println(blogDocument);
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
                    jsonBuilder.startObject("name");
                    {
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

                jsonBuilder.startObject("user");
                {
                    jsonBuilder.field("type", "object");
                    jsonBuilder.startObject("id");
                    {
                        jsonBuilder.field("type","long");
                    }
                    jsonBuilder.endObject();

                    jsonBuilder.startObject("nickname");
                    {
                        jsonBuilder.field("type","long");
                    }
                    jsonBuilder.endObject();

                    jsonBuilder.startObject("avatar");
                    {
                        jsonBuilder.field("type","string");
                    }
                    jsonBuilder.endObject();
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("firstPicture");
                {
                    jsonBuilder.field("type","string");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("views");
                {
                    jsonBuilder.field("type","integer");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("createTime");
                {
                    jsonBuilder.field("type","date");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("updateTime");
                {
                    jsonBuilder.field("type","date");
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
