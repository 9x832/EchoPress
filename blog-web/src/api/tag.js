import request from './request'

const BASE = '/blog/admin/tag'
const PUBLIC = '/blog/tag'

export function getTagList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getTag(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function getPublicTagList() {
  return request({ url: PUBLIC + '/list', method: 'get' })
}

export function getPublicTag(id) {
  return request({ url: PUBLIC + '/' + id, method: 'get' })
}

export function addTag(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateTag(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delTag(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
