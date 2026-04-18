<template>
  <div ref="chartRef" class="overflow-x-auto w-full h-60"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { nextTick, onBeforeUnmount, ref, watch } from 'vue'

const props = defineProps({
  value: {
    type: Object,
    default: null,
  },
})

const chartRef = ref(null)
let chartInstance = null

async function initLineChat() {
  await nextTick()
  const chartDom = chartRef.value
  if (!chartDom || !props.value) return

  if (chartInstance) {
    chartInstance.dispose()
  }

  chartInstance = echarts.init(chartDom)

  const pvDates = props.value.pvDates || []
  const pvCounts = props.value.pvCounts || []

  const option = {
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: pvDates,
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: pvCounts,
        type: 'line',
      },
    ],
  }

  chartInstance.setOption(option)
}

watch(() => props.value, () => initLineChat(), { deep: true, immediate: true })

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>
