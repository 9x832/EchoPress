import { createRouter, createWebHistory } from 'vue-router'
import { getToken, isAdmin } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { layout: 'blank' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { layout: 'blank' }
  },
  {
    path: '/',
    component: () => import('@/layouts/BlogLayout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('@/views/blog/Home.vue') },
      { path: 'article/:id', name: 'ArticleDetail', component: () => import('@/views/blog/ArticleDetail.vue') },
      { path: 'category/:id', name: 'CategoryArticles', component: () => import('@/views/blog/CategoryArticles.vue') },
      { path: 'tag/:id', name: 'TagArticles', component: () => import('@/views/blog/TagArticles.vue') },
      { path: 'timeline', name: 'Timeline', component: () => import('@/views/blog/Timeline.vue'), meta: { requiresAuth: true } },
      { path: 'links', name: 'Links', component: () => import('@/views/blog/Links.vue') },
      { path: 'about', name: 'About', component: () => import('@/views/blog/About.vue') },
      { path: 'guestbook', name: 'Guestbook', component: () => import('@/views/blog/Page.vue'), props: { slug: 'guestbook' } },
      { path: 'page/:slug', name: 'Page', component: () => import('@/views/blog/Page.vue') },
      { path: 'search', name: 'Search', component: () => import('@/views/blog/Search.vue') },
      { path: 'moments', name: 'Moments', component: () => import('@/views/blog/Moments.vue') },
      { path: 'write', name: 'Write', component: () => import('@/views/blog/Write.vue'), meta: { requiresAuth: true } }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      { path: '', name: 'Dashboard', component: () => import('@/views/admin/Dashboard.vue') },
      { path: 'article', name: 'AdminArticle', component: () => import('@/views/admin/ArticleList.vue') },
      { path: 'category', name: 'AdminCategory', component: () => import('@/views/admin/CategoryList.vue') },
      { path: 'tag', name: 'AdminTag', component: () => import('@/views/admin/TagList.vue') },
      { path: 'comment', name: 'AdminComment', component: () => import('@/views/admin/CommentList.vue') },
      { path: 'link', name: 'AdminLink', component: () => import('@/views/admin/LinkList.vue') },
      { path: 'carousel', name: 'AdminCarousel', component: () => import('@/views/admin/CarouselList.vue') },
      { path: 'moment', name: 'AdminMoment', component: () => import('@/views/admin/MomentList.vue') },
      { path: 'page', name: 'AdminPage', component: () => import('@/views/admin/PageList.vue') },
      { path: 'resource', name: 'AdminResource', component: () => import('@/views/admin/ResourceList.vue') },
      { path: 'config', name: 'AdminConfig', component: () => import('@/views/admin/ConfigList.vue') },
      { path: 'subscribe', name: 'AdminSubscribe', component: () => import('@/views/admin/SubscribeList.vue') },
      { path: 'user', name: 'AdminUser', component: () => import('@/views/admin/UserList.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(r => r.meta.requiresAuth)
  const requiresAdmin = to.matched.some(r => r.meta.requiresAdmin)

  if (requiresAuth || requiresAdmin) {
    const token = getToken()
    if (!token) {
      next('/login?redirect=' + to.fullPath)
      return
    }
  }
  if (requiresAdmin && !isAdmin()) {
    next('/')
    return
  }
  next()
})

export default router
