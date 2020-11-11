package com.jay.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: blog
 * @description: 管理员
 * @author: Jay
 * @create: 2020-04-01 17:02
 **/

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Component
@TableName("b_user")
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Integer type;

//    private Date createTime;
//    private Date updateTime;

    public User(Long id){
        this.id = id;
    }
}
