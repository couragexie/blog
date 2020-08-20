DROP TABLE IF EXISTS b_blog;

create table `b_blog`(
    `id` bigint(32) not null auto_increment,
    `title` varchar(256) not null,
    `content` longtext  character set utf8mb4 collate utf8mb4_unicode_ci not null comment'文章内容',
    `first_picture` varchar(256) default null comment'文章首图',
    `description` varchar(256) default null comment'文章描述',
    `views` int default 0,
    `user_id` bigint(32) not null comment'文章所属用户id',
    `type_id` bigint(32) default  null comment'文章所属类型',
    `flag` varchar(256) default null comment'标记文章是原创类型还是转载',
    `appreciation` bit(1)  comment'是否开启赞赏功能',
    `copyright` bit(1)  comment'开启版权',
    `commentabled` bit(1)   comment'开启评论功能',
    `recommend` bit(1)  comment'是否推荐',
    `published` bit(1) comment'是否发布',
    `create_time` timestamp not null  default current_timestamp comment'创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment'更新时间',
    primary key(`id`),
    key `index_user_id` (`user_id`),
    key `index_type_id` (`type_id`)
) engine=InnoDB default charset=utf8 auto_increment=1;

drop table if exists `b_comment`;

create table `b_comment`(
    `id` bigint(32) not null auto_increment,
    `nickname`  varchar(128) not null comment'昵称',
    `avatar` varchar(256) comment'头像',
    `email` varchar(32) comment'邮箱',
    `content` varchar(256) comment'内容',
    `blog_id` bigint(32)  not null comment'所属blog的id',
    `parent_comment_id` bigint(32) default null comment'父类评论',
    `admin_comment` bit(1) not null  comment'是否是管理员的评论',
    `create_time` timestamp not null  default current_timestamp comment'创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment'更新时间',
    primary key(`id`),
    key `index_blog_id` (`blog_id`),
    key `index_parent_comment_id` (parent_comment_id)
)engine=InnoDB default charset=utf8 auto_increment=1;


drop table if exists `b_tag`;

create table `b_tag`(
    `id` bigint(32) not null auto_increment,
    `tag_name` varchar (128) not null comment'标签名称',
    `create_time` timestamp not null  default current_timestamp comment'创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment'更新时间',
    primary key(`id`)
)engine=InnoDB default charset=utf8 auto_increment=1;

drop table if exists `b_type`;

create table `b_type`(
    `id` bigint(32) not null auto_increment,
    `type_name` varchar(128) not null comment'标签名称',
    `create_time` timestamp not null  default current_timestamp comment'创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment'更新时间',
    primary key(`id`)
)engine=InnoDB default charset=utf8 auto_increment=1;



drop table if exists `b_blog_tag`;

create table `b_blog_tag`(
    `blog_id` bigint(32) not null,
    `tag_id` bigint(32) not null,
     key `index_blog_id` (`blog_id`),
     key `index_tag_id` (`tag_id`)

)engine=InnoDB default charset=utf8;


drop table if exists `b_user`;

create table `b_user`(
    `id` bigint(32) not null auto_increment,
    `username` varchar (128) not null comment'用户名',
    `password` varchar (128) not null comment'密码',
    `nickname` varchar (128)  comment'昵称',
    `avatar` varchar(128) comment'头像',
    `email` varchar(64)  comment'邮箱',
    `type` int(8) comment'用户类型',
    `create_time` timestamp not null  default current_timestamp comment'创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment'更新时间',
    primary key(`id`)
)engine=InnoDB default charset=utf8 auto_increment=1;

# 添加外键约束
# b_blog
alter table `b_blog` add
    constraint `b_blog_foreign_key_type_id` foreign key(`type_id`) references `b_type`(`id`);
alter table `b_blog`  add
    constraint `b_blog_foreign_key_user_id` foreign key(`user_id`) references `b_user`(`id`);

# b_comment
alter table `b_comment` add
    constraint `b_comment_foreign_key_blog_id` foreign key(`blog_id`) references `b_blog`(`id`) on delete cascade on update cascade;
alter table `b_comment` add
    constraint `b_comment_foreign_key_parent_id` foreign key(`parent_comment_id`) references `b_comment`(`id`) on delete cascade;

# b_blog_tag

alter table `b_blog_tag` add
    constraint `b_blog_tag_foreign_key_blog_id` foreign key(`blog_id`) references `b_blog`(`id`) on delete cascade on update cascade;
alter table `b_blog_tag` add
    constraint `b_blog_tag_foreign_key_tag_id` foreign key(`tag_id`) references `b_tag`(`id`);
