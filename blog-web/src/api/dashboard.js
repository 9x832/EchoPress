import request from './request'

const BASE = '/blog/admin/dashboard'

export function getDashboardData() {
  return request({ url: BASE, method: 'get' })
}
