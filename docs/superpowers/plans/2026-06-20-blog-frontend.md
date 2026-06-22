# RuoYi-Blog Vue 3 + Element Plus Frontend Implementation Plan

## Overview

Build `D:\Dev\Project\RuoYi-Blog\blog-web` as a Vue 3 + Vite + Element Plus single-page application for the RuoYi-Blog backend (running at `localhost:8080`). The frontend covers three areas: auth pages (login/register), public blog frontend (article listing, detail, timeline, links, about, search), and admin backend (dashboard + 12 CRUD modules).

### Backend API Summary

| Category | Base Path | Auth Required | Response Format |
|---|---|---|---|
| Captcha | `GET /captchaImage` | No | `{code, msg, data: {uuid, img}}` |
| Auth | `POST /api/blog/user/login` | No | `{code, msg, data: {token}}` |
| Auth | `POST /api/blog/user/register` | No | `{code, msg, data: {userId}}` |
| Admin CRUD | `/blog/admin/<module>/list` | Yes (JWT) | `{code, msg, total, rows: [...]}` |
| Admin CRUD | `/blog/admin/<module>/{id}` | Yes (JWT) | `{code, msg, data: {...}}` |
| Admin CRUD | `POST /blog/admin/<module>` | Yes (JWT) | `{code, msg}` |
| Admin CRUD | `PUT /blog/admin/<module>` | Yes (JWT) | `{code, msg}` |
| Admin CRUD | `DELETE /blog/admin/<module>/{ids}` | Yes (JWT) | `{code, msg}` |
| User info | `GET /api/blog/user/info/{userId}` | No | `{code, msg, data: {...}}` |

Pagination query params (all list endpoints): `pageNum` (default 1), `pageSize` (default 10), `orderByColumn`, `isAsc` (asc/desc).

**Note on blog frontend public read endpoints**: The current backend only provides admin CRUD endpoints (all under `/blog/admin/*`, JWT-protected). For the public blog frontend pages (home page, article detail, category/tag pages, timeline, links, about), the plan defines API modules that call the existing admin list/detail endpoints. If read-only public access is desired without requiring admin login, corresponding public `@GetMapping` endpoints would need to be added to the backend controllers outside the `/blog/admin/*` path prefix (outside the JWT interceptor pattern). For the scope of this frontend plan, the API modules use the existing admin endpoints; the blog frontend pages will require admin JWT token or the backend must be extended with public paths.

---

## Phase 1: Project Scaffold

### Task 1: Initialize Vue 3 + Vite Project and Install Dependencies

**Steps:**

- [ ] Run `npm create vite@latest blog-web -- --template vue` in `D:\Dev\Project\RuoYi-Blog`
- [ ] `cd blog-web && npm install`
- [ ] Install dependencies:

```bash
npm install vue-router@4 pinia axios element-plus @element-plus/icons-vue dayjs
npm install @kangc/v-md-editor@next @kangc/v-md-editor@next/lib/theme/github.js
npm install -D @vitejs/plugin-vue
```

**Verification**: Run `npm run dev` and confirm Vite dev server starts on `http://localhost:5173`.

### Task 2: Configure Vite Proxy and Path Aliases

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\vite.config.js`**

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true },
      '/captchaImage': { target: 'http://localhost:8080', changeOrigin: true },
      '/blog': { target: 'http://localhost:8080', changeOrigin: true }
    }
  }
})
```

**Directory structure** - Create all directories:

```
mkdir -p src/api src/stores src/router src/layouts
mkdir -p src/views/auth src/views/blog src/views/admin
mkdir -p src/components src/composables src/utils src/assets
```

**Verification**: `npm run dev` starts without errors after config changes.

### Task 3: Create Axios Instance, Auth Utils, and Format Utils

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\utils\auth.js`**

```js
const TOKEN_KEY = 'Blog-Token'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\utils\format.js`**

```js
import dayjs from 'dayjs'

export function formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  return dayjs(date).format(format)
}

export function formatDateShort(date) {
  return formatDate(date, 'YYYY-MM-DD')
}

export function formatRelative(date) {
  if (!date) return ''
  const d = dayjs(date)
  const now = dayjs()
  const diff = now.diff(d, 'hour')
  if (diff < 1) return '刚刚'
  if (diff < 24) return `${diff}小时前`
  const days = now.diff(d, 'day')
  if (days < 30) return `${days}天前`
  return formatDateShort(date)
}
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\request.js`**

```js
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'
import router from '@/router'

const request = axios.create({
  baseURL: '',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' }
})

// Request interceptor: attach Bearer token
request.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

// Response interceptor: unwrap AjaxResult / TableDataInfo
request.interceptors.response.use(
  response => {
    const res = response.data
    // TableDataInfo response (has `total` and `rows`)
    if (res.hasOwnProperty('total') && res.hasOwnProperty('rows')) {
      return res
    }
    // AjaxResult response (has `code`, `msg`, `data`)
    if (res.code === undefined) {
      return res
    }
    if (res.code === 200 || res.code === 601) {
      return res
    }
    // Token expired or unauthorized
    if (res.code === 401) {
      removeToken()
      router.push('/login')
      ElMessage.error('登录状态已过期，请重新登录')
      return Promise.reject(new Error(res.msg || '未授权'))
    }
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  error => {
    if (error.response && error.response.status === 401) {
      removeToken()
      router.push('/login')
      ElMessage.error('登录状态已过期，请重新登录')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\main.js`**

```js
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus)
app.use(createPinia())
app.use(router)
app.mount('#app')
```

**Verification**: Import statements resolve correctly, `npm run dev` starts without errors.

---

## Phase 2: Core Infrastructure

### Task 4: Create Router with All Routes and Auth Guards

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\router\index.js`**

```js
import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  // Auth pages (no layout)
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { title: '登录', noLayout: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { title: '注册', noLayout: true }
  },
  // Blog frontend (BlogLayout)
  {
    path: '/',
    component: () => import('@/layouts/BlogLayout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('@/views/blog/Home.vue'), meta: { title: '首页' } },
      { path: 'article/:id', name: 'ArticleDetail', component: () => import('@/views/blog/ArticleDetail.vue'), meta: { title: '文章详情' } },
      { path: 'category/:id', name: 'CategoryArticles', component: () => import('@/views/blog/CategoryArticles.vue'), meta: { title: '分类文章' } },
      { path: 'tag/:id', name: 'TagArticles', component: () => import('@/views/blog/TagArticles.vue'), meta: { title: '标签文章' } },
      { path: 'timeline', name: 'Timeline', component: () => import('@/views/blog/Timeline.vue'), meta: { title: '时间轴' } },
      { path: 'links', name: 'Links', component: () => import('@/views/blog/Links.vue'), meta: { title: '友情链接' } },
      { path: 'about', name: 'About', component: () => import('@/views/blog/About.vue'), meta: { title: '关于' } },
      { path: 'search', name: 'Search', component: () => import('@/views/blog/Search.vue'), meta: { title: '搜索' } }
    ]
  },
  // Admin backend (AdminLayout, requires auth)
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', name: 'Dashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: 'article', name: 'AdminArticle', component: () => import('@/views/admin/ArticleList.vue'), meta: { title: '文章管理' } },
      { path: 'category', name: 'AdminCategory', component: () => import('@/views/admin/CategoryList.vue'), meta: { title: '分类管理' } },
      { path: 'tag', name: 'AdminTag', component: () => import('@/views/admin/TagList.vue'), meta: { title: '标签管理' } },
      { path: 'comment', name: 'AdminComment', component: () => import('@/views/admin/CommentList.vue'), meta: { title: '评论管理' } },
      { path: 'link', name: 'AdminLink', component: () => import('@/views/admin/LinkList.vue'), meta: { title: '链接管理' } },
      { path: 'carousel', name: 'AdminCarousel', component: () => import('@/views/admin/CarouselList.vue'), meta: { title: '轮播图管理' } },
      { path: 'moment', name: 'AdminMoment', component: () => import('@/views/admin/MomentList.vue'), meta: { title: '动态管理' } },
      { path: 'page', name: 'AdminPage', component: () => import('@/views/admin/PageList.vue'), meta: { title: '页面管理' } },
      { path: 'resource', name: 'AdminResource', component: () => import('@/views/admin/ResourceList.vue'), meta: { title: '资源管理' } },
      { path: 'config', name: 'AdminConfig', component: () => import('@/views/admin/ConfigList.vue'), meta: { title: '配置管理' } },
      { path: 'subscribe', name: 'AdminSubscribe', component: () => import('@/views/admin/SubscribeList.vue'), meta: { title: '订阅管理' } },
      { path: 'user', name: 'AdminUser', component: () => import('@/views/admin/UserList.vue'), meta: { title: '用户管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - RuoYi-Blog` : 'RuoYi-Blog'
  if (to.meta.requiresAuth && !getToken()) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\App.vue`**

```vue
<template>
  <router-view />
</template>

<script setup>
</script>

<style>
body { margin: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; }
#app { min-height: 100vh; }
</style>
```

**Verification**: Navigate to `http://localhost:5173/` -- page should render (blog layout placeholder will show, no router errors). Navigate to `/admin` without being logged in, should redirect to `/login`.

### Task 5: Create Pinia Stores

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\stores\user.js`**

```js
import { defineStore } from 'pinia'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({ token: getToken() || '', userInfo: null }),
  actions: {
    setToken(token) { this.token = token; setToken(token) },
    removeToken() { this.token = ''; removeToken() },
    setUserInfo(info) { this.userInfo = info }
  }
})
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\stores\app.js`**

```js
import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({ sidebarCollapsed: false }),
  actions: { toggleSidebar() { this.sidebarCollapsed = !this.sidebarCollapsed } }
})
```

**Verification**: Pinia stores import without errors.

### Task 6: Create Layout Components

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\layouts\BlogLayout.vue`**

```vue
<template>
  <div class="blog-layout">
    <header class="blog-header">
      <div class="header-inner">
        <router-link to="/" class="logo">RuoYi-Blog</router-link>
        <nav class="main-nav">
          <router-link to="/">首页</router-link>
          <router-link to="/timeline">时间轴</router-link>
          <router-link to="/links">友链</router-link>
          <router-link to="/about">关于</router-link>
        </nav>
        <div class="header-actions">
          <router-link to="/search"><el-icon><Search /></el-icon></router-link>
          <router-link v-if="userStore.token" to="/admin" class="admin-link">
            <el-button type="primary" size="small">管理后台</el-button>
          </router-link>
          <router-link v-else to="/login" class="login-link">
            <el-button size="small">登录</el-button>
          </router-link>
        </div>
      </div>
    </header>
    <main class="blog-main">
      <div class="main-container"><router-view /></div>
    </main>
    <footer class="blog-footer">
      <p>Powered by RuoYi-Blog &copy; {{ new Date().getFullYear() }}</p>
    </footer>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
</script>

<style scoped>
.blog-layout { min-height: 100vh; display: flex; flex-direction: column; background: #f5f5f5; }
.blog-header { background: #fff; border-bottom: 1px solid #e8e8e8; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 1200px; margin: 0 auto; padding: 0 20px; height: 60px; display: flex; align-items: center; justify-content: space-between; }
.logo { font-size: 22px; font-weight: bold; color: #409eff; text-decoration: none; }
.main-nav { display: flex; gap: 20px; }
.main-nav a { color: #333; text-decoration: none; font-size: 15px; }
.main-nav a:hover { color: #409eff; }
.header-actions { display: flex; align-items: center; gap: 12px; }
.blog-main { flex: 1; max-width: 1200px; margin: 20px auto; padding: 0 20px; width: 100%; box-sizing: border-box; }
.blog-footer { text-align: center; padding: 20px; color: #999; font-size: 13px; border-top: 1px solid #e8e8e8; background: #fff; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\layouts\AdminLayout.vue`**

```vue
<template>
  <div class="admin-layout">
    <el-menu :default-active="route.path" class="admin-sidebar" :collapse="appStore.sidebarCollapsed"
      background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF" router>
      <div class="sidebar-header">
        <span v-if="!appStore.sidebarCollapsed">RuoYi-Blog 管理</span>
        <el-icon v-else><Setting /></el-icon>
      </div>
      <el-menu-item index="/admin"><el-icon><DataBoard /></el-icon><span>仪表盘</span></el-menu-item>
      <el-menu-item index="/admin/article"><el-icon><Document /></el-icon><span>文章管理</span></el-menu-item>
      <el-menu-item index="/admin/category"><el-icon><Collection /></el-icon><span>分类管理</span></el-menu-item>
      <el-menu-item index="/admin/tag"><el-icon><PriceTag /></el-icon><span>标签管理</span></el-menu-item>
      <el-menu-item index="/admin/comment"><el-icon><ChatDotSquare /></el-icon><span>评论管理</span></el-menu-item>
      <el-menu-item index="/admin/link"><el-icon><Link /></el-icon><span>链接管理</span></el-menu-item>
      <el-menu-item index="/admin/carousel"><el-icon><Picture /></el-icon><span>轮播图管理</span></el-menu-item>
      <el-menu-item index="/admin/moment"><el-icon><Notification /></el-icon><span>动态管理</span></el-menu-item>
      <el-menu-item index="/admin/page"><el-icon><Files /></el-icon><span>页面管理</span></el-menu-item>
      <el-menu-item index="/admin/resource"><el-icon><FolderOpened /></el-icon><span>资源管理</span></el-menu-item>
      <el-menu-item index="/admin/config"><el-icon><Tools /></el-icon><span>配置管理</span></el-menu-item>
      <el-menu-item index="/admin/subscribe"><el-icon><Message /></el-icon><span>订阅管理</span></el-menu-item>
      <el-menu-item index="/admin/user"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
    </el-menu>

    <div class="admin-main">
      <header class="admin-topbar">
        <el-button @click="appStore.toggleSidebar" text>
          <el-icon><Expand v-if="appStore.sidebarCollapsed" /><Fold v-else /></el-icon>
        </el-button>
        <div class="topbar-right">
          <router-link to="/"><el-button size="small" text><el-icon><HomeFilled /></el-icon> 返回博客</el-button></router-link>
          <el-button size="small" text @click="handleLogout"><el-icon><SwitchButton /></el-icon> 退出登录</el-button>
        </div>
      </header>
      <section class="admin-content"><router-view /></section>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

function handleLogout() {
  ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' }).then(() => {
    userStore.removeToken()
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped>
.admin-layout { display: flex; height: 100vh; overflow: hidden; }
.admin-sidebar { width: 210px; height: 100vh; overflow-y: auto; border-right: none; }
.sidebar-header { height: 56px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 16px; font-weight: bold; border-bottom: 1px solid rgba(255,255,255,0.1); }
.admin-main { flex: 1; display: flex; flex-direction: column; overflow: hidden; background: #f0f2f5; }
.admin-topbar { height: 50px; display: flex; align-items: center; justify-content: space-between; padding: 0 16px; background: #fff; border-bottom: 1px solid #e8e8e8; flex-shrink: 0; }
.topbar-right { display: flex; align-items: center; gap: 8px; }
.admin-content { flex: 1; padding: 16px; overflow-y: auto; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\layouts\BlankLayout.vue`**

```vue
<template><router-view /></template>
```

**Verification**: Navigate to `/` should render blog layout header/footer. Navigate to `/admin` while logged in should render sidebar + topbar.

---

## Phase 3: Auth Pages

### Task 7: Login Page with Captcha

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\captcha.js`**

```js
import request from './request'
export function getCaptchaImage() {
  return request({ url: '/captchaImage', method: 'get' })
}
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\auth.js`**

```js
import request from './request'
export function login(data) { return request({ url: '/api/blog/user/login', method: 'post', data }) }
export function register(data) { return request({ url: '/api/blog/user/register', method: 'post', data }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\components\CaptchaInput.vue`**

```vue
<template>
  <div class="captcha-input">
    <el-input v-model="inputCode" placeholder="请输入验证码" maxlength="6" @input="handleInput">
      <template #prefix><el-icon><Key /></el-icon></template>
    </el-input>
    <div class="captcha-image" @click="refreshCaptcha">
      <img v-if="captchaImg" :src="'data:image/jpeg;base64,' + captchaImg" alt="" />
      <span v-else>加载中...</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCaptchaImage } from '@/api/captcha'
const emit = defineEmits(['update:code', 'update:uuid'])
const inputCode = ref('')
const captchaImg = ref('')
const captchaUuid = ref('')
function refreshCaptcha() {
  getCaptchaImage().then(res => {
    if (res.data) { captchaUuid.value = res.data.uuid; captchaImg.value = res.data.img }
  })
}
function handleInput(val) { emit('update:code', val); emit('update:uuid', captchaUuid.value) }
onMounted(refreshCaptcha)
</script>

<style scoped>
.captcha-input { display: flex; gap: 10px; align-items: center; }
.captcha-image { flex-shrink: 0; width: 120px; height: 40px; cursor: pointer; border: 1px solid #dcdfe6; border-radius: 4px; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.captcha-image img { width: 100%; height: 100%; object-fit: cover; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\auth\Login.vue`**

```vue
<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="login-title">RuoYi-Blog 登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="large">
        <el-form-item prop="account">
          <el-input v-model="form.account" placeholder="用户名或邮箱" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item prop="code">
          <CaptchaInput @update:code="form.code = $event" @update:uuid="form.uuid = $event" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="submit-btn" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <router-link to="/register">还没有账号？去注册</router-link>
        <router-link to="/">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import CaptchaInput from '@/components/CaptchaInput.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({ account: '', password: '', code: '', uuid: '' })
const rules = {
  account: [{ required: true, message: '请输入用户名或邮箱', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

function handleLogin() {
  formRef.value.validate(valid => {
    if (!valid) return
    loading.value = true
    login(form).then(res => {
      if (res.code === 200 && res.data && res.data.token) {
        userStore.setToken(res.data.token)
        ElMessage.success('登录成功')
        const redirect = route.query.redirect || '/admin'
        router.push(redirect)
      } else {
        ElMessage.error(res.msg || '登录失败')
      }
    }).finally(() => { loading.value = false })
  })
}
</script>

<style scoped>
.login-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.login-card { width: 400px; padding: 40px; background: #fff; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); }
.login-title { text-align: center; margin-bottom: 30px; color: #303133; }
.submit-btn { width: 100%; }
.login-footer { display: flex; justify-content: space-between; margin-top: 16px; font-size: 13px; }
.login-footer a { color: #409eff; text-decoration: none; }
</style>
```

**Verification**: Navigate to /login, see the form with captcha image. Captcha refreshes on click.

### Task 8: Register Page with Captcha

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\auth\Register.vue`**

```vue
<template>
  <div class="register-page">
    <div class="register-card">
      <h2 class="register-title">RuoYi-Blog 注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" size="large">
        <el-form-item prop="userName"><el-input v-model="form.userName" placeholder="用户名" :prefix-icon="User" /></el-form-item>
        <el-form-item prop="nickName"><el-input v-model="form.nickName" placeholder="昵称" :prefix-icon="UserFilled" /></el-form-item>
        <el-form-item prop="email"><el-input v-model="form.email" placeholder="邮箱" :prefix-icon="Message" /></el-form-item>
        <el-form-item prop="password"><el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password /></el-form-item>
        <el-form-item prop="confirmPassword"><el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" :prefix-icon="Lock" show-password /></el-form-item>
        <el-form-item prop="code">
          <CaptchaInput @update:code="form.code = $event" @update:uuid="form.uuid = $event" />
        </el-form-item>
        <el-form-item><el-button type="primary" :loading="loading" class="submit-btn" @click="handleRegister">注册</el-button></el-form-item>
      </el-form>
      <div class="register-footer">
        <router-link to="/login">已有账号？去登录</router-link>
        <router-link to="/">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, UserFilled, Message, Lock } from '@element-plus/icons-vue'
import { register } from '@/api/auth'
import CaptchaInput from '@/components/CaptchaInput.vue'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ userName: '', nickName: '', email: '', password: '', confirmPassword: '', code: '', uuid: '' })
const validatePass2 = (rule, value, callback) => { value !== form.password ? callback(new Error('两次输入密码不一致')) : callback() }
const rules = {
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 3, max: 20, message: '用户名长度3-20字符', trigger: 'blur' }],
  nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }, { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, max: 30, message: '密码长度6-30字符', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validatePass2, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

function handleRegister() {
  formRef.value.validate(valid => {
    if (!valid) return
    loading.value = true
    register({ userName: form.userName, nickName: form.nickName, email: form.email, password: form.password, uuid: form.uuid, code: form.code })
      .then(res => {
        if (res.code === 200) { ElMessage.success('注册成功，请登录'); router.push('/login') }
        else { ElMessage.error(res.msg || '注册失败') }
      }).finally(() => { loading.value = false })
  })
}
</script>

<style scoped>
.register-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.register-card { width: 420px; padding: 40px; background: #fff; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); }
.register-title { text-align: center; margin-bottom: 30px; }
.submit-btn { width: 100%; }
.register-footer { display: flex; justify-content: space-between; margin-top: 16px; font-size: 13px; }
.register-footer a { color: #409eff; text-decoration: none; }
</style>
```

**Verification**: Navigate to /register, fill fields, verify password match validation, submit creates user redirects to /login.

---

## Phase 4: Blog Frontend

### Task 9: Home Page (Article List + Pagination + Sidebar)

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\article.js`**

```js
import request from './request'

export function getArticleList(params) { return request({ url: '/blog/admin/article/list', method: 'get', params }) }
export function getArticle(id) { return request({ url: `/blog/admin/article/${id}`, method: 'get' }) }
export function addArticle(data) { return request({ url: '/blog/admin/article', method: 'post', data }) }
export function updateArticle(data) { return request({ url: '/blog/admin/article', method: 'put', data }) }
export function delArticle(ids) { return request({ url: `/blog/admin/article/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\components\ArticleCard.vue`**

```vue
<template>
  <div class="article-card" @click="$router.push('/article/' + article.articleId)">
    <div class="card-body">
      <h3 class="card-title">{{ article.title }}</h3>
      <p class="card-summary">{{ article.summary }}</p>
      <div class="card-meta">
        <span><el-icon><Clock /></el-icon> {{ formatDate(article.publishTime) }}</span>
        <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }}</span>
        <span><el-icon><ChatDotSquare /></el-icon> {{ article.commentCount || 0 }}</span>
        <span><el-icon><ThumbsUp /></el-icon> {{ article.likeCount || 0 }}</span>
      </div>
    </div>
    <img v-if="article.cover" :src="article.cover" class="card-cover" alt="" />
  </div>
</template>

<script setup>
import { formatDate } from '@/utils/format'
defineProps({ article: { type: Object, required: true } })
</script>

<style scoped>
.article-card { display: flex; background: #fff; border-radius: 8px; padding: 20px; margin-bottom: 16px; cursor: pointer; transition: box-shadow 0.2s; gap: 16px; }
.article-card:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.1); }
.card-body { flex: 1; }
.card-title { margin: 0 0 8px; font-size: 18px; color: #303133; }
.card-summary { color: #606266; font-size: 14px; line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.card-meta { display: flex; gap: 16px; margin-top: 12px; font-size: 13px; color: #909399; }
.card-meta .el-icon { margin-right: 4px; }
.card-cover { width: 120px; height: 90px; object-fit: cover; border-radius: 4px; flex-shrink: 0; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\components\Pagination.vue`**

```vue
<template>
  <el-pagination
    v-if="total > 0"
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="[10, 20, 50, 100]"
    :total="total"
    layout="total, sizes, prev, pager, next, jumper"
    background
    @current-change="handleCurrentChange"
    @size-change="handleSizeChange"
  />
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({ total: { type: Number, default: 0 }, page: { type: Number, default: 1 }, limit: { type: Number, default: 10 } })
const emit = defineEmits(['update:page', 'update:limit', 'change'])
const currentPage = computed({ get: () => props.page, set: val => emit('update:page', val) })
const pageSize = computed({ get: () => props.limit, set: val => emit('update:limit', val) })
function handleCurrentChange(page) { emit('change', { page, limit: pageSize.value }) }
function handleSizeChange(limit) { emit('change', { page: 1, limit }) }
</script>

<style scoped>
.el-pagination { justify-content: center; margin-top: 20px; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\Home.vue`**

```vue
<template>
  <div class="home">
    <div class="home-main">
      <ArticleCard v-for="item in list" :key="item.articleId" :article="item" />
      <el-empty v-if="!loading && list.length === 0" description="暂无文章" />
      <Pagination :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize" @change="handlePageChange" />
    </div>
    <aside class="home-sidebar">
      <el-card shadow="never" class="sidebar-card">
        <template #header><span>文章分类</span></template>
        <div v-for="cat in categories" :key="cat.categoryId" class="sidebar-item" @click="$router.push('/category/' + cat.categoryId)">
          {{ cat.categoryName }}
        </div>
      </el-card>
      <el-card shadow="never" class="sidebar-card">
        <template #header><span>热门标签</span></template>
        <el-tag v-for="tag in tags" :key="tag.tagId" class="sidebar-tag" @click="$router.push('/tag/' + tag.tagId)">
          {{ tag.tagName }}
        </el-tag>
      </el-card>
      <el-card shadow="never" class="sidebar-card">
        <template #header><span>热门文章</span></template>
        <div v-for="hot in hotArticles" :key="hot.articleId" class="sidebar-item hot-item" @click="$router.push('/article/' + hot.articleId)">
          {{ hot.title }}
        </div>
      </el-card>
    </aside>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getArticleList } from '@/api/article'
import { getCategoryList } from '@/api/category'
import { getTagList } from '@/api/tag'
import ArticleCard from '@/components/ArticleCard.vue'
import Pagination from '@/components/Pagination.vue'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const categories = ref([])
const tags = ref([])
const hotArticles = ref([])
const queryParams = reactive({ pageNum: 1, pageSize: 10, orderByColumn: 'publishTime', isAsc: 'desc' })

function fetchList() {
  loading.value = true
  getArticleList(queryParams).then(res => { list.value = res.rows || []; total.value = res.total || 0 }).finally(() => { loading.value = false })
}

function fetchSidebar() {
  getCategoryList({ pageSize: 50 }).then(res => { categories.value = res.rows || [] })
  getTagList({ pageSize: 50 }).then(res => { tags.value = res.rows || [] })
  getArticleList({ pageNum: 1, pageSize: 5, orderByColumn: 'viewCount', isAsc: 'desc' }).then(res => { hotArticles.value = res.rows || [] })
}

function handlePageChange({ page, limit }) { queryParams.pageNum = page; queryParams.pageSize = limit; fetchList() }
onMounted(() => { fetchList(); fetchSidebar() })
</script>

<style scoped>
.home { display: flex; gap: 20px; }
.home-main { flex: 1; }
.home-sidebar { width: 280px; flex-shrink: 0; }
.sidebar-card { margin-bottom: 16px; }
.sidebar-item { padding: 8px 0; cursor: pointer; color: #606266; font-size: 14px; border-bottom: 1px solid #f0f0f0; }
.sidebar-item:last-child { border-bottom: none; }
.sidebar-item:hover { color: #409eff; }
.sidebar-tag { margin: 4px; cursor: pointer; }
.hot-item { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
```

**Verification**: Navigate to /, verify article cards render with data from backend, sidebar shows categories/tags/hot articles, pagination works.

### Task 10: Article Detail Page

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\ArticleDetail.vue`**

```vue
<template>
  <div class="article-detail" v-loading="loading">
    <div v-if="article" class="detail-content">
      <h1 class="detail-title">{{ article.title }}</h1>
      <div class="detail-meta">
        <span>发布于: {{ formatDate(article.publishTime) }}</span>
        <span>阅读: {{ article.viewCount || 0 }}</span>
        <span>评论: {{ article.commentCount || 0 }}</span>
      </div>
      <div class="detail-body" v-html="renderedContent"></div>
    </div>
    <el-empty v-if="!loading && !article" description="文章不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getArticle } from '@/api/article'
import { formatDate } from '@/utils/format'

const route = useRoute()
const article = ref(null)
const loading = ref(true)
const renderedContent = computed(() => {
  if (!article.value) return ''
  return (article.value.content || '').replace(/\n/g, '<br>')
})

onMounted(() => {
  const id = route.params.id
  loading.value = true
  getArticle(id).then(res => { if (res.code === 200) article.value = res.data }).finally(() => { loading.value = false })
})
</script>

<style scoped>
.article-detail { background: #fff; border-radius: 8px; padding: 32px; }
.detail-title { font-size: 28px; color: #303133; margin: 0 0 16px; }
.detail-meta { display: flex; gap: 20px; color: #909399; font-size: 14px; margin-bottom: 24px; padding-bottom: 16px; border-bottom: 1px solid #f0f0f0; }
.detail-body { line-height: 1.8; font-size: 15px; color: #303133; }
</style>
```

**Verification**: Navigate to /article/1, see article title, meta, and content.

### Task 11: Category and Tag Filtered Article Pages

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\category.js`**

```js
import request from './request'
export function getCategoryList(params) { return request({ url: '/blog/admin/category/list', method: 'get', params }) }
export function getCategory(id) { return request({ url: `/blog/admin/category/${id}`, method: 'get' }) }
export function addCategory(data) { return request({ url: '/blog/admin/category', method: 'post', data }) }
export function updateCategory(data) { return request({ url: '/blog/admin/category', method: 'put', data }) }
export function delCategory(ids) { return request({ url: `/blog/admin/category/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\tag.js`**

```js
import request from './request'
export function getTagList(params) { return request({ url: '/blog/admin/tag/list', method: 'get', params }) }
export function getTag(id) { return request({ url: `/blog/admin/tag/${id}`, method: 'get' }) }
export function addTag(data) { return request({ url: '/blog/admin/tag', method: 'post', data }) }
export function updateTag(data) { return request({ url: '/blog/admin/tag', method: 'put', data }) }
export function delTag(ids) { return request({ url: `/blog/admin/tag/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\CategoryArticles.vue`**

```vue
<template>
  <div class="category-articles">
    <h2 class="page-title">{{ categoryName || '分类文章' }}</h2>
    <ArticleCard v-for="item in list" :key="item.articleId" :article="item" />
    <el-empty v-if="!loading && list.length === 0" description="该分类暂无文章" />
    <Pagination :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize" @change="handlePageChange" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleList } from '@/api/article'
import { getCategory } from '@/api/category'
import ArticleCard from '@/components/ArticleCard.vue'
import Pagination from '@/components/Pagination.vue'

const route = useRoute()
const list = ref([])
const total = ref(0)
const loading = ref(false)
const categoryName = ref('')
const queryParams = reactive({ pageNum: 1, pageSize: 10, categoryId: undefined, orderByColumn: 'publishTime', isAsc: 'desc' })

function fetchData() {
  const id = route.params.id
  queryParams.categoryId = id
  loading.value = true
  getCategory(id).then(res => { if (res.code === 200) categoryName.value = res.data.categoryName })
  getArticleList(queryParams).then(res => { list.value = res.rows || []; total.value = res.total || 0 }).finally(() => { loading.value = false })
}

function handlePageChange({ page, limit }) { queryParams.pageNum = page; queryParams.pageSize = limit; fetchData() }
onMounted(fetchData)
watch(() => route.params.id, fetchData)
</script>

<style scoped>
.page-title { margin: 0 0 20px; font-size: 22px; color: #303133; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\TagArticles.vue`**

```vue
<template>
  <div class="tag-articles">
    <h2 class="page-title">{{ tagName || '标签文章' }}</h2>
    <ArticleCard v-for="item in list" :key="item.articleId" :article="item" />
    <el-empty v-if="!loading && list.length === 0" description="该标签暂无文章" />
    <Pagination :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize" @change="handlePageChange" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleList } from '@/api/article'
import { getTag } from '@/api/tag'
import ArticleCard from '@/components/ArticleCard.vue'
import Pagination from '@/components/Pagination.vue'

const route = useRoute()
const list = ref([])
const total = ref(0)
const loading = ref(false)
const tagName = ref('')
const queryParams = reactive({ pageNum: 1, pageSize: 10, orderByColumn: 'publishTime', isAsc: 'desc' })

function fetchData() {
  const id = route.params.id
  loading.value = true
  getTag(id).then(res => { if (res.code === 200) tagName.value = res.data.tagName })
  getArticleList(queryParams).then(res => { list.value = res.rows || []; total.value = res.total || 0 }).finally(() => { loading.value = false })
}

function handlePageChange({ page, limit }) { queryParams.pageNum = page; queryParams.pageSize = limit; fetchData() }
onMounted(fetchData)
watch(() => route.params.id, fetchData)
</script>

<style scoped>
.page-title { margin: 0 0 20px; font-size: 22px; color: #303133; }
</style>
```

**Verification**: Navigate to /category/1 and /tag/1 to see filtered article lists.

### Task 12: Timeline, Links, About, Search Pages

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\link.js`**

```js
import request from './request'
export function getLinkList(params) { return request({ url: '/blog/admin/link/list', method: 'get', params }) }
export function getLink(id) { return request({ url: `/blog/admin/link/${id}`, method: 'get' }) }
export function addLink(data) { return request({ url: '/blog/admin/link', method: 'post', data }) }
export function updateLink(data) { return request({ url: '/blog/admin/link', method: 'put', data }) }
export function delLink(ids) { return request({ url: `/blog/admin/link/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\page.js`**

```js
import request from './request'
export function getPageList(params) { return request({ url: '/blog/admin/page/list', method: 'get', params }) }
export function getPage(id) { return request({ url: `/blog/admin/page/${id}`, method: 'get' }) }
export function addPage(data) { return request({ url: '/blog/admin/page', method: 'post', data }) }
export function updatePage(data) { return request({ url: '/blog/admin/page', method: 'put', data }) }
export function delPage(ids) { return request({ url: `/blog/admin/page/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\Timeline.vue`**

```vue
<template>
  <div class="timeline" v-loading="loading">
    <h2 class="page-title">时间轴</h2>
    <div class="timeline-list" v-if="list.length > 0">
      <div v-for="article in list" :key="article.articleId" class="timeline-item">
        <div class="timeline-dot"></div>
        <div class="timeline-date">{{ formatDateShort(article.publishTime) }}</div>
        <div class="timeline-content" @click="$router.push('/article/' + article.articleId)">
          {{ article.title }}
        </div>
      </div>
    </div>
    <el-empty v-if="!loading && list.length === 0" description="暂无内容" />
    <Pagination :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize" @change="handlePageChange" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getArticleList } from '@/api/article'
import { formatDateShort } from '@/utils/format'
import Pagination from '@/components/Pagination.vue'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 50, orderByColumn: 'publishTime', isAsc: 'desc' })

function fetchList() {
  loading.value = true
  getArticleList(queryParams).then(res => { list.value = res.rows || []; total.value = res.total || 0 }).finally(() => { loading.value = false })
}

function handlePageChange({ page, limit }) { queryParams.pageNum = page; queryParams.pageSize = limit; fetchList() }
onMounted(fetchList)
</script>

<style scoped>
.timeline { background: #fff; border-radius: 8px; padding: 32px; }
.page-title { margin: 0 0 24px; }
.timeline-list { position: relative; padding-left: 20px; }
.timeline-list::before { content: ''; position: absolute; left: 7px; top: 0; bottom: 0; width: 2px; background: #e8e8e8; }
.timeline-item { position: relative; padding: 0 0 20px 24px; }
.timeline-dot { position: absolute; left: -1px; top: 6px; width: 10px; height: 10px; border-radius: 50%; background: #409eff; border: 2px solid #fff; }
.timeline-date { font-size: 13px; color: #909399; margin-bottom: 4px; }
.timeline-content { font-size: 15px; color: #303133; cursor: pointer; }
.timeline-content:hover { color: #409eff; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\Links.vue`**

```vue
<template>
  <div class="links-page" v-loading="loading">
    <h2 class="page-title">友情链接</h2>
    <div class="links-grid">
      <div v-for="link in list" :key="link.linkId" class="link-card" @click="openLink(link)">
        <img v-if="link.logo" :src="link.logo" class="link-logo" alt="" />
        <div class="link-info">
          <div class="link-name">{{ link.name }}</div>
          <div class="link-desc">{{ link.description || '' }}</div>
        </div>
      </div>
    </div>
    <el-empty v-if="!loading && list.length === 0" description="暂无友链" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getLinkList } from '@/api/link'

const list = ref([])
const loading = ref(false)
function openLink(link) { if (link.url) window.open(link.url, '_blank') }
onMounted(() => {
  loading.value = true
  getLinkList({ pageSize: 100 }).then(res => { list.value = res.rows || [] }).finally(() => { loading.value = false })
})
</script>

<style scoped>
.links-page { background: #fff; border-radius: 8px; padding: 32px; }
.page-title { margin: 0 0 24px; }
.links-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 16px; }
.link-card { display: flex; align-items: center; gap: 12px; padding: 16px; border: 1px solid #ebeef5; border-radius: 6px; cursor: pointer; transition: box-shadow 0.2s; }
.link-card:hover { box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.link-logo { width: 48px; height: 48px; border-radius: 50%; object-fit: cover; flex-shrink: 0; }
.link-name { font-size: 15px; font-weight: 500; color: #303133; }
.link-desc { font-size: 13px; color: #909399; margin-top: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\About.vue`**

```vue
<template>
  <div class="about-page" v-loading="loading">
    <h2 class="page-title">关于</h2>
    <div v-if="page" class="about-content" v-html="pageContent"></div>
    <el-empty v-if="!loading && !page" description="暂无内容" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getPageList } from '@/api/page'

const page = ref(null)
const loading = ref(false)
const pageContent = computed(() => {
  if (!page.value) return ''
  return (page.value.content || '').replace(/\n/g, '<br>')
})

onMounted(() => {
  loading.value = true
  getPageList({ pageSize: 50 }).then(res => {
    const pages = res.rows || []
    page.value = pages.find(p => p.slug === 'about') || pages[0] || null
  }).finally(() => { loading.value = false })
})
</script>

<style scoped>
.about-page { background: #fff; border-radius: 8px; padding: 32px; }
.page-title { margin: 0 0 24px; }
.about-content { line-height: 1.8; font-size: 15px; color: #303133; }
</style>
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\blog\Search.vue`**

```vue
<template>
  <div class="search-page">
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索文章..." size="large" clearable @keyup.enter="handleSearch">
        <template #append>
          <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
        </template>
      </el-input>
    </div>
    <div class="search-results" v-if="searched">
      <div v-if="list.length > 0">
        <ArticleCard v-for="item in list" :key="item.articleId" :article="item" />
        <Pagination :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize" @change="handlePageChange" />
      </div>
      <el-empty v-else-if="!loading" description="未找到相关文章" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { getArticleList } from '@/api/article'
import ArticleCard from '@/components/ArticleCard.vue'
import Pagination from '@/components/Pagination.vue'

const keyword = ref('')
const list = ref([])
const total = ref(0)
const loading = ref(false)
const searched = ref(false)
const queryParams = reactive({ pageNum: 1, pageSize: 10, orderByColumn: 'publishTime', isAsc: 'desc' })

function handleSearch() {
  if (!keyword.value.trim()) return
  searched.value = true
  queryParams.pageNum = 1
  fetchList()
}

function fetchList() {
  loading.value = true
  getArticleList({ ...queryParams, title: keyword.value.trim() }).then(res => {
    list.value = res.rows || []; total.value = res.total || 0
  }).finally(() => { loading.value = false })
}

function handlePageChange({ page, limit }) { queryParams.pageNum = page; queryParams.pageSize = limit; fetchList() }
</script>

<style scoped>
.search-bar { margin-bottom: 20px; }
</style>
```

**Verification**: Navigate to /timeline, /links, /about, /search and verify each page renders correctly.

---

## Phase 5: Admin Backend

### Task 13: Admin Dashboard

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\views\admin\Dashboard.vue`**

```vue
<template>
  <div class="dashboard">
    <h2 class="page-title">仪表盘</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticleList } from '@/api/article'
import { getUserList } from '@/api/user'
import { getCommentList } from '@/api/comment'

const statCards = ref([
  { label: '文章总数', value: 0 },
  { label: '用户总数', value: 0 },
  { label: '评论总数', value: 0 },
  { label: '总浏览量', value: 0 }
])

onMounted(() => {
  getArticleList({ pageNum: 1, pageSize: 1 }).then(res => { statCards.value[0].value = res.total || 0 })
  getUserList({ pageNum: 1, pageSize: 1 }).then(res => { statCards.value[1].value = res.total || 0 })
  getCommentList({ pageNum: 1, pageSize: 1 }).then(res => { statCards.value[2].value = res.total || 0 })
  getArticleList({ pageNum: 1, pageSize: 1000 }).then(res => {
    const articles = res.rows || []
    statCards.value[3].value = articles.reduce((sum, a) => sum + (a.viewCount || 0), 0)
  })
})
</script>

<style scoped>
.dashboard { padding: 0; }
.page-title { margin: 0 0 20px; }
.stat-card { text-align: center; margin-bottom: 20px; }
.stat-value { font-size: 36px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>
```

**Verification**: Navigate to /admin, verify stat cards load with correct counts.

### Task 14: Reusable CRUD Composable (useCrud)

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\composables\useCrud.js`**

```js
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export function useCrud(listApi, getApi, addApi, updateApi, delApi, defaultForm = {}, beforeSubmit = null) {
  const loading = ref(false)
  const list = ref([])
  const total = ref(0)
  const dialogVisible = ref(false)
  const dialogTitle = ref('')
  const isEdit = ref(false)

  const queryParams = reactive({ pageNum: 1, pageSize: 10, orderByColumn: undefined, isAsc: undefined })
  const form = reactive({ ...defaultForm })
  const formRef = ref(null)
  const idField = Object.keys(defaultForm).find(k => k.endsWith('Id')) || 'id'

  function fetchList() {
    loading.value = true
    const { pageNum, pageSize, orderByColumn, isAsc, ...filterParams } = queryParams
    const params = { pageNum, pageSize, ...filterParams }
    if (orderByColumn) params.orderByColumn = orderByColumn
    if (isAsc) params.isAsc = isAsc
    listApi(params).then(res => {
      list.value = res.rows || []
      total.value = res.total || 0
    }).finally(() => { loading.value = false })
  }

  function resetQuery() {
    queryParams.pageNum = 1
    queryParams.pageSize = 10
    queryParams.orderByColumn = undefined
    queryParams.isAsc = undefined
    Object.keys(defaultForm).forEach(k => {
      if (k in queryParams && !['pageNum', 'pageSize', 'orderByColumn', 'isAsc'].includes(k)) {
        queryParams[k] = undefined
      }
    })
    fetchList()
  }

  function resetForm() {
    Object.assign(form, { ...defaultForm })
    if (idField in form) form[idField] = undefined
  }

  function handleAdd() {
    isEdit.value = false
    dialogTitle.value = '新增'
    resetForm()
    dialogVisible.value = true
  }

  function handleEdit(row) {
    isEdit.value = true
    dialogTitle.value = '编辑'
    resetForm()
    const id = row[idField]
    getApi(id).then(res => {
      if (res.code === 200) Object.assign(form, res.data || {})
    })
    dialogVisible.value = true
  }

  function handleSubmit() {
    formRef.value.validate(valid => {
      if (!valid) return
      const submitData = beforeSubmit ? beforeSubmit({ ...form }) : { ...form }
      const apiCall = isEdit.value ? updateApi(submitData) : addApi(submitData)
      apiCall.then(res => {
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
          dialogVisible.value = false
          fetchList()
        } else {
          ElMessage.error(res.msg || '操作失败')
        }
      })
    })
  }

  function handleDelete(idOrIds) {
    const ids = Array.isArray(idOrIds) ? idOrIds.join(',') : idOrIds
    ElMessageBox.confirm('确认删除该记录吗？', '提示', {
      type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
    }).then(() => {
      delApi(ids).then(res => {
        if (res.code === 200) { ElMessage.success('删除成功'); fetchList() }
        else { ElMessage.error(res.msg || '删除失败') }
      })
    }).catch(() => {})
  }

  function handlePageChange({ page, limit }) {
    queryParams.pageNum = page
    queryParams.pageSize = limit
    fetchList()
  }

  fetchList()

  return {
    loading, list, total, queryParams,
    dialogVisible, dialogTitle, isEdit,
    form, formRef,
    fetchList, resetQuery,
    handleAdd, handleEdit, handleDelete, handleSubmit,
    handlePageChange
  }
}
```

**Verification**: Import useCrud in a test component to check compilation.

### Task 15-26: 12 Admin CRUD Modules

Each module follows the same template. Implement each by substituting the specific entity name, API calls, form fields, and table columns.

**Generic CRUD Template** (the base structure for all 12 modules):

```vue
<template>
  <div class="crud-page">
    <!-- Search form -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="名称">
          <el-input v-model="queryParams.SEARCH_FIELD" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList"><el-icon><Search /></el-icon> 搜索</el-button>
          <el-button @click="resetQuery"><el-icon><Refresh /></el-icon> 重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Table -->
    <el-card shadow="never" class="table-card">
      <div class="table-toolbar">
        <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon> 新增</el-button>
      </div>
      <el-table :data="list" v-loading="loading" stripe border>
        <!-- Dynamic columns per entity -->
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)"><el-icon><Edit /></el-icon> 编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row.ID_FIELD)"><el-icon><Delete /></el-icon> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <Pagination :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize" @change="handlePageChange" />
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <!-- Dynamic form fields per entity -->
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { useCrud } from '@/composables/useCrud'
import { getMODULEList, getMODULE, addMODULE, updateMODULE, delMODULE } from '@/api/module'
import Pagination from '@/components/Pagination.vue'

const defaultForm = { /* entity-specific fields */ }
const { loading, list, total, queryParams, dialogVisible, dialogTitle, form, formRef, fetchList, resetQuery, handleAdd, handleEdit, handleDelete, handleSubmit, handlePageChange } = useCrud(getMODULEList, getMODULE, addMODULE, updateMODULE, delMODULE, defaultForm)
const rules = { name: [{ required: true, message: '不能为空', trigger: 'blur' }] }
</script>

<style scoped>
.search-card { margin-bottom: 16px; }
.table-card { margin-bottom: 16px; }
.table-toolbar { margin-bottom: 16px; }
</style>
```

#### Per-module customization table:

| # | File | Entity Prefix | ID Field | Default Form Fields | Search Fields | Key Columns | Status Field |
|---|---|---|---|---|---|---|---|
| 15 | ArticleList.vue | Article | articleId | title, slug, summary, content, cover, categoryId, articleType=0, isTop=0, isRecommend=0, isComment=0, status=0, publishTime | title, status, articleType | title, categoryId, articleType, isTop, status, viewCount, commentCount, likeCount, publishTime | status |
| 16 | CategoryList.vue | Category | categoryId | parentId, categoryName, orderNum=0, icon, status=0 | categoryName, status | categoryName, parentId, orderNum, status | status |
| 17 | TagList.vue | Tag | tagId | tagName, color, status=0 | tagName, status | tagName, color, status | status |
| 18 | CommentList.vue | Comment | commentId | nickName, email, website, content, status=0, isTop=0 | nickName, content, status | articleId, nickName, email, content, ip, location, browser, os, status | status |
| 19 | LinkList.vue | Link | linkId | name, url, logo, description, contact, email, orderNum=0, status=0 | name, status | name, url, description, orderNum, status | status |
| 20 | CarouselList.vue | Carousel | carouselId | title, imageUrl, linkUrl, orderNum=0, status=0 | title, status | title, imageUrl, linkUrl, orderNum, status | status |
| 21 | MomentList.vue | Moment | momentId | content, images, location, status=0 | content, status | content, images, location, likeCount, status | status |
| 22 | PageList.vue | Page | pageId | title, slug, content, cover, isShow=0, orderNum=0, status=0 | title, status | title, slug, isShow, orderNum, status | status |
| 23 | ResourceList.vue | Resource | resourceId | Read-only view. Form: fileName, filePath, fileSize, fileType | fileName, fileType | fileName, filePath, fileSize, fileType, mimeType, createTime | N/A |
| 24 | ConfigList.vue | Config | configId | configName, configKey, configValue, configType | configName, configKey | configName, configKey, configValue, configType | N/A |
| 25 | SubscribeList.vue | Subscribe | subscribeId | email, status=0, verified=0 | email, status | email, status, verified, subscribeTime | status |
| 26 | UserList.vue | User | userId | nickName, email, phone, avatar, sex, website, bio, birthday, status=0 | userName, nickName, email, status | userName, nickName, email, phone, status, loginIp, loginDate, loginCount | status |

For each module:
- Create the file under `src/views/admin/`
- Import the corresponding API module (created in Task 29 below)
- Use useCrud with the correct defaultForm
- Add relevant table columns and form fields matching the entity's editable fields
- Status field uses element el-switch or el-select (0=normal, 1=disabled)

**Verification**: Navigate to each admin route and verify the table loads data from the backend, search filters work, add/edit dialog opens, and delete works.

---

## Phase 6: Polish & Integration

### Task 27: Markdown Editor Integration

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\components\MarkdownEditor.vue`**

```vue
<template>
  <div class="markdown-editor">
    <v-md-editor
      v-model="content"
      height="400px"
      left-toolbar="undo redo | bold italic strikethrough | h2 h3 h4 | ul ol | image link table | fullscreen"
      :disabled-menus="[]"
      @change="$emit('update:modelValue', content)"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor'
import '@kangc/v-md-editor/lib/style/codemirror-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index'
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css'

VMdEditor.use(githubTheme)
VMdEditor.use(createEmojiPlugin())

const props = defineProps({ modelValue: { type: String, default: '' } })
const emit = defineEmits(['update:modelValue'])
const content = ref(props.modelValue)
watch(() => props.modelValue, val => { content.value = val })
</script>
```

**Integration**: In ArticleList.vue and PageList.vue dialogs, replace the content textarea with `<MarkdownEditor v-model="form.content" />`.

### Task 28: Image Upload Component

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\components\ImageUpload.vue`**

```vue
<template>
  <div class="image-upload">
    <el-upload
      :action="uploadUrl"
      :headers="headers"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :show-file-list="false"
      accept="image/*"
    >
      <img v-if="modelValue" :src="modelValue" class="uploaded-image" />
      <el-button v-else type="primary"><el-icon><Upload /></el-icon> 上传图片</el-button>
    </el-upload>
  </div>
</template>

<script setup>
import { getToken } from '@/utils/auth'
import { ElMessage } from 'element-plus'

const uploadUrl = '/api/blog/resource/upload'
const props = defineProps({ modelValue: { type: String, default: '' } })
const emit = defineEmits(['update:modelValue'])
const headers = { Authorization: 'Bearer ' + getToken() }

function beforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) { ElMessage.error('只能上传图片文件'); return false }
  if (!isLt5M) { ElMessage.error('图片大小不能超过5MB'); return false }
  return true
}

function handleSuccess(res) {
  if (res.code === 200 && res.data) {
    const url = res.data.url || res.data.filePath || res.data
    emit('update:modelValue', url)
    ElMessage.success('上传成功')
  } else { ElMessage.error(res.msg || '上传失败') }
}

function handleError() { ElMessage.error('上传失败') }
</script>

<style scoped>
.uploaded-image { max-width: 200px; max-height: 200px; border-radius: 4px; cursor: pointer; }
</style>
```

### Task 29: Remaining API Modules

Create the missing API modules:

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\comment.js`**

```js
import request from './request'
export function getCommentList(params) { return request({ url: '/blog/admin/comment/list', method: 'get', params }) }
export function getComment(id) { return request({ url: `/blog/admin/comment/${id}`, method: 'get' }) }
export function addComment(data) { return request({ url: '/blog/admin/comment', method: 'post', data }) }
export function updateComment(data) { return request({ url: '/blog/admin/comment', method: 'put', data }) }
export function delComment(ids) { return request({ url: `/blog/admin/comment/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\carousel.js`**

```js
import request from './request'
export function getCarouselList(params) { return request({ url: '/blog/admin/carousel/list', method: 'get', params }) }
export function getCarousel(id) { return request({ url: `/blog/admin/carousel/${id}`, method: 'get' }) }
export function addCarousel(data) { return request({ url: '/blog/admin/carousel', method: 'post', data }) }
export function updateCarousel(data) { return request({ url: '/blog/admin/carousel', method: 'put', data }) }
export function delCarousel(ids) { return request({ url: `/blog/admin/carousel/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\moment.js`**

```js
import request from './request'
export function getMomentList(params) { return request({ url: '/blog/admin/moment/list', method: 'get', params }) }
export function getMoment(id) { return request({ url: `/blog/admin/moment/${id}`, method: 'get' }) }
export function addMoment(data) { return request({ url: '/blog/admin/moment', method: 'post', data }) }
export function updateMoment(data) { return request({ url: '/blog/admin/moment', method: 'put', data }) }
export function delMoment(ids) { return request({ url: `/blog/admin/moment/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\resource.js`**

```js
import request from './request'
export function getResourceList(params) { return request({ url: '/blog/admin/resource/list', method: 'get', params }) }
export function getResource(id) { return request({ url: `/blog/admin/resource/${id}`, method: 'get' }) }
export function addResource(data) { return request({ url: '/blog/admin/resource', method: 'post', data }) }
export function updateResource(data) { return request({ url: '/blog/admin/resource', method: 'put', data }) }
export function delResource(ids) { return request({ url: `/blog/admin/resource/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\config.js`**

```js
import request from './request'
export function getConfigList(params) { return request({ url: '/blog/admin/config/list', method: 'get', params }) }
export function getConfig(id) { return request({ url: `/blog/admin/config/${id}`, method: 'get' }) }
export function addConfig(data) { return request({ url: '/blog/admin/config', method: 'post', data }) }
export function updateConfig(data) { return request({ url: '/blog/admin/config', method: 'put', data }) }
export function delConfig(ids) { return request({ url: `/blog/admin/config/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\subscribe.js`**

```js
import request from './request'
export function getSubscribeList(params) { return request({ url: '/blog/admin/subscribe/list', method: 'get', params }) }
export function getSubscribe(id) { return request({ url: `/blog/admin/subscribe/${id}`, method: 'get' }) }
export function addSubscribe(data) { return request({ url: '/blog/admin/subscribe', method: 'post', data }) }
export function updateSubscribe(data) { return request({ url: '/blog/admin/subscribe', method: 'put', data }) }
export function delSubscribe(ids) { return request({ url: `/blog/admin/subscribe/${ids}`, method: 'delete' }) }
```

**File: `D:\Dev\Project\RuoYi-Blog\blog-web\src\api\user.js`**

```js
import request from './request'
export function getUserList(params) { return request({ url: '/blog/admin/user/list', method: 'get', params }) }
export function getUser(id) { return request({ url: `/blog/admin/user/${id}`, method: 'get

export function delUser(ids) { return request({ url: `/blog/admin/user/${ids}`, method: 'delete' }) }
```

**Verification**: All API imports resolve correctly, no missing module errors.

---

## Final Verification Checklist

Run through these checks before declaring the implementation complete:

- [ ] `npm run dev` starts without errors
- [ ] Login page renders with captcha image; typing credentials and captcha code + clicking "Login" redirects to admin
- [ ] Register page creates a new user and redirects to login
- [ ] Blog home page (/) shows article cards with pagination
- [ ] Sidebar shows categories, tags, and hot articles
- [ ] Article detail page (/article/:id) shows full content
- [ ] Category page (/category/:id) filters articles by category
- [ ] Tag page (/tag/:id) shows tag-filtered articles (requires backend tag filtering support)
- [ ] Timeline (/timeline) shows articles in chronological order
- [ ] Links (/links) displays friend links in grid
- [ ] About (/about) renders the about page content
- [ ] Search (/search) finds articles by keyword
- [ ] Navigating to /admin without a token redirects to /login
- [ ] Dashboard (/admin) shows stat cards with counts
- [ ] Each admin CRUD page:
  - [ ] Loads and displays paginated table
  - [ ] Search form filters results
  - [ ] "Add" button opens dialog, submitting creates entity
  - [ ] "Edit" button opens dialog with pre-filled data, submitting updates entity
  - [ ] "Delete" button shows confirmation, then deletes entity
- [ ] Logout clears token and redirects to login
- [ ] Unauthorized (401) response clears token and redirects to login

---

## Key Architectural Decisions

1. **Vite proxy for CORS**: All /api, /blog, /captchaImage paths are proxied to localhost:8080 in dev mode. The backend also has CORS fully open. In production, either keep the proxy or deploy the frontend through the same origin.

2. **useCrud composable**: All 12 admin CRUD pages use identical logic. The useCrud composable eliminates duplication. Each admin page only defines its API imports, default form fields, search fields, table columns, and validation rules. The composable handles loading state, pagination, dialog state, form submission, and delete confirmation.

3. **Token management**: JWT token lives in localStorage via utils/auth.js. The Axios request interceptor attaches Authorization: Bearer <token> to every request. The response interceptor catches 401 responses, clears the token, and redirects to /login.

4. **Router auth guard**: Vue Router's beforeEach guard checks meta.requiresAuth on route records. Admin routes (/admin/**) all have requiresAuth: true. Unauthenticated users are redirected to /login with a redirect query param for post-login navigation.

5. **Component structure**: CaptchaInput.vue, Pagination.vue, ArticleCard.vue, MarkdownEditor.vue, and ImageUpload.vue are all reusable across multiple pages. The layout components (BlogLayout, AdminLayout, BlankLayout) provide consistent chrome for each section.

6. **Blog frontend public access**: The current backend stores all read endpoints under /blog/admin/* (JWT-protected). The blog frontend pages defined in Phase 4 use the same admin list/detail APIs, which means they require admin authentication. To make them truly public, the backend needs:
   - Public @GetMapping endpoints outside /blog/admin/* path prefix
   - OR the JWT interceptor must be configured to allow certain paths
   - OR the blog frontend should pass the admin token (if the admin is logged in)

   For a production deployment, the recommended approach is to add public read-only controllers on the backend. This is a frontend-only plan and defers that backend work.

---

## Dependency Ordering Between Tasks

```
Task 1 (init)
   |
Task 2 (vite config)
   |
Task 3 (axios + utils)
   |
   +-- Task 4 (router)
   |      |
   |      +-- Task 5 (stores)
   |             |
   |             +-- Task 6 (layouts)
   |                    |
   |                    +-- Task 7-8 (auth pages)
   |                           |
   |                           +-- Task 9-12 (blog frontend)
   |                                  |
   |                                  +-- Task 13 (dashboard)
   |                                         |
   |                                         +-- Task 14 (useCrud)
   |                                                |
   |                                                +-- Task 15-26 (admin CRUD modules)
   |                                                       |
   |                                                       +-- Task 27-28 (polish)
   |                                                              |
   +--------------------------------------------------------------+
                                                                  |
                                                           Task 29 (verification)
```

Tasks 3 (API modules for article/category/tag), 9 (ArticleCard), 14 (Pagination), and 7 (CaptchaInput) should be done early as they are dependencies for multiple downstream tasks. The 12 admin CRUD modules (Tasks 15-26) are independent of each other and can be done in any order.
