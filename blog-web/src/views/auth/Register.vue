<template>
  <div class="register-wrap">
    <el-card class="register-card">
      <h2>注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="large">
        <el-form-item prop="userName">
          <el-input v-model="form.userName" placeholder="用户名"><template #prefix><el-icon><User /></el-icon></template></el-input>
        </el-form-item>
        <el-form-item prop="nickName">
          <el-input v-model="form.nickName" placeholder="昵称" prefix-icon="Postcard" />
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="邮箱" prefix-icon="Message" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="密码" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password placeholder="确认密码" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item prop="code">
          <CaptchaInput ref="captchaRef" v-model:code="form.code" v-model:uuid="form.uuid" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%" @click="handleRegister">注册</el-button>
        </el-form-item>
      </el-form>
      <div class="footer-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/auth'
import { ElMessage } from 'element-plus'
import CaptchaInput from '@/components/CaptchaInput.vue'

const router = useRouter()
const formRef = ref(null)
const captchaRef = ref(null)
const loading = ref(false)

const form = reactive({
  userName: '',
  nickName: '',
  email: '',
  password: '',
  confirmPassword: '',
  uuid: '',
  code: ''
})

const validatePass = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 2, max: 30, message: '2-30个字符', trigger: 'blur' }],
  nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }, { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '至少6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validatePass, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await register({
      userName: form.userName,
      nickName: form.nickName,
      email: form.email,
      password: form.password,
      uuid: form.uuid,
      code: form.code
    })
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '注册失败')
      captchaRef.value?.refresh()
    }
  } catch (e) {
    ElMessage.error('注册失败')
    captchaRef.value?.refresh()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-wrap { width: 100%; }
.register-card { width: 420px; margin: 0 auto; }
.register-card h2 { text-align: center; margin-bottom: 24px; color: #303133; }
.footer-link { text-align: center; font-size: 14px; color: #999; }
.footer-link a { color: #409eff; }
</style>
