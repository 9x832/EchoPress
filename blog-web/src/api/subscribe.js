import request from './request'

const BASE = '/blog/admin/subscribe'

export function getSubscribeList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getSubscribe(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addSubscribe(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateSubscribe(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delSubscribe(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}

export function submitSubscribe(data) {
  return request({ url: '/blog/subscribe', method: 'post', data })
}
