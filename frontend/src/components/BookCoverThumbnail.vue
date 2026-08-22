<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    title: string
    size?: 'sm' | 'md' | 'lg'
  }>(),
  {
    size: 'md'
  }
)

const normalizedTitle = computed(() => props.title.toLowerCase().trim())

const bookTheme = computed(() => {
  const t = normalizedTitle.value
  if (t.includes('gatsby')) {
    return 'gatsby'
  }
  if (t.includes('1984')) {
    return 'nineteen-eighty-four'
  }
  if (t.includes('brief history') || t.includes('hawking')) {
    return 'brief-history'
  }
  if (t.includes('sapiens')) {
    return 'sapiens'
  }
  if (t.includes('hobbit')) {
    return 'hobbit'
  }
  if (t.includes('mockingbird')) {
    return 'mockingbird'
  }
  if (t.includes('da vinci') || t.includes('davinci')) {
    return 'davinci'
  }
  if (t.includes('pride and prejudice') || t.includes('prejudice')) {
    return 'pride'
  }
  if (t.includes('alchemist')) {
    return 'alchemist'
  }
  return 'default'
})
</script>

<template>
  <div
    class="relative select-none overflow-hidden rounded shadow-xs transition-transform hover:scale-105 shrink-0 flex items-center justify-center border border-black/10 dark:border-white/10"
    :class="[
      size === 'sm' ? 'w-8 h-11' : size === 'lg' ? 'w-12 h-16' : 'w-9 h-12'
    ]"
  >
    <!-- 1. The Great Gatsby -->
    <div
      v-if="bookTheme === 'gatsby'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#0e1e38] via-[#162a4d] to-[#0a1526] p-1 text-center"
    >
      <div class="text-[5px] font-serif font-bold uppercase tracking-wider text-amber-300/90 leading-tight">
        Gatsby
      </div>
      <div class="my-auto flex flex-col items-center">
        <!-- Celestial / Art Deco Eyes -->
        <svg class="size-4 text-amber-400" viewBox="0 0 24 16" fill="currentColor">
          <ellipse cx="6" cy="8" rx="4" ry="2.5" fill="none" stroke="currentColor" stroke-width="1.2" />
          <circle cx="6" cy="8" r="1" fill="currentColor" />
          <ellipse cx="18" cy="8" rx="4" ry="2.5" fill="none" stroke="currentColor" stroke-width="1.2" />
          <circle cx="18" cy="8" r="1" fill="currentColor" />
          <path d="M 3 4 Q 6 2 9 4 M 15 4 Q 18 2 21 4" stroke="#FDE047" stroke-width="1" fill="none" />
        </svg>
      </div>
      <div class="text-[4px] uppercase tracking-tighter text-slate-300">
        Fitzgerald
      </div>
    </div>

    <!-- 2. 1984 -->
    <div
      v-else-if="bookTheme === 'nineteen-eighty-four'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#991b1b] to-[#7f1d1d] p-1 text-center text-white"
    >
      <div class="text-[4px] font-mono uppercase tracking-widest text-red-200">
        Orwell
      </div>
      <div class="my-auto flex flex-col items-center leading-none">
        <span class="font-mono text-[9px] font-black tracking-tight text-white">1984</span>
        <svg class="size-3 text-red-200" viewBox="0 0 20 20" fill="currentColor">
          <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
          <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
        </svg>
      </div>
      <div class="h-0.5 w-full bg-amber-400/80" />
    </div>

    <!-- 3. A Brief History of Time -->
    <div
      v-else-if="bookTheme === 'brief-history'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#0b132b] via-[#1c2541] to-[#0b132b] p-1 text-center"
    >
      <div class="text-[4.5px] font-serif font-semibold text-cyan-200 leading-tight">
        A Brief History
      </div>
      <div class="my-auto flex items-center justify-center">
        <!-- Spiral galaxy ring -->
        <svg class="size-4 text-cyan-400" viewBox="0 0 24 24" fill="none">
          <circle cx="12" cy="12" r="7" stroke="currentColor" stroke-width="1" stroke-dasharray="2 1" />
          <ellipse cx="12" cy="12" rx="9" ry="3" stroke="#FDE047" stroke-width="0.8" transform="rotate(-30 12 12)" />
          <circle cx="12" cy="12" r="2" fill="#FFFFFF" />
        </svg>
      </div>
      <div class="text-[4px] uppercase text-cyan-300/80">
        Hawking
      </div>
    </div>

    <!-- 4. Sapiens -->
    <div
      v-else-if="bookTheme === 'sapiens'"
      class="relative flex h-full w-full flex-col justify-between bg-[#f7f2ea] p-1 text-center text-[#212529]"
    >
      <div class="text-[4px] font-bold uppercase tracking-widest text-[#7a2a00]">
        Harari
      </div>
      <div class="my-auto flex flex-col items-center">
        <!-- Fingerprint red circle -->
        <div class="relative flex size-4 items-center justify-center rounded-full border border-[#c95a2c] p-0.5">
          <svg class="size-3 text-[#c95a2c]" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M12 2a10 10 0 0 0-10 10c0 4.4 2.9 8.2 7 9.5" />
            <path d="M12 6a6 6 0 0 0-6 6c0 2.2 1.2 4.1 3 5.2" />
            <path d="M12 10a2 2 0 0 0-2 2c0 .7.4 1.4 1 1.7" />
          </svg>
        </div>
        <span class="mt-0.5 font-serif text-[5px] font-bold tracking-tight text-[#1E3A5F]">Sapiens</span>
      </div>
      <div class="h-0.5 w-4 mx-auto bg-[#c95a2c]" />
    </div>

    <!-- 5. The Hobbit -->
    <div
      v-else-if="bookTheme === 'hobbit'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#143d2b] to-[#0a2318] p-1 text-center text-white"
    >
      <div class="text-[5px] font-serif font-bold tracking-wide text-amber-300">
        The Hobbit
      </div>
      <div class="my-auto flex items-center justify-center">
        <!-- Mountain peaks with sun -->
        <svg class="size-4 text-amber-400" viewBox="0 0 24 24" fill="none">
          <path d="M4 18 L10 8 L14 14 L17 10 L22 18 Z" fill="#1b5139" stroke="currentColor" stroke-width="1" />
          <circle cx="16" cy="6" r="2" fill="#FDE047" />
        </svg>
      </div>
      <div class="text-[4px] uppercase text-emerald-300/80">
        Tolkien
      </div>
    </div>

    <!-- 6. To Kill a Mockingbird -->
    <div
      v-else-if="bookTheme === 'mockingbird'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#133e37] to-[#0a231f] p-1 text-center text-white"
    >
      <div class="text-[4px] font-serif uppercase tracking-tight text-teal-200">
        Harper Lee
      </div>
      <div class="my-auto flex items-center justify-center">
        <!-- Tree branch and bird -->
        <svg class="size-4 text-amber-300" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.2">
          <path d="M2 16 Q8 14 14 12 Q18 10 22 12" />
          <path d="M12 12 L14 8 Q17 6 18 9 Q16 11 14 12" fill="currentColor" />
        </svg>
      </div>
      <div class="text-[4.5px] font-serif font-semibold text-white leading-tight">
        Mockingbird
      </div>
    </div>

    <!-- 7. The Da Vinci Code -->
    <div
      v-else-if="bookTheme === 'davinci'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#1e293b] via-[#0f172a] to-[#3b0712] p-1 text-center text-white"
    >
      <div class="text-[4px] font-mono uppercase text-red-400">
        Dan Brown
      </div>
      <div class="my-auto flex items-center justify-center">
        <svg class="size-4 text-amber-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
          <circle cx="12" cy="12" r="8" />
          <polygon points="12,4 19,16 5,16" />
          <line x1="12" y1="4" x2="12" y2="20" stroke="#EF4444" stroke-width="0.8" />
        </svg>
      </div>
      <div class="text-[4.5px] font-serif font-bold text-slate-200">
        Da Vinci Code
      </div>
    </div>

    <!-- 8. Pride and Prejudice -->
    <div
      v-else-if="bookTheme === 'pride'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#701a30] to-[#4c0519] p-1 text-center text-white"
    >
      <div class="text-[4px] font-serif uppercase tracking-widest text-pink-200">
        Jane Austen
      </div>
      <div class="my-auto flex items-center justify-center">
        <!-- Floral Victorian filigree -->
        <svg class="size-4 text-amber-300" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
          <path d="M12 4 C8 8, 8 16, 12 20 C16 16, 16 8, 12 4 Z" fill="#9d174d" />
          <circle cx="12" cy="12" r="2" fill="#FDE047" />
        </svg>
      </div>
      <div class="text-[4.5px] font-serif font-semibold text-rose-100 leading-tight">
        Pride & Prejudice
      </div>
    </div>

    <!-- 9. The Alchemist -->
    <div
      v-else-if="bookTheme === 'alchemist'"
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#d97706] via-[#b45309] to-[#78350f] p-1 text-center text-white"
    >
      <div class="text-[4px] font-serif uppercase text-amber-200">
        Paulo Coelho
      </div>
      <div class="my-auto flex flex-col items-center">
        <!-- Radiant Sun & Pyramids -->
        <svg class="size-4 text-yellow-300" viewBox="0 0 24 24" fill="currentColor">
          <circle cx="12" cy="7" r="3" />
          <path d="M4 18 L10 11 L16 18 Z" fill="#78350f" stroke="#FDE047" stroke-width="0.8" />
          <path d="M13 18 L17 13 L21 18 Z" fill="#92400e" stroke="#FDE047" stroke-width="0.8" />
        </svg>
      </div>
      <div class="text-[5px] font-serif font-bold text-amber-100">
        The Alchemist
      </div>
    </div>

    <!-- 10. Default Elegant Book Cover -->
    <div
      v-else
      class="relative flex h-full w-full flex-col justify-between bg-gradient-to-b from-[#1e3a5f] to-[#112238] p-1 text-center text-white"
    >
      <div class="text-[4px] font-mono uppercase tracking-widest text-amber-400">
        Athenaeum
      </div>
      <div class="my-auto flex items-center justify-center">
        <UIcon name="i-lucide-book-marked" class="size-4 text-amber-400" />
      </div>
      <div class="truncate text-[4.5px] font-serif font-bold text-slate-100">
        {{ title }}
      </div>
    </div>
  </div>
</template>
