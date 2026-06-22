import request from './request'

export function getCaptcha() {
  return request({ url: '/captchaImage', method: 'get' })
}
