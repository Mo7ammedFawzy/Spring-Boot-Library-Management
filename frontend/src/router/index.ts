import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
      meta: { guestOnly: true },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { guestOnly: true },
    },
    {
      path: '/',
      component: () => import('../components/AppLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: { name: 'books' } },
        {
          path: 'books',
          name: 'books',
          component: () => import('../views/BooksView.vue'),
        },
        {
          path: 'authors',
          name: 'authors',
          component: () => import('../views/AuthorsView.vue'),
        },
        {
          path: 'categories',
          name: 'categories',
          component: () => import('../views/CategoriesView.vue'),
        },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: { name: 'books' } },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  if (to.meta.guestOnly && auth.isAuthenticated) {
    return { name: 'books' }
  }
})

export default router
