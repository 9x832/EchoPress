<template>
  <div>
    <h2 class="page-title">分类：{{ categoryName }}</h2>
    <div v-for="a in articles" :key="a.articleId" class="article-card">
      <router-link :to="'/article/' + a.articleId" class="article-title">{{ a.title }}</router-link>
      <div class="article-meta">
        <span>{{ formatDate(a.publishTime || a.createTime) }}</span>
        <span>{{ a.viewCount || 0 }} 阅读</span>
        <LikeButton :article-id="a.articleId" v-model:like-count="a.likeCount" v-model:liked="a._liked" />
      </div>
      <p>{{ (a.summary || (a.content || '').slice(0, 150)) }}</p>
    </div>
    <el-empty v-if="!loading && articles.length === 0" description="暂无文章" />
    <el-pagination
      v-if="total > queryParams.pageSize"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total" layout="prev,pager,next" @current-change="fetch" style="justify-content:center;margin-top:20px"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPublicArticleList, getLikedStatus } from '@/api/article'
import { getPublicCategory } from '@/api/category'
import { formatDate } from '@/utils/format'
import { useUserStore } from '@/stores/user'
import LikeButton from '@/components/LikeButton.vue'

const route = useRoute()
const userStore = useUserStore()
const articles = ref([])
const total = ref(0)
const loading = ref(true)
const categoryName = ref('')
const queryParams = reactive({ pageNum: 1, pageSize: 10 })

async function fetch() {
  loading.value = true
  try {
    const [listRes] = await Promise.all([
      getPublicArticleList({ ...queryParams, categoryId: route.params.id }),
      getPublicCategory(route.params.id).then(r => { if (r.code === 200) categoryName.value = r.data.categoryName })
    ])
    if (listRes.code === 200) {
      articles.value = listRes.rows || []; total.value = listRes.total || 0
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
  } finally { loading.value = false }
}

onMounted(fetch)
</script>

<style scoped>
.page-title { font-size: 22px; margin-bottom: 24px; padding-bottom: 12px; border-bottom: 1px solid #eee; }
.article-card { padding: 20px 0; border-bottom: 1px solid #f0f0f0; }
.article-title { font-size: 18px; font-weight: 600; color: #303133; }
.article-title:hover { color: #409eff; }
.article-meta { font-size: 13px; color: #999; margin: 6px 0; display: flex; gap: 16px; }
p { color: #666; font-size: 14px; line-height: 1.6; }
</style>
