import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export function useCrud(getListFn, getOneFn, addFn, updateFn, delFn, defaultForm = {}, idField = '', ruleDefs = {}) {
  const loading = ref(false)
  const list = ref([])
  const total = ref(0)
  const dialogVisible = ref(false)
  const dialogTitle = ref('')
  const isEdit = ref(false)
  const form = reactive({ ...defaultForm })
  const queryParams = reactive({ pageNum: 1, pageSize: 10 })
  const rules = reactive({ ...ruleDefs })

  async function fetchList() {
    loading.value = true
    try {
      const res = await getListFn(queryParams)
      if (res.code === 200) {
        list.value = res.rows || []
        total.value = res.total || 0
      }
    } finally {
      loading.value = false
    }
  }

  function handleQuery() {
    queryParams.pageNum = 1
    fetchList()
  }

  function resetQuery() {
    Object.keys(queryParams).forEach(k => {
      if (k !== 'pageNum' && k !== 'pageSize') {
        queryParams[k] = undefined
      }
    })
    handleQuery()
  }

  function handleSizeChange(val) {
    queryParams.pageSize = val
    fetchList()
  }

  function handleCurrentChange(val) {
    queryParams.pageNum = val
    fetchList()
  }

  function getIdField(row) {
    if (idField) return idField
    if (row.__idField) return row.__idField
    const idKey = Object.keys(row).find(k => /[a-z]Id$/i.test(k))
    return idKey || Object.keys(row)[0]
  }

  function handleAdd() {
    isEdit.value = false
    dialogTitle.value = '新增'
    Object.keys(defaultForm).forEach(k => { form[k] = defaultForm[k] })
    // clear any id field still on the form from a previous edit
    for (const k of Object.keys(form)) {
      if (!(k in defaultForm)) delete form[k]
    }
    dialogVisible.value = true
  }

  async function handleEdit(row) {
    isEdit.value = true
    dialogTitle.value = '修改'
    const idField = getIdField(row)
    try {
      const res = await getOneFn(row[idField])
      if (res.code === 200 && res.data) {
        Object.keys(defaultForm).forEach(k => { form[k] = res.data[k] ?? defaultForm[k] })
        form[idField] = res.data[idField]
        dialogVisible.value = true
      } else {
        ElMessage.error(res.msg || '获取数据失败')
      }
    } catch {
      ElMessage.error('获取数据失败')
    }
  }

  async function handleDelete(row) {
    const idField = getIdField(row)
    try {
      await ElMessageBox.confirm('确定要删除吗？', '提示', { type: 'warning' })
      const res = await delFn(row[idField])
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch {}
  }

  async function handleSubmit(formEl) {
    if (typeof formEl === 'number' || typeof formEl === 'string') { fetchList(); return }
    const formComp = formEl?.value || formEl
    if (!formComp) { fetchList(); return }
    try {
      await formComp.validate()
    } catch {
      return
    }
    try {
      const res = isEdit.value ? await updateFn({ ...form }) : await addFn({ ...form })
      if (res.code === 200) {
        ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
        dialogVisible.value = false
        fetchList()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    } catch {
      ElMessage.error('操作失败')
    }
  }

  return {
    loading, list, total, queryParams, dialogVisible, dialogTitle, isEdit, form, rules,
    fetchList, handleQuery, resetQuery, handleSizeChange, handleCurrentChange,
    handleAdd, handleEdit, handleDelete, handleSubmit
  }
}
