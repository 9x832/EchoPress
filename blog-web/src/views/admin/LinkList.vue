<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="名称"><el-input v-model="queryParams.name" placeholder="网站名称" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增友链</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="linkId" label="ID" width="70" />
      <el-table-column prop="name" label="网站名称" width="150" />
      <el-table-column prop="url" label="地址" min-width="200" show-overflow-tooltip />
      <el-table-column prop="contact" label="联系人" width="100" />
      <el-table-column prop="orderNum" label="排序" width="70" />
      <el-table-column label="状态" width="80"><template #default="{row}">{{ row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="网站名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="网址" prop="url"><el-input v-model="form.url" /></el-form-item>
        <el-form-item label="Logo" prop="logo">
          <div style="display:flex;gap:12px;align-items:center">
            <el-input v-model="form.logo" placeholder="Logo 地址" style="flex:1" />
            <input ref="uploadInput" type="file" accept="image/*" style="display:none" @change="handleUpload" />
            <el-button @click="uploadInput?.click()">本地上传</el-button>
          </div>
          <el-image v-if="form.logo" :src="form.logo" style="width:80px;height:80px;margin-top:10px;border-radius:6px" fit="contain" />
        </el-form-item>
        <el-form-item label="描述" prop="description"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="联系人" prop="contact"><el-input v-model="form.contact" /></el-form-item>
        <el-form-item label="邮箱" prop="email"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="排序" prop="orderNum"><el-input-number v-model="form.orderNum" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getLinkList, getLink, addLink, updateLink, delLink } from '@/api/link'
import { uploadImage } from '@/api/upload'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const uploadInput = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getLinkList, getLink, addLink, updateLink, delLink,
    { name: '', url: '', logo: '', description: '', contact: '', email: '', orderNum: 0, status: '0' }, '',
    { name: [{ required: true, message: '网站名称不能为空', trigger: 'blur' }], url: [{ required: true, message: '网址不能为空', trigger: 'blur' }] }
  )
fetchList()

async function handleUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const res = await uploadImage(file)
    if (res.code === 200) {
      form.logo = res.data.url
    } else {
      ElMessage.error(res.msg || '上传失败')
    }
  } catch {
    ElMessage.error('上传失败')
  } finally {
    uploadInput.value.value = ''
  }
}
</script>
