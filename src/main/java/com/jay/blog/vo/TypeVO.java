package com.jay.blog.vo;

import com.jay.blog.entity.Type;
import lombok.Data;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @description:
 * @author: Jay
 * @create: 2020-04-03 20:20
 **/
@Data
@Component
public class TypeVO implements Serializable {
    private Long id;
    private String name;

    private Integer blogNum;
}
