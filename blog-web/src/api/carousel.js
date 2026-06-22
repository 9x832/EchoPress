import request from './request'

const BASE = '/blog/admin/carousel'
const PUBLIC = '/blog/carousel'

export function getCarouselList(params) {
  return request({ url: BASE + '/list', method: 'get', params })
}

export function getPublicCarouselList() {
  return request({ url: PUBLIC + '/list', method: 'get' })
}

export function getCarousel(id) {
  return request({ url: BASE + '/' + id, method: 'get' })
}

export function addCarousel(data) {
  return request({ url: BASE, method: 'post', data })
}

export function updateCarousel(data) {
  return request({ url: BASE, method: 'put', data })
}

export function delCarousel(ids) {
  return request({ url: BASE + '/' + ids, method: 'delete' })
}
