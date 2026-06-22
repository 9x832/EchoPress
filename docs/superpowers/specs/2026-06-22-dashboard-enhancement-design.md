# Admin Dashboard Enhancement Design

**Date:** 2026-06-22
**Status:** Approved

## Overview

Enhance the admin dashboard from 4 basic stat cards to 8 cards + 2 trend charts (ECharts line charts), with a single backend API endpoint providing all dashboard data.

## Backend Design

### Controller

**New: BlogDashboardAdminController** (`/blog/admin/dashboard`)

`GET /` → returns all dashboard data in one response:

```json
{
  "stats": {
    "articleCount": 42,
    "categoryCount": 8,
    "tagCount": 15,
    "userCount": 120,
    "commentCount": 256,
    "momentCount": 10,
    "totalViews": 15800,
    "resourceCount": 35
  },
  "publishTrend": [
    {"date": "2026-06-16", "count": 3},
    {"date": "2026-06-17", "count": 5},
    ...
  ],
  "viewTrend": [
    {"date": "2026-06-16", "count": 120},
    {"date": "2026-06-17", "count": 89},
    ...
  ]
}
```

### Mapper

No new mapper methods needed — reuse existing count/list queries. The controller calls existing service methods:
- `IBlogArticleService.selectBlogArticleList` (count)
- `IBlogCategoryService.selectBlogCategoryList` (count)
- `IBlogTagService.selectBlogTagList` (count)
- `IBlogUserService.selectBlogUserList` (count)
- `IBlogCommentService.selectBlogCommentList` (count)
- `IBlogMomentService.selectBlogMomentList` (count)
- `IBlogArticleMapper` — `selectTotalViews()` (new, `SELECT SUM(view_count) FROM blog_article`)
- `IBlogResourceService` — count
- `IBlogArticleMapper` — `selectPublishTrend(days)` (new)
- `IBlogArticleViewMapper` — `selectViewTrend(days)` (new)

New mapper methods needed:

**BlogArticleMapper:**
- `Long selectTotalViews()` → `SELECT COALESCE(SUM(view_count), 0) FROM blog_article`
- `List<Map<String, Object>> selectPublishTrend(@Param("days") int days)` → group by date

**BlogArticleViewMapper** (already exists, add method):
- `List<Map<String, Object>> selectViewTrend(@Param("days") int days)` → group by date

### SQL for trends

```sql
-- Publish trend
SELECT DATE(publish_time) as date, COUNT(*) as count
FROM blog_article
WHERE publish_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY)
GROUP BY DATE(publish_time)
ORDER BY date

-- View trend
SELECT DATE(view_time) as date, COUNT(*) as count
FROM blog_article_view
WHERE view_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY)
GROUP BY DATE(view_time)
ORDER BY date
```

## Frontend Design

### Dependencies

Install `echarts`:
```
npm install echarts
```

### API

New file: `blog-web/src/api/dashboard.js`
```js
export function getDashboardData() {
  return request({ url: '/blog/admin/dashboard', method: 'get' })
}
```

### Dashboard.vue Changes

**Template:**
- 8 stat cards (2 rows of 4, using `el-row` + `el-col :span="6"`)
- Below cards: 2 ECharts line charts side by side (each `el-col :span="12"`)

**Script:**
- Import echarts, dashboard API
- onMounted: fetch dashboard data, init charts
- Chart options: xAxis = dates, yAxis = count, smooth line, tooltip, area fill

## Files Changed

| File | Change |
|------|--------|
| `BlogArticleMapper.java` | +2 methods |
| `BlogArticleMapper.xml` | +2 SQL |
| `BlogArticleViewMapper.java` | +1 method |
| `BlogArticleViewMapper.xml` | +1 SQL |
| `BlogDashboardAdminController.java` | **New** |
| `blog-web/src/api/dashboard.js` | **New** |
| `blog-web/src/views/admin/Dashboard.vue` | Rewrite |
| `blog-web/package.json` | +echarts dep |
