<template>
  <div class="captcha-input">
    <el-input v-model="code" placeholder="验证码" :maxlength="4" style="flex:1" />
    <img :src="imgData" @click="refresh" class="captcha-img" title="点击刷新" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getCaptcha } from '@/api/captcha'

const code = ref('')
const uuid = ref('')
const imgData = ref('')

const emit = defineEmits(['update:code', 'update:uuid'])

watch(code, (val) => emit('update:code', val))

async function refresh() {
  code.value = ''
  try {
    const res = await getCaptcha()
    if (res.code === 200) {
      uuid.value = res.data.uuid
      imgData.value = 'data:image/jpeg;base64,' + res.data.img
      emit('update:uuid', uuid.value)
    }
  } catch (e) {
    console.error('获取验证码失败', e)
  }
}

onMounted(() => {
  refresh()
})

defineExpose({ refresh })
</script>

<style scoped>
.captcha-input {
  display: flex;
  gap: 10px;
  align-items: center;
}
.captcha-img {
  height: 40px;
  width: 120px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}
</style>
