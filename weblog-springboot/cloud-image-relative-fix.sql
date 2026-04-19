USE weblog;

-- Normalize blog asset URLs so the browser requests them from the Vercel site,
-- which then proxies to the backend through vercel.json rewrites.
UPDATE t_blog_settings
SET logo = '/images/logo.svg';

UPDATE t_blog_settings
SET avatar = '/images/avatars/avatar-01.svg'
WHERE owner_id = 1;

UPDATE t_blog_settings
SET avatar = '/images/avatars/avatar-02.svg'
WHERE owner_id = 4;

UPDATE t_blog_settings
SET avatar = '/images/avatars/avatar-03.svg'
WHERE owner_id = 5;

UPDATE t_blog_settings
SET avatar = '/images/avatar.svg'
WHERE avatar LIKE '%/uploads/%';

UPDATE t_blog_settings
SET avatar = CONCAT('/images/', SUBSTRING_INDEX(avatar, '/images/', -1))
WHERE avatar LIKE '%/images/%';

-- Normalize article cover URLs.
UPDATE t_article
SET cover = CONCAT('/images/', SUBSTRING_INDEX(cover, '/images/', -1))
WHERE cover LIKE '%/images/%';

UPDATE t_article
SET cover = '/images/cover-project.svg'
WHERE cover LIKE '%/uploads/%';
