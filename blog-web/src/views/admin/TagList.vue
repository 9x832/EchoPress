<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="标签名称"><el-input v-model="queryParams.tagName" placeholder="标签名称" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增标签</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="tagId" label="ID" width="70" />
      <el-table-column prop="tagName" label="标签名称" min-width="150" />
      <el-table-column label="颜色" width="100"><template #default="{row}"><el-tag :color="row.color" size="small">{{ row.tagName }}</el-tag></template></el-table-column>
      <el-table-column label="状态" width="80"><template #default="{row}">{{ row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标签名称" prop="tagName"><el-input v-model="form.tagName" /></el-form-item>
        <el-form-item label="颜色" prop="color"><el-color-picker v-model="form.color" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getTagList, getTag, addTag, updateTag, delTag } from '@/api/tag'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getTagList, getTag, addTag, updateTag, delTag,
    { tagName: '', color: '#409eff', status: '0' }, '',
    { tagName: [{ required: true, message: '标签名称不能为空', trigger: 'blur' }] }
  )
fetchList()
</script>
