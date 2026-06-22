import request from './request'

const BASE = '/blog/admin/moment'

export function getMomentList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getMoment(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addMoment(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateMoment(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delMoment(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}

export function getPublicMomentList() {
  return request({ url: '/blog/moment/list', method: 'get' })
}
