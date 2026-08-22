<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useColorMode, useLocalStorage } from '@vueuse/core'
import {
  Notivue,
  Notification,
  push,
  lightTheme,
  darkTheme,
  type NotivueItem,
  type NotivueTheme
} from 'notivue'
import type { DropdownMenuItem } from '@nuxt/ui'
import { checkBackendHealth } from './services/api'
import { logout } from './services/auth'

onMounted(async () => {
  const notification = push.promise({
    title: 'Connecting',
    message: 'Checking the server connection...',
    props: { compact: true }
  })
  const [healthy] = await Promise.all([
    checkBackendHealth(),
    new Promise((resolve) => setTimeout(resolve, 3000))
  ])
  if (healthy) {
    notification.resolve({
      title: 'Server connected',
      message: 'Everything is up and running. Enjoy!',
      props: { compact: true }
    })
  } else {
    notification.reject({
      title: 'Server not connected',
      message: 'No worries — sample data is being used for now.',
      props: { compact: true }
    })
  }
})

const route = useRoute()
const router = useRouter()

const isAuthPage = computed(() => ['/login', '/register'].includes(route.path))

const colorMode = useColorMode()

const notivueBaseTheme = computed<NotivueTheme>(() =>
  colorMode.value === 'dark' ? darkTheme : lightTheme
)

const notivueCompactTheme = computed<NotivueTheme>(() => ({
  ...notivueBaseTheme.value,
  '--nv-width': '18rem',
  '--nv-spacing': '0.4375rem',
  '--nv-radius': '0.5rem',
  '--nv-icon-size': '1rem',
  '--nv-title-size': '0.8125rem',
  '--nv-message-size': '0.75rem'
}))

function getNotivueTheme(item: NotivueItem) {
  return item.props?.compact === true ? notivueCompactTheme.value : notivueBaseTheme.value
}

const colorModeOptions = [
  { value: 'light', icon: 'i-lucide-sun', label: 'Light' },
  { value: 'dark', icon: 'i-lucide-moon', label: 'Dark' },
  { value: 'auto', icon: 'i-lucide-monitor', label: 'System' }
] as const

const colorModeIndex = computed(() =>
  Math.max(0, colorModeOptions.findIndex((option) => option.value === colorMode.store.value))
)

const colorModeIcon = computed(() => colorModeOptions[colorModeIndex.value].icon)

function cycleColorMode() {
  colorMode.store.value = colorModeOptions[(colorModeIndex.value + 1) % colorModeOptions.length].value
}

const sidebarOpen = useLocalStorage('sidebar-open', true)

const sidebarUi = {
  root: '[--sidebar-width:15.5rem] [--sidebar-width-icon:4.5rem] border-r border-(--ui-border) bg-(--ui-bg-card)',
  header: 'flex items-center gap-1.5 overflow-hidden px-4 min-h-(--ui-header-height) group-data-[state=collapsed]/sidebar:px-0 border-b border-(--ui-border)',
  body: 'flex min-h-0 flex-1 flex-col gap-4 overflow-y-auto p-3 group-data-[state=collapsed]/sidebar:overflow-hidden group-data-[state=collapsed]/sidebar:p-2'
}

interface NavItem {
  label: string
  icon: string
  to: string | { path: string; query?: Record<string, string> }
  active: boolean
}

function navItem(
  label: string,
  icon: string,
  to: string | { path: string; query?: Record<string, string> },
  activeFn?: () => boolean
): NavItem {
  return {
    label,
    icon,
    to,
    active: activeFn ? activeFn() : (typeof to === 'string' ? route.path === to : route.path === to.path && !route.query.tab)
  }
}

const mainNavItems = computed<NavItem[]>(() => [
  navItem('Dashboard',  'i-lucide-layout-dashboard', '/dashboard'),
  navItem('Catalog',    'i-lucide-book-open',         '/books'),
  navItem('Members',    'i-lucide-users',              '/members'),
  navItem('Loans',      'i-lucide-file-text',          '/borrowings'),
  navItem('Settings',   'i-lucide-settings',           '/settings')
])

function navigateQuickAction(path: string, query?: Record<string, string>) {
  router.push({ path, query: { ...query, _t: Date.now().toString() } })
}

const quickActions = [
  {
    label: 'Add New Book',
    icon: 'i-lucide-plus-square',
    action: () => navigateQuickAction('/books', { action: 'add' })
  },
  {
    label: 'Add New Member',
    icon: 'i-lucide-user-plus',
    action: () => navigateQuickAction('/members', { action: 'add' })
  }
]

function toggleSidebar() {
  sidebarOpen.value = !sidebarOpen.value
}

function closeSidebar() {
  sidebarOpen.value = false
}

const headerMenuItems: DropdownMenuItem[][] = [
  [
    {
      label: 'Profile',
      icon: 'i-lucide-circle-user',
      to: '/profile'
    },
    {
      label: 'Change Password',
      icon: 'i-lucide-lock',
      to: '/change-password'
    }
  ],
  [
    {
      label: 'Logout',
      icon: 'i-lucide-log-out',
      color: 'error',
      onSelect: () => handleLogout()
    }
  ]
]

function handleLogout() {
  logout()
  router.push('/login')
}
</script>

<template>
  <Suspense>
    <UApp>
      <div v-if="isAuthPage" class="min-h-svh w-full bg-(--ui-bg) text-default">
        <RouterView />
      </div>

      <div v-else class="flex h-svh overflow-hidden bg-(--ui-bg)">
        <USidebar
          v-model:open="sidebarOpen"
          collapsible="icon"
          :ui="sidebarUi"
        >
          <template #header="{ state }">
            <div
              v-if="state === 'collapsed'"
              class="flex w-full items-center justify-center py-2"
            >
              <button
                type="button"
                aria-label="Expand sidebar"
                class="flex items-center justify-center rounded-lg p-2 text-slate-500 hover:bg-slate-100 hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-800"
                @click="toggleSidebar"
              >
                <!-- Gold Open Book Icon -->
                <svg
                  class="size-7 text-amber-500"
                  viewBox="0 0 48 48"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M8 12C8 9.79086 9.79086 8 12 8H22C23.1046 8 24 8.89543 24 10V38C24 38.5523 23.5523 39 23 39H12C9.79086 39 8 37.2091 8 35V12Z"
                    stroke="currentColor"
                    stroke-width="3"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M40 12C40 9.79086 38.2091 8 36 8H26C24.8954 8 24 8.89543 24 10V38C24 38.5523 24.4477 39 25 39H36C38.2091 39 40 37.2091 40 35V12Z"
                    stroke="currentColor"
                    stroke-width="3"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M8 35C8 32.7909 9.79086 31 12 31H24"
                    stroke="currentColor"
                    stroke-width="3"
                    stroke-linecap="round"
                  />
                  <path
                    d="M40 35C40 32.7909 38.2091 31 36 31H24"
                    stroke="currentColor"
                    stroke-width="3"
                    stroke-linecap="round"
                  />
                </svg>
              </button>
            </div>

            <div v-else class="flex min-w-0 flex-1 items-center justify-between px-2">
              <RouterLink to="/dashboard" class="flex items-center gap-2.5 select-none">
                <!-- Warm Amber Open Book Logo -->
                <svg
                  class="size-8 text-amber-500 shrink-0"
                  viewBox="0 0 48 48"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M8 12C8 9.79086 9.79086 8 12 8H22C23.1046 8 24 8.89543 24 10V38C24 38.5523 23.5523 39 23 39H12C9.79086 39 8 37.2091 8 35V12Z"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M40 12C40 9.79086 38.2091 8 36 8H26C24.8954 8 24 8.89543 24 10V38C24 38.5523 24.4477 39 25 39H36C38.2091 39 40 37.2091 40 35V12Z"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M8 35C8 32.7909 9.79086 31 12 31H24"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                  />
                  <path
                    d="M40 35C40 32.7909 38.2091 31 36 31H24"
                    stroke="currentColor"
                    stroke-width="2.5"
                    stroke-linecap="round"
                  />
                  <line
                    x1="24"
                    y1="10"
                    x2="24"
                    y2="39"
                    stroke="currentColor"
                    stroke-width="2"
                  />
                </svg>

                <div class="flex flex-col">
                  <span class="font-serif text-[16px] font-bold tracking-[0.14em] uppercase text-brand-600 dark:text-brand-300 leading-none">
                    Athenaeum
                  </span>
                  <span class="text-[8px] font-bold uppercase tracking-[0.18em] text-slate-500 dark:text-slate-400 mt-1 leading-none">
                    Library Management System
                  </span>
                </div>
              </RouterLink>

              <UButton
                icon="i-lucide-x"
                color="neutral"
                variant="ghost"
                size="sm"
                aria-label="Close menu"
                class="lg:hidden"
                @click="closeSidebar"
              />
            </div>
          </template>

          <template #default="{ state }">
            <div class="flex flex-1 flex-col justify-between overflow-y-auto">
              <div>
                <!-- Main Navigation -->
                <ul class="space-y-1">
                  <li
                    v-for="item in mainNavItems"
                    :key="item.label"
                    :class="state === 'collapsed' ? 'flex justify-center' : ''"
                  >
                    <RouterLink
                      :to="item.to"
                      class="flex items-center gap-3 font-medium transition-colors"
                      :class="[
                        state === 'expanded'
                          ? 'rounded-lg px-3 py-2.5 text-sm'
                          : 'size-9 justify-center rounded-lg',
                        item.active
                          ? 'bg-brand-600 text-white shadow-xs dark:bg-brand-500'
                          : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900 dark:text-slate-300 dark:hover:bg-slate-800 dark:hover:text-white'
                      ]"
                      :title="state === 'collapsed' ? item.label : undefined"
                    >
                      <UIcon
                        :name="item.icon"
                        class="size-[18px] shrink-0"
                        :class="item.active ? 'text-white' : 'text-slate-500 dark:text-slate-400'"
                      />
                      <span v-if="state === 'expanded'" class="truncate">{{ item.label }}</span>
                    </RouterLink>
                  </li>
                </ul>

                <!-- Quick Actions Section -->
                <div v-if="state === 'expanded'" class="mt-6 pt-4 border-t border-slate-200 dark:border-slate-800">
                  <p class="mb-2.5 px-3 text-[10px] font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">
                    Quick Actions
                  </p>

                  <ul class="space-y-1">
                    <li v-for="action in quickActions" :key="action.label">
                      <button
                        type="button"
                        class="flex w-full items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium text-slate-600 transition-colors hover:bg-slate-100 hover:text-slate-900 dark:text-slate-300 dark:hover:bg-slate-800 dark:hover:text-white"
                        @click="action.action()"
                      >
                        <UIcon :name="action.icon" class="size-[18px] text-slate-500 dark:text-slate-400 shrink-0" />
                        <span class="truncate">{{ action.label }}</span>
                      </button>
                    </li>
                  </ul>
                </div>
              </div>

              <!-- Sidebar Footer: Library Card & Illustration -->
              <div v-if="state === 'expanded'" class="mt-8 pt-4 flex flex-col gap-3">
                <!-- Shelf decorative illustration -->
                <div class="px-2 opacity-70">
                  <svg viewBox="0 0 200 40" fill="none" xmlns="http://www.w3.org/2000/svg" class="w-full h-8 text-slate-300 dark:text-slate-700">
                    <circle cx="20" cy="28" r="8" fill="currentColor" fill-opacity="0.2" />
                    <path d="M18 20 C18 14, 14 10, 10 14 C14 18, 18 18, 18 20 Z" fill="currentColor" />
                    <path d="M22 20 C22 14, 26 10, 30 14 C26 18, 22 18, 22 20 Z" fill="currentColor" />
                    <line x1="20" y1="20" x2="20" y2="34" stroke="currentColor" stroke-width="1.5" />
                    <rect x="45" y="10" width="8" height="24" rx="1" fill="currentColor" fill-opacity="0.3" stroke="currentColor" stroke-width="1" />
                    <rect x="56" y="6" width="9" height="28" rx="1" fill="currentColor" fill-opacity="0.4" stroke="currentColor" stroke-width="1" />
                    <rect x="68" y="14" width="7" height="20" rx="1" fill="currentColor" fill-opacity="0.2" stroke="currentColor" stroke-width="1" />
                    <path d="M78 34 L88 12 L96 15 L86 34 Z" fill="currentColor" fill-opacity="0.35" stroke="currentColor" stroke-width="1" />
                    <rect x="99" y="8" width="8" height="26" rx="1" fill="currentColor" fill-opacity="0.5" stroke="currentColor" stroke-width="1" />
                    <rect x="110" y="4" width="10" height="30" rx="1" fill="currentColor" fill-opacity="0.4" stroke="currentColor" stroke-width="1" />
                    <line x1="5" y1="34" x2="195" y2="34" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" />
                  </svg>
                </div>

                <!-- Library Est badge box -->
                <div class="flex items-center gap-3 rounded-xl border border-slate-200 bg-white p-3 shadow-2xs dark:border-slate-800 dark:bg-slate-900/60">
                  <div class="flex size-9 shrink-0 items-center justify-center rounded-lg bg-slate-100 text-brand-600 dark:bg-slate-800 dark:text-brand-300">
                    <UIcon name="i-lucide-landmark" class="size-5" />
                  </div>
                  <div class="min-w-0">
                    <p class="truncate text-xs font-semibold text-slate-800 dark:text-slate-200">
                      Athenaeum Library
                    </p>
                    <p class="truncate text-[11px] text-slate-500 dark:text-slate-400">
                      Est. 1890
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </USidebar>

        <div class="flex min-w-0 flex-1 flex-col bg-(--ui-bg)">
          <UHeader :ui="{ root: '!h-16 border-b border-(--ui-border) bg-(--ui-bg-card) px-4 sm:px-6' }">
            <template #left>
              <div class="flex items-center gap-3">
                <UButton
                  icon="i-lucide-menu"
                  color="neutral"
                  variant="ghost"
                  aria-label="Toggle sidebar"
                  class="shrink-0 text-slate-600 hover:text-slate-900 dark:text-slate-300"
                  @click="toggleSidebar"
                />

                <div class="relative hidden w-80 md:block lg:w-96">
                  <UIcon
                    name="i-lucide-search"
                    class="pointer-events-none absolute left-3 top-1/2 size-[18px] -translate-y-1/2 text-slate-400"
                  />
                  <input
                    type="text"
                    placeholder="Search books, members, loans..."
                    class="h-10 w-full rounded-lg border border-slate-200 bg-white pl-9 pr-4 text-sm text-slate-800 shadow-2xs outline-none transition-colors placeholder:text-slate-400 focus:border-brand-500 focus:ring-1 focus:ring-brand-500 dark:border-slate-700 dark:bg-slate-900 dark:text-slate-100 dark:placeholder:text-slate-500"
                  >
                </div>
              </div>
            </template>

            <template #right>
              <div class="flex items-center gap-3 sm:gap-4">
                <!-- Notifications Bell with Badge -->
                <div class="relative">
                  <UButton
                    icon="i-lucide-bell"
                    color="neutral"
                    variant="ghost"
                    aria-label="Notifications"
                    class="relative text-slate-600 hover:text-slate-900 dark:text-slate-300"
                  />
                  <span class="absolute right-1.5 top-1.5 flex size-4 items-center justify-center rounded-full bg-amber-500 text-[10px] font-bold text-white shadow-xs">
                    3
                  </span>
                </div>

                <!-- Color mode toggle -->
                <UButton
                  :icon="colorModeIcon"
                  color="neutral"
                  variant="ghost"
                  aria-label="Toggle color mode"
                  class="text-slate-600 hover:text-slate-900 dark:text-slate-300"
                  @click="cycleColorMode"
                />

                <div class="h-6 w-px bg-slate-200 dark:bg-slate-800" />

                <!-- User Profile Dropdown -->
                <UDropdownMenu
                  :items="headerMenuItems"
                  :content="{ align: 'end', side: 'bottom', sideOffset: 8 }"
                  :ui="{ content: 'w-48' }"
                >
                  <button
                    type="button"
                    class="flex items-center gap-3 rounded-lg p-1 transition-colors hover:bg-slate-100 dark:hover:bg-slate-800"
                    aria-label="Account options"
                  >
                    <div class="flex size-9 items-center justify-center rounded-full bg-brand-600 text-xs font-bold text-white shadow-2xs dark:bg-brand-500">
                      AU
                    </div>
                    <div class="hidden text-left sm:block">
                      <p class="text-sm font-semibold leading-tight text-slate-800 dark:text-slate-100">
                        Admin User
                      </p>
                      <p class="text-xs leading-tight text-slate-500 dark:text-slate-400">
                        Administrator
                      </p>
                    </div>
                    <UIcon name="i-lucide-chevron-down" class="size-4 text-slate-400" />
                  </button>
                </UDropdownMenu>
              </div>
            </template>
          </UHeader>

          <main class="mx-auto flex min-h-0 w-full flex-1 flex-col overflow-y-auto px-4 py-5 sm:px-6 sm:py-6 lg:px-8">
            <RouterView />
          </main>
        </div>
      </div>
    </UApp>
  </Suspense>

  <Notivue v-slot="item">
    <Notification
      :item="item"
      :theme="getNotivueTheme(item)"
    />
  </Notivue>
</template>

<style>
.Notivue__notification[data-notivue='promise-reject'] {
  --nv-bg: var(--nv-warning-bg);
  --nv-fg: var(--nv-warning-fg);
  --nv-accent: var(--nv-warning-accent, var(--nv-global-accent));
  --nv-border: var(--nv-warning-border);
}
</style>
