<template>
  <div class="links-page">
    <h2>友情链接</h2>
    <div class="links-intro">
      <p>欢迎交换友链！</p>
      <div class="links-rules">
        <h4>申请条件</h4>
        <ul>
          <li>内容健康，无违法违规信息</li>
          <li>保持更新，非长期停更站点</li>
          <li>技术类或个人博客优先</li>
        </ul>
      </div>
      <p class="links-apply">申请方式：在<a href="/guestbook">留言板</a>留言或发送邮件，附上你的网站信息即可。</p>
    </div>
    <div v-if="loading"><el-skeleton :rows="3" animated /></div>
    <div v-else-if="!links.length"><el-empty description="暂无友链" /></div>
    <div v-else class="links-grid">
      <a v-for="link in links" :key="link.linkId" :href="link.url" target="_blank" class="link-card">
        <img v-if="link.logo" :src="link.logo" class="link-logo" />
        <div class="link-info">
          <h4>{{ link.name }}</h4>
          <p>{{ link.description }}</p>
        </div>
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPublicLinkList } from '@/api/link'

const links = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getPublicLinkList()
    if (res.code === 200) links.value = res.data || []
  } finally { loading.value = false }
})
</script>

<style scoped>
.links-page { width: min(800px, 100%); margin: 0 auto; }
h2 { text-align: center; margin-bottom: 24px; }
.links-intro { background: #fafafa; border: 1px solid #eee; border-radius: 8px; padding: 20px 24px; margin-bottom: 30px; font-size: 14px; color: #555; line-height: 1.8; }
.links-intro p { margin: 0; }
.links-rules { margin: 12px 0; }
.links-rules h4 { font-size: 14px; color: #303133; margin-bottom: 4px; }
.links-rules ul { margin: 0; padding-left: 20px; }
.links-rules li { color: #777; }
.links-apply { font-size: 14px; }
.links-apply a { color: #409eff; }
.links-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 16px; }
.link-card { display: flex; gap: 12px; padding: 16px; border: 1px solid #eee; border-radius: 8px; transition: box-shadow 0.2s; }
.link-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
.link-logo { width: 48px; height: 48px; border-radius: 8px; object-fit: cover; flex-shrink: 0; }
.link-info h4 { font-size: 15px; margin-bottom: 4px; color: #303133; }
.link-info p { font-size: 13px; color: #999; }
</style>
