package com.jay.blog.search.mq;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 17:33
 */

public class MqEsIndexMessage implements Serializable {
    // 消息类型
    public final static String CREATE_OR_UPDATE = "create_or_update";
    public final static String REMOVE = "remove";

    private long postId;
    private String type;

    public MqEsIndexMessage() {
    }

    public MqEsIndexMessage(long postId, String type) {
        this.postId = postId;
        this.type = type;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MqEsIndexMessage{" +
                "postId=" + postId +
                ", type='" + type + '\'' +
                '}';
    }
}

