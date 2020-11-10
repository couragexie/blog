package com.jay.blog.search.mq;

import java.io.Serializable;

/**
 * @Author: xiejie
 * @Date: 2020/11/10 17:33
 */
public class MqEsIndexMessage implements Serializable {
    private long postId;
    private String type;

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

