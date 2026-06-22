import request from './request'

const BASE = '/blog/admin/link'
const PUBLIC = '/blog/link'

export function getLinkList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getPublicLinkList() {
  return request({ url: PUBLIC + '/list', method: 'get' })
}

export function getLink(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addLink(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateLink(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delLink(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
