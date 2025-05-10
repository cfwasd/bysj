import request  from "@/api/request";

export function queryCode() {
  return request({
    url: '/shared/code',
    method: 'get',
  })
}
export function login(code) {
    return request({
      url: '/shared/login',
      method: 'get',
      params: code
    })
  }

export function getFileInfo(param) {
    return request({
      url: '/shared/file',
      method: 'get',
      params: param
    })
}
export function preview(param) {
    return request({
      url: '/shared/preview',
      method: 'get',
      params: param
    })
}
export function download(param) {
    return request({
      url: '/shared/download',
      method: 'get',
      params: param,
      responseType: 'blob'
    })
}

export function queryfile(param) {
    return request({
      url: '/shared/queryfile',
      method: 'get',
      params: param
    })
}

