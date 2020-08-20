package com.jay.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

/**
 * @program: blog
 * @description: 分类
 * @author: Jay
 * @create: 2020-04-01 17:02
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Component
@TableName("b_type")
public class Type {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;
}
