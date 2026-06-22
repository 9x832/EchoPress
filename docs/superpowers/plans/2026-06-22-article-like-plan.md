# Article Like Feature Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement toggle-based article liking — logged-in users can like/unlike articles from detail pages and list cards.

**Architecture:** Backend adds atomic like_count increment/decrement in MyBatis XML, transactional toggle logic in service layer, and a new `@LoginRequired` controller at `/blog/user/article`. Frontend adds a reusable `LikeButton.vue` component and integrates it into article detail and list pages, batch-fetching liked status for the current user.

**Tech Stack:** Spring Boot 4.0, MyBatis, Vue 3 + Element Plus, Axios

**Note:** Project has no existing test infrastructure. Manual verification steps are included instead of automated tests. Each task includes specific verification commands.

## Global Constraints

- Paths: backend controllers at `/blog/user/article/**`, frontend API at `api/article.js`
- Auth: Controller uses `@LoginRequired` annotation, intercepted by existing JwtInterceptor
- Transactional: toggleLike must be `@Transactional` to keep like_count consistent with blog_user_like records
- Style: Follow existing code patterns — no new abstractions, match existing controller/service/mapper conventions

---

### Task 1: Backend — BlogUserLikeMapper new methods

**Files:**
- Modify: `blog-server/src/main/java/com/ruoyiblog/mapper/BlogUserLikeMapper.java`
- Modify: `blog-server/src/main/resources/mapper/blog/BlogUserLikeMapper.xml`

**Interface:**
- Produces:
  - `BlogUserLike selectByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId)`
  - `int deleteByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId)`
  - `List<Long> selectLikedArticleIds(@Param("userId") Long userId, @Param("articleIds") Long[] articleIds)`

- [ ] **Step 1: Add 3 method signatures to BlogUserLikeMapper.java**

Add after `deleteBlogUserLikeByUserIds`:

```java
public BlogUserLike selectByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

public int deleteByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

public List<Long> selectLikedArticleIds(@Param("userId") Long userId, @Param("articleIds") Long[] articleIds);
```

- [ ] **Step 2: Add 3 SQL blocks to BlogUserLikeMapper.xml**

Add before `</mapper>`:

```xml
<select id="selectByUserIdAndArticleId" resultMap="BlogUserLikeResult">
    <include refid="selectBlogUserLikeVo"/>
    where user_id = #{userId} and article_id = #{articleId}
</select>

<delete id="deleteByUserIdAndArticleId">
    delete from blog_user_like where user_id = #{userId} and article_id = #{articleId}
</delete>

<select id="selectLikedArticleIds" resultType="Long">
    select article_id from blog_user_like
    where user_id = #{userId}
    and article_id in
    <foreach item="id" collection="articleIds" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

- [ ] **Step 3: Verify compilation**

```bash
cd blog-server && mvn compile -q
```

Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add blog-server/src/main/java/com/ruoyiblog/mapper/BlogUserLikeMapper.java blog-server/src/main/resources/mapper/blog/BlogUserLikeMapper.xml
git commit -m "feat: add BlogUserLikeMapper methods for like toggle and batch status query"
```

---

### Task 2: Backend — BlogArticleMapper atomic like_count operations

**Files:**
- Modify: `blog-server/src/main/java/com/ruoyiblog/mapper/BlogArticleMapper.java`
- Modify: `blog-server/src/main/resources/mapper/blog/BlogArticleMapper.xml`

**Interface:**
- Produces:
  - `int incrementLikeCount(Long articleId)` — atomic SQL increment
  - `int decrementLikeCount(Long articleId)` — atomic SQL decrement

- [ ] **Step 1: Add 2 method signatures to BlogArticleMapper.java**

Add after `deleteBlogArticleByArticleIds`:

```java
public int incrementLikeCount(Long articleId);

public int decrementLikeCount(Long articleId);
```

- [ ] **Step 2: Add 2 SQL blocks to BlogArticleMapper.xml**

Add before `</mapper>`:

```xml
<update id="incrementLikeCount" parameterType="Long">
    update blog_article set like_count = like_count + 1 where article_id = #{articleId}
</update>

<update id="decrementLikeCount" parameterType="Long">
    update blog_article set like_count = like_count - 1 where article_id = #{articleId} and like_count > 0
</update>
```

- [ ] **Step 3: Verify compilation**

```bash
cd blog-server && mvn compile -q
```

Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add blog-server/src/main/java/com/ruoyiblog/mapper/BlogArticleMapper.java blog-server/src/main/resources/mapper/blog/BlogArticleMapper.xml
git commit -m "feat: add atomic like_count increment/decrement to BlogArticleMapper"
```

---

### Task 3: Backend — IBlogUserLikeService toggle logic

**Files:**
- Modify: `blog-server/src/main/java/com/ruoyiblog/service/IBlogUserLikeService.java`
- Modify: `blog-server/src/main/java/com/ruoyiblog/service/impl/BlogUserLikeServiceImpl.java`

**Interface:**
- Consumes: `BlogUserLikeMapper.selectByUserIdAndArticleId`, `deleteByUserIdAndArticleId`, `insertBlogUserLike`, `selectLikedArticleIds`; `BlogArticleMapper.incrementLikeCount`, `decrementLikeCount`
- Produces:
  - `Map<String, Object> toggleLike(Long userId, Long articleId)` — returns `{liked: boolean, likeCount: long}`
  - `Map<Long, Boolean> getLikedArticleIds(Long userId, Long[] articleIds)` — returns `{articleId: true/false}`

- [ ] **Step 1: Add method signatures to IBlogUserLikeService.java**

Add after `deleteBlogUserLikeByUserId`:

```java
public Map<String, Object> toggleLike(Long userId, Long articleId);

public Map<Long, Boolean> getLikedArticleIds(Long userId, Long[] articleIds);
```

- [ ] **Step 2: Implement toggleLike in BlogUserLikeServiceImpl.java**

Add `@Autowired` for `BlogArticleMapper`, then add methods after existing CRUD methods:

```java
@Autowired
private BlogArticleMapper blogArticleMapper;

@Override
@Transactional
public Map<String, Object> toggleLike(Long userId, Long articleId)
{
    BlogUserLike existing = blogUserLikeMapper.selectByUserIdAndArticleId(userId, articleId);
    Map<String, Object> result = new HashMap<>();
    if (existing != null)
    {
        blogUserLikeMapper.deleteByUserIdAndArticleId(userId, articleId);
        blogArticleMapper.decrementLikeCount(articleId);
        result.put("liked", false);
    }
    else
    {
        BlogUserLike like = new BlogUserLike();
        like.setUserId(userId);
        like.setArticleId(articleId);
        like.setCreateTime(DateUtils.getNowDate());
        blogUserLikeMapper.insertBlogUserLike(like);
        blogArticleMapper.incrementLikeCount(articleId);
        result.put("liked", true);
    }
    BlogArticle updated = blogArticleMapper.selectBlogArticleByArticleId(articleId);
    result.put("likeCount", updated != null ? updated.getLikeCount() : 0L);
    return result;
}

@Override
public Map<Long, Boolean> getLikedArticleIds(Long userId, Long[] articleIds)
{
    Map<Long, Boolean> result = new HashMap<>();
    if (articleIds == null || articleIds.length == 0)
    {
        return result;
    }
    List<Long> likedIds = blogUserLikeMapper.selectLikedArticleIds(userId, articleIds);
    for (Long id : articleIds)
    {
        result.put(id, likedIds.contains(id));
    }
    return result;
}
```

Add imports at top of file:
```java
import java.util.HashMap;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyiblog.mapper.BlogArticleMapper;
import com.ruoyiblog.domain.BlogArticle;
```

- [ ] **Step 3: Verify compilation**

```bash
cd blog-server && mvn compile -q
```

Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add blog-server/src/main/java/com/ruoyiblog/service/IBlogUserLikeService.java blog-server/src/main/java/com/ruoyiblog/service/impl/BlogUserLikeServiceImpl.java
git commit -m "feat: implement toggleLike and getLikedArticleIds with transactional consistency"
```

---

### Task 4: Backend — BlogArticleUserController

**Files:**
- Create: `blog-server/src/main/java/com/ruoyiblog/controller/BlogArticleUserController.java`

**Interface:**
- Consumes: `IBlogUserLikeService.toggleLike`, `IBlogUserLikeService.getLikedArticleIds`; `SecurityUtils.getUserId()`
- Produces:
  - `POST /blog/user/article/like/{articleId}` → `AjaxResult` with `{liked, likeCount}`
  - `GET /blog/user/article/liked-status?ids=1,2,3` → `AjaxResult` with `{articleId: boolean}`

- [ ] **Step 1: Create BlogArticleUserController.java**

```java
package com.ruoyiblog.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.utils.SecurityUtils;
import com.ruoyiblog.interceptor.LoginRequired;
import com.ruoyiblog.service.IBlogUserLikeService;

@RestController
@RequestMapping("/blog/user/article")
@LoginRequired
public class BlogArticleUserController
{
    @Autowired
    private IBlogUserLikeService blogUserLikeService;

    @PostMapping("/like/{articleId}")
    public AjaxResult like(@PathVariable("articleId") Long articleId)
    {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = blogUserLikeService.toggleLike(userId, articleId);
        return AjaxResult.success(result);
    }

    @GetMapping("/liked-status")
    public AjaxResult likedStatus(@RequestParam("ids") Long[] ids)
    {
        Long userId = SecurityUtils.getUserId();
        Map<Long, Boolean> result = blogUserLikeService.getLikedArticleIds(userId, ids);
        return AjaxResult.success(result);
    }
}
```

- [ ] **Step 2: Verify compilation and startup**

```bash
cd blog-server && mvn compile -q
```

Expected: BUILD SUCCESS

- [ ] **Step 3: Manual verification with curl (requires running server + valid token)**

Start the server, then test with a valid JWT token:

```bash
# Toggle like on article 1
curl -X POST http://localhost:8080/blog/user/article/like/1 \
  -H "Authorization: Bearer <token>"
# Expected: {"code":200,"data":{"liked":true,"likeCount":1}}

# Toggle again (unlike)
curl -X POST http://localhost:8080/blog/user/article/like/1 \
  -H "Authorization: Bearer <token>"
# Expected: {"code":200,"data":{"liked":false,"likeCount":0}}

# Batch check liked status
curl "http://localhost:8080/blog/user/article/liked-status?ids=1,2,3" \
  -H "Authorization: Bearer <token>"
# Expected: {"code":200,"data":{"1":false,"2":false,"3":false}}

# Without token
curl -X POST http://localhost:8080/blog/user/article/like/1
# Expected: 401 with "未登录或登录已过期"
```

- [ ] **Step 4: Commit**

```bash
git add blog-server/src/main/java/com/ruoyiblog/controller/BlogArticleUserController.java
git commit -m "feat: add BlogArticleUserController for article like toggle and status endpoints"
```

---

### Task 5: Frontend — API methods

**Files:**
- Modify: `blog-web/src/api/article.js`

**Interface:**
- Produces:
  - `likeArticle(articleId)` → Promise
  - `getLikedStatus(articleIds)` → Promise

- [ ] **Step 1: Add API methods to article.js**

Add at end of file before the last export:

```js
const USER = '/blog/user/article'

export function likeArticle(articleId) {
  return request({ url: USER + '/like/' + articleId, method: 'post' })
}

export function getLikedStatus(ids) {
  return request({ url: USER + '/liked-status', method: 'get', params: { ids: ids.join(',') } })
}
```

- [ ] **Step 2: Verify frontend compiles**

```bash
cd blog-web && npm run build --if-present 2>&1 || npx vite build 2>&1
```

Actually, this is just an API file change. Check it parses:

```bash
cd blog-web && node -e "require('./src/api/article.js')" 2>&1 || echo "ESM expected — check with vite"
```

Expected: The file should be syntactically valid. ESLint or vite build will catch issues.

- [ ] **Step 3: Commit**

```bash
git add blog-web/src/api/article.js
git commit -m "feat: add likeArticle and getLikedStatus API methods"
```

---

### Task 6: Frontend — LikeButton component

**Files:**
- Create: `blog-web/src/components/LikeButton.vue`

**Interface:**
- Props: `articleId` (Number), `likeCount` (Number), `liked` (Boolean)
- Emits: `update:likeCount`, `update:liked`

- [ ] **Step 1: Create LikeButton.vue**

```vue
<template>
  <span class="like-button" :class="{ liked: localLiked }" @click="handleClick">
    <el-icon><component :is="localLiked ? 'StarFilled' : 'Star'" /></el-icon>
    {{ localLikeCount || 0 }}
  </span>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { likeArticle } from '@/api/article'
import { ElMessage } from 'element-plus'

const props = defineProps({
  articleId: { type: Number, required: true },
  likeCount: { type: Number, default: 0 },
  liked: { type: Boolean, default: false }
})

const emit = defineEmits(['update:likeCount', 'update:liked'])

const router = useRouter()
const userStore = useUserStore()

const localLiked = ref(props.liked)
const localLikeCount = ref(props.likeCount)

watch(() => props.liked, (v) => { localLiked.value = v })
watch(() => props.likeCount, (v) => { localLikeCount.value = v })

let loading = false

async function handleClick() {
  if (!userStore.token) {
    router.push('/login')
    return
  }
  if (loading) return
  loading = true
  try {
    const res = await likeArticle(props.articleId)
    if (res.code === 200) {
      localLiked.value = res.data.liked
      localLikeCount.value = res.data.likeCount
      emit('update:liked', res.data.liked)
      emit('update:likeCount', res.data.likeCount)
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch {
    ElMessage.error('操作失败')
  } finally {
    loading = false
  }
}
</script>

<style scoped>
.like-button {
  display: inline-flex; align-items: center; gap: 4px;
  font-size: 14px; color: #999; cursor: pointer; user-select: none;
}
.like-button:hover { color: #f56c6c; }
.like-button.liked { color: #f56c6c; }
</style>
```

- [ ] **Step 2: Verify frontend compiles**

```bash
cd blog-web && npx vite build 2>&1 | tail -5
```

Expected: Build completes without error.

- [ ] **Step 3: Commit**

```bash
git add blog-web/src/components/LikeButton.vue
git commit -m "feat: add reusable LikeButton component with toggle and login redirect"
```

---

### Task 7: Frontend — Integrate LikeButton into pages

**Files:**
- Modify: `blog-web/src/views/blog/ArticleDetail.vue`
- Modify: `blog-web/src/views/blog/Home.vue`
- Modify: `blog-web/src/views/blog/CategoryArticles.vue`
- Modify: `blog-web/src/views/blog/TagArticles.vue`

**Interface:**
- Consumes: `LikeButton` component, `getLikedStatus` API, `useUserStore`

- [ ] **Step 1: ArticleDetail.vue — Add like button in meta bar**

**Import:**
```js
import LikeButton from '@/components/LikeButton.vue'
import { getLikedStatus } from '@/api/article'
```

**Replace meta bar** (line 7-12 in template) — add LikeButton after the comment count span:

```html
<div class="meta">
  <span><el-icon><Clock /></el-icon> {{ formatDateTime(article.publishTime || article.createTime) }}</span>
  <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }} 次阅读</span>
  <span><el-icon><ChatDotSquare /></el-icon> {{ comments.length }} 条评论</span>
  <LikeButton :article-id="article.articleId" v-model:like-count="article.likeCount" v-model:liked="isLiked" />
  <el-button size="small" type="warning" plain @click="openSubscribe">订阅此作者</el-button>
</div>
```

**Add reactive state:**
```js
const isLiked = ref(false)
```

**Add liked status fetch in onMounted** — after the article is loaded, inside the `try` block after `article.value = res.data || {}`:

```js
if (userStore.token) {
  try {
    const statusRes = await getLikedStatus([Number(route.params.id)])
    if (statusRes.code === 200) {
      isLiked.value = statusRes.data[route.params.id] || false
    }
  } catch {}
}
```

Wait — the article detail onMounted currently does not use `await` for the article load (it's fire-and-forget with `fetchComments()` also called separately). Let me restructure the onMounted:

The current onMounted is:
```js
onMounted(async () => {
  try {
    const res = await getPublicArticle(route.params.id)
    if (res.code === 200) article.value = res.data || {}
  } finally {
    loading.value = false
  }
  fetchComments()
  try {
    const tagRes = await getPublicTagList()
    ...
  } catch {}
})
```

Change to:
```js
onMounted(async () => {
  try {
    const res = await getPublicArticle(route.params.id)
    if (res.code === 200) {
      article.value = res.data || {}
      if (userStore.token) {
        try {
          const statusRes = await getLikedStatus([Number(route.params.id)])
          if (statusRes.code === 200) {
            isLiked.value = statusRes.data[route.params.id] || false
          }
        } catch {}
      }
    }
  } finally {
    loading.value = false
  }
  fetchComments()
  try {
    const tagRes = await getPublicTagList()
    if (tagRes.code === 200) {
      const m = {}
      ;(tagRes.data || []).forEach(t => { m[t.tagId] = t.tagName })
      tagMap.value = m
    }
  } catch {}
})
```

- [ ] **Step 2: Verify ArticleDetail page**

Start frontend: `cd blog-web && npx vite --host 0.0.0.0`
- Open an article detail page — verify like button appears in meta bar
- Not logged in: click → should redirect to /login
- Logged in: click → toggle like, count updates, icon toggles fill/outline
- Refresh: liked state persists

- [ ] **Step 3: Home.vue — Add like button in article cards**

**Import:**
```js
import LikeButton from '@/components/LikeButton.vue'
import { getLikedStatus } from '@/api/article'
```

**Modify article card** — add LikeButton in the `.article-meta` div, after the comment count span:

```html
<div class="article-meta">
  <span><el-icon><Clock /></el-icon> {{ formatDate(article.publishTime || article.createTime) }}</span>
  <span v-if="article.categoryName"><el-icon><Collection /></el-icon> {{ article.categoryName }}</span>
  <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }}</span>
  <span><el-icon><ChatDotSquare /></el-icon> {{ article.commentCount || 0 }}</span>
  <LikeButton :article-id="article.articleId" v-model:like-count="article.likeCount" v-model:liked="article._liked" />
</div>
```

**Add batch liked status fetch** — create a helper function, call it after article list loads. Add after `articles.value = res.rows || []` in `fetchList()`:

```js
articles.value = res.rows || []
total.value = res.total || 0
if (userStore.token && articles.value.length) {
  fetchLikedStatus()
}
```

Add helper function after `fetchList`:
```js
async function fetchLikedStatus() {
  try {
    const ids = articles.value.map(a => a.articleId)
    const res = await getLikedStatus(ids)
    if (res.code === 200) {
      articles.value.forEach(a => { a._liked = res.data[a.articleId] || false })
    }
  } catch {}
}
```

Also, need to initialize `_liked` for each article when articles are set. Since Vue 3 reactivity tracks new properties on reactive objects via Proxy, `a._liked = ...` will be reactive for each article object.

Also add `import { useUserStore } from '@/stores/user'` and `const userStore = useUserStore()` in Home.vue script setup.

Wait — looking at the Home.vue script, it doesn't currently import useUserStore. I need to add that import.

Same for recommended articles? No, the recommended section doesn't show meta, so no need.

- [ ] **Step 4: Verify Home page**

Start frontend, visit home page:
- Article cards show like button with count
- Logged in: batch fetch works, liked articles show filled icon
- Click to toggle like works

- [ ] **Step 5: CategoryArticles.vue — Add like button**

**Import:**
```js
import LikeButton from '@/components/LikeButton.vue'
import { getLikedStatus } from '@/api/article'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
```

**Modify article meta:**
```html
<div class="article-meta">
  <span>{{ formatDate(a.publishTime || a.createTime) }}</span>
  <span>{{ a.viewCount || 0 }} 阅读</span>
  <LikeButton :article-id="a.articleId" v-model:like-count="a.likeCount" v-model:liked="a._liked" />
</div>
```

**Add batch fetch after list load** — in `fetch()` after `articles.value = listRes.rows || []`:
```js
if (listRes.code === 200) {
  articles.value = listRes.rows || []
  total.value = listRes.total || 0
  if (userStore.token && articles.value.length) {
    try {
      const ids = articles.value.map(a => a.articleId)
      const statusRes = await getLikedStatus(ids)
      if (statusRes.code === 200) {
        articles.value.forEach(a => { a._liked = statusRes.data[a.articleId] || false })
      }
    } catch {}
  }
}
```

- [ ] **Step 6: TagArticles.vue — Add like button** (same pattern as CategoryArticles)

**Import:**
```js
import LikeButton from '@/components/LikeButton.vue'
import { getLikedStatus } from '@/api/article'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
```

**Modify article meta:**
```html
<div class="article-meta">
  <span>{{ formatDate(a.publishTime || a.createTime) }}</span>
  <span>{{ a.viewCount || 0 }} 阅读</span>
  <LikeButton :article-id="a.articleId" v-model:like-count="a.likeCount" v-model:liked="a._liked" />
</div>
```

**Modify onMounted** — after `articles.value = listRes.rows || []`:
```js
if (listRes.code === 200) articles.value = listRes.rows || []
if (userStore.token && articles.value.length) {
  try {
    const ids = articles.value.map(a => a.articleId)
    const statusRes = await getLikedStatus(ids)
    if (statusRes.code === 200) {
      articles.value.forEach(a => { a._liked = statusRes.data[a.articleId] || false })
    }
  } catch {}
}
```

- [ ] **Step 7: Build verification**

```bash
cd blog-web && npx vite build 2>&1
```

Expected: Build succeeds with no errors.

- [ ] **Step 8: Manual end-to-end test**

1. Start both backend and frontend
2. Visit homepage without login — like buttons show gray outline icon
3. Click a like button — redirects to /login
4. Login, return to homepage — like buttons should show current state
5. Click to like an article — icon turns filled red, count increments
6. Click again — icon turns gray, count decrements
7. Navigate to article detail — like state preserved
8. Navigate to category/tag pages — like state preserved across pages

- [ ] **Step 9: Commit**

```bash
git add blog-web/src/views/blog/ArticleDetail.vue blog-web/src/views/blog/Home.vue blog-web/src/views/blog/CategoryArticles.vue blog-web/src/views/blog/TagArticles.vue
git commit -m "feat: integrate LikeButton into article detail, home, category, and tag pages"
```
