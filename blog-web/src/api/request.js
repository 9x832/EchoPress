import axios from 'axios'
import { getToken, removeToken } from '@/utils/auth'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: '',
  timeout: 30000
})

service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const data = response.data
    if (data.code === 401) {
      removeToken()
      ElMessage.error('登录已过期，请重新登录')
      setTimeout(() => { window.location.href = '/login' }, 1500)
      return Promise.reject(new Error(data.msg || '登录已过期'))
    }
    return data
  },
  error => {
    if (error.response && error.response.status === 401) {
      removeToken()
      ElMessage.error('登录已过期，请重新登录')
      setTimeout(() => { window.location.href = '/login' }, 1500)
    }
    return Promise.reject(error)
  }
)

export default service
