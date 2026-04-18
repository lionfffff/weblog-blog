const TOKEN_KEY = 'Authorization'

function canUseSessionStorage() {
  return typeof window !== 'undefined' && typeof window.sessionStorage !== 'undefined'
}

export function getToken() {
  if (!canUseSessionStorage()) return ''
  return window.sessionStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token) {
  if (!canUseSessionStorage()) return ''
  window.sessionStorage.setItem(TOKEN_KEY, token)
  return token
}

export function removeToken() {
  if (!canUseSessionStorage()) return
  window.sessionStorage.removeItem(TOKEN_KEY)
}
