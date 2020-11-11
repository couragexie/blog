package com.jay.blog.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

    @Value("${es.port}")
    public int port;

    @Value("${es.hostname}")
    public String hostname;

    public final String SCHEME="http";

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(hostname, port, SCHEME));
        return  new RestHighLevelClient(restClientBuilder);
    }



}
