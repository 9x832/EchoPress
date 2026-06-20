-- ----------------------------
-- -------------------------------------
-- Blog 独立博客系统 — 数据库初始化脚本
-- 完全独立，不依赖任何外部框架
-- -------------------------------------
create database if not exists `blog` default character set utf8mb4 collate utf8mb4_general_ci;

use `blog`;

-- ----------------------------
-- 1. 博客用户表
-- 前台注册用户（读者/博主）
-- ----------------------------
drop table if exists blog_user;
create table blog_user (
  user_id        bigint(20)    not null auto_increment    comment '用户ID',
  user_name      varchar(30)   not null                   comment '用户名（登录账号）',
  nick_name      varchar(30)   not null                   comment '用户昵称（展示用）',
  email          varchar(50)   default null               comment '邮箱',
  phone          varchar(11)   default null               comment '手机号',
  password       varchar(100)  not null                   comment '密码（BCrypt加密）',
  avatar         varchar(200)  default null               comment '头像URL',
  sex            char(1)       default '0'                comment '性别（0未知 1男 2女）',
  website        varchar(200)  default null               comment '个人网站',
  bio            varchar(500)  default null               comment '个人简介',
  birthday       date                                     comment '出生日期',
  login_ip       varchar(128)  default ''                 comment '最后登录IP',
  login_date     datetime                                 comment '最后登录时间',
  login_count    int(11)       default 0                  comment '登录次数',
  article_count  int(11)       default 0                  comment '文章数量',
  user_type      varchar(2)    default '00'               comment '用户类型（00普通用户 01博主 02管理员）',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  del_flag       char(1)       default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (user_id),
  unique key uk_blog_user_name  (user_name),
  unique key uk_blog_user_email (email)
) engine=innodb auto_increment=100 comment = '博客用户表';

-- ----------------------------
-- 2. 用户第三方登录绑定表
-- 支持 GitHub、微信、QQ 等第三方登录
-- ----------------------------
drop table if exists blog_user_social;
create table blog_user_social (
  social_id      bigint(20)    not null auto_increment    comment '绑定记录ID',
  user_id        bigint(20)    not null                   comment '用户ID',
  platform       varchar(20)   not null                   comment '平台类型（github/wechat/qq/weibo/gitee）',
  open_id        varchar(100)  not null                   comment '平台唯一标识',
  union_id       varchar(100)  default null               comment '平台UnionID',
  access_token   varchar(500)  default null               comment '访问令牌',
  refresh_token  varchar(500)  default null               comment '刷新令牌',
  expires_in     int(11)       default null               comment '令牌过期时间（秒）',
  nick_name      varchar(30)   default null               comment '平台昵称',
  avatar         varchar(200)  default null               comment '平台头像',
  raw_info       varchar(2000) default null               comment '平台原始信息（JSON）',
  create_time    datetime                                 comment '绑定时间',
  primary key (social_id),
  key idx_blog_social_uid (user_id),
  unique key uk_blog_social (platform, open_id)
) engine=innodb auto_increment=100 comment = '用户第三方登录绑定表';

-- ----------------------------
-- 3. 用户关注关系表
-- 多对多关联-- ----------------------------
drop table if exists blog_user_follow;
create table blog_user_follow (
  follower_id    bigint(20)    not null                   comment '关注者ID',
  followee_id    bigint(20)    not null                   comment '被关注者ID',
  create_time    datetime                                 comment '关注时间',
  primary key (follower_id, followee_id),
  key idx_blog_follow_ee (followee_id)
) engine=innodb comment = '用户关注关系表';

-- ----------------------------
-- 4. 文章分类表
-- 树形结构（支持父子分类）
-- ----------------------------
drop table if exists blog_category;
create table blog_category (
  category_id    bigint(20)    not null auto_increment    comment '分类ID',
  parent_id      bigint(20)    default 0                  comment '父分类ID',
  ancestors      varchar(200)  default ''                 comment '祖级列表',
  category_name  varchar(50)   not null                   comment '分类名称',
  order_num      int(4)        default 0                  comment '显示顺序',
  icon           varchar(100)  default null               comment '分类图标',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  del_flag       char(1)       default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (category_id)
) engine=innodb auto_increment=200 comment = '文章分类表';

-- ----------------------------
-- 5. 文章标签表
-- ----------------------------
drop table if exists blog_tag;
create table blog_tag (
  tag_id         bigint(20)    not null auto_increment    comment '标签ID',
  tag_name       varchar(30)   not null                   comment '标签名称',
  color          varchar(20)   default null               comment '标签颜色',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (tag_id),
  unique key uk_blog_tag_name (tag_name)
) engine=innodb auto_increment=100 comment = '文章标签表';

-- ----------------------------
-- 6. 文章表
-- ----------------------------
drop table if exists blog_article;
create table blog_article (
  article_id     bigint(20)    not null auto_increment    comment '文章ID',
  title          varchar(200)  not null                   comment '文章标题',
  slug           varchar(200)  default null               comment '文章别名（SEO友好URL）',
  summary        varchar(500)  default ''                 comment '文章摘要',
  content        longblob                                 comment '文章内容',
  cover          varchar(200)  default null               comment '封面图片URL',
  category_id    bigint(20)    default null               comment '分类ID',
  article_type   char(1)       default '0'                comment '文章类型（0原创 1转载 2翻译）',
  article_source varchar(200)  default null               comment '转载来源',
  source_url     varchar(200)  default null               comment '原文链接',
  is_top         tinyint(1)    default 0                  comment '是否置顶（0否 1是）',
  is_recommend   tinyint(1)    default 0                  comment '是否推荐（0否 1是）',
  is_comment     char(1)       default '0'                comment '是否允许评论（0允许 1禁止）',
  view_count     int(11)       default 0                  comment '浏览次数',
  like_count     int(11)       default 0                  comment '点赞次数',
  comment_count  int(11)       default 0                  comment '评论次数',
  word_count     int(11)       default 0                  comment '文章字数',
  status         char(1)       default '0'                comment '文章状态（0草稿 1已发布 2私密 3待审核）',
  del_flag       char(1)       default '0'                comment '删除标志（0代表存在 2代表删除）',
  publish_time   datetime                                 comment '发布时间',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (article_id),
  key idx_blog_article_ct (category_id),
  key idx_blog_article_st (status),
  key idx_blog_article_pt (publish_time)
) engine=innodb auto_increment=100 comment = '文章表';

-- ----------------------------
-- 7. 文章标签关联表
-- 多对多关联-- ----------------------------
drop table if exists blog_article_tag;
create table blog_article_tag (
  article_id     bigint(20)    not null                   comment '文章ID',
  tag_id         bigint(20)    not null                   comment '标签ID',
  primary key (article_id, tag_id),
  key idx_blog_article_tag_tid (tag_id)
) engine=innodb comment = '文章标签关联表';

-- ----------------------------
-- 8. 评论表
-- 支持嵌套回复（parent_id）
-- ----------------------------
drop table if exists blog_comment;
create table blog_comment (
  comment_id     bigint(20)    not null auto_increment    comment '评论ID',
  article_id     bigint(20)    not null                   comment '文章ID',
  parent_id      bigint(20)    default 0                  comment '父评论ID',
  reply_to       bigint(20)    default null               comment '回复目标用户ID',
  user_id        bigint(20)    default null               comment '评论用户ID（blog_user.user_id）',
  nick_name      varchar(30)   not null                   comment '评论者昵称',
  email          varchar(50)   default null               comment '评论者邮箱',
  website        varchar(200)  default null               comment '评论者网站',
  content        text                                      comment '评论内容',
  ip             varchar(128)  default ''                 comment '评论IP',
  location       varchar(255)  default null               comment '评论者所在地',
  browser        varchar(50)   default null               comment '浏览器类型',
  os             varchar(50)   default null               comment '操作系统',
  status         char(1)       default '0'                comment '评论状态（0待审核 1已通过 2已拒绝）',
  is_top         tinyint(1)    default 0                  comment '是否置顶（0否 1是）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (comment_id),
  key idx_blog_comment_art (article_id),
  key idx_blog_comment_pid (parent_id),
  key idx_blog_comment_st  (status),
  key idx_blog_comment_ct  (create_time)
) engine=innodb auto_increment=100 comment = '评论表';

-- ----------------------------
-- 9. 用户点赞记录表
-- 用于去重统计
-- ----------------------------
drop table if exists blog_user_like;
create table blog_user_like (
  user_id        bigint(20)    not null                   comment '用户ID',
  article_id     bigint(20)    not null                   comment '文章ID',
  create_time    datetime                                 comment '点赞时间',
  primary key (user_id, article_id),
  key idx_blog_user_like_art (article_id)
) engine=innodb comment = '用户点赞记录表';

-- ----------------------------
-- 10. 友情链接表
-- ----------------------------
drop table if exists blog_link;
create table blog_link (
  link_id        bigint(20)    not null auto_increment    comment '链接ID',
  name           varchar(50)   not null                   comment '网站名称',
  url            varchar(200)  not null                   comment '网站地址',
  logo           varchar(200)  default null               comment '网站Logo',
  description    varchar(500)  default ''                 comment '网站描述',
  contact        varchar(50)   default null               comment '联系人',
  email          varchar(50)   default null               comment '联系邮箱',
  order_num      int(4)        default 0                  comment '显示顺序',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (link_id)
) engine=innodb auto_increment=100 comment = '友情链接表';

-- ----------------------------
-- 11. 首页轮播图表
-- ----------------------------
drop table if exists blog_carousel;
create table blog_carousel (
  carousel_id    bigint(20)    not null auto_increment    comment '轮播图ID',
  title          varchar(50)   not null                   comment '轮播标题',
  image_url      varchar(200)  not null                   comment '图片地址',
  link_url       varchar(200)  default null               comment '跳转链接',
  order_num      int(4)        default 0                  comment '显示顺序',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (carousel_id)
) engine=innodb auto_increment=100 comment = '首页轮播图表';

-- ----------------------------
-- 12. 独立页面表
-- 如：关于我、留言板、友情链接页等
-- ----------------------------
drop table if exists blog_page;
create table blog_page (
  page_id        bigint(20)    not null auto_increment    comment '页面ID',
  title          varchar(100)  not null                   comment '页面标题',
  slug           varchar(200)  not null                   comment '页面别名（/about、/contact）',
  content        longblob                                 comment '页面内容',
  cover          varchar(200)  default null               comment '页面封面',
  is_show        char(1)       default '1'                comment '是否显示在导航栏（0隐藏 1显示）',
  order_num      int(4)        default 0                  comment '显示顺序',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (page_id),
  unique key uk_blog_page_slug (slug)
) engine=innodb auto_increment=100 comment = '独立页面表';

-- ----------------------------
-- 13. 站点配置表
-- 系统配置表
-- ----------------------------
drop table if exists blog_site_config;
create table blog_site_config (
  config_id      int(5)        not null auto_increment    comment '配置ID',
  config_name    varchar(100)  default ''                 comment '配置名称',
  config_key     varchar(100)  default ''                 comment '配置键名',
  config_value   varchar(500)  default ''                 comment '配置键值',
  config_type    char(1)       default 'N'                comment '系统内置（Y是 N否）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (config_id),
  unique key uk_blog_config_key (config_key)
) engine=innodb auto_increment=100 comment = '站点配置表';

-- ----------------------------
-- 14. 动态/说说表
-- 类似微博，用于发表简短动态
-- ----------------------------
drop table if exists blog_moment;
create table blog_moment (
  moment_id      bigint(20)    not null auto_increment    comment '动态ID',
  content        varchar(2000) not null                   comment '动态内容',
  images         varchar(2000) default null               comment '图片列表（JSON数组）',
  location       varchar(255)  default null               comment '发布位置',
  like_count     int(11)       default 0                  comment '点赞次数',
  status         char(1)       default '0'                comment '状态（0公开 1私密 2草稿）',
  del_flag       char(1)       default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (moment_id),
  key idx_blog_moment_ct (create_time)
) engine=innodb auto_increment=100 comment = '动态/说说表';

-- ----------------------------
-- 15. 资源上传记录表
-- 用于管理上传的图片、文件等资源
-- ----------------------------
drop table if exists blog_resource;
create table blog_resource (
  resource_id    bigint(20)    not null auto_increment    comment '资源ID',
  file_name      varchar(200)  not null                   comment '原始文件名',
  file_path      varchar(200)  not null                   comment '存储路径',
  file_size      bigint(20)    default 0                  comment '文件大小（字节）',
  file_type      varchar(20)   default null               comment '文件类型（image/file/video）',
  mime_type      varchar(100)  default null               comment 'MIME类型',
  extension      varchar(20)   default null               comment '文件扩展名',
  width          int(11)       default null               comment '图片宽度',
  height         int(11)       default null               comment '图片高度',
  storage_type   char(1)       default '0'                comment '存储方式（0本地 1阿里云OSS 2七牛云）',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (resource_id),
  key idx_blog_resource_ft (file_type),
  key idx_blog_resource_ct (create_time)
) engine=innodb auto_increment=100 comment = '资源上传记录表';

-- ----------------------------
-- 16. 文章浏览记录表
-- 用于去重统计文章阅读量
-- ----------------------------
drop table if exists blog_article_view;
create table blog_article_view (
  view_id        bigint(20)    not null auto_increment    comment '浏览记录ID',
  article_id     bigint(20)    not null                   comment '文章ID',
  user_id        bigint(20)    default null               comment '用户ID（未登录为null）',
  ip             varchar(128)  default ''                 comment '浏览IP',
  user_agent     varchar(500)  default null               comment 'User-Agent',
  view_time      datetime                                 comment '浏览时间',
  primary key (view_id),
  key idx_blog_view_art (article_id),
  key idx_blog_view_vt  (view_time)
) engine=innodb auto_increment=100 comment = '文章浏览记录表';

-- ----------------------------
-- 17. 邮件订阅表
-- ----------------------------
drop table if exists blog_subscribe;
create table blog_subscribe (
  subscribe_id   bigint(20)    not null auto_increment    comment '订阅ID',
  email          varchar(50)   not null                   comment '订阅邮箱',
  status         char(1)       default '0'                comment '状态（0正常 1停用）',
  verify_code    varchar(64)   default null               comment '验证码',
  verified       char(1)       default '0'                comment '是否已验证（0否 1是）',
  subscribe_time datetime                                 comment '订阅时间',
  create_by      varchar(64)   default ''                 comment '创建者',
  create_time    datetime                                 comment '创建时间',
  update_by      varchar(64)   default ''                 comment '更新者',
  update_time    datetime                                 comment '更新时间',
  remark         varchar(500)  default null               comment '备注',
  primary key (subscribe_id),
  unique key uk_blog_subscribe_email (email)
) engine=innodb auto_increment=100 comment = '邮件订阅表';

-- ----------------------------
-- 初始化数据：默认分类
-- ----------------------------
insert into blog_category (category_id, parent_id, ancestors, category_name, order_num, status, create_time) values
(1, 0, '0', '技术', 1, '0', sysdate()),
(2, 0, '0', '生活', 2, '0', sysdate()),
(3, 0, '0', '随笔', 3, '0', sysdate()),
(4, 1, '0,1', 'Java', 1, '0', sysdate()),
(5, 1, '0,1', '前端', 2, '0', sysdate()),
(6, 1, '0,1', '数据库', 3, '0', sysdate()),
(7, 1, '0,1', '架构', 4, '0', sysdate());

-- ----------------------------
-- 初始化数据：默认标签
-- ----------------------------
insert into blog_tag (tag_id, tag_name, color, status, create_time) values
(1, 'Spring Boot', '#67C23A', '0', sysdate()),
(2, 'Vue', '#409EFF', '0', sysdate()),
(3, 'MyBatis', '#E6A23C', '0', sysdate()),
(4, 'MySQL', '#F56C6C', '0', sysdate()),
(5, 'Redis', '#DCDFE6', '0', sysdate()),
(6, 'Java', '#909399', '0', sysdate());

-- ----------------------------
-- 初始化数据：站点配置
-- ----------------------------
insert into blog_site_config (config_id, config_name, config_key, config_value, config_type, create_time) values
(1,  '站点名称',   'site_name',         'My Blog',            'Y', sysdate()),
(2,  '站点描述',   'site_description',  '一个简洁的个人博客',     'Y', sysdate()),
(3,  '站点关键词',  'site_keywords',     '博客,Blog,技术',        'Y', sysdate()),
(4,  '站点Logo',   'site_logo',         '',                     'Y', sysdate()),
(5,  '站长头像',   'site_avatar',       '',                     'Y', sysdate()),
(6,  '站长昵称',   'site_author',       '管理员',               'Y', sysdate()),
(7,  '站长简介',   'site_intro',        '',                     'Y', sysdate()),
(8,  'GitHub地址', 'site_github',       '',                     'Y', sysdate()),
(9,  '邮箱地址',   'site_email',        '',                     'Y', sysdate()),
(10, '备案号',     'site_record',       '',                     'Y', sysdate()),
(11, '页脚信息',   'site_footer',       'Powered by Blog',      'Y', sysdate()),
(12, '评论审核',   'comment_audit',     '0',                    'Y', sysdate()),
(13, '页面Size',   'page_size',         '10',                   'Y', sysdate());

-- ----------------------------
-- 初始化数据：默认页面
-- ----------------------------
insert into blog_page (page_id, title, slug, content, is_show, order_num, status, create_time) values
(1, '关于我', 'about', null, '0', 1, '0', sysdate()),
(2, '友情链接', 'links', null, '0', 2, '0', sysdate()),
(3, '留言板', 'guestbook', null, '0', 3, '0', sysdate());
