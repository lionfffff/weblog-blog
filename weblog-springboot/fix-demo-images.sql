USE weblog;

UPDATE t_blog_settings
SET logo = 'http://localhost:8086/images/logo.svg',
    avatar = 'http://localhost:8086/images/avatar.svg'
WHERE id = 1;

UPDATE t_article SET cover = 'http://localhost:8086/images/cover-threadpool.svg' WHERE id = 1;
UPDATE t_article SET cover = 'http://localhost:8086/images/cover-modules.svg' WHERE id = 2;
UPDATE t_article SET cover = 'http://localhost:8086/images/cover-mysql.svg' WHERE id = 3;
UPDATE t_article SET cover = 'http://localhost:8086/images/cover-lucene.svg' WHERE id = 4;
UPDATE t_article SET cover = 'http://localhost:8086/images/cover-project.svg' WHERE id = 5;
UPDATE t_article SET cover = 'http://localhost:8086/images/cover-empty-home.svg' WHERE id = 6;
