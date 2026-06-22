-- Remove email and website columns from blog_comment table
-- These fields are no longer used; nickname now comes from the logged-in user
ALTER TABLE blog_comment DROP COLUMN email;
ALTER TABLE blog_comment DROP COLUMN website;
