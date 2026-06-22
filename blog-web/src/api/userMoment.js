import request from './request'

const BASE = '/blog/user/moment'

export function getUserMomentList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getUserMoment(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addUserMoment(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateUserMoment(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delUserMoment(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
