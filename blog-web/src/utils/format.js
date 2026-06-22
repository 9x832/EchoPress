import dayjs from 'dayjs'

export function formatDateTime(str) {
  if (!str) return ''
  return dayjs(str).format('YYYY-MM-DD HH:mm:ss')
}

export function formatDate(str) {
  if (!str) return ''
  return dayjs(str).format('YYYY-MM-DD')
}

export function readingTime(wordCount) {
  if (!wordCount || wordCount <= 0) return ''
  const minutes = Math.max(1, Math.ceil(wordCount / 300))
  return '约' + minutes + '分钟'
}

export function formatDateHuman(str) {
  if (!str) return ''
  const d = dayjs(str)
  const now = dayjs()
  const diff = now.diff(d, 'day')
  if (diff === 0) return '今天 ' + d.format('HH:mm')
  if (diff === 1) return '昨天 ' + d.format('HH:mm')
  if (diff < 7) return diff + '天前'
  return d.format('YYYY-MM-DD')
}
