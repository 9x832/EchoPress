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
