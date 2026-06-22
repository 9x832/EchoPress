<template>
  <div>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增动态</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="momentId" label="ID" width="70" />
      <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip />
      <el-table-column label="图片" width="100"><template #default="{row}"><el-image v-if="parseImages(row.images).length" :src="parseImages(row.images)[0]" style="width:50px;height:50px" fit="cover" /></template></el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80" />
      <el-table-column label="状态" width="80"><template #default="{row}">{{ ['公开','私密','草稿'][row.status] || row.status }}</template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEditMoment(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
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
        <el-form-item label="位置" prop="location"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">公开</el-radio><el-radio value="1">私密</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="doSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getMomentList, getMoment, addMoment, updateMoment, delMoment } from '@/api/moment'
import { uploadImage } from '@/api/upload'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const uploadInput = ref(null)
const images = ref([])

const { loading, list, total, queryParams, dialogVisible, dialogTitle, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getMomentList, getMoment, addMoment, updateMoment, delMoment,
    { content: '', location: '', images: '', status: '0' }, '',
    { content: [{ required: true, message: '内容不能为空', trigger: 'blur' }] }
  )
fetchList()

function parseImages(str) {
  if (!str) return []
  try { return JSON.parse(str) } catch { return [] }
}

watch(dialogVisible, (val) => {
  if (!val) return
  images.value = parseImages(form.images)
})

function handleEditMoment(row) {
  handleEdit(row)
}

async function doSubmit() {
  form.images = images.value.length ? JSON.stringify(images.value) : ''
  await handleSubmit(formRef)
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

function triggerUpload() {
  uploadInput.value?.click()
}

function removeImage(idx) {
  images.value.splice(idx, 1)
}
</script>

<style scoped>
.moment-upload { display: flex; gap: 8px; flex-wrap: wrap; }
.moment-upload-item { position: relative; width: 80px; height: 80px; border-radius: 6px; overflow: hidden; }
.moment-upload-img { width: 100%; height: 100%; object-fit: cover; }
.moment-upload-remove { position: absolute; top: -6px; right: -6px; color: #f56c6c; cursor: pointer; font-size: 18px; background: #fff; border-radius: 50%; }
.moment-upload-btn { width: 80px; height: 80px; border: 1px dashed #d9d9d9; border-radius: 6px; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #999; transition: border-color 0.2s; }
.moment-upload-btn:hover { border-color: #409eff; color: #409eff; }
</style>
