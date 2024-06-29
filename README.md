# LIGHT CHASER SERVER

LIGHT CASER 数据可视化编辑器后端基础开源版

- Java 17
- SpringBoot 3.2.5
- Mybatis Plus 3.5.4.1

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
