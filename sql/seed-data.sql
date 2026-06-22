-- ============================================
-- EchoPress 种子数据
-- 先清空所有数据，再插入默认数据
-- 密码：admin123 / 123456
-- ============================================

-- 1. 清空所有表（FK 安全顺序）
delete from blog_article_view;
delete from blog_user_like;
delete from blog_user_follow;
delete from blog_user_social;
delete from blog_comment;
delete from blog_article_tag;
delete from blog_article;
delete from blog_moment;
delete from blog_resource;
delete from blog_subscribe;
delete from blog_carousel;
delete from blog_link;
delete from blog_page;
delete from blog_tag;
delete from blog_category;
delete from blog_site_config;
delete from blog_user;

-- 重置自增
alter table blog_user auto_increment = 100;
alter table blog_category auto_increment = 200;
alter table blog_tag auto_increment = 100;
alter table blog_article auto_increment = 100;
alter table blog_comment auto_increment = 100;
alter table blog_moment auto_increment = 100;
alter table blog_link auto_increment = 100;
alter table blog_page auto_increment = 100;
alter table blog_carousel auto_increment = 100;
alter table blog_resource auto_increment = 100;

-- ============================================
-- 2. 用户数据
-- ============================================
-- 密码：admin123 (bcrypt)
insert into blog_user (user_id, user_name, nick_name, email, password, avatar, sex, website, bio, user_type, status, create_time, update_time)
values (1, 'admin', '站长', 'admin@echopress.com', '$2b$10$xL27X/LddArIyeN4RkeroeOo1FVwxDeRbEp0dt7KmwVv//aFn.lB6', '', '1', 'https://echopress.com', 'EchoPress 博客站长，热爱技术与写作。', '01', '0', sysdate(), sysdate());

insert into blog_user (user_id, user_name, nick_name, email, password, sex, user_type, status, create_time, update_time)
values (2, 'reader', '读者小明', 'reader@echopress.com', '$2b$10$xL27X/LddArIyeN4RkeroeOo1FVwxDeRbEp0dt7KmwVv//aFn.lB6', '1', '00', '0', sysdate(), sysdate());

-- ============================================
-- 3. 分类
-- ============================================
insert into blog_category (category_id, parent_id, ancestors, category_name, order_num, status, create_time, update_time) values
(1, 0, '0', '技术',     1, '0', sysdate(), sysdate()),
(2, 0, '0', '生活',     2, '0', sysdate(), sysdate()),
(3, 0, '0', '随笔',     3, '0', sysdate(), sysdate()),
(4, 1, '0,1', '后端',   1, '0', sysdate(), sysdate()),
(5, 1, '0,1', '前端',   2, '0', sysdate(), sysdate()),
(6, 1, '0,1', '运维',   3, '0', sysdate(), sysdate());

-- ============================================
-- 4. 标签
-- ============================================
insert into blog_tag (tag_id, tag_name, color, status, create_time, update_time) values
(1, 'Spring Boot', '#67C23A', '0', sysdate(), sysdate()),
(2, 'Vue',         '#409EFF', '0', sysdate(), sysdate()),
(3, 'MyBatis',     '#E6A23C', '0', sysdate(), sysdate()),
(4, 'MySQL',       '#F56C6C', '0', sysdate(), sysdate()),
(5, 'Redis',       '#DCDFE6', '0', sysdate(), sysdate()),
(6, 'Docker',      '#909399', '0', sysdate(), sysdate()),
(7, 'Java',        '#B37FEB', '0', sysdate(), sysdate()),
(8, 'TypeScript',  '#36CFC9', '0', sysdate(), sysdate());

-- ============================================
-- 5. 站点配置
-- ============================================
insert into blog_site_config (config_id, config_name, config_key, config_value, config_type, create_time, update_time) values
(1,  '站点名称',   'site_name',         'EchoPress',             'Y', sysdate(), sysdate()),
(2,  '站点描述',   'site_description',  '一个简洁优雅的个人博客',  'Y', sysdate(), sysdate()),
(3,  '站点关键词',  'site_keywords',     '博客,EchoPress,技术',     'Y', sysdate(), sysdate()),
(4,  '站点Logo',   'site_logo',         '',                      'Y', sysdate(), sysdate()),
(5,  '站长头像',   'site_avatar',       '',                      'Y', sysdate(), sysdate()),
(6,  '站长昵称',   'site_author',       '站长',                   'Y', sysdate(), sysdate()),
(7,  '站长简介',   'site_intro',        '全栈开发者，热爱开源技术。', 'Y', sysdate(), sysdate()),
(8,  'GitHub地址', 'site_github',       'https://github.com',    'Y', sysdate(), sysdate()),
(9,  '邮箱地址',   'site_email',        'admin@echopress.com',   'Y', sysdate(), sysdate()),
(10, '备案号',     'site_record',       '',                      'Y', sysdate(), sysdate()),
(11, '页脚信息',   'site_footer',       'Powered by EchoPress',  'Y', sysdate(), sysdate()),
(12, '评论审核',   'comment_audit',     '0',                     'Y', sysdate(), sysdate()),
(13, '页面Size',   'page_size',         '10',                    'Y', sysdate(), sysdate());

-- ============================================
-- 6. 独立页面
-- ============================================
insert into blog_page (page_id, title, slug, content, is_show, order_num, status, create_time, update_time) values
(1, '关于',   'about', '## 关于我\n\n一名热爱技术的全栈开发者，专注于 Java 和前端技术。\n\n### 技术栈\n\n- **后端**：Java · Spring Boot · MyBatis · MySQL · Redis\n- **前端**：Vue 3 · TypeScript · Element Plus\n- **运维**：Docker · Linux · Nginx\n\n### 关于博客\n\nEchoPress 是一个轻量级个人博客系统，由 Spring Boot + Vue 3 构建。', '1', 1, '0', sysdate(), sysdate()),
(2, '友链',   'links', '## 友情链接\n\n欢迎交换友链！\n\n申请条件：\n1. 内容健康，无违规信息\n2. 保持更新，非长期停更站点\n3. 技术类或个人博客优先', '1', 2, '0', sysdate(), sysdate()),
(3, '留言板', 'guestbook', '## 留言板\n\n欢迎留言交流！', '0', 3, '0', sysdate(), sysdate());

-- ============================================
-- 7. 文章
-- ============================================
insert into blog_article (article_id, user_id, title, slug, summary, content, category_id, article_type, is_top, is_recommend, is_comment, view_count, like_count, comment_count, word_count, status, publish_time, create_time, update_time) values
(1, 1, 'Spring Boot 4.0 新特性一览',
 'spring-boot-4-features',
 'Spring Boot 4.0 正式发布，带来虚拟线程、GraalVM 原生镜像、声明式安全等重磅特性。',
 concat(
   '## 概述\n\nSpring Boot 4.0 基于 Spring Framework 7.0 构建，最低要求 JDK 17，全面拥抱 Java 21 虚拟线程。\n\n',
   '## 1. 虚拟线程\n\n只需一行配置即可启用：\n\n```yaml\nspring:\n  threads:\n    virtual:\n      enabled: true\n```\n\n',
   '虚拟线程可以大幅提升 I/O 密集型应用的吞吐量，代码无需任何改动。\n\n',
   '## 2. GraalVM 原生镜像\n\n启动时间缩短至毫秒级：\n\n```bash\nmvn -Pnative native:compile\n```\n\n',
   '## 3. 声明式安全配置\n\n```java\n@Bean\npublic SecurityFilterChain filterChain(HttpSecurity http) {\n    return http\n        .authorizeRequests(auth -> auth\n            .requestMatchers("/api/**").authenticated()\n            .anyRequest().permitAll()\n        )\n        .build();\n}\n```\n\n',
   '## 升级建议\n\n1. 先升级到 Spring Boot 3.4.x\n2. 阅读官方迁移指南\n3. 测试环境充分验证后再上线'
 ), 4, '0', 1, 1, '0', 1580, 42, 6, 3100, '1', '2026-06-15 10:00:00', '2026-06-15 10:00:00', '2026-06-20 10:00:00'),
(2, 1, 'Vue 3 Composition API 实战指南',
 'vue3-composition-api-guide',
 '深入理解 Vue 3 Composition API 的核心概念，从 ref/reactive 到自定义 Hook。',
 concat(
   '## Options API vs Composition API\n\nOptions API 在简单组件中够用，但逻辑复杂时会分散在 data/methods/computed 中。Composition API 把同一关注点的代码聚合在一起。\n\n',
   '## 核心响应式 API\n\n```typescript\nimport { ref, reactive, computed, watch } from ''vue''\n\nconst count = ref(0)\nconst state = reactive({ name: ''EchoPress'' })\nconst double = computed(() => count.value * 2)\n\nwatch(count, (n, o) => console.log(`${o} -> ${n}`))\n```\n\n',
   '## 自定义 Hook\n\nComposition API 最强大的特性——逻辑复用：\n\n```typescript\nfunction useFetch(url: string) {\n  const data = ref(null)\n  const loading = ref(true)\n\n  fetch(url)\n    .then(r => r.json())\n    .then(json => { data.value = json; loading.value = false })\n\n  return { data, loading }\n}\n```\n\n',
   '## `<script setup>` 语法糖\n\n```vue\n<script setup>\nimport { ref } from ''vue''\nconst msg = ref(''Hello EchoPress!'')\n</script>\n```\n\n更简洁、更少的样板代码，是官方推荐的写法。'
 ), 5, '0', 0, 1, '0', 980, 28, 3, 2200, '1', '2026-06-16 14:30:00', '2026-06-16 14:30:00', '2026-06-16 14:30:00'),
(3, 1, 'MySQL 索引优化实战',
 'mysql-index-optimization',
 '通过实际案例讲解 MySQL 索引优化的核心思路，让你的查询性能飞起来。',
 concat(
   '## 索引基础\n\nInnoDB 引擎使用 B+Tree 索引：\n- 所有数据存在叶子节点\n- 叶子节点通过链表连接，支持范围查询\n- 非叶子节点只存键值\n\n',
   '| 索引类型 | 叶子节点内容 | 说明 |\n|---------|------------|------|\n| 聚集索引 | 完整行数据 | 主键索引 |\n| 二级索引 | 主键值 | 需要回表 |\n\n',
   '## 最左前缀原则\n\n```sql\n-- 联合索引 (a, b, c)\nSELECT * FROM t WHERE a = 1;           -- 走索引\nSELECT * FROM t WHERE a = 1 AND b = 2; -- 走索引\nSELECT * FROM t WHERE b = 2;           -- 不走索引\n```\n\n',
   '## 覆盖索引\n\n```sql\n-- 避免回表查询\nSELECT a, b FROM t WHERE a = 1;  -- Using index\nSELECT * FROM t WHERE a = 1;     -- 需要回表\n```\n\n',
   '## 优化要点\n\n1. 用 `EXPLAIN` 分析执行计划\n2. `type` 字段：ALL < index < range < ref < const\n3. 合理使用覆盖索引避免回表\n4. 避免在索引列上使用函数'
 ), 6, '0', 0, 0, '0', 756, 22, 2, 1800, '1', '2026-06-18 09:00:00', '2026-06-18 09:00:00', '2026-06-18 09:00:00'),
(4, 1, 'Docker Compose 部署 Spring Boot 全攻略',
 'docker-compose-spring-boot',
 '从 Dockerfile 到 Compose 编排，一站式掌握 Spring Boot 容器化部署。',
 concat(
   '## 为什么容器化\n\n解决"在我机器上能跑"的经典问题，保证环境一致性。\n\n',
   '## 多阶段构建\n\n```dockerfile\nFROM maven:3.9-eclipse-temurin-17 AS builder\nWORKDIR /build\nCOPY . .\nRUN mvn package -DskipTests -q\n\nFROM eclipse-temurin:17-jre-alpine\nWORKDIR /app\nCOPY --from=builder /build/target/*.jar app.jar\nEXPOSE 8080\nENTRYPOINT ["java", "-jar", "app.jar"]\n```\n\n',
   '## Compose 编排\n\n```yaml\nservices:\n  mysql:\n    image: mysql:8.0\n    environment:\n      MYSQL_ROOT_PASSWORD: root\n  app:\n    build: .\n    ports:\n      - "8080:8080"\n    depends_on:\n      - mysql\n```\n\n',
   '## 生产建议\n\n1. 配置 `healthcheck` 健康检查\n2. 设置 CPU/内存资源限制\n3. 日志输出到 stdout\n4. 配置 `STOPSIGNAL SIGTERM` 优雅关闭\n5. 非 root 用户运行'
 ), 6, '0', 0, 0, '0', 520, 15, 1, 1500, '1', '2026-06-20 16:00:00', '2026-06-20 16:00:00', '2026-06-20 16:00:00'),
(5, 1, 'TypeScript 泛型完全指南',
 'typescript-generics-guide',
 '从基础到高级，全面掌握 TypeScript 泛型的使用技巧和最佳实践。',
 concat(
   '## 为什么需要泛型\n\n泛型让我们编写可复用的类型安全代码，避免重复定义。\n\n',
   '## 基础泛型\n\n```typescript\nfunction identity<T>(arg: T): T {\n  return arg\n}\n\nconst num = identity(42)    // number\nconst str = identity(''hi'') // string\n```\n\n',
   '## 泛型约束\n\n```typescript\ninterface HasLength {\n  length: number\n}\n\nfunction logLength<T extends HasLength>(arg: T): T {\n  console.log(arg.length)\n  return arg\n}\n\nlogLength(''hello'')  // OK\nlogLength([1, 2, 3])  // OK\nlogLength(42)         // Error!\n```\n\n',
   '## 高级用法\n\n```typescript\n// 条件类型\ntype IsString<T> = T extends string ? true : false\n\n// 映射类型\ntype Readonly<T> = {\n  readonly [K in keyof T]: T[K]\n}\n\n// infer 关键字\ntype ReturnType<T> = T extends (...args: any[]) => infer R ? R : never\n```\n\n掌握泛型是 TypeScript 进阶的必经之路。'
 ), 5, '0', 0, 1, '0', 420, 18, 2, 1600, '1', '2026-06-21 11:00:00', '2026-06-21 11:00:00', '2026-06-21 11:00:00');

-- 文章-标签关联
insert into blog_article_tag (article_id, tag_id) values
(1, 1), (1, 7),   -- Spring Boot + Java
(2, 2), (2, 8),   -- Vue + TypeScript
(3, 3), (3, 4),   -- MyBatis + MySQL
(4, 1), (4, 6),   -- Spring Boot + Docker
(5, 2), (5, 8);   -- Vue + TypeScript

-- ============================================
-- 8. 评论
-- ============================================
insert into blog_comment (comment_id, article_id, parent_id, user_id, nick_name, content, status, create_time) values
(1, 1, 0, 2, '读者小明', '写的太好了！Spring Boot 4.0 的虚拟线程支持真的很期待。', '1', '2026-06-16 10:30:00'),
(2, 1, 1, 1, '站长', '谢谢支持！虚拟线程对 IO 密集型应用提升特别明显。', '1', '2026-06-16 11:00:00'),
(3, 1, 0, 2, '读者小明', '请问升级到 4.0 需要改很多代码吗？', '1', '2026-06-17 09:00:00'),
(4, 1, 3, 1, '站长', '从 3.x 升级大部分代码无需修改，建议先看官方迁移指南。', '1', '2026-06-17 09:30:00'),
(5, 2, 0, null, '前端爱好者', 'Composition API 比 Options API 好用太多了！', '1', '2026-06-17 15:00:00'),
(6, 3, 0, null, 'DBA小张', '覆盖索引那个例子很实用，收藏了。', '1', '2026-06-19 10:00:00');

-- ============================================
-- 9. 动态/说说
-- ============================================
insert into blog_moment (moment_id, content, images, location, like_count, status, create_time) values
(1, 'EchoPress 博客上线了！使用 Spring Boot + Vue 3 构建，简洁高效。', null, '深圳', 15, '0', '2026-06-15 08:00:00'),
(2, '分享一个小技巧：Vite 的 `vite-plugin-compression` 可以自动压缩静态资源为 gzip，部署到 Nginx 后加载速度快了不少。', '["https://picsum.photos/seed/vite/800/400"]', '深圳', 8, '0', '2026-06-17 14:00:00'),
(3, '周末去海边走了走，顺便拍了几张照片。工作之余要记得享受生活~', '["https://picsum.photos/seed/sea1/400/400","https://picsum.photos/seed/sea2/400/400","https://picsum.photos/seed/sea3/400/400"]', '深圳·大梅沙', 25, '0', '2026-06-19 17:00:00'),
(4, '刚读完《凤凰架构》，对分布式系统有了新的理解。推荐所有后端开发者阅读。', null, null, 6, '0', '2026-06-21 10:00:00');

-- ============================================
-- 10. 友情链接
-- ============================================
insert into blog_link (link_id, name, url, logo, description, order_num, status, create_time) values
(1, 'Spring 官方', 'https://spring.io', null, 'Spring Framework 官网', 1, '0', sysdate()),
(2, 'Vue.js', 'https://vuejs.org', null, '渐进式 JavaScript 框架', 2, '0', sysdate()),
(3, 'MySQL 文档', 'https://dev.mysql.com/doc', null, 'MySQL 官方文档', 3, '0', sysdate()),
(4, 'Docker Hub', 'https://hub.docker.com', null, '容器镜像仓库', 4, '0', sysdate());

-- ============================================
-- 11. 轮播图
-- ============================================
insert into blog_carousel (carousel_id, title, image_url, link_url, order_num, status, create_time) values
(1, 'Spring Boot 4.0 已发布', 'https://picsum.photos/seed/spring/800/300', '/article/1', 1, '0', sysdate()),
(2, 'Vue 3 实战指南', 'https://picsum.photos/seed/vue/800/300', '/article/2', 2, '0', sysdate()),
(3, 'MySQL 索引优化', 'https://picsum.photos/seed/mysql/800/300', '/article/3', 3, '0', sysdate());
