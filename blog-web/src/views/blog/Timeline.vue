<template>
  <div class="timeline-page">
    <h2>时间线</h2>
    <div v-if="loading"><el-skeleton :rows="5" animated /></div>
    <div v-else-if="!articles.length"><el-empty description="暂无文章" /></div>
    <el-timeline v-else>
      <el-timeline-item v-for="a in articles" :key="a.articleId" :timestamp="formatDate(a.publishTime || a.createTime)" placement="top">
        <el-card shadow="hover">
          <router-link :to="'/article/' + a.articleId" class="tl-title">{{ a.title }}</router-link>
          <p class="tl-summary">{{ (a.summary || (a.content || '').slice(0, 200)) }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserArticleList } from '@/api/userArticle'
import { formatDate } from '@/utils/format'

const articles = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getUserArticleList({ pageNum: 1, pageSize: 100, orderByColumn: 'createTime', isAsc: 'desc' })
    if (res.code === 200) articles.value = res.rows || []
  } finally { loading.value = false }
})
</script>

<style scoped>
.timeline-page { width: min(800px, 100%); margin: 0 auto; }
h2 { text-align: center; margin-bottom: 30px; }
.tl-title { font-size: 16px; font-weight: 600; color: #303133; }
.tl-title:hover { color: #409eff; }
.tl-summary { margin-top: 8px; font-size: 14px; color: #666; }
</style>
