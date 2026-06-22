<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="标题"><el-input v-model="queryParams.title" placeholder="页面标题" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增页面</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="pageId" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="slug" label="别名" width="120" />
      <el-table-column label="显示" width="80"><template #default="{row}">{{ row.isShow === '1' ? '是' : '否' }}</template></el-table-column>
      <el-table-column prop="orderNum" label="排序" width="70" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="别名" prop="slug"><el-input v-model="form.slug" placeholder="如 about, contact" /></el-form-item>
        <el-form-item label="内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="10" /></el-form-item>
        <el-form-item label="排序" prop="orderNum"><el-input-number v-model="form.orderNum" :min="0" /></el-form-item>
        <el-form-item label="导航显示"><el-switch v-model="form.isShow" active-value="1" inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getPageList, getPage, addPage, updatePage, delPage } from '@/api/page'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getPageList, getPage, addPage, updatePage, delPage,
    { title: '', slug: '', content: '', orderNum: 0, isShow: '1', status: '0' }, '',
    { title: [{ required: true, message: '标题不能为空', trigger: 'blur' }], slug: [{ required: true, message: '别名不能为空', trigger: 'blur' }], content: [{ required: true, message: '内容不能为空', trigger: 'blur' }] }
  )
fetchList()
</script>
