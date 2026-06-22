import request from './request'

const BASE = '/blog/admin/user'

export function getUserList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getUser(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addUser(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateUser(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delUser(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
