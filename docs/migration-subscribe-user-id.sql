-- Add user_id column to blog_subscribe for per-author subscriptions
ALTER TABLE blog_subscribe ADD COLUMN user_id bigint(20) DEFAULT NULL COMMENT '订阅作者用户ID' AFTER subscribe_id;
