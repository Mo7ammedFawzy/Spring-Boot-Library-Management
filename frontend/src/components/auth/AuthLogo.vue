<template>
  <RouterLink
    to="/login"
    class="inline-flex items-center gap-3.5 group select-none"
  >
    <div class="relative flex items-center justify-center">
      <!-- Open Book Icon matching design -->
      <svg
        class="w-10 h-10 text-primary transition-transform duration-200 group-hover:scale-105"
        viewBox="0 0 48 48"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <!-- Left Book Page -->
        <path
          d="M8 12C8 9.79086 9.79086 8 12 8H22C23.1046 8 24 8.89543 24 10V38C24 38.5523 23.5523 39 23 39H12C9.79086 39 8 37.2091 8 35V12Z"
          stroke="currentColor"
          stroke-width="2.5"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
        <!-- Right Book Page -->
        <path
          d="M40 12C40 9.79086 38.2091 8 36 8H26C24.8954 8 24 8.89543 24 10V38C24 38.5523 24.4477 39 25 39H36C38.2091 39 40 37.2091 40 35V12Z"
          stroke="currentColor"
          stroke-width="2.5"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
        <!-- Bottom Curves for Book Depth -->
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
        <!-- Center Spine Line -->
        <line
          x1="24"
          y1="10"
          x2="24"
          y2="39"
          stroke="currentColor"
          stroke-width="2"
        />
      </svg>
    </div>

    <div class="flex flex-col">
      <span class="font-serif text-[23px] font-bold tracking-tight text-default leading-none">
        Athenaeum
      </span>
      <span class="text-[10px] font-bold uppercase tracking-[0.24em] text-dimmed mt-1.5 leading-none">
        Management
      </span>
    </div>
  </RouterLink>
</template>
