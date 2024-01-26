create table source_image
(
    id          bigint auto_increment comment '主键'
        primary key,
    url         varchar(255)                       not null comment '图片地址',
    name        varchar(255)                       null comment '图片名称',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    deleted     int      default 0                 not null comment '是否删除 0:未删除 1:已删除',
    project_id  bigint                             not null comment '项目id',
    hash        varchar(255)                       null comment '图片hash',
    cover       varchar(255)                       null comment '封面'
);

