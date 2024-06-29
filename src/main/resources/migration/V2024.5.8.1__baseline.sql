create table datasource
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(255)                        null comment '数据源链接名称',
    type        int                                 not null comment '数据源类型',
    username    varchar(20)                         not null comment '用户名',
    password    varchar(255)                        not null comment '数据源链接密码',
    url         varchar(255)                        not null comment '数据源链接地址',
    des         varchar(255)                        null comment '数据源链接描述',
    deleted     int                                 not null comment '是否删除',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '数据源管理';

create table file
(
    id          bigint auto_increment comment '主键'
        primary key,
    url         varchar(255)                       not null comment '图片路径片段',
    name        varchar(255)                       null comment '图片名称',
    type        int                                not null comment '文件类型，例如：图片、视频、模型等',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    deleted     int      default 0                 not null comment '是否删除 0:未删除 1:已删除',
    project_id  bigint                             not null comment '项目id',
    hash        varchar(255)                       null comment '图片hash'
);

create table project
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(255)                       not null comment '项目名称',
    des         varchar(255)                       null comment '项目描述',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    deleted     int      default 0                 not null comment '是否删除 0:未删除 1:已删除',
    data_json   longtext                           null comment '数据json',
    cover       varchar(255)                       null comment '封面'
);

