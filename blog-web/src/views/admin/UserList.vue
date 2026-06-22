<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="用户名"><el-input v-model="queryParams.userName" placeholder="用户名" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAdd">新增用户</el-button></el-row>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="userId" label="ID" width="70" />
      <el-table-column prop="userName" label="用户名" width="120" />
      <el-table-column prop="nickName" label="昵称" width="120" />
      <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
      <el-table-column label="类型" width="80"><template #default="{row}">{{ { '00':'普通','01':'博主','02':'管理' }[row.userType] || row.userType }}</template></el-table-column>
      <el-table-column label="状态" width="80"><template #default="{row}">{{ row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column prop="articleCount" label="文章数" width="80" />
      <el-table-column prop="createTime" label="注册时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="userName"><el-input v-model="form.userName" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" placeholder="留空则不修改" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickName" /></el-form-item>
        <el-form-item label="邮箱" prop="email"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="类型"><el-select v-model="form.userType" style="width:100%"><el-option label="普通用户" value="00" /><el-option label="博主" value="01" /><el-option label="管理员" value="02" /></el-select></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getUserList, getUser, addUser, updateUser, delUser } from '@/api/user'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getUserList, getUser, addUser, updateUser, delUser,
    { userName: '', password: '', nickName: '', email: '', userType: '00', status: '0' }, 'userId',
    { userName: [{ required: true, message: '用户名不能为空', trigger: 'blur' }], email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }] }
  )
fetchList()
</script>
