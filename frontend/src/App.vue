<script setup lang="ts">
import {computed} from 'vue'
import {useRoute} from 'vue-router'
import {useLocalStorage} from '@vueuse/core'

const route = useRoute()

const sidebarOpen = useLocalStorage('sidebar-open', true)

const sidebarUi = {
  root: '[--sidebar-width:15rem] [--sidebar-width-icon:4.5rem] border-r border-(--ui-border)'
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
</script>

<template>
  <Suspense>
    <UApp>
      <div class="flex min-h-svh bg-white">
        <USidebar
          v-model:open="sidebarOpen"
          collapsible="icon"
          :ui="sidebarUi"
        >
          <template #header="{ state }">
            <div class="flex min-w-0 flex-1 items-center gap-3 px-1">
              <UIcon name="i-lucide-book-open" class="size-7 shrink-0 text-primary" />

              <div v-if="state === 'expanded'" class="min-w-0">
                <p class="truncate font-display text-[16px] font-bold leading-tight tracking-tight text-highlighted">
                  Athenaeum
                </p>
                <p class="truncate text-[10px] font-semibold uppercase tracking-[0.16em] text-muted">
                  Management
                </p>
              </div>
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
          </template>

          <template #default="{ state }">
            <nav class="flex-1 space-y-6 overflow-y-auto px-3">
              <div v-for="section in navSections" :key="section.label">
                <p
                  v-if="state === 'expanded'"
                  class="mb-2 px-2 text-[10px] font-semibold uppercase tracking-wider text-muted"
                >
                  {{ section.label }}
                </p>

                <ul class="space-y-0.5">
                  <li v-for="item in section.items" :key="item.to">
                    <RouterLink
                      :to="item.to"
                      class="flex items-center gap-3 rounded-lg py-2 transition-colors"
                      :class="[
                        state === 'expanded' ? 'px-2' : 'justify-center px-0',
                        item.active
                          ? 'bg-[#f9ebe4] font-medium text-primary'
                          : 'text-muted hover:bg-[#f5f3f2] hover:text-highlighted'
                      ]"
                      :title="state === 'collapsed' ? item.label : undefined"
                    >
                      <UIcon :name="item.icon" class="size-[18px] shrink-0" />
                      <span v-if="state === 'expanded'" class="truncate">{{ item.label }}</span>
                    </RouterLink>
                  </li>
                </ul>
              </div>
            </nav>
          </template>

          <template #footer="{ state }">
            <div class="flex min-w-0 flex-1 items-center gap-2.5 px-1">
              <UAvatar size="md" text="MF" color="primary" class="shrink-0" />

              <div v-if="state === 'expanded'" class="min-w-0 flex-1">
                <p class="truncate text-sm font-medium leading-tight text-highlighted">
                  Mohammad Fawzy
                </p>
                <p class="truncate text-xs leading-tight text-muted">
                  Administrator
                </p>
              </div>

              <UButton
                v-if="state === 'expanded'"
                icon="i-lucide-chevron-down"
                color="neutral"
                variant="ghost"
                size="sm"
                aria-label="Account options"
                class="shrink-0"
              />
            </div>
          </template>
        </USidebar>

        <div class="flex min-w-0 flex-1 flex-col bg-(--ui-bg)">
          <UHeader :ui="{ root: '!h-16 border-b border-(--ui-border) bg-white' }">
            <template #left>
              <UButton
                icon="i-lucide-menu"
                color="neutral"
                variant="ghost"
                aria-label="Open menu"
                class="shrink-0 lg:hidden"
                @click="sidebarOpen = true"
              />

              <div class="relative hidden w-full max-w-md md:block">
                <UIcon
                  name="i-lucide-search"
                  class="pointer-events-none absolute left-2.5 top-1/2 size-[18px] -translate-y-1/2 text-muted"
                />
                <input
                  type="text"
                  placeholder="Search anything..."
                  class="h-9 w-full rounded-lg border border-(--ui-border) bg-white pl-9 pr-14 text-sm text-highlighted shadow-sm outline-none transition-colors placeholder:text-muted focus:border-primary focus:ring-1 focus:ring-primary"
                >
                <span class="absolute right-2 top-1/2 -translate-y-1/2 rounded border border-(--ui-border) bg-[#f5f3f2] px-1.5 py-0.5 text-[10px] font-medium text-muted">
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
                  icon="i-lucide-sun"
                  color="neutral"
                  variant="ghost"
                  aria-label="Light mode"
                />
                <div class="flex items-center gap-2">
                  <UAvatar size="sm" text="MF" color="primary" class="shrink-0" />
                  <UIcon name="i-lucide-chevron-down" class="size-4 text-muted" />
                </div>
              </div>
            </template>
          </UHeader>

          <main class="mx-auto w-full max-w-7xl flex-1 px-4 py-6 md:px-8 md:py-8">
            <RouterView />
          </main>
        </div>
      </div>
    </UApp>
  </Suspense>
</template>
