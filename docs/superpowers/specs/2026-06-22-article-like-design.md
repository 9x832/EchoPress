# Article Like Feature Design

**Date:** 2026-06-22
**Status:** Approved

## Overview

Implement toggle-based article liking for the blog. Users must be logged in to like/unlike. Like buttons appear on article detail pages and article list cards (homepage, category, tag pages).

## Scope

- Articles only (not moments, not comments)
- Login required to like
- Toggle mode: click to like, click again to unlike
- Display: detail page button + list card button

## Backend Design

### Database

Already exists:
- `blog_user_like` (user_id, article_id, create_time) — PK on (user_id, article_id)
- `blog_article.like_count` column (int, default 0)

### Mapper Layer

**BlogUserLikeMapper** (new methods):
- `selectByUserIdAndArticleId(userId, articleId)` — check existing like
- `deleteByUserIdAndArticleId(userId, articleId)` — remove like
- `selectLikedArticleIds(userId, articleIds)` — batch check liked status

**BlogArticleMapper** (new methods):
- `incrementLikeCount(articleId)` — atomic +1
- `decrementLikeCount(articleId)` — atomic -1

### Service Layer

**IBlogUserLikeService** (new methods):
- `toggleLike(userId, articleId)` → transactional: insert+increment or delete+decrement → `{liked, likeCount}`
- `getLikedArticleIds(userId, articleIds)` → batch lookup

### Controller Layer

**New: BlogArticleUserController** (`/blog/user/article`, class-level `@LoginRequired`):
- `POST /like/{articleId}` → toggle, returns `{liked, likeCount}`
- `GET /liked-status?ids=1,2,3` → returns `{articleId: boolean}` map

### Auth

Controller at `/blog/user/**` path is intercepted by JwtInterceptor. `@LoginRequired` ensures 401 if unauthenticated.

## Frontend Design

### API (`api/article.js`)

```js
likeArticle(articleId)        // POST /blog/user/article/like/{articleId}
getLikedStatus(articleIds)    // GET  /blog/user/article/liked-status?ids=1,2,3
```

### Component (`components/LikeButton.vue`)

- Props: `articleId`, `likeCount`, `liked`
- Emits: `update:likeCount`, `update:liked`
- Icon: heart (filled red when liked, outline gray when not)
- If not logged in, clicking redirects to `/login`

### Page Integration

- **ArticleDetail.vue** — like button in meta bar; fetch liked status on mount if logged in
- **Home.vue** — like button in each article card meta; batch fetch status after list load
- **CategoryArticles.vue** — like button in card meta; batch fetch after list load
- **TagArticles.vue** — like button in card meta; batch fetch after list load

## Files Changed

| File | Change |
|------|--------|
| `BlogUserLikeMapper.java` | Add 3 methods |
| `BlogUserLikeMapper.xml` | Add 3 SQL blocks |
| `BlogArticleMapper.java` | Add 2 methods |
| `BlogArticleMapper.xml` | Add 2 SQL blocks |
| `IBlogUserLikeService.java` | Add 2 methods |
| `BlogUserLikeServiceImpl.java` | Implement 2 methods |
| `BlogArticleUserController.java` | **New** — like endpoints |
| `api/article.js` | Add 2 API functions |
| `components/LikeButton.vue` | **New** — reusable like button |
| `ArticleDetail.vue` | Integrate LikeButton |
| `Home.vue` | Integrate LikeButton |
| `CategoryArticles.vue` | Integrate LikeButton |
| `TagArticles.vue` | Integrate LikeButton |
