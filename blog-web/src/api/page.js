import request from './request'

const BASE = '/blog/admin/page'
const PUBLIC = '/blog/page'

export function getPageList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getPage(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function getPublicPageList() {
  return request({ url: PUBLIC + '/list', method: 'get' })
}

export function getPublicPageBySlug(slug) {
  return request({ url: PUBLIC + '/' + slug, method: 'get' })
}

export function addPage(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updatePage(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delPage(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
