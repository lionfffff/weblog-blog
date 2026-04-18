SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE t_blog_settings
    ADD COLUMN owner_id BIGINT NULL COMMENT '博客所属用户ID' AFTER id;

ALTER TABLE t_article
    ADD COLUMN owner_id BIGINT NULL COMMENT '文章所属用户ID' AFTER summary;

ALTER TABLE t_category
    ADD COLUMN owner_id BIGINT NULL COMMENT '分类所属用户ID' AFTER id;

ALTER TABLE t_tag
    ADD COLUMN owner_id BIGINT NULL COMMENT '标签所属用户ID' AFTER id;

ALTER TABLE t_statistics_article_pv
    ADD COLUMN owner_id BIGINT NULL COMMENT 'PV 记录所属用户ID' AFTER pv_date;

UPDATE t_blog_settings
SET owner_id = 1
WHERE owner_id IS NULL;

UPDATE t_article
SET owner_id = 1
WHERE owner_id IS NULL;

UPDATE t_category
SET owner_id = 1
WHERE owner_id IS NULL;

UPDATE t_tag
SET owner_id = 1
WHERE owner_id IS NULL;

UPDATE t_statistics_article_pv
SET owner_id = 1
WHERE owner_id IS NULL;

ALTER TABLE t_blog_settings
    MODIFY COLUMN owner_id BIGINT NOT NULL COMMENT '博客所属用户ID';

ALTER TABLE t_article
    MODIFY COLUMN owner_id BIGINT NOT NULL COMMENT '文章所属用户ID';

ALTER TABLE t_category
    MODIFY COLUMN owner_id BIGINT NOT NULL COMMENT '分类所属用户ID';

ALTER TABLE t_tag
    MODIFY COLUMN owner_id BIGINT NOT NULL COMMENT '标签所属用户ID';

ALTER TABLE t_statistics_article_pv
    MODIFY COLUMN owner_id BIGINT NOT NULL COMMENT 'PV 记录所属用户ID';

ALTER TABLE t_blog_settings
    ADD UNIQUE KEY uk_blog_settings_owner_id (owner_id);

ALTER TABLE t_article
    ADD KEY idx_article_owner_id (owner_id);

ALTER TABLE t_category
    ADD KEY idx_category_owner_id (owner_id);

ALTER TABLE t_tag
    ADD KEY idx_tag_owner_id (owner_id);

ALTER TABLE t_statistics_article_pv
    DROP INDEX uk_pv_date,
    ADD KEY idx_statistics_article_pv_owner_id (owner_id),
    ADD UNIQUE KEY uk_statistics_article_pv_owner_date (owner_id, pv_date);

SET FOREIGN_KEY_CHECKS = 1;
