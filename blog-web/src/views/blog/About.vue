<template>
  <div class="about-page">
    <div v-if="loading"><el-skeleton :rows="5" animated /></div>
    <div v-else-if="!rawContent"><el-empty description="页面不存在" /></div>
    <v-md-preview v-else :text="rawContent" class="content" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPublicPageBySlug } from '@/api/page'

const rawContent = ref('')
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getPublicPageBySlug('about')
    if (res.code === 200 && res.data) {
      rawContent.value = res.data.content
    }
  } finally { loading.value = false }
})
</script>

<style scoped>
.about-page { width: min(800px, 100%); margin: 0 auto; }
.content { line-height: 2; font-size: 16px; }
.content :deep(img) { max-width: 100%; }
.content :deep(h1) { font-size: 2em; margin: 24px 0 16px; padding-bottom: .3em; border-bottom: 1px solid #eaecef; }
.content :deep(h2) { font-size: 1.5em; margin: 24px 0 16px; padding-bottom: .3em; border-bottom: 1px solid #eaecef; }
.content :deep(h3) { font-size: 1.25em; margin: 24px 0 16px; }
.content :deep(h4) { font-size: 1em; margin: 24px 0 16px; }
.content :deep(p) { margin: 0 0 16px; }
.content :deep(blockquote) { margin: 0 0 16px; padding: 0 1em; color: #6a737d; border-left: .25em solid #dfe2e5; }
.content :deep(pre) { margin-bottom: 16px; padding: 16px; overflow: auto; font-size: 85%; line-height: 1.45; background: #f6f8fa; border-radius: 6px; }
.content :deep(code) { margin: 0; padding: .2em .4em; font-size: 85%; background-color: rgba(27,31,35,.05); border-radius: 3px; }
.content :deep(pre) > code { padding: 0; background: transparent; border: 0; }
.content :deep(ul), .content :deep(ol) { margin: 0 0 16px; padding-left: 2em; }
.content :deep(li) { display: list-item; }
.content :deep(table) { width: 100%; margin-bottom: 16px; border-collapse: collapse; }
.content :deep(table th), .content :deep(table td) { padding: 6px 13px; border: 1px solid #dfe2e5; }
.content :deep(table tr) { background: #fff; border-top: 1px solid #c6cbd1; }
.content :deep(table tr:nth-child(2n)) { background: #f6f8fa; }
.content :deep(hr) { height: .25em; margin: 24px 0; background: #e1e4e8; border: 0; }
</style>
