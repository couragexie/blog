package com.jay.blog.service.Imp;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jay.blog.converter.BlogVOConverter;
import com.jay.blog.search.es.EsHandler;
import com.jay.blog.search.model.BlogDocument;
import com.jay.blog.service.BlogService;
import com.jay.blog.service.SearchService;
import com.jay.blog.utils.PageUtil;
import com.jay.blog.vo.BlogVO;
import org.elasticsearch.action.delete.DeleteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xiejie
 * @Date: 2020/11/16 15:39
 */
@Component
public class SearchServiceImp implements SearchService {
    private final static String INDEX = "blog";
    private final static String TYPE = "_doc";
    @Autowired
    private EsHandler esHandler;

    @Autowired
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
        Page<BlogVO> resultPage = PageUtil.copyPage((Page) page);
        resultPage.setRecords( eliminateContent(searchBlogVO));
        return resultPage;
    }
    // 去除内容
    private List<BlogVO> eliminateContent(List<BlogVO> blogVOS){
        return blogVOS.stream()
                .peek(blogVO -> blogVO.setContentMd(null))
                .collect(Collectors.toList());
    }

    @Override
    public void createDoc(Long blogId) throws IOException {
        BlogVO blogVO = blogService.getBlogVOById(blogId);
        BlogDocument blogDocument = BlogVOConverter.blogVOToBlogDocument(blogVO);
        String documentJson = JSONObject.toJSONString(blogDocument);
        System.out.println(documentJson);
        esHandler.createDoc(INDEX, blogId.toString() , documentJson);
    }

    @Override
    public void deleteDoc(Long blogId) throws IOException {
        esHandler.deleteDoc(INDEX, blogId.toString());
    }
}
