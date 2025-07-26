# LIGHT CHASER SERVER

LIGHT CASER 数据可视化编辑器后端基础开源版

- Java 17
- SpringBoot 3.2.5
- Mybatis Plus 3.5.4.1

## 快速开始

1. clone项目
```bash
git clone https://github.com/xiaopujun/light-chaser-server.git
```
2. 数据库初始化

执行数据库初始化脚本
[V2024.5.8.1__baseline.sql](src/main/resources/migration/V2024.5.8.1__baseline.sql)

3. 修改application.yml数据库配置

```yaml 
spring:
    datasource:
      url: jdbc:mysql://localhost:3306/light_chaser?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true
      username: root
      password: 123456

```

4. 运行项目

```bash
mvn spring-boot:run
```


## 其他设置

application.yml中设置资源文件上传的磁盘路径

```yaml
light-chaser:
  project-resource-path: /D:/project/light-chaser-server # 项目资源路径
  source-image-path: /static/images/ # 项目图片组件上传路径
  cover-path: /static/covers/ # 项目封面路径
  migration: # 是否自动跑sql脚本
    enable: true

```
