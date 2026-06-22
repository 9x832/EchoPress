<template>
  <div class="moments-page">
    <div class="moments-header">
      <h2>瞬间</h2>
      <el-button v-if="isLoggedIn" type="primary" @click="openDialog">发瞬间</el-button>
    </div>
    <div v-if="loading"><el-skeleton :rows="5" animated /></div>
    <div v-else-if="!moments.length"><el-empty description="暂无动态" /></div>
    <div v-else class="moment-list">
      <div v-for="m in moments" :key="m.momentId" class="moment-item">
        <p class="moment-content">{{ m.content }}</p>
        <div v-if="m.images" class="moment-images">
          <img v-for="(img, idx) in parseImages(m.images)" :key="idx" :src="img" class="moment-img" />
        </div>
        <div class="moment-meta">
          <span v-if="m.nickName">{{ m.nickName }}</span>
          <span v-if="m.location"><el-icon><Location /></el-icon> {{ m.location }}</span>
          <span><el-icon><Clock /></el-icon> {{ formatDateTime(m.createTime) }}</span>
        </div>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" title="发瞬间" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="60px">
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" maxlength="2000" show-word-limit placeholder="分享此刻的想法..." />
        </el-form-item>
        <el-form-item label="图片">
          <div class="moment-upload">
            <div v-for="(img, idx) in images" :key="idx" class="moment-upload-item">
              <img :src="img" class="moment-upload-img" />
              <el-icon class="moment-upload-remove" @click="removeImage(idx)"><CircleClose /></el-icon>
            </div>
            <div class="moment-upload-btn" @click="triggerUpload" v-if="images.length < 9">
              <el-icon :size="28"><Plus /></el-icon>
            </div>
          </div>
          <input ref="uploadInput" type="file" accept="image/*" style="display:none" @change="handleUpload" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location" placeholder="可选，如：北京" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio value="0">公开</el-radio>
            <el-radio value="1">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getPublicMomentList } from '@/api/moment'
import { addUserMoment } from '@/api/userMoment'
import { uploadImage } from '@/api/upload'
import { formatDateTime } from '@/utils/format'

const userStore = useUserStore()
const isLoggedIn = computed(() => !!userStore.token)

const moments = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const submitting = ref(false)
const formRef = ref(null)
const uploadInput = ref(null)
const images = ref([])

const form = reactive({
  content: '',
  location: '',
  status: '0'
})

const rules = {
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

function parseImages(imagesStr) {
  if (!imagesStr) return []
  try { return JSON.parse(imagesStr) } catch { return [] }
}

function openDialog() {
  form.content = ''
  form.location = ''
  form.status = '0'
  images.value = []
  dialogVisible.value = true
}

function triggerUpload() {
  uploadInput.value?.click()
}

async function handleUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const res = await uploadImage(file)
    if (res.code === 200) {
      images.value.push(res.data.url)
    } else {
      ElMessage.error(res.msg || '上传失败')
    }
  } catch {
    ElMessage.error('上传失败')
  } finally {
    uploadInput.value.value = ''
  }
}

function removeImage(idx) {
  images.value.splice(idx, 1)
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const data = { ...form, images: images.value.length ? JSON.stringify(images.value) : null }
    const res = await addUserMoment(data)
    if (res.code === 200) {
      ElMessage.success('发布成功')
      dialogVisible.value = false
      fetchMoments()
    } else {
      ElMessage.error(res.msg || '发布失败')
    }
  } catch {
    ElMessage.error('发布失败')
  } finally {
    submitting.value = false
  }
}

async function fetchMoments() {
  loading.value = true
  try {
    const res = await getPublicMomentList()
    if (res.code === 200) moments.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchMoments()
})
</script>

<style scoped>
.moments-page { width: min(800px, 100%); margin: 0 auto; }
.moments-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.moments-header h2 { margin: 0; }
.moment-item { padding: 20px 0; border-bottom: 1px solid #f0f0f0; }
.moment-content { font-size: 15px; color: #333; line-height: 1.8; }
.moment-images { display: flex; gap: 8px; margin-top: 10px; flex-wrap: wrap; }
.moment-img { max-width: 200px; max-height: 200px; border-radius: 6px; object-fit: cover; }
.moment-meta { display: flex; gap: 16px; font-size: 13px; color: #bbb; margin-top: 10px; }
.moment-meta span { display: flex; align-items: center; gap: 3px; }
.moment-upload { display: flex; gap: 8px; flex-wrap: wrap; }
.moment-upload-item { position: relative; width: 80px; height: 80px; border-radius: 6px; overflow: hidden; }
.moment-upload-img { width: 100%; height: 100%; object-fit: cover; }
.moment-upload-remove { position: absolute; top: -6px; right: -6px; color: #f56c6c; cursor: pointer; font-size: 18px; background: #fff; border-radius: 50%; }
.moment-upload-btn { width: 80px; height: 80px; border: 1px dashed #d9d9d9; border-radius: 6px; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #999; transition: border-color 0.2s; }
.moment-upload-btn:hover { border-color: #409eff; color: #409eff; }
</style>
