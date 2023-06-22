import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const userInfoKey = "userInfo"
const platformUserCorpId = 0 || -1

export function getToken() {
  //return true;
  return sessionStorage.getItem(TokenKey)
}

export function setToken(token) {
  return sessionStorage.setItem(TokenKey, token)
}


export function setUserInfo(userInfo) {
  sessionStorage.setItem(userInfoKey, JSON.stringify(userInfo))
}

export function isPlatformUser() {
  let userInfo = JSON.parse(sessionStorage.getItem(userInfoKey))
  console.log(userInfo)
  return userInfo != null && userInfo.corp === platformUserCorpId
}

export function getUserInfo() {
  return sessionStorage.getItem(userInfoKey);
}

export function getPermissions() {
  let userInfo = JSON.parse(sessionStorage.getItem(userInfoKey))
  if (userInfo != null) {
    return userInfo.permissions;
  }
  return null;
}

export function logOut() {
  sessionStorage.clear()
}
