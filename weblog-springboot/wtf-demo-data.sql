SET NAMES utf8mb4;
USE weblog;

SET @owner_id = (SELECT id FROM t_user WHERE username = 'wtf' LIMIT 1);

DELETE atr
FROM t_article_tag_rel atr
JOIN t_article a ON a.id = atr.article_id
WHERE a.owner_id = @owner_id;

DELETE acr
FROM t_article_category_rel acr
JOIN t_article a ON a.id = acr.article_id
WHERE a.owner_id = @owner_id;

DELETE ac
FROM t_article_content ac
JOIN t_article a ON a.id = ac.article_id
WHERE a.owner_id = @owner_id;

DELETE FROM t_article WHERE owner_id = @owner_id;
DELETE FROM t_tag WHERE owner_id = @owner_id;
DELETE FROM t_category WHERE owner_id = @owner_id;
DELETE FROM t_statistics_article_pv WHERE owner_id = @owner_id;

INSERT INTO t_blog_settings (owner_id, logo, name, author, introduction, avatar, github_homepage, csdn_homepage, gitee_homepage, zhihu_homepage)
SELECT
  @owner_id,
  'http://localhost:8086/images/logo.svg',
  '江岸手记',
  '江岸',
  '主要记录博客改造、联调与排错过程。',
  'http://localhost:8086/images/avatar.svg',
  '',
  '',
  '',
  ''
FROM dual
WHERE @owner_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM t_blog_settings WHERE owner_id = @owner_id);

UPDATE t_blog_settings
SET
  logo = 'http://localhost:8086/images/logo.svg',
  name = '江岸手记',
  author = '江岸',
  introduction = '主要记录博客改造、联调与排错过程。',
  avatar = 'http://localhost:8086/images/avatar.svg',
  github_homepage = '',
  csdn_homepage = '',
  gitee_homepage = '',
  zhihu_homepage = ''
WHERE owner_id = @owner_id;

INSERT INTO t_category (owner_id, name, create_time, update_time, is_deleted, articles_total)
SELECT @owner_id, '部署整理', '2026-04-10 09:00:00', '2026-04-10 09:00:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '并发实践', '2026-04-10 09:10:00', '2026-04-10 09:10:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '接口整理', '2026-04-10 09:20:00', '2026-04-10 09:20:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '搜索接入', '2026-04-10 09:30:00', '2026-04-10 09:30:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '页面复盘', '2026-04-10 09:40:00', '2026-04-10 09:40:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL;

INSERT INTO t_tag (owner_id, name, create_time, update_time, is_deleted, articles_total)
SELECT @owner_id, '部署记录', '2026-04-10 10:00:00', '2026-04-10 10:00:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '线程调度', '2026-04-10 10:05:00', '2026-04-10 10:05:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, 'Java整理', '2026-04-10 10:10:00', '2026-04-10 10:10:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '接口约定', '2026-04-10 10:15:00', '2026-04-10 10:15:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '索引维护', '2026-04-10 10:20:00', '2026-04-10 10:20:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '权限边界', '2026-04-10 10:25:00', '2026-04-10 10:25:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '数据隔离', '2026-04-10 10:30:00', '2026-04-10 10:30:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '错误定位', '2026-04-10 10:35:00', '2026-04-10 10:35:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '联调记录', '2026-04-10 10:40:00', '2026-04-10 10:40:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '内容编排', '2026-04-10 10:45:00', '2026-04-10 10:45:00', 0, 0 FROM dual WHERE @owner_id IS NOT NULL;

INSERT INTO t_article (owner_id, title, cover, summary, create_time, update_time, is_deleted, read_num)
SELECT @owner_id, '把一套空后台补成可用博客', 'http://localhost:8086/images/cover-project.svg', '把博客设置、分类、标签和统计一起补齐，让前后台都能直接查看。', '2026-04-10 14:00:00', '2026-04-10 14:00:00', 0, 36 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '一次线程池堆积问题排查记录', 'http://localhost:8086/images/cover-threadpool.svg', '从线程数、队列长度和拒绝策略入手，判断任务为什么一直堆积。', '2026-04-11 10:00:00', '2026-04-11 10:00:00', 0, 52 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '前台接口和后台接口该怎样拆分', 'http://localhost:8086/images/cover-modules.svg', '梳理权限边界、路由职责和 owner 约束，避免一处修改影响两边。', '2026-04-11 16:30:00', '2026-04-11 16:30:00', 0, 41 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '接入 Lucene 搜索前我会先查哪些链路', 'http://localhost:8086/images/cover-lucene.svg', '把索引初始化、发布同步和前台查询的关键点都检查一遍。', '2026-04-12 09:20:00', '2026-04-12 09:20:00', 0, 33 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '多管理员模式下哪些表需要 owner_id', 'http://localhost:8086/images/cover-mysql.svg', '从文章到标签再到统计，逐张表确认数据隔离到底落在哪。', '2026-04-12 20:10:00', '2026-04-12 20:10:00', 0, 47 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '首页没有内容时的排查顺序', 'http://localhost:8086/images/cover-empty-home.svg', '按文章、关系表、接口入参和前端渲染顺序快速定位空白原因。', '2026-04-13 11:15:00', '2026-04-13 11:15:00', 0, 28 FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT @owner_id, '从登录到前台展示的一次完整走查', 'http://localhost:8086/images/cover-project.svg', '串起登录、后台、前台切换和页面展示，方便后续继续扩展。', '2026-04-14 15:00:00', '2026-04-14 15:00:00', 0, 39 FROM dual WHERE @owner_id IS NOT NULL;

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 把一套空后台补成可用博客

如果一个新后台刚建好就直接打开，前台通常看不出结构。

我习惯先补三类内容：

1. 博客名称、头像和简介。
2. 分类、标签和几篇文章。
3. 归档、搜索、统计需要的基础数据。

准备到这一步之后，再看页面时就不会分不清是功能问题，还是只是数据还没齐。'
FROM t_article
WHERE owner_id = @owner_id AND title = '把一套空后台补成可用博客';

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 一次线程池堆积问题排查记录

最近在排查一个批量任务时，我主要看了这些指标：

- 核心线程数和最大线程数
- 队列是否持续增长
- 拒绝策略有没有被触发
- 任务本身是不是被 IO 阻塞

任务堆积时不要先加线程，先找到真正的瓶颈，调参数才更稳。'
FROM t_article
WHERE owner_id = @owner_id AND title = '一次线程池堆积问题排查记录';

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 前台接口和后台接口该怎样拆分

同一套数据库同时服务前台和后台时，最容易混乱的是职责边界。

我这次的处理方式是：

- 后台接口统一走登录态和 owner 约束。
- 前台接口统一通过 blogUsername 解析 owner。
- 相同的数据表，只在 Service 层区分后台与前台用法。

这样改完后，接口职责会清楚很多。'
FROM t_article
WHERE owner_id = @owner_id AND title = '前台接口和后台接口该怎样拆分';

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 接入 Lucene 搜索前我会先查哪些链路

搜索功能真正能用，不只是接口返回结果这么简单。

我会先确认：

1. 新文章是否进入索引。
2. owner 信息有没有一起写入。
3. 搜索是否只返回当前博客。
4. 高亮和摘要显示是否正常。

少了其中任何一环，搜索看起来都会像是偶尔可用。'
FROM t_article
WHERE owner_id = @owner_id AND title = '接入 Lucene 搜索前我会先查哪些链路';

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 多管理员模式下哪些表需要 owner_id

这次改造的核心不是单个接口，而是 owner_id 这条主线。

需要按管理员隔离的数据包括：

- 博客设置
- 文章
- 分类
- 标签
- PV 统计

前台和后台都必须先定位 owner，再继续查文章详情、分类列表和标签关系。'
FROM t_article
WHERE owner_id = @owner_id AND title = '多管理员模式下哪些表需要 owner_id';

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 首页没有内容时的排查顺序

如果首页空白，我通常按这个顺序排查：

1. 文章表里有没有这个 owner 的文章。
2. 文章和分类、标签的关系表有没有数据。
3. 前台接口有没有传 blogUsername。
4. 页面上拿到的数据是不是空数组。

顺着这条路径查，问题一般都会缩小到很少的几个点。'
FROM t_article
WHERE owner_id = @owner_id AND title = '首页没有内容时的排查顺序';

INSERT INTO t_article_content (article_id, content)
SELECT id, '# 从登录到前台展示的一次完整走查

现在这条链路基本是：

1. 登录成功先进入后台工作台。
2. 从后台点击前往前台，直接进入自己的博客。
3. 在前台 Header 中通过下拉切换其他博客。
4. 页面根据 blogUsername 拉取对应 owner 的数据。

把这条链路走顺后，后续再做功能扩展会轻松很多。'
FROM t_article
WHERE owner_id = @owner_id AND title = '从登录到前台展示的一次完整走查';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '把一套空后台补成可用博客'
  AND c.name = '部署整理';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '一次线程池堆积问题排查记录'
  AND c.name = '并发实践';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '前台接口和后台接口该怎样拆分'
  AND c.name = '接口整理';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '接入 Lucene 搜索前我会先查哪些链路'
  AND c.name = '搜索接入';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '多管理员模式下哪些表需要 owner_id'
  AND c.name = '接口整理';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '首页没有内容时的排查顺序'
  AND c.name = '页面复盘';

INSERT INTO t_article_category_rel (article_id, category_id)
SELECT a.id, c.id
FROM t_article a
JOIN t_category c ON c.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '从登录到前台展示的一次完整走查'
  AND c.name = '页面复盘';

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '把一套空后台补成可用博客'
  AND t.name IN ('部署记录', '联调记录', '内容编排');

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '一次线程池堆积问题排查记录'
  AND t.name IN ('Java整理', '线程调度', '错误定位');

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '前台接口和后台接口该怎样拆分'
  AND t.name IN ('接口约定', '联调记录');

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '接入 Lucene 搜索前我会先查哪些链路'
  AND t.name IN ('索引维护', '联调记录');

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '多管理员模式下哪些表需要 owner_id'
  AND t.name IN ('权限边界', '数据隔离', '内容编排');

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '首页没有内容时的排查顺序'
  AND t.name IN ('错误定位', '内容编排');

INSERT INTO t_article_tag_rel (article_id, tag_id)
SELECT a.id, t.id
FROM t_article a
JOIN t_tag t ON t.owner_id = @owner_id
WHERE a.owner_id = @owner_id
  AND a.title = '从登录到前台展示的一次完整走查'
  AND t.name IN ('权限边界', '联调记录', '内容编排');

UPDATE t_category c
SET c.articles_total = (
  SELECT COUNT(*)
  FROM t_article_category_rel acr
  WHERE acr.category_id = c.id
)
WHERE c.owner_id = @owner_id;

UPDATE t_tag t
SET t.articles_total = (
  SELECT COUNT(*)
  FROM t_article_tag_rel atr
  WHERE atr.tag_id = t.id
)
WHERE t.owner_id = @owner_id;

INSERT INTO t_statistics_article_pv (pv_date, owner_id, pv_count, create_time, update_time)
SELECT '2026-04-10', @owner_id, 18, '2026-04-10 23:59:00', '2026-04-10 23:59:00' FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT '2026-04-11', @owner_id, 25, '2026-04-11 23:59:00', '2026-04-11 23:59:00' FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT '2026-04-12', @owner_id, 31, '2026-04-12 23:59:00', '2026-04-12 23:59:00' FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT '2026-04-13', @owner_id, 42, '2026-04-13 23:59:00', '2026-04-13 23:59:00' FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT '2026-04-14', @owner_id, 56, '2026-04-14 23:59:00', '2026-04-14 23:59:00' FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT '2026-04-15', @owner_id, 48, '2026-04-15 23:59:00', '2026-04-15 23:59:00' FROM dual WHERE @owner_id IS NOT NULL
UNION ALL
SELECT '2026-04-16', @owner_id, 63, '2026-04-16 23:59:00', '2026-04-16 23:59:00' FROM dual WHERE @owner_id IS NOT NULL;
