<template>
  <div class="dashboard">
    <!-- Stat cards -->
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in cards" :key="card.title">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" :style="{ background: card.color }">
              <el-icon :size="28"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-title">{{ card.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Trend charts -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>近7天文章发布趋势</template>
          <div ref="publishChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>近7天访问量趋势</template>
          <div ref="viewChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDashboardData } from '@/api/dashboard'

const cards = ref([
  { title: '文章总数', value: 0, icon: 'Document', color: '#409eff' },
  { title: '分类总数', value: 0, icon: 'Collection', color: '#67c23a' },
  { title: '标签总数', value: 0, icon: 'PriceTag', color: '#e6a23c' },
  { title: '用户总数', value: 0, icon: 'User', color: '#f56c6c' },
  { title: '评论总数', value: 0, icon: 'ChatDotSquare', color: '#909399' },
  { title: '动态总数', value: 0, icon: 'Picture', color: '#b37feb' },
  { title: '总浏览量', value: 0, icon: 'View', color: '#36cfc9' },
  { title: '资源文件', value: 0, icon: 'FolderOpened', color: '#ff9c6e' }
])

const publishChartRef = ref(null)
const viewChartRef = ref(null)

function makeLineChart(data, color) {
  return {
    grid: { top: 20, right: 20, bottom: 30, left: 40 },
    xAxis: { type: 'category', data: data.map(d => d.date) },
    yAxis: { type: 'value', minInterval: 1 },
    tooltip: { trigger: 'axis' },
    series: [{
      data: data.map(d => d.count),
      type: 'line',
      smooth: true,
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: color },
        { offset: 1, color: 'rgba(255,255,255,0)' }
      ])},
      itemStyle: { color },
      lineStyle: { color }
    }]
  }
}

function fillDateGaps(data) {
  const result = []
  const map = {}
  data.forEach(d => { map[d.date] = d.count })
  for (let i = 6; i >= 0; i--) {
    const d = new Date()
    d.setDate(d.getDate() - i)
    const key = d.toISOString().slice(0, 10)
    result.push({ date: key, count: map[key] || 0 })
  }
  return result
}

onMounted(async () => {
  try {
    const res = await getDashboardData()
    if (res.code === 200) {
      const s = res.data.stats
      cards.value[0].value = s.articleCount
      cards.value[1].value = s.categoryCount
      cards.value[2].value = s.tagCount
      cards.value[3].value = s.userCount
      cards.value[4].value = s.commentCount
      cards.value[5].value = s.momentCount
      cards.value[6].value = s.totalViews
      cards.value[7].value = s.resourceCount

      await nextTick()
      if (publishChartRef.value) {
        const c1 = echarts.init(publishChartRef.value)
        c1.setOption(makeLineChart(fillDateGaps(res.data.publishTrend), '#409eff'))
      }
      if (viewChartRef.value) {
        const c2 = echarts.init(viewChartRef.value)
        c2.setOption(makeLineChart(fillDateGaps(res.data.viewTrend), '#67c23a'))
      }
    }
  } catch {}
})
</script>

<style scoped>
.stat-card { display: flex; align-items: center; gap: 16px; }
.stat-icon { width: 60px; height: 60px; display: flex; align-items: center; justify-content: center; border-radius: 12px; color: #fff; flex-shrink: 0; }
.stat-value { font-size: 28px; font-weight: 700; }
.stat-title { font-size: 14px; color: #999; margin-top: 4px; }
</style>
