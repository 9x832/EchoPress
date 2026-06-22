<template>
  <div class="admin-layout">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '220px'" class="admin-sidebar">
        <div class="logo-box">
          <span v-show="!isCollapse">{{ siteTitle }} 后台</span>
          <span v-show="isCollapse">B</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/admin">
            <el-icon><DataAnalysis /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/admin/article">
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/category">
            <el-icon><Collection /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/tag">
            <el-icon><PriceTag /></el-icon>
            <span>标签管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/comment">
            <el-icon><ChatDotSquare /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/link">
            <el-icon><Link /></el-icon>
            <span>友链管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/carousel">
            <el-icon><PictureFilled /></el-icon>
            <span>轮播管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/moment">
            <el-icon><Clock /></el-icon>
            <span>动态管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/page">
            <el-icon><Notebook /></el-icon>
            <span>页面管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/resource">
            <el-icon><FolderOpened /></el-icon>
            <span>资源管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/config">
            <el-icon><Setting /></el-icon>
            <span>站点配置</span>
          </el-menu-item>
          <el-menu-item index="/admin/subscribe">
            <el-icon><Message /></el-icon>
            <span>订阅管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="admin-header">
          <div class="header-left">
            <el-icon class="collapse-btn" @click="toggleSidebar" :size="22">
              <Fold v-if="!isCollapse" /><Expand v-else />
            </el-icon>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-icon><UserFilled /></el-icon>
                <span class="username">{{ userStore.nickName || '管理员' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="home">访问前台</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { getPublicSiteConfig } from '@/api/config'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

const isCollapse = computed(() => appStore.sidebarCollapsed)
const activeMenu = computed(() => {
  const path = route.path
  return path.endsWith('/admin') ? '/admin' : path
})
const siteTitle = ref('EchoPress')

onMounted(async () => {
  try {
    const res = await getPublicSiteConfig()
    if (res.code === 200 && res.data) {
      siteTitle.value = res.data.site_name || 'EchoPress'
    }
  } catch {}
})

function toggleSidebar() {
  appStore.toggleSidebar()
}

function handleCommand(command) {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (command === 'home') {
    window.open('/', '_blank')
  }
}
</script>

<style scoped>
.admin-layout { height: 100vh; }
.admin-layout .el-container { height: 100%; }
.admin-sidebar { background: #304156; overflow-x: hidden; transition: width 0.3s; }
.logo-box { height: 60px; line-height: 60px; text-align: center; color: #fff; font-size: 18px; font-weight: 700; border-bottom: 1px solid rgba(255,255,255,0.1); }
.el-menu { border-right: none; }
.admin-header { background: #fff; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 1px 4px rgba(0,0,0,0.08); height: 50px; }
.header-left { display: flex; align-items: center; }
.collapse-btn { cursor: pointer; }
.header-right { display: flex; align-items: center; }
.user-info { display: flex; align-items: center; gap: 6px; cursor: pointer; color: #333; }
.username { font-size: 14px; }
.el-main { background: #f0f2f5; padding: 20px; }
</style>
