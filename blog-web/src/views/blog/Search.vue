<template>
  <div class="search-page">
    <h2>搜索文章</h2>
    <div class="search-box">
      <el-input v-model="keyword" placeholder="输入关键词搜索" size="large" clearable @keyup.enter="doSearch">
        <template #append><el-button :icon="Search" @click="doSearch">搜索</el-button></template>
      </el-input>
    </div>
    <div v-if="loading"><el-skeleton :rows="5" animated /></div>
    <div v-else-if="searched && !articles.length"><el-empty description="未找到相关文章" /></div>
    <div v-else-if="articles.length">
      <div v-for="a in articles" :key="a.articleId" class="article-card">
        <router-link :to="'/article/' + a.articleId" class="article-title">{{ a.title }}</router-link>
        <div class="article-meta">{{ formatDate(a.publishTime || a.createTime) }}</div>
        <p>{{ (a.summary || (a.content || '').slice(0, 150)) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { getPublicArticleList } from '@/api/article'
import { formatDate } from '@/utils/format'

const keyword = ref('')
const articles = ref([])
const loading = ref(false)
const searched = ref(false)

async function doSearch() {
  if (!keyword.value.trim()) return
  loading.value = true
  searched.value = true
  try {
    const res = await getPublicArticleList({ pageNum: 1, pageSize: 50, title: keyword.value })
    if (res.code === 200) articles.value = res.rows || []
  } finally { loading.value = false }
}
</script>

<style scoped>
.search-page { width: min(800px, 100%); margin: 0 auto; }
h2 { text-align: center; margin-bottom: 24px; }
.search-box { margin-bottom: 30px; }
.article-card { padding: 20px 0; border-bottom: 1px solid #f0f0f0; }
.article-title { font-size: 18px; font-weight: 600; color: #303133; }
.article-title:hover { color: #409eff; }
.article-meta { font-size: 13px; color: #999; margin: 6px 0; }
p { color: #666; font-size: 14px; line-height: 1.6; }
</style>
