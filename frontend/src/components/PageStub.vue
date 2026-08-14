<script setup lang="ts">
defineProps<{
  title: string
  description?: string
}>()
</script>

<template>
  <div class="flex flex-col gap-6">
    <div>
      <h1 class="font-display text-2xl font-semibold text-highlighted">
        {{ title }}
      </h1>
      <p v-if="description" class="mt-1 text-sm text-muted">
        {{ description }}
      </p>
    </div>

    <div class="flex flex-col items-center justify-center gap-3 rounded-lg border border-dashed border-(--ui-border-accented) bg-(--ui-bg-elevated)/40 px-6 py-16 text-center">
      <UIcon name="i-lucide-construction" class="size-8 text-dimmed" />
      <p class="text-sm font-medium text-highlighted">
        This page is under construction
      </p>
      <p class="max-w-sm text-sm text-muted">
        The Athenaeum theme is in place. Content for this section will be added as the application grows.
      </p>
    </div>
  </div>
</template>
