<template>
  <div>
    <el-table :data="list" v-loading="loading" border stripe>
      <el-table-column prop="subscribeId" label="ID" width="70" />
      <el-table-column prop="email" label="邮箱" min-width="200" />
      <el-table-column label="已验证" width="80"><template #default="{row}">{{ row.verified === '1' ? '是' : '否' }}</template></el-table-column>
      <el-table-column label="状态" width="80"><template #default="{row}">{{ row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column prop="subscribeTime" label="订阅时间" width="160" />
      <el-table-column label="操作" width="80" fixed="right">
        <template #default="{row}"><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total>queryParams.pageSize" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next,jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="justify-content:flex-end;margin-top:16px" />
  </div>
</template>

<script setup>
import { getSubscribeList, getSubscribe, addSubscribe, updateSubscribe, delSubscribe } from '@/api/subscribe'
import { useCrud } from '@/composables/useCrud'

const { loading, list, total, queryParams,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleDelete } = useCrud(
    getSubscribeList, getSubscribe, addSubscribe, updateSubscribe, delSubscribe, {}
  )
fetchList()
</script>
