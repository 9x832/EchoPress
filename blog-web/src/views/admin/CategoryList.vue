<template>
  <div>
    <el-row style="margin-bottom:12px"><el-button type="primary" @click="handleAddWrapper">新增分类</el-button></el-row>
    <el-table :data="treeList" v-loading="loading" border stripe row-key="categoryId">
      <el-table-column label="分类名称" min-width="200">
        <template #default="{row}">
          <span :style="{ paddingLeft: (row._depth || 0) * 24 + 'px' }">
            <span v-if="row._depth > 0" style="color:#c0c4cc;margin-right:4px">&#9492;&#9472;</span>
            {{ row.categoryName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="80" />
      <el-table-column label="状态" width="80"><template #default="{row}">{{ row.status === '0' ? '正常' : '停用' }}</template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{row}"><el-button link type="primary" @click="handleEdit(row)">修改</el-button><el-button link type="danger" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级分类">
          <el-tree-select
            v-model="form.parentId"
            :data="treeData"
            :props="{ label: 'categoryName', value: 'categoryId', children: 'children' }"
            placeholder="无（顶级分类）"
            clearable
            check-strictly
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.orderNum" :min="0" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" placeholder="图标名称" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit(formRef)">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getCategoryList, getCategory, addCategory, updateCategory, delCategory } from '@/api/category'
import { useCrud } from '@/composables/useCrud'

const formRef = ref(null)
const { loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
  fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
  handleAdd, handleEdit, handleDelete, handleSubmit } = useCrud(
    getCategoryList, getCategory, addCategory, updateCategory, delCategory,
    { parentId: 0, categoryName: '', orderNum: 0, icon: '', status: '0' }, '',
    { categoryName: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }] }
  )

// Build tree data for el-tree-select
const treeData = computed(() => {
  const all = list.value || []
  const roots = all.filter(c => c.parentId === 0 || !c.parentId)
  function build(nodes) {
    return nodes.map(n => {
      const children = all.filter(c => c.parentId === n.categoryId)
      return { ...n, children: children.length ? build(children) : undefined }
    })
  }
  return build(roots)
})

// Flatten tree with depth for table display
const treeList = computed(() => {
  const all = (list.value || []).slice().sort((a, b) => (a.orderNum || 0) - (b.orderNum || 0))
  const roots = all.filter(c => c.parentId === 0 || !c.parentId)
  const result = []
  function flatten(nodes, depth) {
    for (const n of nodes) {
      result.push({ ...n, _depth: depth })
      const children = all.filter(c => c.parentId === n.categoryId)
      if (children.length) flatten(children, depth + 1)
    }
  }
  flatten(roots, 0)
  return result
})

fetchList()

function handleAddWrapper() {
  handleAdd()
  form.parentId = 0
}
</script>
