# Dashboard Enhancement Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Enhance admin dashboard from 4 stat cards to 8 cards + 2 ECharts trend charts, with a single backend API.

**Architecture:** Backend adds 3 SQL queries to existing mappers (total views, publish trend, view trend) and a new BlogDashboardAdminController returning all stats in one response. Frontend installs echarts, creates dashboard API module, and rewrites Dashboard.vue.

**Tech Stack:** Spring Boot 4.0 + MyBatis, Vue 3 + Element Plus + ECharts

## Global Constraints

- No test infrastructure — compilation/build check is sufficient
- Follow existing code patterns in mapper/controller/frontend
- Dashboard API: single `GET /blog/admin/dashboard` returning all data
- 8 cards: article, category, tag, user, comment, moment, views, resources
- 2 charts: 7-day publish trend + 7-day view trend, side by side
- Card layout: 2 rows of 4 (el-col :span="6")
- Chart layout: 2 columns (el-col :span="12")

---

### Task 1: Backend — Mapper methods for dashboard stats

**Files:**
- Modify: `blog-server/src/main/java/com/ruoyiblog/mapper/BlogArticleMapper.java`
- Modify: `blog-server/src/main/resources/mapper/blog/BlogArticleMapper.xml`
- Modify: `blog-server/src/main/java/com/ruoyiblog/mapper/BlogArticleViewMapper.java`
- Modify: `blog-server/src/main/resources/mapper/blog/BlogArticleViewMapper.xml`

**Interface:**
- Produces: `Long selectTotalViews()`, `List<Map<String, Object>> selectPublishTrend(@Param("days") int days)`, `List<Map<String, Object>> selectViewTrend(@Param("days") int days)`

- [ ] **Step 1: Add 2 methods to BlogArticleMapper.java**

Add after `decrementLikeCount`:

```java
public Long selectTotalViews();

public List<Map<String, Object>> selectPublishTrend(@Param("days") int days);
```

- [ ] **Step 2: Add 2 SQL blocks to BlogArticleMapper.xml**

Add before `</mapper>`:

```xml
<select id="selectTotalViews" resultType="Long">
    select coalesce(sum(view_count), 0) from blog_article
</select>

<select id="selectPublishTrend" resultType="java.util.HashMap">
    select date(publish_time) as date, count(*) as count
    from blog_article
    where publish_time >= date_sub(curdate(), interval #{days} day)
    group by date(publish_time)
    order by date
</select>
```

- [ ] **Step 3: Add 1 method to BlogArticleViewMapper.java**

Add at end of interface:

```java
public List<Map<String, Object>> selectViewTrend(@Param("days") int days);
```

- [ ] **Step 4: Add 1 SQL to BlogArticleViewMapper.xml**

Add before `</mapper>`:

```xml
<select id="selectViewTrend" resultType="java.util.HashMap">
    select date(view_time) as date, count(*) as count
    from blog_article_view
    where view_time >= date_sub(curdate(), interval #{days} day)
    group by date(view_time)
    order by date
</select>
```

- [ ] **Step 5: Verify compilation**

```bash
cd blog-server && mvn compile -q
```

Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add blog-server/src/main/java/com/ruoyiblog/mapper/BlogArticleMapper.java blog-server/src/main/resources/mapper/blog/BlogArticleMapper.xml blog-server/src/main/java/com/ruoyiblog/mapper/BlogArticleViewMapper.java blog-server/src/main/resources/mapper/blog/BlogArticleViewMapper.xml
git commit -m "feat: add dashboard mapper methods for total views and trend data"
```

---

### Task 2: Backend — BlogDashboardAdminController

**Files:**
- Create: `blog-server/src/main/java/com/ruoyiblog/controller/admin/BlogDashboardAdminController.java`

**Interface:**
- Produces: `GET /blog/admin/dashboard` → `AjaxResult` with `{stats: {...}, publishTrend: [...], viewTrend: [...]}`

- [ ] **Step 1: Create BlogDashboardAdminController.java**

```java
package com.ruoyiblog.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.mapper.BlogArticleMapper;
import com.ruoyiblog.mapper.BlogArticleViewMapper;
import com.ruoyiblog.service.IBlogCategoryService;
import com.ruoyiblog.service.IBlogCommentService;
import com.ruoyiblog.service.IBlogMomentService;
import com.ruoyiblog.service.IBlogResourceService;
import com.ruoyiblog.service.IBlogTagService;
import com.ruoyiblog.service.IBlogUserService;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.domain.BlogCategory;
import com.ruoyiblog.domain.BlogComment;
import com.ruoyiblog.domain.BlogMoment;
import com.ruoyiblog.domain.BlogResource;
import com.ruoyiblog.domain.BlogTag;
import com.ruoyiblog.domain.BlogUser;

@RestController
@RequestMapping("/blog/admin/dashboard")
public class BlogDashboardAdminController
{
    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogArticleViewMapper blogArticleViewMapper;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogTagService blogTagService;

    @Autowired
    private IBlogUserService blogUserService;

    @Autowired
    private IBlogCommentService blogCommentService;

    @Autowired
    private IBlogMomentService blogMomentService;

    @Autowired
    private IBlogResourceService blogResourceService;

    @GetMapping
    public AjaxResult dashboard()
    {
        Map<String, Object> stats = new HashMap<>();
        stats.put("articleCount", blogArticleMapper.selectBlogArticleList(new BlogArticle()).size());
        stats.put("categoryCount", blogCategoryService.selectBlogCategoryList(new BlogCategory()).size());
        stats.put("tagCount", blogTagService.selectBlogTagList(new BlogTag()).size());
        stats.put("userCount", blogUserService.selectBlogUserList(new BlogUser()).size());
        stats.put("commentCount", blogCommentService.selectBlogCommentList(new BlogComment()).size());
        stats.put("momentCount", blogMomentService.selectBlogMomentList(new BlogMoment()).size());
        stats.put("totalViews", blogArticleMapper.selectTotalViews());
        stats.put("resourceCount", blogResourceService.selectBlogResourceList(new BlogResource()).size());

        List<Map<String, Object>> publishTrend = blogArticleMapper.selectPublishTrend(7);
        List<Map<String, Object>> viewTrend = blogArticleViewMapper.selectViewTrend(7);

        Map<String, Object> result = new HashMap<>();
        result.put("stats", stats);
        result.put("publishTrend", publishTrend);
        result.put("viewTrend", viewTrend);

        return AjaxResult.success(result);
    }
}
```

- [ ] **Step 2: Verify compilation**

```bash
cd blog-server && mvn compile -q
```

Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add blog-server/src/main/java/com/ruoyiblog/controller/admin/BlogDashboardAdminController.java
git commit -m "feat: add BlogDashboardAdminController with aggregated stats and trends"
```

---

### Task 3: Frontend — Install echarts + create dashboard API

**Files:**
- Modify: `blog-web/package.json` (npm install)
- Create: `blog-web/src/api/dashboard.js`

- [ ] **Step 1: Install echarts**

```bash
cd blog-web && npm install echarts
```

- [ ] **Step 2: Create dashboard.js API**

```js
import request from './request'

const BASE = '/blog/admin/dashboard'

export function getDashboardData() {
  return request({ url: BASE, method: 'get' })
}
```

- [ ] **Step 3: Commit**

```bash
git add blog-web/package.json blog-web/package-lock.json blog-web/src/api/dashboard.js
git commit -m "feat: add echarts dependency and dashboard API module"
```

---

### Task 4: Frontend — Rewrite Dashboard.vue

**Files:**
- Modify: `blog-web/src/views/admin/Dashboard.vue`

- [ ] **Step 1: Rewrite Dashboard.vue**

```vue
<template>
  <div class="dashboard">
    <!-- Stat cards -->
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in cards" :key="card.title">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" :style="{ background: card.color }">
              <el-icon :size="28"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-title">{{ card.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Trend charts -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>近7天文章发布趋势</template>
          <div ref="publishChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>近7天访问量趋势</template>
          <div ref="viewChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDashboardData } from '@/api/dashboard'

const cards = ref([
  { title: '文章总数', value: 0, icon: 'Document', color: '#409eff' },
  { title: '分类总数', value: 0, icon: 'Collection', color: '#67c23a' },
  { title: '标签总数', value: 0, icon: 'PriceTag', color: '#e6a23c' },
  { title: '用户总数', value: 0, icon: 'User', color: '#f56c6c' },
  { title: '评论总数', value: 0, icon: 'ChatDotSquare', color: '#909399' },
  { title: '动态总数', value: 0, icon: 'Picture', color: '#b37feb' },
  { title: '总浏览量', value: 0, icon: 'View', color: '#36cfc9' },
  { title: '资源文件', value: 0, icon: 'FolderOpened', color: '#ff9c6e' }
])

const publishChartRef = ref(null)
const viewChartRef = ref(null)

function makeLineChart(data, color) {
  return {
    grid: { top: 20, right: 20, bottom: 30, left: 40 },
    xAxis: { type: 'category', data: data.map(d => d.date) },
    yAxis: { type: 'value', minInterval: 1 },
    tooltip: { trigger: 'axis' },
    series: [{
      data: data.map(d => d.count),
      type: 'line',
      smooth: true,
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: color },
        { offset: 1, color: 'rgba(255,255,255,0)' }
      ])},
      itemStyle: { color },
      lineStyle: { color }
    }]
  }
}

function fillDateGaps(data) {
  const result = []
  const map = {}
  data.forEach(d => { map[d.date] = d.count })
  for (let i = 6; i >= 0; i--) {
    const d = new Date()
    d.setDate(d.getDate() - i)
    const key = d.toISOString().slice(0, 10)
    result.push({ date: key, count: map[key] || 0 })
  }
  return result
}

onMounted(async () => {
  try {
    const res = await getDashboardData()
    if (res.code === 200) {
      const s = res.data.stats
      cards.value[0].value = s.articleCount
      cards.value[1].value = s.categoryCount
      cards.value[2].value = s.tagCount
      cards.value[3].value = s.userCount
      cards.value[4].value = s.commentCount
      cards.value[5].value = s.momentCount
      cards.value[6].value = s.totalViews
      cards.value[7].value = s.resourceCount

      await nextTick()
      if (publishChartRef.value) {
        const c1 = echarts.init(publishChartRef.value)
        c1.setOption(makeLineChart(fillDateGaps(res.data.publishTrend), '#409eff'))
      }
      if (viewChartRef.value) {
        const c2 = echarts.init(viewChartRef.value)
        c2.setOption(makeLineChart(fillDateGaps(res.data.viewTrend), '#67c23a'))
      }
    }
  } catch {}
})
</script>

<style scoped>
.stat-card { display: flex; align-items: center; gap: 16px; }
.stat-icon { width: 60px; height: 60px; display: flex; align-items: center; justify-content: center; border-radius: 12px; color: #fff; flex-shrink: 0; }
.stat-value { font-size: 28px; font-weight: 700; }
.stat-title { font-size: 14px; color: #999; margin-top: 4px; }
</style>
```

- [ ] **Step 2: Verify frontend build**

```bash
cd blog-web && npx vite build 2>&1 | tail -10
```

Expected: Build succeeds.

- [ ] **Step 3: Manual verification**

1. Start backend and frontend
2. Login to admin dashboard
3. Verify 8 stat cards show with correct counts
4. Verify 2 charts render with smooth lines and area fill
5. Verify charts show full 7-day range (even dates with 0 count)

- [ ] **Step 4: Commit**

```bash
git add blog-web/src/views/admin/Dashboard.vue
git commit -m "feat: rewrite dashboard with 8 stat cards and 2 ECharts trend charts"
```
