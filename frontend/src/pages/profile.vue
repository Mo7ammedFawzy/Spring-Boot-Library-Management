<template>
  <div class="flex min-h-0 flex-1 flex-col">
    <div class="mb-3">
      <div class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 class="font-display text-[28px] font-semibold leading-9 tracking-tight text-highlighted">
            My Profile
          </h1>
          <p class="mt-1 text-sm text-muted">
            Your account details and role in Athenaeum.
          </p>
        </div>
      </div>
    </div>

    <div class="flex min-h-0 flex-1 flex-col rounded-xl bg-(--ui-bg-card) shadow-sm ring-1 ring-(--ui-border)">
      <div class="flex items-center gap-4 border-b border-(--ui-border) p-4">
        <UAvatar size="xl" text="MF" color="primary" class="shrink-0" />
        <div class="min-w-0">
          <p class="truncate font-display text-[18px] font-semibold leading-tight text-highlighted">
            Mohammad Fawzy
          </p>
          <p class="mt-0.5 text-sm text-muted">
            Administrator
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
