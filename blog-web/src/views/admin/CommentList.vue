<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="状态"><el-select v-model="queryParams.status" placeholder="状态" clearable style="width:120px"><el-option label="待审核" value="0" /><el-option label="已通过" value="1" /><el-option label="已拒绝" value="2" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="commentId" label="ID" width="70" />
      <el-table-column prop="nickName" label="评论者" width="120" />
      <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="articleId" label="文章ID" width="80" />
      <el-table-column label="状态" width="80"><template #default="{row}">{{ ['待审核','已通过','已拒绝'][row.status] || row.status }}</template></el-table-column>
      <el-table-column prop="createTime" label="时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">待审核</el-radio><el-radio value="1">已通过</el-radio><el-radio value="2">已拒绝</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getCommentList, getComment, addComment, updateComment, delComment } from '@/api/comment'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getCommentList, getComment, addComment, updateComment, delComment,
    { content: '', status: '1' }, 'commentId',
    { content: [{ required: true, message: '内容不能为空', trigger: 'blur' }] }
  )
fetchList()
</script>
