<template>
  <div class="article-detail">
    <div v-if="loading" class="loading-box"><el-skeleton :rows="8" animated /></div>
    <div v-else-if="!article.articleId" class="empty-box"><el-empty description="文章不存在" /></div>
    <article v-else>
      <h1 class="title">{{ article.title }}</h1>
      <div class="meta">
        <span><el-icon><Clock /></el-icon> {{ formatDateTime(article.publishTime || article.createTime) }}</span>
        <span v-if="article.wordCount"><el-icon><Timer /></el-icon> {{ readingTime(article.wordCount) }}</span>
        <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }} 次阅读</span>
        <span><el-icon><ChatDotSquare /></el-icon> {{ comments.length }} 条评论</span>
        <LikeButton :article-id="article.articleId" v-model:like-count="article.likeCount" v-model:liked="isLiked" />
        <el-button size="small" type="warning" plain @click="openSubscribe">订阅此作者</el-button>
      </div>
      <div v-if="article.tagIds && article.tagIds.length" class="article-tags">
        <router-link v-for="tid in article.tagIds" :key="tid" :to="'/tag/' + tid" class="article-tag">{{ tagMap[tid] || tid }}</router-link>
      </div>
      <v-md-preview :text="article.content" class="content" />

      <!-- 评论区域 -->
      <div class="comment-section">
        <h3>评论 ({{ comments.length }})</h3>
        <div v-if="commentTree.length" class="comment-list">
          <template v-for="node in commentTree" :key="node.commentId">
            <!-- 父评论 -->
            <div class="comment-item">
              <div class="comment-avatar">
                <el-avatar :size="40">{{ node.nickName?.charAt(0) }}</el-avatar>
              </div>
              <div class="comment-body">
                <div class="comment-header">
                  <span class="comment-name">{{ node.nickName }}</span>
                  <span class="comment-time">{{ formatDateTime(node.createTime) }}</span>
                </div>
                <p class="comment-content">{{ node.content }}</p>
                <a class="reply-btn" @click="handleReply(node)"><el-icon><ChatLineSquare /></el-icon> 回复</a>
              </div>
            </div>
            <!-- 子回复 -->
            <div v-if="node._children && node._children.length" class="comment-replies">
              <div v-for="child in node._children" :key="child.commentId" class="comment-item reply-item">
                <div class="comment-avatar">
                  <el-avatar :size="32">{{ child.nickName?.charAt(0) }}</el-avatar>
                </div>
                <div class="comment-body">
                  <div class="comment-header">
                    <span class="comment-name">{{ child.nickName }}</span>
                    <span v-if="child.replyTo && child.replyTo !== node.userId" class="reply-to">回复 @{{ findNickName(child.replyTo) }}</span>
                    <span class="comment-time">{{ formatDateTime(child.createTime) }}</span>
                  </div>
                  <p class="comment-content">{{ child.content }}</p>
                  <a class="reply-btn" @click="handleReply(child)"><el-icon><ChatLineSquare /></el-icon> 回复</a>
                </div>
              </div>
            </div>
            <!-- 回复表单 -->
            <div v-if="replyingTo?.commentId === node.commentId" class="reply-form-inline">
              <div class="reply-form-header">回复 @{{ replyingTo.nickName }} <a class="cancel-reply" @click="cancelReply">取消</a></div>
              <el-input ref="replyInput" v-model="replyContent" type="textarea" :rows="2" placeholder="写下你的回复..." maxlength="500" show-word-limit @keyup.enter.ctrl="handleReplySubmit" />
              <el-button size="small" type="primary" :loading="replying" style="margin-top:8px" @click="handleReplySubmit">回复</el-button>
            </div>
          </template>
        </div>
        <div v-else class="no-comments">暂无评论，来说两句吧</div>

        <!-- 评论表单 -->
        <div class="comment-form">
          <h4>发表评论</h4>
          <template v-if="isLoggedIn">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="default">
              <el-form-item prop="content">
                <el-input v-model="form.content" type="textarea" :rows="4" placeholder="写下你的评论..." maxlength="500" show-word-limit />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :loading="submitting" @click="handleSubmit">发表评论</el-button>
              </el-form-item>
            </el-form>
          </template>
          <div v-else class="login-hint">
            <router-link to="/login">登录</router-link> 后发表评论
          </div>
        </div>
      </div>
    </article>

    <!-- 订阅对话框 -->
    <el-dialog v-model="subscribeVisible" title="订阅此作者" width="400px" :close-on-click-modal="false">
      <el-form :model="subscribeForm" :rules="subscribeRules" ref="subscribeFormRef" label-width="0">
        <el-form-item prop="email">
          <el-input v-model="subscribeForm.email" placeholder="请输入你的邮箱地址" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="subscribeVisible = false">取消</el-button>
        <el-button type="primary" :loading="subscribing" @click="handleSubscribe">确认订阅</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getPublicArticle, getLikedStatus } from '@/api/article'
import LikeButton from '@/components/LikeButton.vue'
import { getPublicCommentList, submitPublicComment } from '@/api/comment'
import { submitSubscribe } from '@/api/subscribe'
import { getPublicTagList } from '@/api/tag'
import { formatDateTime, readingTime } from '@/utils/format'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const isLoggedIn = computed(() => !!userStore.token)
const loading = ref(true)
const submitting = ref(false)
const replying = ref(false)
const article = ref({})
const isLiked = ref(false)
const comments = ref([])
const tagMap = ref({})
const formRef = ref(null)
const replyInput = ref(null)
const replyingTo = ref(null)
const replyContent = ref('')
const subscribeVisible = ref(false)
const subscribing = ref(false)
const subscribeFormRef = ref(null)
const subscribeForm = reactive({ email: '' })
const subscribeRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { pattern: /^[\w.%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

const form = reactive({
  content: ''
})

const rules = {
  content: [{ required: true, message: '请输入评论内容', trigger: 'blur' }, { max: 500, message: '不超过500字', trigger: 'blur' }]
}

const commentTree = computed(() => {
  const list = comments.value
  if (!list.length) return []
  const map = {}
  list.forEach(c => { c._children = []; map[c.commentId] = c })
  const roots = []
  list.forEach(c => {
    if (c.parentId && map[c.parentId]) {
      map[c.parentId]._children.push(c)
    } else {
      roots.push(c)
    }
  })
  return roots
})

function findNickName(userId) {
  const c = comments.value.find(x => x.userId === userId)
  return c?.nickName || ''
}

async function loadArticle() {
  const id = Number(route.params.id)
  loading.value = true
  isLiked.value = false
  try {
    const res = await getPublicArticle(id)
    if (res.code === 200) {
      article.value = res.data || {}
      if (userStore.token) {
        try {
          const statusRes = await getLikedStatus([id])
          if (statusRes.code === 200) {
            isLiked.value = statusRes.data[id] || false
          }
        } catch {}
      }
    } else {
      article.value = {}
    }
  } finally {
    loading.value = false
  }
  fetchComments()
}

onMounted(async () => {
  loadArticle()
  try {
    const tagRes = await getPublicTagList()
    if (tagRes.code === 200) {
      const m = {}
      ;(tagRes.data || []).forEach(t => { m[t.tagId] = t.tagName })
      tagMap.value = m
    }
  } catch {}
})

watch(() => route.params.id, loadArticle)

async function fetchComments() {
  try {
    const res = await getPublicCommentList(route.params.id, { pageNum: 1, pageSize: 100 })
    if (res.code === 200) comments.value = res.rows || []
  } catch {}
}

function handleReply(comment) {
  replyingTo.value = comment
  replyContent.value = ''
  nextTick(() => {
    replyInput.value?.focus?.()
  })
}

function cancelReply() {
  replyingTo.value = null
  replyContent.value = ''
}

function openSubscribe() {
  subscribeForm.email = ''
  subscribeVisible.value = true
}

async function handleSubscribe() {
  const valid = await subscribeFormRef.value.validate().catch(() => false)
  if (!valid) return
  subscribing.value = true
  try {
    const res = await submitSubscribe({ email: subscribeForm.email, userId: article.value.userId })
    if (res.code === 200) {
      ElMessage.success('订阅成功')
      subscribeVisible.value = false
      subscribeForm.email = ''
    } else {
      ElMessage.error(res.msg || '订阅失败')
    }
  } catch {
    ElMessage.error('订阅失败')
  } finally {
    subscribing.value = false
  }
}

async function handleReplySubmit() {
  if (!replyContent.value.trim()) return
  const target = replyingTo.value
  replying.value = true
  try {
    const res = await submitPublicComment({
      articleId: Number(route.params.id),
      content: replyContent.value,
      parentId: target.commentId,
      replyTo: target.userId
    })
    if (res.code === 200) {
      ElMessage.success('回复成功')
      cancelReply()
      fetchComments()
    } else {
      ElMessage.error(res.msg || '回复失败')
    }
  } catch {
    ElMessage.error('回复失败')
  } finally {
    replying.value = false
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const res = await submitPublicComment({
      articleId: Number(route.params.id),
      content: form.content
    })
    if (res.code === 200) {
      ElMessage.success('评论成功')
      form.content = ''
      fetchComments()
    } else {
      ElMessage.error(res.msg || '评论失败')
    }
  } catch {
    ElMessage.error('评论失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.article-detail { width: min(800px, 100%); margin: 0 auto; box-sizing: border-box; }
.article-detail article { width: 100%; }
.title { font-size: 28px; font-weight: 700; line-height: 1.4; margin-bottom: 12px; }
.meta { display: flex; align-items: center; gap: 20px; font-size: 14px; color: #999; margin-bottom: 16px; flex-wrap: wrap; }
.article-tags { display: flex; gap: 6px; margin-bottom: 24px; flex-wrap: wrap; }
.article-tag { padding: 2px 10px; background: #ecf5ff; color: #409eff; border-radius: 4px; font-size: 12px; }
.article-tag:hover { background: #d9ecff; }
.meta span { display: flex; align-items: center; gap: 4px; }
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
.loading-box, .empty-box { padding: 60px; }

.comment-section { margin-top: 50px; padding-top: 30px; border-top: 1px solid #eee; }
.comment-section h3 { font-size: 18px; margin-bottom: 20px; }

.comment-item { display: flex; gap: 14px; padding: 16px 0; border-bottom: 1px solid #f5f5f5; }
.comment-avatar { flex-shrink: 0; }
.comment-body { flex: 1; min-width: 0; }
.comment-header { display: flex; align-items: center; gap: 10px; margin-bottom: 6px; flex-wrap: wrap; }
.comment-name { font-weight: 600; font-size: 14px; color: #303133; }
.reply-to { font-size: 12px; color: #999; }
.reply-btn { font-size: 13px; color: #409eff; cursor: pointer; display: inline-flex; align-items: center; gap: 3px; margin-top: 6px; }
.reply-btn:hover { color: #337ecc; }
.reply-item { margin-left: 48px; border-bottom: 1px solid #f9f9f9; }
.reply-item .comment-avatar { flex-shrink: 0; }
.comment-replies { background: #fafafa; border-radius: 6px; }
.reply-form-inline { margin-left: 48px; margin-top: 10px; margin-bottom: 10px; }
.reply-form-header { font-size: 13px; color: #666; margin-bottom: 6px; }
.cancel-reply { color: #999; margin-left: 8px; cursor: pointer; font-size: 12px; }
.cancel-reply:hover { color: #f56c6c; }
.comment-time { font-size: 12px; color: #bbb; }
.comment-content { font-size: 14px; color: #555; line-height: 1.7; white-space: pre-wrap; }
.no-comments { color: #999; font-size: 14px; padding: 20px 0; }

.comment-form { margin-top: 30px; }
.comment-form h4 { font-size: 15px; margin-bottom: 16px; color: #303133; }
.login-hint { color: #999; font-size: 14px; }
.login-hint a { color: #409eff; }
</style>
