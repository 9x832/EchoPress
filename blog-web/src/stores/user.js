import { defineStore } from 'pinia'
import { getToken, setToken, removeToken, getUserType, setUserType, removeUserType, getNickName, setNickName, removeNickName } from '@/utils/auth'
import { login as loginApi, getUserInfo as getUserInfoApi } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userId: null,
    userName: '',
    nickName: getNickName() || '',
    userType: getUserType() || ''
  }),

  getters: {
    isAdmin: (state) => state.userType === '01' || state.userType === '02'
  },

  actions: {
    async login(loginForm) {
      const res = await loginApi(loginForm)
      if (res.code === 200) {
        this.token = res.data.token
        this.userType = res.data.userType || '00'
        this.nickName = res.data.nickName || ''
        setToken(res.data.token)
        setUserType(res.data.userType || '00')
        setNickName(res.data.nickName || '')
      }
      return res
    },

    async getInfo() {
      return { nickName: this.nickName }
    },

    logout() {
      this.token = ''
      this.userId = null
      this.userName = ''
      this.nickName = ''
      this.userType = ''
      removeToken()
      removeUserType()
      removeNickName()
    }
  }
})
