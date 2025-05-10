import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


import App from './App.vue'
// 尝试检查 './router' 模块是否存在，如果不存在，需要创建该模块。
// 假设已创建，为解决类型声明问题，可使用 @ts-ignore 临时忽略类型检查，但不推荐长期使用。
// @ts-ignore
import router from './router'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }

app.use(ElementPlus)
app.use(createPinia())
app.use(router)

app.mount('#app')
