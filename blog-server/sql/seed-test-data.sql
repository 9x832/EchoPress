-- Seed data for blog testing
-- Run with: mysql -uroot -proot blog --default-character-set=utf8mb4

-- ============ Articles ============
INSERT INTO blog_article (article_id, title, slug, summary, content, cover, category_id, article_type, is_top, is_recommend, is_comment, view_count, like_count, comment_count, word_count, status, create_time, del_flag)
VALUES
(1, 'Spring Boot 4.0 新特性详解',
 'spring-boot-4-features',
 'Spring Boot 4.0 正式发布，带来了诸多激动人心的新特性。本文详细介绍最值得关注的改进。',
 CONCAT(
   '## Spring Boot 4.0 概述\n\n',
   'Spring Boot 4.0 于 2026 年正式发布，基于 Spring Framework 7.0 构建，',
   '要求最低 JDK 17，支持虚拟线程（Virtual Threads）和 GraalVM 原生镜像。\n\n',
   '## 核心新特性\n\n',
   '### 1. 虚拟线程支持\n\n',
   'Spring Boot 4.0 内置了对 Java 21+ 虚拟线程的支持。只需在配置文件中设置：\n\n',
   '```yaml\nspring:\n  threads:\n    virtual:\n      enabled: true\n```\n\n',
   '虚拟线程可以大幅提升 I/O 密集型应用的吞吐量，且代码无需任何改动。\n\n',
   '### 2. 原生镜像改进\n\n',
   'GraalVM 原生镜像的支持更加成熟，启动时间可缩短至毫秒级：\n\n',
   '```bash\nmvn -Pnative native:compile\n```\n\n',
   '### 3. 新的安全配置\n\n',
   '安全配置全面简化，引入了声明式安全模型：\n\n',
   '```java\n@Bean\npublic SecurityFilterChain filterChain(HttpSecurity http) {\n    return http\n        .authorizeRequests(auth -> auth\n            .requestMatchers("/api/**").authenticated()\n            .anyRequest().permitAll()\n        )\n        .build();\n}\n```\n\n',
   '## 升级建议\n\n',
   '1. 先升级到 Spring Boot 3.4.x，确保无废弃 API 警告\n',
   '2. 阅读官方迁移指南\n',
   '3. 逐步迁移，先在测试环境验证\n\n',
   '> 温馨提示：升级前请务必备份项目代码！'
 ),
 NULL, 4, '0', 1, 0, '0', 1280, 36, 5, 2800, '1', '2026-06-20 12:48:38', '0'),

(2, 'Vue 3 Composition API 实战指南',
 'vue3-composition-api-guide',
 '深入理解 Vue 3 Composition API 的核心概念和实际应用场景。',
 CONCAT(
   '## 为什么要用 Composition API？\n\n',
   'Options API 在小型组件中工作良好，但当组件变得复杂时，逻辑关注点会被分散在 `data`、`methods`、`computed` 等选项中。\n\n',
   'Composition API 允许将同一逻辑关注点的代码组织在一起。\n\n',
   '## 核心 API\n\n',
   '### ref 和 reactive\n\n',
   '```typescript\nimport { ref, reactive } from ''vue''\n\n',
   'const count = ref(0)\n',
   'const state = reactive({\n',
   '  name: ''Vue'',\n',
   '  version: 3\n',
   '})\n```\n\n',
   '### computed\n\n',
   '```typescript\nconst double = computed(() => count.value * 2)\n```\n\n',
   '### watch\n\n',
   '```typescript\nwatch(count, (newVal, oldVal) => {\n',
   '  console.log(`${oldVal} -> ${newVal}`)\n',
   '})\n```\n\n',
   '## 自定义 Hook\n\n',
   '这是 Composition API 最强大的特性——逻辑复用：\n\n',
   '```typescript\nfunction useFetch(url) {\n',
   '  const data = ref(null)\n',
   '  const loading = ref(true)\n',
   '  \n',
   '  fetch(url)\n',
   '    .then(res => res.json())\n',
   '    .then(json => { data.value = json; loading.value = false })\n',
   '  \n',
   '  return { data, loading }\n',
   '}\n```\n\n',
   '## `<script setup>` 语法糖\n\n',
   '这是推荐的使用方式，代码更简洁：\n\n',
   '```vue\n<script setup>\nimport { ref } from ''vue''\nconst msg = ref(''Hello!'')\n</script>\n```\n\n',
   'Composition API 是 Vue 3 的核心特性，掌握它能让你的代码更优雅、更易维护。'
 ),
 NULL, 5, '0', 0, 1, '0', 960, 28, 3, 2200, '1', '2026-06-20 12:48:39', '0'),

(3, 'MySQL 索引优化实战',
 'mysql-index-optimization',
 '通过实际案例讲解 MySQL 索引优化的思路和技巧，让你的查询飞起来。',
 CONCAT(
   '## 前言\n\n',
   '索引是数据库性能优化最常用的手段。本文通过实际案例，介绍 MySQL 索引优化的核心思路。\n\n',
   '## 索引基础\n\n',
   '### B+Tree 索引\n\n',
   'MySQL InnoDB 引擎默认使用 B+Tree 索引结构：\n\n',
   '- 所有数据存储在叶子节点\n',
   '- 叶子节点之间通过链表连接，支持范围查询\n',
   '- 非叶子节点只存储键值，不存储数据\n\n',
   '### 聚集索引 vs 二级索引\n\n',
   '| 类型 | 叶子节点存储 | 说明 |\n',
   '|------|-------------|------|\n',
   '| 聚集索引 | 完整行数据 | 主键索引 |\n',
   '| 二级索引 | 主键值 | 需要回表 |\n\n',
   '## 实战案例\n\n',
   '### 案例 1：最左前缀原则\n\n',
   '```sql\n-- 联合索引 (a, b, c)\nSELECT * FROM t WHERE a = 1;           -- 走索引\nSELECT * FROM t WHERE a = 1 AND b = 2; -- 走索引\nSELECT * FROM t WHERE b = 2;           -- 不走索引！\n```\n\n',
   '### 案例 2：覆盖索引\n\n',
   '```sql\n-- 避免回表\nSELECT a, b FROM t WHERE a = 1;  -- Using index\nSELECT * FROM t WHERE a = 1;     -- 需要回表\n```\n\n',
   '## 优化建议\n\n',
   '1. 用 `EXPLAIN` 分析执行计划\n',
   '2. 关注 `type` 字段：`ALL` < `index` < `range` < `ref` < `const`\n',
   '3. 合理使用覆盖索引避免回表\n',
   '4. 避免在索引列上使用函数\n\n',
   '索引是一把双刃剑，写多读少的场景要适度索引。'
 ),
 NULL, 6, '0', 0, 0, '0', 756, 22, 2, 1800, '1', '2026-06-20 12:48:40', '0'),

(4, 'Docker 部署 Spring Boot 最佳实践',
 'docker-spring-boot-deploy',
 '从 Dockerfile 编写到 Compose 编排，分享 Spring Boot 容器化部署的完整实践。',
 CONCAT(
   '## 为什么需要容器化？\n\n',
   '容器化解决了"在我机器上能跑"的经典问题，保证开发、测试、生产环境的一致性。\n\n',
   '## 多阶段构建\n\n',
   '```dockerfile\nFROM eclipse-temurin:21-jdk AS builder\nWORKDIR /app\nCOPY . .\nRUN ./mvnw package -DskipTests\n\n',
   'FROM eclipse-temurin:21-jre\nWORKDIR /app\nCOPY --from=builder /app/target/*.jar app.jar\n',
   'EXPOSE 8080\nENTRYPOINT ["java", "-jar", "app.jar"]\n```\n\n',
   '## Docker Compose 编排\n\n',
   '```yaml\nversion: "3.8"\nservices:\n  mysql:\n    image: mysql:8.0\n    environment:\n      MYSQL_ROOT_PASSWORD: root\n    volumes:\n      - mysql_data:/var/lib/mysql\n',
   '  app:\n    build: .\n    ports:\n      - "8080:8080"\n    depends_on:\n      - mysql\n      - redis\n    environment:\n      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/blog\n```\n\n',
   '## 生产环境建议\n\n',
   '1. **健康检查**：配置 `healthcheck` 指令\n',
   '2. **资源限制**：设置 CPU/内存上限\n',
   '3. **日志收集**：将日志输出到 stdout，便于收集\n',
   '4. **优雅关闭**：配置 `STOPSIGNAL SIGTERM`\n',
   '5. **非 root 运行**：使用 `USER 1000`\n\n',
   '容器化是现代应用部署的最佳实践，值得投入时间学习。'
 ),
 NULL, 1, '0', 0, 0, '0', 520, 15, 1, 1500, '1', '2026-06-21 10:00:00', '0');

-- ============ Article-Tag associations ============
INSERT INTO blog_article_tag (article_id, tag_id) VALUES
(1, 1), (1, 6),  -- Spring Boot + Java
(2, 2), (2, 6),  -- Vue + Java
(3, 3), (3, 4),  -- MyBatis + MySQL
(4, 1), (4, 5);  -- Spring Boot + Redis

-- ============ Friend Links ============
INSERT INTO blog_link (link_id, name, url, logo, description, email, order_num, status, create_time) VALUES
(1, 'Spring 官方', 'https://spring.io', NULL, 'Spring Framework 官方网站', NULL, 1, '0', '2026-06-20 12:48:37'),
(2, 'Vue.js', 'https://vuejs.org', NULL, '渐进式 JavaScript 框架', NULL, 2, '0', '2026-06-20 12:48:37'),
(3, 'MySQL 官方文档', 'https://dev.mysql.com/doc', NULL, 'MySQL 数据库文档', NULL, 3, '0', '2026-06-20 12:48:37'),
(4, 'Docker Hub', 'https://hub.docker.com', NULL, '容器镜像仓库', NULL, 4, '0', '2026-06-20 12:48:37');

-- ============ Pages content ============
UPDATE blog_page SET content = CONCAT(
  '## 关于我\n\n',
  '一名热爱技术的全栈开发者，专注于 Java 和前端技术。\n\n',
  '### 技术栈\n\n',
  '- **后端**: Java, Spring Boot, MyBatis, MySQL, Redis\n',
  '- **前端**: Vue.js, TypeScript, Element Plus\n',
  '- **运维**: Docker, Linux, Nginx\n\n',
  '### 关于博客\n\n',
  '这个博客使用 Spring Boot 4.0 + Vue 3 构建，分享技术学习过程中的心得与笔记。\n\n',
  '> 学无止境，保持好奇心。'
) WHERE slug = 'about';

UPDATE blog_page SET content = CONCAT(
  '## 友情链接\n\n',
  '欢迎交换友链！\n\n',
  '### 申请条件\n\n',
  '1. 内容健康，无违法违规信息\n',
  '2. 保持更新，非长期停更站点\n',
  '3. 技术类或个人博客优先\n\n',
  '### 申请方式\n\n',
  '在留言板留言或发送邮件，附上你的网站信息即可。'
) WHERE slug = 'links';

UPDATE blog_page SET content = '## 留言板\n\n欢迎留言交流！' WHERE slug = 'guestbook';

-- ============ Carousel ============
INSERT INTO blog_carousel (carousel_id, title, image_url, link_url, order_num, status, create_time) VALUES
(1, 'Spring Boot 4.0', '', NULL, 1, '0', '2026-06-20 12:48:37'),
(2, 'Vue 3 实战', '', NULL, 2, '0', '2026-06-20 12:48:37'),
(3, 'MySQL 优化', '', NULL, 3, '0', '2026-06-20 12:48:37');

-- ============ Moments ============
INSERT INTO blog_moment (moment_id, content, images, location, like_count, status, del_flag, create_time) VALUES
(1, '今天完成了博客的第一个版本，Spring Boot 4.0 + Vue 3 的组合真的很棒！',
 NULL, '深圳', 12, '0', '0', '2026-06-18 10:30:00'),
(2, '分享一个 Vite 小技巧：使用 `vite-plugin-compression` 可以自动压缩静态资源为 gzip，部署到 Nginx 后加载速度快了不少。',
 '["https://picsum.photos/seed/vite/800/400"]', '深圳', 8, '0', '0', '2026-06-19 14:20:00'),
(3, '周末去海边走了走，顺便拍了几张照片。工作之余也要享受生活呀~',
 '["https://picsum.photos/seed/sea1/400/400","https://picsum.photos/seed/sea2/400/400","https://picsum.photos/seed/sea3/400/400"]', '深圳·大梅沙', 25, '0', '0', '2026-06-20 16:00:00'),
(4, '刚看了 Spring Boot 4.0 的 release notes，虚拟线程的支持太香了，IO 密集型应用的吞吐量直接翻倍。',
 NULL, NULL, 6, '0', '0', '2026-06-21 09:15:00');

-- No article_count column in blog_category
