import request from './request'

const BASE = '/blog/user/article'

export function getUserArticleList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getUserArticle(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addUserArticle(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateUserArticle(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delUserArticle(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
