create table project
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(255)                       not null comment '项目名称',
    des         varchar(255) null comment '项目描述',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    status      int      default 0                 not null comment '状态 ',
    save_type   int      default 0                 not null comment '存储类型 0:本地 1:服务器',
    deleted     int      default 0                 not null comment '是否删除 0:未删除 1:已删除',
    data_json   text null comment '数据json',
    cover       varchar(255) null comment '封面'
);

