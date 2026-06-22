<template>
  <div class="blog-layout">
    <header class="blog-header">
      <div class="container">
        <router-link to="/" class="logo">{{ siteTitle }}</router-link>
        <nav>
          <router-link to="/">首页</router-link>
          <router-link to="/timeline">时间线</router-link>
          <router-link to="/links">友情链接</router-link>
          <router-link to="/moments">瞬间</router-link>
          <router-link v-for="p in pages" :key="p.slug" :to="'/page/' + p.slug">{{ p.title }}</router-link>
          <router-link to="/about">关于</router-link>
        </nav>
        <div class="header-right">
          <router-link to="/search">搜索</router-link>
          <template v-if="isLoggedIn">
            <router-link :to="publishLink" class="btn-publish">
              <el-icon><Edit /></el-icon>
              写文章
            </router-link>
            <router-link v-if="isAdmin" to="/admin">后台</router-link>
            <span class="user-name">{{ nickName }}</span>
            <a class="btn-login" @click="handleLogout">退出</a>
          </template>
          <router-link v-else to="/login" class="btn-login">登录</router-link>
        </div>
      </div>
    </header>
    <main class="container">
      <router-view />
    </main>
    <footer class="blog-footer">
      <p>&copy; 2026 {{ siteTitle }}. {{ siteFooter }} · <a href="/blog/rss" target="_blank" class="rss-link">RSS</a></p>
    </footer>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getPublicSiteConfig } from '@/api/config'
import { getPublicPageList } from '@/api/page'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => !!userStore.token)
const isAdmin = computed(() => userStore.isAdmin)
const nickName = computed(() => userStore.nickName || '')
const publishLink = computed(() => '/write')
const siteTitle = ref('EchoPress')
const siteFooter = ref('Powered by EchoPress')
const pages = ref([])

onMounted(async () => {
  try {
    const res = await getPublicSiteConfig()
    if (res.code === 200 && res.data) {
      siteTitle.value = res.data.site_name || 'EchoPress'
      siteFooter.value = res.data.site_footer || 'Powered by EchoPress'
      document.title = siteTitle.value || 'EchoPress'
    }
  } catch {}
  try {
    const res = await getPublicPageList()
    if (res.code === 200) pages.value = res.data || []
  } catch {}
})

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.blog-layout { min-height: 100vh; display: flex; flex-direction: column; }
.blog-header { background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,0.08); position: sticky; top: 0; z-index: 100; }
.container { width: min(1100px, 100%); margin: 0 auto; padding: 0 20px; box-sizing: border-box; }
.blog-header .container { display: flex; align-items: center; height: 60px; gap: 30px; }
.logo { font-size: 22px; font-weight: 700; color: #409eff; }
nav { display: flex; gap: 20px; flex: 1; }
nav a { color: #555; transition: color 0.2s; }
nav a:hover, nav a.router-link-exact-active { color: #409eff; }
.header-right { display: flex; gap: 15px; align-items: center; }
.btn-login { padding: 6px 16px; background: #409eff; color: #fff; border-radius: 4px; }
.btn-publish { display: inline-flex; align-items: center; gap: 4px; padding: 7px 18px; background: #e6a23c; color: #fff; border-radius: 4px; font-size: 14px; font-weight: 500; transition: background 0.2s; }
.btn-publish:hover { background: #cf9236; color: #fff; }
.blog-layout main { flex: 1; padding: 30px 0; }
.blog-footer { text-align: center; padding: 20px; color: #999; font-size: 14px; border-top: 1px solid #eee; }
.rss-link { color: #e6a23c; }
</style>
