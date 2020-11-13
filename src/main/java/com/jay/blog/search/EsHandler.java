package com.jay.blog.search;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jay.blog.utils.CollectionsUtil;
import com.jay.blog.utils.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class EsHandler {
    private final static Logger logger = LoggerFactory.getLogger("ES-SYN");
    @Qualifier("restHighLevelClient")
    @Autowired
    RestHighLevelClient client;

    private final static String TYPE = "_doc";

    /**
     * 批量操作
     * @author xiejie
     * @since 2020/11/13
     * @param
     */
    public <T> void bulkDoc(List<DocWriteRequest<T>> requests) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (DocWriteRequest<T> request : requests){
            bulkRequest.add(request);
        }
        BulkResponse response = client.bulk(bulkRequest);
    }
    /**
     * 批量插入文档
     * @author xiejie
     * @since 2020/11/13
     * @param
     */
    public void bulkCreateDoc(String index, Map<String, String> docs){
        Set<String> keys = docs.keySet();
        for (String key : keys){
            String docId = key;
            String document = docs.get(key);
            try {
                createDoc(index, docId, document);
            } catch (IOException e) {
                logger.info(">>>> 同步 Es 数据失败: index={}, docId={}", index, docId);
            }
        }
    }

    /**
     * 创建 document，如果 es 存在相同 id 的 document 的话，会覆盖掉前一份 document
     * @author xiejie
     * @since 2020/11/13
     * @param
     */
    public void createDoc(String index, String docId,String document) throws IOException {
        logger.info(">>>>> 开始同步 Es 数据: index={}, docId={}:", index, docId);
        IndexRequest indexRequest = new IndexRequest(index,TYPE , docId);
        indexRequest.source(document, XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        if (indexResponse.getShardInfo().getFailed() > 0){
            logger.info(">>>> 同步 Es 数据失败: index={}, docId={}", index, docId);
        }
        logger.info(">>>>> 结束同步 Es 数据");
    }


    /**
     * 创建 index mapping
     * @author xiejie
     * @since 2020/11/13
     * @param
     */
    public boolean createIndexMapping(String index, String mapping) throws IOException {
        logger.info(">>>>> Es 创建索引 mapping, index={}, mapping={}", index, mapping);
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.source(mapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }
    /**
     * 更新 index mapping
     * @author xiejie
     * @since 2020/11/13
     * @param
     */

    public boolean putIndexMapping(String index, String mapping) throws IOException {
        logger.info(">>>>> Es 更新索引 mapping, index={}, mapping={}", index, mapping);
        PutMappingRequest putMappingRequest = new PutMappingRequest(index);
        putMappingRequest.source(mapping, XContentType.JSON);
        AcknowledgedResponse response =  client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 删除索引
     * @author xiejie
     * @since 2020/11/13
     * @param
     */
    public boolean deleteIndex(String index) throws IOException {
        logger.info(">>>>> Es 删除索引 index={}", index);
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = client.indices().delete(request,RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }
    /**
     * 删除 document
     * @author xiejie
     * @since 2020/11/13
     * @param
     */
    public void deleteDoc(String index, String docId) throws IOException {
        logger.info(">>>>> Es 删除文档: index={}, docId={}", index,docId);
        DeleteRequest request = new DeleteRequest(index, TYPE, docId);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 全文搜索
     * @author xiejie
     * @since 2020/11/13
     * @param index 索引
     * @param querys key 为指定搜索的字段，value 为查询的值
     */
    public <T> IPage<T> fullSearch(String index, Map<String, String> querys, IPage<T> page, Class<T> returnType ) throws IOException {
        logger.info(">>>>> Es 全文搜索: index={}, query={}", index,querys);
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (!StringUtils.checkIsEmpty(null)){
            searchRequest.indices(index);
        }
        // 构建查询的字段和值
        Set<String> keys = querys.keySet();
        for (String field : keys){
            sourceBuilder.query(QueryBuilders.matchQuery(field, querys.get(field)));
        }
        // TODO 构建多个字段搜索
        sourceBuilder.from((int)page.getCurrent());
        sourceBuilder.size((int)page.getSize());
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        page.setTotal(searchHits.getTotalHits());
        // 获取查询的 document
        List<T> hitObjects = new ArrayList<>();
        for (SearchHit searchHit : searchHits){
            String docJson = searchHit.getSourceAsString();
            T object = JSONObject.parseObject(docJson, returnType);
            hitObjects.add(object);
        }

        page.setRecords(hitObjects);
        return page;
    }

}
