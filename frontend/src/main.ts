import './assets/css/main.css'

import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { routes, handleHotUpdate } from 'vue-router/auto-routes'
import { createHead } from '@unhead/vue/client'
import { createNotivue } from 'notivue'
import 'notivue/notification.css'
import 'notivue/animations.css'
import ui from '@nuxt/ui/vue-plugin'

import App from './App.vue'
import { isAuthenticated } from './services/api'

const app = createApp(App)

const head = createHead()
const router = createRouter({
  routes,
  history: createWebHistory()
})

const authPages = ['/login', '/register']

router.beforeEach((to) => {
  if (to.path === '/') {
    return { path: '/dashboard' }
  }

  const authed = isAuthenticated()
  if (authPages.includes(to.path) && authed) {
    return { path: '/dashboard' }
  }
  if (!authPages.includes(to.path) && !authed) {
    return { path: '/login' }
  }
})

app.use(head)
app.use(router)
app.use(ui)
app.use(
  createNotivue({
    position: 'bottom-right',
    notifications: {
      global: { duration: Infinity }
    }
  })
)

app.mount('#app')

if (import.meta.hot) {
  handleHotUpdate(router)
}
