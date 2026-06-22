import request from './request'

export function login(data) {
  return request({ url: '/api/blog/user/login', method: 'post', data })
}

export function register(data) {
  return request({ url: '/api/blog/user/register', method: 'post', data })
}

export function getUserInfo(userId) {
  return request({ url: '/api/blog/user/info/' + userId, method: 'get' })
}
