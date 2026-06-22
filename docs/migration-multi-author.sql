-- Multi-author support migration
-- Run this on existing databases to add user ownership columns

ALTER TABLE blog_article ADD COLUMN user_id bigint(20) DEFAULT NULL COMMENT '作者用户ID' AFTER article_id;
ALTER TABLE blog_moment ADD COLUMN user_id bigint(20) DEFAULT NULL COMMENT '作者用户ID' AFTER moment_id;
CREATE INDEX idx_blog_article_uid ON blog_article(user_id);
CREATE INDEX idx_blog_moment_uid ON blog_moment(user_id);
