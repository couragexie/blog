package com.jay.blog.service.Imp;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.BlogVOConverter;
import com.jay.blog.search.es.EsHandler;
import com.jay.blog.search.model.BlogDocument;
import com.jay.blog.service.SearchService;
import com.jay.blog.utils.PageUtils;
import com.jay.blog.vo.BlogVO;
import lombok.AllArgsConstructor;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xiejie
 * @Date: 2020/11/16 15:39
 */
@Component
@AllArgsConstructor
public class SearchServiceImp implements SearchService {
    private final static String INDEX = "blog";
    private final static String TYPE = "_doc";

    private EsHandler esHandler;

    private BlogServiceImp blogService;

    @Override
    public Page<BlogVO> search(Integer pageNO, Integer pageSize, String keyword) throws IOException {
        List<String> searchFields = new ArrayList<>();
        searchFields.add("contentMd");
        searchFields.add("title");
        searchFields.add("description");
        Page<BlogDocument> page = new Page<>(pageNO, pageSize);
        String[] fields = searchFields.toArray(new String[searchFields.size()]);
        // es 全文检索
        esHandler.fullSearch("blog", fields, keyword, page, BlogDocument.class);

        List<BlogVO> searchBlogVO = page.getRecords().stream()
                .map(doc -> BlogVOConverter.blogDocumentToBlogVO(doc))
                .collect(Collectors.toList());
        Page<BlogVO> resultPage = PageUtils.copyPage((Page) page);
        resultPage.setRecords(eliminateContent(searchBlogVO));
        return resultPage;
    }

    // 去除内容
    private List<BlogVO> eliminateContent(List<BlogVO> blogVOS) {
        return blogVOS.stream()
                .peek(blogVO -> blogVO.setContentMd(null))
                .collect(Collectors.toList());
    }

    @Override
    public void createDoc(Long blogId) throws IOException {
        BlogVO blogVO = blogService.getBlogVOById(blogId);
        BlogDocument blogDocument = BlogVOConverter.blogVOToBlogDocument(blogVO);
        String documentJson = JSONObject.toJSONString(blogDocument);
        esHandler.createDoc(INDEX, blogId.toString(), documentJson);
    }

    @Override
    public void deleteDoc(Long blogId) throws IOException {
        esHandler.deleteDoc(INDEX, blogId.toString());
    }

    @Override
    public void bulkDoc() throws IOException {
        List<Long> blogsId = blogService.listBlogId();
        for (Long blogId : blogsId){
            BlogVO blogVO = blogService.getBlogVOById(blogId);
            BlogDocument blogDocument = BlogVOConverter.blogVOToBlogDocument(blogVO);
            String documentJson = JSONObject.toJSONString(blogDocument);
            esHandler.createDoc("blog", blogId.toString(), documentJson);
        }
    }

    @Override
    public boolean createIndex(String index) throws IOException {
        boolean flag1 = esHandler.createIndexMapping(index);

        String blogMapping = blogMapping();

        boolean flag2 = esHandler.putIndexMapping(index, blogMapping);

        return flag1 && flag2;
    }

    @Override
    public boolean deleteIndex(String index) throws IOException {
        return esHandler.deleteIndex(index);
    }

    private static String blogMapping() throws IOException {
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
                    jsonBuilder.field("type", "text");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("views");
                {
                    jsonBuilder.field("type", "integer");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("createTime");
                {
                    jsonBuilder.field("type", "long");
                }
                jsonBuilder.endObject();

                jsonBuilder.startObject("updateTime");
                {
                    jsonBuilder.field("type", "long");
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
