import request from './request'

const BASE = '/blog/admin/category'
const PUBLIC = '/blog/category'

export function getCategoryList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getCategory(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function getPublicCategoryList() {
  return request({ url: PUBLIC + '/list', method: 'get' })
}

export function getPublicCategory(id) {
  return request({ url: PUBLIC + '/' + id, method: 'get' })
}

export function addCategory(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateCategory(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delCategory(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
