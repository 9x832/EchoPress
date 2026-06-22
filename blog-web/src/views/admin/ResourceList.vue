<template>
  <div>
    <el-form :inline="true" :model="queryParams" size="default">
      <el-form-item label="文件名"><el-input v-model="queryParams.fileName" placeholder="文件名" clearable @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="类型"><el-select v-model="queryParams.fileType" placeholder="类型" clearable style="width:100px"><el-option label="图片" value="image" /><el-option label="文件" value="file" /><el-option label="视频" value="video" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="resourceId" label="ID" width="70" />
      <el-table-column prop="fileName" label="文件名" min-width="180" show-overflow-tooltip />
      <el-table-column prop="fileType" label="类型" width="80" />
      <el-table-column prop="fileSize" label="大小" width="100"><template #default="{row}">{{ (row.fileSize / 1024).toFixed(1) }}KB</template></el-table-column>
      <el-table-column prop="extension" label="扩展名" width="80" />
      <el-table-column prop="createTime" label="上传时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="copyUrl(row.filePath)">复制</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { getResourceList, getResource, addResource, updateResource, delResource } from '@/api/resource'
import { useCrud } from '@/composables/useCrud'

const { loading, list, total, queryParams,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleDelete } = useCrud(
    getResourceList, getResource, addResource, updateResource, delResource, {}
  )
fetchList()

function copyUrl(url) {
  navigator.clipboard.writeText(url).then(() => ElMessage.success('已复制'))
}
</script>
