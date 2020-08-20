package com.jay.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-04 19:03
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Component
@TableName("b_blog_tag")
public class BlogAndTag {

    private Long blogId;

    private Long  tagId;
}
