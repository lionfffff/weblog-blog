SET NAMES utf8mb4;
USE weblog;

SET @owner_id = (SELECT id FROM t_user WHERE username = 'xhr' LIMIT 1);

UPDATE t_blog_settings
SET
  name = '清屿手记',
  author = '清屿',
  introduction = '主要写接口联调、样式调整和日常开发记录。',
  avatar = 'http://localhost:8086/images/avatars/avatar-03.svg'
WHERE owner_id = @owner_id;

UPDATE t_category SET name = '工程配置备忘' WHERE id = 43 AND owner_id = @owner_id;
UPDATE t_category SET name = '接口排查记录' WHERE id = 44 AND owner_id = @owner_id;
UPDATE t_category SET name = '页面整理笔记' WHERE id = 45 AND owner_id = @owner_id;
UPDATE t_category SET name = '搜索接入清单' WHERE id = 46 AND owner_id = @owner_id;
UPDATE t_category SET name = '发布流程复盘' WHERE id = 47 AND owner_id = @owner_id;

UPDATE t_tag SET name = '登录流程' WHERE id = 50 AND owner_id = @owner_id;
UPDATE t_tag SET name = '接口联调' WHERE id = 51 AND owner_id = @owner_id;
UPDATE t_tag SET name = '页面样式' WHERE id = 52 AND owner_id = @owner_id;
UPDATE t_tag SET name = '后台设置' WHERE id = 53 AND owner_id = @owner_id;
UPDATE t_tag SET name = '站内搜索' WHERE id = 54 AND owner_id = @owner_id;
UPDATE t_tag SET name = '发布流程' WHERE id = 55 AND owner_id = @owner_id;
UPDATE t_tag SET name = '分类维护' WHERE id = 56 AND owner_id = @owner_id;
UPDATE t_tag SET name = '归档查看' WHERE id = 57 AND owner_id = @owner_id;
UPDATE t_tag SET name = '错误处理' WHERE id = 58 AND owner_id = @owner_id;
UPDATE t_tag SET name = '内容整理' WHERE id = 59 AND owner_id = @owner_id;

DELETE FROM t_statistics_article_pv WHERE owner_id = @owner_id;

INSERT INTO t_statistics_article_pv (pv_date, owner_id, pv_count, create_time, update_time) VALUES
('2026-04-10', @owner_id, 14, '2026-04-10 23:59:00', '2026-04-10 23:59:00'),
('2026-04-11', @owner_id, 21, '2026-04-11 23:59:00', '2026-04-11 23:59:00'),
('2026-04-12', @owner_id, 19, '2026-04-12 23:59:00', '2026-04-12 23:59:00'),
('2026-04-13', @owner_id, 27, '2026-04-13 23:59:00', '2026-04-13 23:59:00'),
('2026-04-14', @owner_id, 34, '2026-04-14 23:59:00', '2026-04-14 23:59:00'),
('2026-04-15', @owner_id, 29, '2026-04-15 23:59:00', '2026-04-15 23:59:00'),
('2026-04-16', @owner_id, 38, '2026-04-16 23:59:00', '2026-04-16 23:59:00');
