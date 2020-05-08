package com.jay.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * @program: blog
 * @description: 标签类
 * @author: Jay
 * @create: 2020-04-01 17:01
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Component
@TableName("b_tag")
public class Tag {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "名称不能为空")
    private String name;
}
