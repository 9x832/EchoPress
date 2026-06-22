<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="标题"><el-input v-model="queryParams.title" placeholder="文章标题" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="状态"><el-select v-model="queryParams.status" placeholder="状态" clearable style="width:120px"><el-option label="草稿" value="0" /><el-option label="已发布" value="1" /><el-option label="私密" value="2" /></el-select></el-form-item>
      <el-form-item label="推荐"><el-select v-model="queryParams.isRecommend" placeholder="全部" clearable style="width:100px"><el-option label="推荐" :value="1" /><el-option label="非推荐" :value="0" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增文章</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="articleId" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="viewCount" label="浏览" width="80" />
      <el-table-column prop="commentCount" label="评论" width="80" />
      <el-table-column label="状态" width="80"><template #default="{row}">{{ ['草稿','已发布','私密','待审核'][row.status] || row.status }}</template></el-table-column>
      <el-table-column label="推荐" width="70"><template #default="{row}"><el-tag :type="row.isRecommend ? 'warning' : 'info'" size="small">{{ row.isRecommend ? '是' : '否' }}</el-tag></template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}">
          <el-button link type="primary" @click="handleEdit(row)">修改</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-if="total > queryParams.pageSize"
      v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :total="total" layout="total,sizes,prev,pager,next,jumper"
      @size-change="handleSizeChange" @current-change="handleCurrentChange"
      style="justify-content:flex-end;margin-top:16px"
    />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="内容" prop="content">
          <div style="margin-bottom:6px">
            <input ref="fileInput" type="file" accept="image/*" style="display:none" @change="handleLocalUpload" />
            <el-button size="small" @click="fileInput?.click()">本地上传图片</el-button>
            <span style="font-size:12px;color:#999;margin-left:8px">也可拖拽或粘贴图片到编辑器</span>
          </div>
          <v-md-editor ref="editorRef" v-model="form.content" height="400px" @upload-image="handleUploadImage" />
        </el-form-item>
        <el-form-item label="分类"><el-select v-model="form.categoryId" placeholder="选择分类" style="width:100%"><el-option v-for="c in categories" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" /></el-select></el-form-item>
        <el-form-item label="标签"><el-select v-model="form.tagIds" multiple placeholder="选择标签" style="width:100%"><el-option v-for="t in tags" :key="t.tagId" :label="t.tagName" :value="t.tagId" /></el-select></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">草稿</el-radio><el-radio value="1">已发布</el-radio><el-radio value="2">私密</el-radio></el-radio-group></el-form-item>
        <el-form-item label="是否置顶"><el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="是否推荐"><el-switch v-model="form.isRecommend" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="允许评论"><el-switch v-model="form.isComment" active-value="0" inactive-value="1" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getArticleList, getArticle, addArticle, updateArticle, delArticle } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getPublicTagList } from '@/api/tag'
import { uploadImage } from '@/api/upload'
import { useCrud } from '@/composables/useCrud'

const categories = ref([])
const tags = ref([])
const formRef = ref(null)
const fileInput = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getArticleList, getArticle, addArticle, updateArticle, delArticle,
    { title: '', summary: '', content: '', categoryId: null, tagIds: [], status: '0', isTop: 0, isRecommend: 0, isComment: '0' }, '',
    { title: [{ required: true, message: '标题不能为空', trigger: 'blur' }], content: [{ required: true, message: '内容不能为空', trigger: 'blur' }] }
  )

onMounted(async () => {
  fetchList()
  try {
    const [catRes, tagRes] = await Promise.all([
      getCategoryList({ pageSize: 100 }), getPublicTagList()
    ])
    if (catRes.code === 200) categories.value = catRes.rows || []
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
</script>
