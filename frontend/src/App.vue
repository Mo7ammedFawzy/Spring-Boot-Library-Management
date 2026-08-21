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
  root: '[--sidebar-width:15rem] [--sidebar-width-icon:4.5rem] border-r border-(--ui-border)',
  header: 'flex items-center gap-1.5 overflow-hidden px-4 min-h-(--ui-header-height) group-data-[state=collapsed]/sidebar:px-0',
  body: 'flex min-h-0 flex-1 flex-col gap-4 overflow-y-auto p-4 group-data-[state=collapsed]/sidebar:overflow-hidden group-data-[state=collapsed]/sidebar:p-2'
}

const navItem = (label: string, icon: string, to: string) => ({
  label,
  icon,
  to,
  active: route.path === to
})

const navSections = computed(() => [
  {
    label: 'Main',
    items: [
      navItem('Dashboard', 'i-lucide-home', '/dashboard'),
      navItem('Books', 'i-lucide-book-open', '/books'),
      navItem('Members', 'i-lucide-users', '/members'),
      navItem('Borrowings', 'i-lucide-arrow-left-right', '/borrowings')
    ]
  },
  {
    label: 'Library',
    items: [
      navItem('Authors', 'i-lucide-user', '/authors'),
      navItem('Categories', 'i-lucide-tag', '/categories')
    ]
  },
  {
    label: 'System',
    items: [
      navItem('Settings', 'i-lucide-settings', '/settings')
    ]
  }
])

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
              class="flex w-full items-center justify-center"
            >
              <UButton
                icon="i-lucide-chevrons-left"
                color="neutral"
                variant="ghost"
                size="sm"
                aria-label="Toggle sidebar"
                @click="toggleSidebar"
              />
            </div>

            <div v-else class="flex min-w-0 flex-1 items-center gap-3 px-1">
              <UAvatar size="md" text="MF" color="primary" class="shrink-0" />

              <div class="min-w-0">
                <p class="truncate text-sm font-medium leading-tight text-highlighted">
                  Mohammad Fawzy
                </p>
                <p class="truncate text-xs leading-tight text-muted">
                  Administrator
                </p>
              </div>

              <div class="flex shrink-0 items-center gap-1">
                <UButton
                  icon="i-lucide-chevrons-left"
                  color="neutral"
                  variant="ghost"
                  size="sm"
                  aria-label="Toggle sidebar"
                  class="hidden lg:inline-flex"
                  @click="toggleSidebar"
                />
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
            </div>
          </template>

          <template #default="{ state }">
            <nav
              class="flex-1 space-y-6 overflow-y-auto"
              :class="state === 'expanded' ? 'px-3' : 'px-0'"
            >
              <div v-for="section in navSections" :key="section.label">
                <p
                  v-if="state === 'expanded'"
                  class="mb-2 px-2 text-[10px] font-semibold uppercase tracking-wider text-muted"
                >
                  {{ section.label }}
                </p>

                <ul class="space-y-0.5">
                  <li
                    v-for="item in section.items"
                    :key="item.to"
                    :class="state === 'collapsed' ? 'flex justify-center' : ''"
                  >
                    <RouterLink
                      :to="item.to"
                      class="flex items-center gap-3 transition-colors"
                      :class="[
                        state === 'expanded'
                          ? 'rounded-lg px-3 py-2'
                          : 'size-9 justify-center rounded-full',
                        item.active
                          ? 'bg-(--ui-nav-active) text-primary'
                          : 'text-muted hover:bg-(--ui-bg-accented) hover:text-highlighted'
                      ]"
                      :title="state === 'collapsed' ? item.label : undefined"
                    >
                      <UIcon :name="item.icon" class="size-[16px] shrink-0" />
                      <span v-if="state === 'expanded'" class="truncate">{{ item.label }}</span>
                    </RouterLink>
                  </li>
                </ul>
              </div>
            </nav>
          </template>

          <template #footer="{ state }">
            <div class="flex min-w-0 flex-1 items-center px-1">
              <UButton
                color="error"
                variant="ghost"
                icon="i-lucide-log-out"
                aria-label="Logout"
                class="!h-auto !w-full !gap-2.5 !rounded-lg !px-1.5 !py-2"
                :class="state === 'collapsed' ? '!justify-center' : '!justify-start'"
                @click="handleLogout"
              >
                <span v-if="state === 'expanded'" class="text-sm font-medium">
                  Logout
                </span>
              </UButton>
            </div>
          </template>
        </USidebar>

        <div class="flex min-w-0 flex-1 flex-col bg-(--ui-bg)">
          <UHeader :ui="{ root: '!h-12 border-b border-(--ui-border) bg-(--ui-bg-card)' }">
            <template #left>
              <div class="relative hidden w-full max-w-md md:block">
                <UIcon
                  name="i-lucide-search"
                  class="pointer-events-none absolute left-2.5 top-1/2 size-[18px] -translate-y-1/2 text-muted"
                />
                <input
                  type="text"
                  placeholder="Search anything..."
                  class="h-8 w-full rounded-lg border border-(--ui-border) bg-(--ui-bg-card) pl-9 pr-14 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
                >
                <span class="absolute right-2 top-1/2 -translate-y-1/2 rounded border border-(--ui-border) bg-(--ui-bg-accented) px-1.5 py-0.5 text-[10px] font-medium text-muted">
                  Ctrl K
                </span>
              </div>
            </template>

            <template #right>
              <div class="flex items-center gap-4">
                <UButton
                  icon="i-lucide-bell"
                  color="neutral"
                  variant="ghost"
                  aria-label="Notifications"
                />
                <UButton
                  :icon="colorModeIcon"
                  color="neutral"
                  variant="ghost"
                  aria-label="Toggle color mode"
                  @click="cycleColorMode"
                />

                <UDropdownMenu
                  :items="headerMenuItems"
                  :content="{ align: 'end', side: 'bottom', sideOffset: 8 }"
                  :ui="{ content: 'w-48' }"
                >
                  <UButton
                    color="neutral"
                    variant="ghost"
                    aria-label="Account options"
                    class="!h-auto !gap-2 !px-1.5 !py-1"
                  >
                    <UAvatar size="sm" text="MF" color="primary" class="shrink-0" />
                    <UIcon name="i-lucide-chevron-down" class="size-4 shrink-0 text-muted" />
                  </UButton>
                </UDropdownMenu>
              </div>
            </template>
          </UHeader>

          <main class="mx-auto flex min-h-0 w-full max-w-7xl flex-1 flex-col px-4 py-3 md:px-6 md:py-4">
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
