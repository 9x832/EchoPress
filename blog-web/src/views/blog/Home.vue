<template>
  <div class="home-page">
    <div class="content-area">
      <!-- 轮播图 -->
      <div v-if="carousels.length" class="carousel-box">
        <el-carousel :interval="5000" arrow="hover" height="280px">
          <el-carousel-item v-for="item in carousels" :key="item.carouselId">
            <a v-if="item.linkUrl" :href="item.linkUrl" target="_blank" class="carousel-link">
              <div class="carousel-slide" :style="item.imageUrl ? { backgroundImage: 'url(' + item.imageUrl + ')' } : {}">
                <h3>{{ item.title }}</h3>
              </div>
            </a>
            <div v-else class="carousel-slide" :style="item.imageUrl ? { backgroundImage: 'url(' + item.imageUrl + ')' } : {}">
              <h3>{{ item.title }}</h3>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div v-if="loading" class="loading-box">
        <el-skeleton :rows="5" animated />
      </div>
      <div v-else-if="articles.length === 0 && recommended.length === 0" class="empty-box">
        <el-empty description="暂无文章" />
      </div>
      <template v-else>
        <!-- 推荐文章 -->
        <div v-if="recommended.length" class="recommended-section">
          <h3 class="section-title">推荐文章</h3>
          <div class="recommended-grid">
            <div v-for="a in recommended" :key="a.articleId" class="recommended-card">
              <router-link :to="'/article/' + a.articleId" class="rec-title">{{ a.title }}</router-link>
              <p class="rec-summary">{{ a.summary || (a.content || '').slice(0, 80) }}</p>
            </div>
          </div>
        </div>
        <!-- 文章列表 -->
        <div v-if="articles.length" class="article-list">
          <div v-for="article in articles" :key="article.articleId" :class="['article-card', { 'is-top': article.isTop }]">
            <router-link :to="'/article/' + article.articleId" class="article-title">{{ article.title }}</router-link>
            <div class="article-meta">
              <span><el-icon><Clock /></el-icon> {{ formatDate(article.publishTime || article.createTime) }}</span>
              <span v-if="article.categoryName"><el-icon><Collection /></el-icon> {{ article.categoryName }}</span>
              <span v-if="article.wordCount"><el-icon><Timer /></el-icon> {{ readingTime(article.wordCount) }}</span>
              <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }}</span>
              <span><el-icon><ChatDotSquare /></el-icon> {{ article.commentCount || 0 }}</span>
              <LikeButton :article-id="article.articleId" v-model:like-count="article.likeCount" v-model:liked="article._liked" />
            </div>
            <p class="article-summary">{{ article.summary || (article.content || '').slice(0, 200) }}</p>
            <div v-if="article.tagIds && article.tagIds.length" class="article-tags">
              <router-link v-for="tid in article.tagIds" :key="tid" :to="'/tag/' + tid" class="article-tag">{{ tagMap[tid] || tid }}</router-link>
            </div>
            <router-link :to="'/article/' + article.articleId" class="read-more">阅读全文 →</router-link>
          </div>
        </div>
        <el-pagination
          v-if="total > queryParams.pageSize"
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="fetchList"
          @size-change="fetchList"
          style="justify-content:center; margin-top:30px"
        />
      </template>
    </div>
    <aside class="sidebar">
      <el-card header="分类" shadow="never">
        <div v-if="categories.length">
          <router-link v-for="cat in categories" :key="cat.categoryId" :to="'/category/' + cat.categoryId" class="tag-link">
            {{ cat.categoryName }} <span class="count">({{ cat.articleCount || 0 }})</span>
          </router-link>
        </div>
        <el-empty v-else description="暂无分类" :image-size="40" />
      </el-card>
      <el-card header="标签" shadow="never" style="margin-top:16px">
        <div v-if="tags.length" class="tag-cloud">
          <router-link v-for="t in tags" :key="t.tagId" :to="'/tag/' + t.tagId" class="tag-item" :style="{ color: t.color || '#409eff' }">
            {{ t.tagName }}
          </router-link>
        </div>
        <el-empty v-else description="暂无标签" :image-size="40" />
      </el-card>
    </aside>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getPublicArticleList, getLikedStatus } from '@/api/article'
import { getPublicCategoryList } from '@/api/category'
import { getPublicTagList } from '@/api/tag'
import { getPublicCarouselList } from '@/api/carousel'
import { formatDate, readingTime } from '@/utils/format'
import { useUserStore } from '@/stores/user'
import LikeButton from '@/components/LikeButton.vue'
const loading = ref(true)
const articles = ref([])
const total = ref(0)
const categories = ref([])
const tags = ref([])
const carousels = ref([])
const recommended = ref([])
const tagMap = computed(() => {
  const m = {}
  if (tags.value) tags.value.forEach(t => { m[t.tagId] = t.tagName })
  return m
})
const queryParams = reactive({ pageNum: 1, pageSize: 10 })
const userStore = useUserStore()

async function fetchList() {
  loading.value = true
  try {
    const res = await getPublicArticleList({ ...queryParams, status: '1' })
    if (res.code === 200) {
      articles.value = res.rows || []
      total.value = res.total || 0
      if (userStore.token && articles.value.length) {
        fetchLikedStatus()
      }
    }
  } finally {
    loading.value = false
  }
}

async function fetchLikedStatus() {
  try {
    const ids = articles.value.map(a => a.articleId)
    const res = await getLikedStatus(ids)
    if (res.code === 200) {
      articles.value.forEach(a => { a._liked = res.data[a.articleId] || false })
    }
  } catch {}
}

onMounted(async () => {
  await fetchList()
  try {
    const [catRes, tagRes, carouselRes, recRes, ownerRes] = await Promise.all([
      getPublicCategoryList(), getPublicTagList(), getPublicCarouselList(),
      getPublicArticleList({ pageNum: 1, pageSize: 4, isRecommend: 1, status: '1' })
    ])
    if (catRes.code === 200) categories.value = catRes.data || []
    if (tagRes.code === 200) tags.value = tagRes.data || []
    if (carouselRes.code === 200) carousels.value = carouselRes.data || []
    if (recRes.code === 200) recommended.value = recRes.rows || []
  } catch {}
})

</script>

<style scoped>
.home-page { display: flex; gap: 30px; }
.content-area { flex: 1; }
.sidebar { width: 280px; flex-shrink: 0; position: sticky; top: 76px; align-self: flex-start; }
.article-card { padding: 24px 0; border-bottom: 1px solid #eee; }
.article-card.is-top { background: #faf9f5; margin: 0 -16px; padding-left: 16px; padding-right: 16px; border-radius: 6px; border-bottom-color: #e8e4d8; }
.article-title { font-size: 20px; font-weight: 600; color: #303133; line-height: 1.4; }
.article-title:hover { color: #409eff; }
.article-meta { display: flex; gap: 16px; margin: 8px 0; font-size: 13px; color: #999; }
.article-meta span { display: flex; align-items: center; gap: 3px; }
.article-summary { color: #666; line-height: 1.7; margin: 10px 0; font-size: 14px; }
.read-more { color: #409eff; font-size: 14px; }
.tag-link { display: block; padding: 4px 0; color: #555; font-size: 14px; }
.tag-link:hover { color: #409eff; }
.count { color: #999; }
.tag-cloud { display: flex; flex-wrap: wrap; gap: 8px; }
.tag-item { padding: 4px 12px; background: #f0f2f5; border-radius: 4px; font-size: 13px; }
.tag-item:hover { opacity: 0.8; }
.loading-box, .empty-box { padding: 40px; }
.carousel-box { margin-bottom: 24px; border-radius: 8px; overflow: hidden; }
.carousel-slide {
  height: 100%; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #409eff 0%, #337ecc 100%);
  background-size: cover; background-position: center;
}
.carousel-slide h3 { color: #fff; font-size: 28px; text-shadow: 0 2px 8px rgba(0,0,0,0.3); }
.carousel-link { display: block; height: 100%; }
.recommended-section { margin-bottom: 30px; }
.section-title { font-size: 18px; margin-bottom: 16px; color: #303133; padding-left: 10px; border-left: 3px solid #409eff; }
.recommended-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; }
.recommended-card { padding: 14px 18px; background: #f9fafb; border-radius: 8px; border: 1px solid #eee; }
.rec-title { font-size: 15px; font-weight: 600; color: #303133; }
.rec-title:hover { color: #409eff; }
.rec-summary { font-size: 13px; color: #999; margin-top: 4px; }
.article-tags { display: flex; gap: 6px; margin-top: 8px; flex-wrap: wrap; }
.article-tag { padding: 2px 10px; background: #ecf5ff; color: #409eff; border-radius: 4px; font-size: 12px; }
.article-tag:hover { background: #d9ecff; }
</style>
