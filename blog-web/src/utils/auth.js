const TOKEN_KEY = 'EchoPress-Token'
const USER_TYPE_KEY = 'EchoPress-UserType'
const NICK_NAME_KEY = 'EchoPress-NickName'

export function getToken() { return localStorage.getItem(TOKEN_KEY) }
export function setToken(token) { localStorage.setItem(TOKEN_KEY, token) }
export function removeToken() { localStorage.removeItem(TOKEN_KEY) }

export function getUserType() { return localStorage.getItem(USER_TYPE_KEY) }
export function setUserType(userType) { localStorage.setItem(USER_TYPE_KEY, userType) }
export function removeUserType() { localStorage.removeItem(USER_TYPE_KEY) }

export function getNickName() { return localStorage.getItem(NICK_NAME_KEY) }
export function setNickName(name) { localStorage.setItem(NICK_NAME_KEY, name) }
export function removeNickName() { localStorage.removeItem(NICK_NAME_KEY) }

export function isAdmin() {
  const ut = getUserType()
  return ut === '01' || ut === '02'
}
