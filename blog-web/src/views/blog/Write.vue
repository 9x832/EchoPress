<template>
  <div class="write-page">
    <h2>写文章</h2>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" size="default">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="文章标题" maxlength="200" show-word-limit />
      </el-form-item>
      <el-form-item label="摘要">
        <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="可选" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <div style="margin-bottom:6px">
          <input ref="fileInput" type="file" accept="image/*" style="display:none" @change="handleLocalUpload" />
          <el-button size="small" @click="fileInput?.click()">本地上传图片</el-button>
          <span style="font-size:12px;color:#999;margin-left:8px">也可拖拽或粘贴图片到编辑器</span>
        </div>
        <v-md-editor ref="editorRef" v-model="form.content" height="500px" @upload-image="handleUploadImage" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.categoryId" placeholder="选择分类" clearable style="width:100%">
          <el-option v-for="c in categories" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="form.tagIds" multiple placeholder="选择标签" style="width:100%">
          <el-option v-for="t in tags" :key="t.tagId" :label="t.tagName" :value="t.tagId" />
        </el-select>
      </el-form-item>
      <el-form-item label="允许评论">
        <el-switch v-model="form.isComment" active-value="0" inactive-value="1" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="large" :loading="submitting" @click="handleSubmit">发布文章</el-button>
        <el-button size="large" :loading="submitting" @click="handleSavePrivate">私密发布</el-button>
        <el-button size="large" :loading="submitting" @click="handleSaveDraft">保存草稿</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addUserArticle } from '@/api/userArticle'
import { getPublicCategoryList } from '@/api/category'
import { getPublicTagList } from '@/api/tag'
import { uploadImage } from '@/api/upload'

const router = useRouter()
const formRef = ref(null)
const fileInput = ref(null)
const editorRef = ref(null)
const submitting = ref(false)
const categories = ref([])
const tags = ref([])

const form = reactive({
  title: '',
  summary: '',
  content: '',
  categoryId: null,
  tagIds: [],
  isComment: '0'
})

const rules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
}

onMounted(async () => {
  try {
    const [catRes, tagRes] = await Promise.all([
      getPublicCategoryList(), getPublicTagList()
    ])
    if (catRes.code === 200) categories.value = catRes.data || []
    if (tagRes.code === 200) tags.value = tagRes.data || []
  } catch {}
})

async function handleUploadImage(file, insertImage) {
  try {
    const res = await uploadImage(file)
    if (res.code === 200) {
      insertImage(res.data.url)
    } else {
      ElMessage.error(res.msg || '图片上传失败')
    }
  } catch {
    ElMessage.error('图片上传失败')
  }
}

async function handleLocalUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const res = await uploadImage(file)
    if (res.code === 200) {
      form.content += `\n![](${res.data.url})\n`
      ElMessage.success('图片已插入')
    } else {
      ElMessage.error(res.msg || '图片上传失败')
    }
  } catch {
    ElMessage.error('图片上传失败')
  } finally {
    if (fileInput.value) fileInput.value.value = ''
  }
}

async function doSubmit(status) {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const res = await addUserArticle({ ...form, status })
    if (res.code === 200) {
      ElMessage.success(status === '0' ? '已保存为草稿' : status === '2' ? '私密发布成功' : '发布成功')
      router.push('/')
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

function handleSubmit() {
  doSubmit('1')
}

function handleSavePrivate() {
  doSubmit('2')
}

function handleSaveDraft() {
  doSubmit('0')
}
</script>

<style scoped>
.write-page { width: min(900px, 100%); margin: 0 auto; }
h2 { margin-bottom: 24px; }
</style>
