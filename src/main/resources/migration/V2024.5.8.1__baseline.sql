-- SQLite版本的baseline脚本
-- 不需要CREATE DATABASE语句，SQLite会自动创建数据库文件

CREATE TABLE datasource (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NULL, -- 数据源链接名称
    type INTEGER NOT NULL, -- 数据源类型
    username VARCHAR(20) NOT NULL, -- 用户名
    password VARCHAR(255) NOT NULL, -- 数据源链接密码
    url VARCHAR(255) NOT NULL, -- 数据源链接地址
    des VARCHAR(255) NULL, -- 数据源链接描述
    deleted INTEGER NOT NULL DEFAULT 0, -- 是否删除
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP -- 创建时间
);

CREATE TABLE file (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- 主键
    url VARCHAR(255) NOT NULL, -- 图片路径片段
    name VARCHAR(255) NULL, -- 图片名称
    type INTEGER NOT NULL, -- 文件类型，例如：图片、视频、模型等
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0, -- 是否删除 0:未删除 1:已删除
    project_id INTEGER NOT NULL, -- 项目id
    hash VARCHAR(255) NULL -- 图片hash
);

CREATE TABLE project (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- 主键
    name VARCHAR(255) NOT NULL, -- 项目名称
    des VARCHAR(255) NULL, -- 项目描述
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0, -- 是否删除 0:未删除 1:已删除
    data_json TEXT NULL, -- 数据json (使用TEXT替代LONGTEXT)
    cover VARCHAR(255) NULL -- 封面
);