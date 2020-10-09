package com.jay.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Component
@TableName("b_blog_content")
public class BlogContent {
    /*id*/
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long blogId;

    private String title;

    private String contentMd;

    private String contentHtml;

}
