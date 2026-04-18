import '@/assets/main.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { createApp } from 'vue'
import App from '@/App.vue'
import 'animate.css'
import 'nprogress/nprogress.css'
import router from '@/router'
import '@/permission'
import pinia from '@/stores'
import 'viewerjs/dist/viewer.css'
import VueViewer from 'v-viewer'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(pinia)
app.use(VueViewer)

app.mount('#app')
