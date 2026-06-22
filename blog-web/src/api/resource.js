import request from './request'

const BASE = '/blog/admin/resource'

export function getResourceList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getResource(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addResource(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateResource(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delResource(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
