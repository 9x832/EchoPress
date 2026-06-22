<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="配置名称"><el-input v-model="queryParams.configName" placeholder="配置名称" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增配置</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="configId" label="ID" width="70" />
      <el-table-column prop="configName" label="名称" width="150" />
      <el-table-column prop="configKey" label="键名" width="180" />
      <el-table-column prop="configValue" label="键值" min-width="200" show-overflow-tooltip />
      <el-table-column label="类型" width="80"><template #default="{row}">{{ row.configType === 'Y' ? '系统' : '自定义' }}</template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="configName"><el-input v-model="form.configName" /></el-form-item>
        <el-form-item label="键名" prop="configKey"><el-input v-model="form.configKey" /></el-form-item>
        <el-form-item label="键值" prop="configValue"><el-input v-model="form.configValue" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="类型"><el-radio-group v-model="form.configType"><el-radio value="Y">系统</el-radio><el-radio value="N">自定义</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getConfigList, getConfig, addConfig, updateConfig, delConfig } from '@/api/config'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getConfigList, getConfig, addConfig, updateConfig, delConfig,
    { configName: '', configKey: '', configValue: '', configType: 'N' }, '',
    { configName: [{ required: true, message: '配置名称不能为空', trigger: 'blur' }], configKey: [{ required: true, message: '配置键名不能为空', trigger: 'blur' }], configValue: [{ required: true, message: '配置键值不能为空', trigger: 'blur' }] }
  )
fetchList()
</script>
