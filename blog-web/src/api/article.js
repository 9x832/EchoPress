import request from './request'

const BASE = '/blog/admin/article'
const PUBLIC = '/blog/article'

export function getArticleList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getArticle(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function getPublicArticleList(params) {
  return request({ url: PUBLIC + '/list', method: 'get', params })
}

export function getPublicArticle(id) {
  return request({ url: PUBLIC + '/' + id, method: 'get' })
}

export function addArticle(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateArticle(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delArticle(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}

const USER = '/blog/user/article'

export function likeArticle(articleId) {
  return request({ url: USER + '/like/' + articleId, method: 'post' })
}

export function getLikedStatus(ids) {
  return request({ url: USER + '/liked-status', method: 'get', params: { ids: ids.join(',') } })
}
