SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM t_article_tag_rel;
DELETE FROM t_article_category_rel;
DELETE FROM t_article_content;
DELETE FROM t_article;
DELETE FROM t_tag;
DELETE FROM t_category;
DELETE FROM t_statistics_article_pv;
DELETE FROM t_user_role;
DELETE FROM t_blog_settings;
DELETE FROM t_user;

INSERT INTO t_user
    (id, username, password, create_time, update_time, is_deleted)
VALUES
    (1, 'admin', '$2a$10$bQCT7jlu6egJQCjRIBph9uX0TSTSXDizOY0qYQ9OjAXkhYkhjc.56', NOW(), NOW(), 0);

INSERT INTO t_user_role
    (id, username, role, create_time)
VALUES
    (1, 'admin', 'ROLE_ADMIN', NOW());

INSERT INTO t_blog_settings
    (id, owner_id, logo, name, author, introduction, avatar, github_homepage, csdn_homepage, gitee_homepage, zhihu_homepage)
VALUES
    (1,
     1,
     'https://placehold.co/120x40/png',
     '手写线程池的博客',
     'admin',
     '一个用于课程实验和学习记录的前后端分离博客系统。',
     'https://placehold.co/120x120/png',
     'https://github.com/',
     'https://blog.csdn.net/',
     'https://gitee.com/',
     'https://www.zhihu.com/');

INSERT INTO t_category
    (id, owner_id, name, create_time, update_time, is_deleted, articles_total)
VALUES
    (1, 1, 'Java 基础', '2026-03-20 10:00:00', '2026-04-09 10:00:00', 0, 2),
    (2, 1, 'Spring Boot', '2026-03-22 10:00:00', '2026-04-09 10:00:00', 0, 2),
    (3, 1, '数据库', '2026-03-24 10:00:00', '2026-04-09 10:00:00', 0, 1),
    (4, 1, '项目实战', '2026-03-26 10:00:00', '2026-04-09 10:00:00', 0, 1);

INSERT INTO t_tag
    (id, owner_id, name, create_time, update_time, is_deleted, articles_total)
VALUES
    (1, 1, 'Java', '2026-03-20 10:00:00', '2026-04-09 10:00:00', 0, 2),
    (2, 1, '线程池', '2026-03-20 10:10:00', '2026-04-09 10:00:00', 0, 1),
    (3, 1, '并发编程', '2026-03-20 10:20:00', '2026-04-09 10:00:00', 0, 1),
    (4, 1, 'SpringBoot', '2026-03-22 10:00:00', '2026-04-09 10:00:00', 0, 2),
    (5, 1, 'MySQL', '2026-03-24 10:00:00', '2026-04-09 10:00:00', 0, 1),
    (6, 1, 'Lucene', '2026-03-28 10:00:00', '2026-04-09 10:00:00', 0, 1),
    (7, 1, '实战', '2026-03-30 10:00:00', '2026-04-09 10:00:00', 0, 2),
    (8, 1, '博客系统', '2026-03-30 10:10:00', '2026-04-09 10:00:00', 0, 2);

INSERT INTO t_article
    (id, owner_id, title, cover, summary, create_time, update_time, is_deleted, read_num)
VALUES
    (1, 1, 'Java 线程池入门：从 Executors 到 ThreadPoolExecutor', 'https://placehold.co/800x420/png', '通过一个循序渐进的例子理解线程池的核心参数、任务队列和拒绝策略。', '2026-03-25 09:00:00', '2026-04-05 11:00:00', 0, 128),
    (2, 1, 'Spring Boot 多模块项目如何组织', 'https://placehold.co/800x420/png', '结合当前博客项目，介绍父工程、子模块、公共模块与启动模块的职责划分。', '2026-03-27 14:00:00', '2026-04-06 10:00:00', 0, 96),
    (3, 1, 'MySQL 常见表设计小结', 'https://placehold.co/800x420/png', '整理博客系统中用户、文章、分类、标签和关系表的设计思路。', '2026-03-29 20:00:00', '2026-04-06 21:00:00', 0, 73),
    (4, 1, '给博客系统接入 Lucene 站内搜索', 'https://placehold.co/800x420/png', '从索引结构、分词器到初始化流程，梳理 Lucene 在项目中的实际用法。', '2026-04-01 13:00:00', '2026-04-07 15:00:00', 0, 88),
    (5, 1, '第一次跑通前后端分离项目，我做了什么', 'https://placehold.co/800x420/png', '面向新手的实战记录：配置数据库、启动后端、解决 npm 依赖问题并跑通前端。', '2026-04-03 16:30:00', '2026-04-08 10:30:00', 0, 115),
    (6, 1, '博客首页为什么是空的：从数据初始化说起', 'https://placehold.co/800x420/png', '当前台没有文章、分类和标签时，我们应该如何检查数据库与接口返回。', '2026-04-05 19:00:00', '2026-04-08 20:00:00', 0, 54);

INSERT INTO t_article_content
    (id, article_id, content)
VALUES
    (1, 1, '# Java 线程池入门

线程池的目标是复用线程并降低频繁创建和销毁线程的成本。

## 为什么要用线程池

1. 降低资源消耗
2. 提高响应速度
3. 统一管理线程数量

## 核心参数

- `corePoolSize`
- `maximumPoolSize`
- `keepAliveTime`
- `workQueue`
- `RejectedExecutionHandler`

## 经验建议

在课程实验或中小型项目里，先把任务类型区分清楚，再决定线程池大小。CPU 密集型任务更适合较小线程数，IO 密集型任务则可以适度放大。'),

    (2, 2, '# Spring Boot 多模块项目如何组织

当前博客项目本身就是一个多模块工程：

- `weblog-web`：前台接口和启动入口
- `weblog-admin`：后台管理
- `weblog-common`：公共实体、Mapper 和工具
- `weblog-jwt`：登录认证
- `weblog-search`：Lucene 搜索

## 推荐的理解顺序

先看启动类，再看配置文件，之后顺着 Controller、Service、Mapper 往下读，更容易建立整体结构感。'),

    (3, 3, '# MySQL 常见表设计小结

博客系统里最常见的表分为三类：

## 主实体表

- 用户表 `t_user`
- 文章表 `t_article`
- 分类表 `t_category`
- 标签表 `t_tag`

## 扩展表

- 文章内容表 `t_article_content`
- 博客设置表 `t_blog_settings`

## 关系表

- 文章分类关系 `t_article_category_rel`
- 文章标签关系 `t_article_tag_rel`

关系表的存在可以让结构更清晰，也更方便后续扩展。'),

    (4, 4, '# 给博客系统接入 Lucene 站内搜索

Lucene 的使用流程大致是：

1. 读取数据库文章数据
2. 构建 Document
3. 建立索引
4. 执行查询并返回高亮结果

在这个项目中，应用启动后会通过 `InitLuceneIndexRunner` 初始化文章索引，因此数据库中有文章时，搜索功能会更容易验证。'),

    (5, 5, '# 第一次跑通前后端分离项目，我做了什么

作为新手，最容易卡住的地方通常不是业务代码，而是环境：

- JDK 版本不对
- MySQL 密码错误
- 没导入 SQL
- npm 被代理或环境变量影响

只要把这些基础问题一项项排掉，项目就能顺利跑起来。'),

    (6, 6, '# 博客首页为什么是空的

首页空白通常不是前端崩了，而是接口返回的数据本来就为空。

建议按下面顺序排查：

1. `t_article` 是否有文章
2. `t_category` 是否有分类
3. `t_tag` 是否有标签
4. 文章和分类、标签的关系表是否有记录

只要种子数据准备好，首页、分类页和标签页就会一起有内容。');

INSERT INTO t_article_category_rel
    (id, article_id, category_id)
VALUES
    (1, 1, 1),
    (2, 2, 2),
    (3, 3, 3),
    (4, 4, 2),
    (5, 5, 4),
    (6, 6, 4);

INSERT INTO t_article_tag_rel
    (id, article_id, tag_id)
VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
    (4, 2, 4),
    (5, 2, 7),
    (6, 3, 1),
    (7, 3, 5),
    (8, 4, 4),
    (9, 4, 6),
    (10, 5, 7),
    (11, 5, 8),
    (12, 6, 8);

INSERT INTO t_statistics_article_pv
    (id, owner_id, pv_date, pv_count, create_time, update_time)
VALUES
    (1, 1, '2026-04-03', 56, '2026-04-03 23:59:00', '2026-04-03 23:59:00'),
    (2, 1, '2026-04-04', 73, '2026-04-04 23:59:00', '2026-04-04 23:59:00'),
    (3, 1, '2026-04-05', 64, '2026-04-05 23:59:00', '2026-04-05 23:59:00'),
    (4, 1, '2026-04-06', 81, '2026-04-06 23:59:00', '2026-04-06 23:59:00'),
    (5, 1, '2026-04-07', 95, '2026-04-07 23:59:00', '2026-04-07 23:59:00'),
    (6, 1, '2026-04-08', 110, '2026-04-08 23:59:00', '2026-04-08 23:59:00'),
    (7, 1, '2026-04-09', 67, '2026-04-09 23:59:00', '2026-04-09 23:59:00');

SET FOREIGN_KEY_CHECKS = 1;
