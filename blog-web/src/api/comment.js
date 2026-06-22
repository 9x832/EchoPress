import request from './request'

const BASE = '/blog/admin/comment'
const PUBLIC = '/blog/comment'

export function getCommentList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getComment(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addComment(data) {
  return request({ url: BASE, method: 'post', data })
}

export function getPublicCommentList(articleId, params) {
  return request({ url: PUBLIC + '/list/' + articleId, method: 'get', params })
}

export function submitPublicComment(data) {
  return request({ url: PUBLIC, method: 'post', data })
}

export function updateComment(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delComment(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
