import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import VMdEditor from '@kangc/v-md-editor'
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import '@kangc/v-md-editor/lib/style/preview.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github'
import '@kangc/v-md-editor/lib/theme/style/github.css'
import hljs from 'highlight.js'
import App from './App.vue'
import router from './router'
import pinia from './stores'
import './assets/main.css'

VMdEditor.use(githubTheme, {
  Hljs: hljs,
})
VMdPreview.use(githubTheme, {
  Hljs: hljs,
})

const app = createApp(App)
app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.use(VMdEditor)
app.use(VMdPreview)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.mount('#app')
