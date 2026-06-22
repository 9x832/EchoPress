<template>
  <div>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增轮播</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="carouselId" label="ID" width="70" />
      <el-table-column label="图片" width="120"><template #default="{row}"><el-image v-if="row.imageUrl" :src="row.imageUrl" style="width:100px;height:56px" fit="cover" /></template></el-table-column>
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="linkUrl" label="跳转链接" min-width="150" show-overflow-tooltip />
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
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <div style="display:flex;gap:12px;align-items:center">
            <el-input v-model="form.imageUrl" placeholder="图片地址" style="flex:1" />
            <input ref="uploadInput" type="file" accept="image/*" style="display:none" @change="handleUpload" />
            <el-button @click="uploadInput?.click()">本地上传</el-button>
          </div>
          <el-image v-if="form.imageUrl" :src="form.imageUrl" style="width:200px;height:112px;margin-top:10px;border-radius:6px" fit="cover" />
        </el-form-item>
        <el-form-item label="跳转链接" prop="linkUrl"><el-input v-model="form.linkUrl" /></el-form-item>
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
import { getCarouselList, getCarousel, addCarousel, updateCarousel, delCarousel } from '@/api/carousel'
import { uploadImage } from '@/api/upload'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const uploadInput = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getCarouselList, getCarousel, addCarousel, updateCarousel, delCarousel,
    { title: '', imageUrl: '', linkUrl: '', orderNum: 0, status: '0' }, '',
    { title: [{ required: true, message: '标题不能为空', trigger: 'blur' }], imageUrl: [{ required: true, message: '请上传图片', trigger: 'blur' }] }
  )
fetchList()

async function handleUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const res = await uploadImage(file)
    if (res.code === 200) {
      form.imageUrl = res.data.url
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
