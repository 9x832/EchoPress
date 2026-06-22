import request from './request'

const BASE = '/blog/admin/config'

export function getConfigList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getConfig(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addConfig(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateConfig(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delConfig(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}

export function getPublicSiteConfig() {
  return request({ url: '/blog/site/config', method: 'get' })
}
