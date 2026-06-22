<template>
  <div class="login-wrap">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="large">
        <el-form-item prop="account">
          <el-input v-model="form.account" placeholder="用户名/邮箱"><template #prefix><el-icon><User /></el-icon></template></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="密码"><template #prefix><el-icon><Lock /></el-icon></template></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <CaptchaInput ref="captchaRef" v-model:code="form.code" v-model:uuid="form.uuid" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="footer-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import CaptchaInput from '@/components/CaptchaInput.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref(null)
const captchaRef = ref(null)
const loading = ref(false)

const form = reactive({
  account: '',
  password: '',
  uuid: '',
  code: ''
})

const rules = {
  account: [{ required: true, message: '请输入用户名或邮箱', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await userStore.login({ ...form })
    if (res.code === 200) {
      ElMessage.success('登录成功')
      const redirect = route.query.redirect
      if (redirect && redirect !== '/login') {
        router.push(redirect)
      } else if (userStore.isAdmin) {
        router.push('/admin')
      } else {
        router.push('/')
      }
    } else {
      ElMessage.error(res.msg || '登录失败')
      captchaRef.value?.refresh()
    }
  } catch (e) {
    ElMessage.error('登录失败')
    captchaRef.value?.refresh()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrap { width: 100%; }
.login-card { width: 400px; margin: 0 auto; }
.login-card h2 { text-align: center; margin-bottom: 24px; color: #303133; }
.footer-link { text-align: center; font-size: 14px; color: #999; }
.footer-link a { color: #409eff; }
</style>
