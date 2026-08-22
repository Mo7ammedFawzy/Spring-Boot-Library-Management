<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { useColorMode, useResizeObserver } from '@vueuse/core'
import * as echarts from 'echarts'
import BookCoverThumbnail from '../components/BookCoverThumbnail.vue'

const colorMode = useColorMode()

// Time Range Selector for Library Activity Chart
const selectedPeriod = ref('This Month')
const periodOptions = ['This Week', 'This Month', 'This Quarter', 'This Year']

// Top Metrics Summary Data
const metrics = [
  {
    id: 'available-copies',
    label: 'Available Copies',
    value: '842',
    subtext: '67.6% of total',
    dotColor: 'bg-[#2E7D32] dark:bg-green-400',
    iconBg: 'bg-[#EAF5EC] dark:bg-green-950/60',
    iconColor: 'text-[#2E7D32] dark:text-green-300',
    icon: 'i-lucide-book-check'
  },
  {
    id: 'active-loans',
    label: 'Active Loans',
    value: '156',
    subtext: 'Currently borrowed',
    dotColor: 'bg-amber-500 dark:bg-amber-400',
    iconBg: 'bg-[#FEF6E7] dark:bg-amber-950/60',
    iconColor: 'text-amber-600 dark:text-amber-300',
    icon: 'i-lucide-refresh-cw'
  },
  {
    id: 'overdue-loans',
    label: 'Overdue Loans',
    value: '23',
    subtext: 'Need attention',
    dotColor: 'bg-[#C0392B] dark:bg-red-400',
    iconBg: 'bg-[#FDF0EE] dark:bg-red-950/60',
    iconColor: 'text-[#C0392B] dark:text-red-400',
    icon: 'i-lucide-alert-circle'
  }
]

// Category Breakdown Data for Donut Chart
const categoryData = [
  { name: 'Fiction', count: 412, percent: 33, color: '#1E3A5F', darkColor: '#4A7A9D' },
  { name: 'Science', count: 278, percent: 22, color: '#3B9C9C', darkColor: '#2DD4BF' },
  { name: 'History', count: 196, percent: 16, color: '#D4A017', darkColor: '#FBBF24' },
  { name: 'Philosophy', count: 142, percent: 11, color: '#6F5AA8', darkColor: '#A78BFA' },
  { name: 'Technology', count: 118, percent: 9, color: '#E56B5D', darkColor: '#FB923C' },
  { name: 'Other', count: 102, percent: 8, color: '#ADB5BD', darkColor: '#9CA3AF' }
]

// Recent Activity List
const recentActivities = [
  {
    id: 1,
    title: 'The Great Gatsby',
    author: 'F. Scott Fitzgerald',
    member: 'Sarah Johnson',
    initials: 'SJ',
    action: 'Borrowed by',
    date: 'May 30, 2024',
    time: '10:24 AM',
    avatarClass: 'bg-slate-200 text-slate-700 dark:bg-slate-700 dark:text-slate-200'
  },
  {
    id: 2,
    title: '1984',
    author: 'George Orwell',
    member: 'Emily Davis',
    initials: 'ED',
    action: 'Borrowed by',
    date: 'May 30, 2024',
    time: '09:15 AM',
    avatarClass: 'bg-amber-100 text-amber-800 dark:bg-amber-900/60 dark:text-amber-200'
  },
  {
    id: 3,
    title: 'A Brief History of Time',
    author: 'Stephen Hawking',
    member: 'Michael Brown',
    initials: 'MB',
    action: 'Borrowed by',
    date: 'May 29, 2024',
    time: '04:42 PM',
    avatarClass: 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/60 dark:text-emerald-200'
  },
  {
    id: 4,
    title: 'Sapiens: A Brief History...',
    author: 'Yuval Noah Harari',
    member: 'Olivia Martinez',
    initials: 'OM',
    action: 'Borrowed by',
    date: 'May 28, 2024',
    time: '02:11 PM',
    avatarClass: 'bg-orange-100 text-orange-800 dark:bg-orange-900/60 dark:text-orange-200'
  },
  {
    id: 5,
    title: 'The Hobbit',
    author: 'J.R.R. Tolkien',
    member: 'James Taylor',
    initials: 'JT',
    action: 'Returned by',
    date: 'May 28, 2024',
    time: '11:03 AM',
    avatarClass: 'bg-slate-200 text-slate-700 dark:bg-slate-700 dark:text-slate-200'
  }
]

// Needs Attention / Overdue Items List
const overdueItems = [
  {
    id: 1,
    title: 'The Hobbit',
    author: 'J.R.R. Tolkien',
    member: 'David Anderson',
    borrowDate: 'May 10, 2024',
    overdueDays: '7 days overdue'
  },
  {
    id: 2,
    title: 'To Kill a Mockingbird',
    author: 'Harper Lee',
    member: 'Jessica Taylor',
    borrowDate: 'May 12, 2024',
    overdueDays: '5 days overdue'
  },
  {
    id: 3,
    title: 'The Da Vinci Code',
    author: 'Dan Brown',
    member: 'Daniel Thomas',
    borrowDate: 'May 14, 2024',
    overdueDays: '3 days overdue'
  },
  {
    id: 4,
    title: 'Pride and Prejudice',
    author: 'Jane Austen',
    member: 'Laura White',
    borrowDate: 'May 15, 2024',
    overdueDays: '2 days overdue'
  },
  {
    id: 5,
    title: 'The Alchemist',
    author: 'Paulo Coelho',
    member: 'Robert Clark',
    borrowDate: 'May 16, 2024',
    overdueDays: '1 day overdue'
  }
]

// --- ECharts Integration ---
const activityChartRef = ref<HTMLDivElement | null>(null)
const categoryChartRef = ref<HTMLDivElement | null>(null)

let activityChart: echarts.ECharts | null = null
let categoryChart: echarts.ECharts | null = null

const isDark = computed(() => colorMode.value === 'dark')

function initActivityChart() {
  if (!activityChartRef.value) return
  if (activityChart) {
    activityChart.dispose()
  }

  activityChart = echarts.init(activityChartRef.value)

  const textColor = isDark.value ? '#9eaebf' : '#6c757d'
  const textHighlight = isDark.value ? '#ffffff' : '#212529'
  const gridColor = isDark.value ? '#2e3e53' : '#e1e4e8'
  const lineColor = isDark.value ? '#4a7a9d' : '#1e3a5f'
  const cardBg = isDark.value ? '#1a2433' : '#ffffff'

  const option: echarts.EChartsOption = {
    grid: {
      top: 20,
      right: 15,
      bottom: 25,
      left: 20,
      containLabel: true
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: cardBg,
      borderColor: gridColor,
      borderWidth: 1,
      padding: [8, 12],
      textStyle: {
        color: textHighlight,
        fontSize: 12,
        fontFamily: 'Inter, sans-serif'
      },
      extraCssText: 'box-shadow: 0 4px 16px rgba(16, 24, 40, 0.08); border-radius: 8px;',
      formatter: (params: unknown) => {
        const p = (Array.isArray(params) ? params[0] : params) as { name: string; value: number }
        return `<div style="font-size: 11px; color: ${textColor};">${p.name}</div><div style="font-size: 13px; font-weight: bold; color: ${textHighlight};">${p.value} loans</div>`
      }
    },
    xAxis: {
      type: 'category',
      data: ['May 1', 'May 7', 'May 14', 'May 21', 'May 28', 'May 31'],
      boundaryGap: false,
      axisLine: {
        lineStyle: { color: gridColor }
      },
      axisTick: { show: false },
      axisLabel: {
        color: textColor,
        fontSize: 11,
        fontFamily: 'Inter, sans-serif',
        formatter: (value: string) => {
          return value === 'May 14' ? `{highlight|${value}}` : value
        },
        rich: {
          highlight: {
            fontWeight: 'bold',
            color: textHighlight
          }
        }
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 80,
      interval: 20,
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: {
        lineStyle: {
          color: gridColor,
          type: 'solid'
        }
      },
      axisLabel: {
        color: textColor,
        fontSize: 11,
        fontFamily: 'Inter, sans-serif'
      }
    },
    series: [
      {
        name: 'Loans',
        type: 'line',
        smooth: 0.35,
        symbol: 'circle',
        symbolSize: 6,
        showSymbol: false,
        data: [22, 45, 62, 57, 40, 35],
        lineStyle: {
          color: lineColor,
          width: 2.5
        },
        itemStyle: {
          color: lineColor
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: isDark.value ? 'rgba(74, 122, 157, 0.25)' : 'rgba(30, 58, 95, 0.14)' },
            { offset: 1, color: isDark.value ? 'rgba(74, 122, 157, 0.0)' : 'rgba(30, 58, 95, 0.0)' }
          ])
        },
        markPoint: {
          symbol: 'circle',
          symbolSize: 10,
          itemStyle: {
            color: '#D4A017',
            borderColor: cardBg,
            borderWidth: 2,
            shadowBlur: 6,
            shadowColor: 'rgba(212, 160, 23, 0.4)'
          },
          data: [
            {
              name: 'May 14 Milestone',
              coord: ['May 14', 62],
              label: {
                show: true,
                position: 'top',
                distance: 10,
                backgroundColor: cardBg,
                borderColor: gridColor,
                borderWidth: 1,
                borderRadius: 6,
                padding: [4, 8],
                formatter: '{date|May 14}\n{val|62 loans}',
                rich: {
                  date: {
                    fontSize: 10,
                    color: textColor,
                    lineHeight: 14,
                    align: 'center'
                  },
                  val: {
                    fontSize: 11,
                    fontWeight: 'bold',
                    color: textHighlight,
                    lineHeight: 16,
                    align: 'center'
                  }
                }
              }
            }
          ]
        },
        markLine: {
          symbol: 'none',
          lineStyle: {
            color: '#D4A017',
            type: 'dashed',
            width: 1.5
          },
          data: [
            {
              xAxis: 'May 14'
            }
          ]
        }
      }
    ]
  }

  activityChart.setOption(option)
}

function initCategoryChart() {
  if (!categoryChartRef.value) return
  if (categoryChart) {
    categoryChart.dispose()
  }

  categoryChart = echarts.init(categoryChartRef.value)

  const cardBg = isDark.value ? '#1a2433' : '#ffffff'
  const textHighlight = isDark.value ? '#ffffff' : '#212529'
  const gridColor = isDark.value ? '#2e3e53' : '#e1e4e8'

  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'item',
      backgroundColor: cardBg,
      borderColor: gridColor,
      borderWidth: 1,
      padding: [6, 10],
      textStyle: {
        color: textHighlight,
        fontSize: 12,
        fontFamily: 'Inter, sans-serif'
      },
      extraCssText: 'box-shadow: 0 4px 16px rgba(16, 24, 40, 0.08); border-radius: 8px;',
      formatter: '{b}: <b>{c}</b> ({d}%)'
    },
    series: [
      {
        name: 'Books by Category',
        type: 'pie',
        radius: ['52%', '84%'],
        center: ['50%', '50%'],
        padAngle: 3,
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 6,
          borderColor: cardBg,
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          scale: true,
          scaleSize: 6,
          itemStyle: {
            shadowBlur: 16,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.18)'
          }
        },
        data: categoryData.map((cat) => ({
          value: cat.count,
          name: cat.name,
          itemStyle: { color: isDark.value ? cat.darkColor : cat.color }
        }))
      }
    ]
  }

  categoryChart.setOption(option)
}

function initAllCharts() {
  initActivityChart()
  initCategoryChart()
}

useResizeObserver(activityChartRef, () => {
  activityChart?.resize()
})

useResizeObserver(categoryChartRef, () => {
  categoryChart?.resize()
})

watch(isDark, () => {
  nextTick(() => {
    initAllCharts()
  })
})

onMounted(() => {
  nextTick(() => {
    initAllCharts()
  })
})

onUnmounted(() => {
  activityChart?.dispose()
  categoryChart?.dispose()
})
</script>

<template>
  <div class="space-y-6">


    <!-- 3 Top Metric Cards -->
    <div class="grid grid-cols-1 gap-4 sm:grid-cols-3">
      <div
        v-for="item in metrics"
        :key="item.id"
        class="flex flex-col justify-between rounded-xl border border-slate-200 bg-white p-5 shadow-2xs transition-shadow hover:shadow-xs dark:border-slate-800 dark:bg-slate-900"
      >
        <div class="flex items-center gap-4">
          <!-- Rounded Icon Container -->
          <div
            class="flex size-12 shrink-0 items-center justify-center rounded-full"
            :class="item.iconBg"
          >
            <UIcon :name="item.icon" class="size-6" :class="item.iconColor" />
          </div>

          <!-- Value & Label -->
          <div class="min-w-0">
            <p class="text-3xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
              {{ item.value }}
            </p>
            <p class="text-sm font-medium text-slate-500 dark:text-slate-400">
              {{ item.label }}
            </p>
          </div>
        </div>

        <!-- Indicator subtext -->
        <div class="mt-4 flex items-center gap-2">
          <span class="size-2 rounded-full" :class="item.dotColor" />
          <span class="text-xs text-slate-500 dark:text-slate-400">
            {{ item.subtext }}
          </span>
        </div>
      </div>
    </div>

    <!-- Charts Row: Apache ECharts Line Chart + Donut Chart -->
    <div class="grid grid-cols-1 gap-6 lg:grid-cols-12">
      <!-- 1. Library Activity Line Chart -->
      <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-2xs dark:border-slate-800 dark:bg-slate-900 lg:col-span-7 flex flex-col justify-between">
        <div class="flex items-start justify-between gap-4">
          <div>
            <h2 class="text-lg font-semibold text-slate-900 dark:text-slate-50">
              Library Activity
            </h2>
            <p class="text-xs text-slate-500 dark:text-slate-400">
              Loans this month
            </p>
          </div>

          <!-- Time period dropdown -->
          <div class="relative">
            <select
              v-model="selectedPeriod"
              class="appearance-none rounded-lg border border-slate-200 bg-white py-1.5 pl-3 pr-8 text-xs font-medium text-slate-700 shadow-2xs outline-none hover:bg-slate-50 focus:border-brand-500 focus:ring-1 focus:ring-brand-500 dark:border-slate-700 dark:bg-slate-800 dark:text-slate-200"
            >
              <option v-for="opt in periodOptions" :key="opt" :value="opt">
                {{ opt }}
              </option>
            </select>
            <UIcon
              name="i-lucide-chevron-down"
              class="pointer-events-none absolute right-2.5 top-1/2 size-3.5 -translate-y-1/2 text-slate-400"
            />
          </div>
        </div>

        <!-- Apache ECharts Line Chart Container -->
        <div ref="activityChartRef" class="h-60 w-full mt-2" />
      </div>

      <!-- 2. Books by Category Donut Chart -->
      <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-2xs dark:border-slate-800 dark:bg-slate-900 lg:col-span-5 flex flex-col justify-between">
        <div class="flex items-center justify-between">
          <h2 class="text-lg font-semibold text-slate-900 dark:text-slate-50">
            Books by Category
          </h2>
          <RouterLink
            to="/categories"
            class="flex items-center gap-1 text-xs font-semibold text-slate-700 hover:text-brand-600 dark:text-slate-300 dark:hover:text-brand-400 transition-colors"
          >
            View all
            <UIcon name="i-lucide-chevron-right" class="size-3.5" />
          </RouterLink>
        </div>

        <!-- Donut Chart & Legend Stack -->
        <div class="my-auto flex flex-col items-center justify-between gap-6 py-3 sm:flex-row sm:items-center">
          <!-- ECharts Donut Container with Center Total Overlay -->
          <div class="relative flex size-44 shrink-0 items-center justify-center">
            <div ref="categoryChartRef" class="size-full" />

            <!-- Donut Center Total -->
            <div class="absolute inset-0 flex flex-col items-center justify-center text-center select-none pointer-events-none">
              <span class="text-2xl font-bold tracking-tight text-slate-900 dark:text-slate-50 leading-tight">
                1,248
              </span>
              <span class="text-xs font-medium text-slate-500 dark:text-slate-400 leading-tight">
                Total Books
              </span>
            </div>
          </div>

          <!-- Category Legend List on the right -->
          <div class="flex flex-1 flex-col gap-2.5 w-full">
            <div
              v-for="cat in categoryData"
              :key="cat.name"
              class="flex items-center justify-between text-xs"
            >
              <div class="flex items-center gap-2.5">
                <span
                  class="size-2.5 rounded-full shrink-0"
                  :style="{ backgroundColor: isDark ? cat.darkColor : cat.color }"
                />
                <span class="font-medium text-slate-700 dark:text-slate-300">
                  {{ cat.name }}
                </span>
              </div>
              <span class="text-slate-500 dark:text-slate-400 font-medium">
                {{ cat.count }} ({{ cat.percent }}%)
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bottom Row: Recent Activity + Needs Attention -->
    <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
      <!-- 1. Recent Activity Card -->
      <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-2xs dark:border-slate-800 dark:bg-slate-900">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-semibold text-slate-900 dark:text-slate-50">
            Recent Activity
          </h2>
          <RouterLink
            to="/borrowings"
            class="flex items-center gap-1 text-xs font-semibold text-slate-700 hover:text-brand-600 dark:text-slate-300 dark:hover:text-brand-400 transition-colors"
          >
            View all activity
            <UIcon name="i-lucide-chevron-right" class="size-3.5" />
          </RouterLink>
        </div>

        <!-- Activity List -->
        <div class="divide-y divide-slate-100 dark:divide-slate-800/80">
          <div
            v-for="item in recentActivities"
            :key="item.id"
            class="flex items-center justify-between py-3 transition-colors hover:bg-slate-50/60 dark:hover:bg-slate-800/40 rounded-lg px-1.5"
          >
            <!-- Left: Book Cover + Details -->
            <div class="flex items-center gap-3.5 min-w-0">
              <BookCoverThumbnail :title="item.title" size="md" />

              <div class="min-w-0">
                <p class="truncate text-sm font-semibold text-slate-900 dark:text-slate-100">
                  {{ item.title }}
                </p>
                <p class="truncate text-xs text-slate-500 dark:text-slate-400">
                  {{ item.author }}
                </p>
              </div>
            </div>

            <!-- Middle: Member Avatar & Action -->
            <div class="hidden sm:flex items-center gap-2.5 mx-3 min-w-0">
              <div
                class="flex size-7 shrink-0 items-center justify-center rounded-full text-[11px] font-bold"
                :class="item.avatarClass"
              >
                {{ item.initials }}
              </div>
              <div class="min-w-0 text-left">
                <p class="text-[11px] text-slate-500 dark:text-slate-400 leading-tight">
                  {{ item.action }}
                </p>
                <p class="text-xs font-medium text-slate-800 dark:text-slate-200 leading-tight truncate">
                  {{ item.member }}
                </p>
              </div>
            </div>

            <!-- Right: Date & Time -->
            <div class="text-right shrink-0">
              <p class="text-xs font-medium text-slate-700 dark:text-slate-300">
                {{ item.date }}
              </p>
              <p class="text-[11px] text-slate-400 dark:text-slate-500">
                {{ item.time }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- 2. Needs Attention Card -->
      <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-2xs dark:border-slate-800 dark:bg-slate-900">
        <div class="flex items-center justify-between mb-3.5">
          <h2 class="text-lg font-semibold text-slate-900 dark:text-slate-50">
            Needs Attention
          </h2>
          <RouterLink
            to="/borrowings"
            class="flex items-center gap-1 text-xs font-semibold text-slate-700 hover:text-brand-600 dark:text-slate-300 dark:hover:text-brand-400 transition-colors"
          >
            View all overdue
            <UIcon name="i-lucide-chevron-right" class="size-3.5" />
          </RouterLink>
        </div>

        <!-- Overdue Alert Banner -->
        <div class="mb-3.5 flex items-center gap-3 rounded-lg border border-red-100 bg-[#FDF2F2] p-3 dark:border-red-950/60 dark:bg-red-950/30">
          <div class="flex size-7 shrink-0 items-center justify-center rounded-full bg-red-100 text-[#C0392B] dark:bg-red-900/60 dark:text-red-400">
            <UIcon name="i-lucide-alert-circle" class="size-4.5" />
          </div>
          <div>
            <p class="text-xs font-bold text-slate-900 dark:text-slate-100">
              23 books need attention
            </p>
            <p class="text-[11px] text-slate-500 dark:text-slate-400">
              Overdue loans require follow-up.
            </p>
          </div>
        </div>

        <!-- Overdue Books List -->
        <div class="divide-y divide-slate-100 dark:divide-slate-800/80">
          <div
            v-for="item in overdueItems"
            :key="item.id"
            class="flex items-center justify-between py-2.5 transition-colors hover:bg-slate-50/60 dark:hover:bg-slate-800/40 rounded-lg px-1.5"
          >
            <!-- Left: Cover + Details -->
            <div class="flex items-center gap-3.5 min-w-0">
              <BookCoverThumbnail :title="item.title" size="md" />

              <div class="min-w-0">
                <p class="truncate text-sm font-semibold text-slate-900 dark:text-slate-100">
                  {{ item.title }}
                </p>
                <p class="truncate text-xs text-slate-500 dark:text-slate-400">
                  {{ item.author }}
                </p>
              </div>
            </div>

            <!-- Borrower information -->
            <div class="hidden sm:block min-w-0 mx-2">
              <p class="truncate text-xs font-medium text-slate-800 dark:text-slate-200">
                {{ item.member }}
              </p>
              <p class="truncate text-[11px] text-slate-400 dark:text-slate-500">
                Borrowed on {{ item.borrowDate }}
              </p>
            </div>

            <!-- Overdue days + Link -->
            <div class="flex items-center gap-4 shrink-0">
              <span class="text-xs font-semibold text-[#C0392B] dark:text-red-400">
                {{ item.overdueDays }}
              </span>

              <RouterLink
                to="/borrowings"
                class="flex items-center gap-0.5 text-xs font-medium text-slate-500 hover:text-brand-600 dark:text-slate-400 dark:hover:text-brand-400"
              >
                View loan
                <UIcon name="i-lucide-chevron-right" class="size-3.5" />
              </RouterLink>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <footer class="pt-6 pb-2 text-center text-xs text-slate-400 dark:text-slate-500">
      © 2024 Athenaeum Library Management System. All rights reserved.
    </footer>
  </div>
</template>
