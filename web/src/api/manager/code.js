import request from '@/utils/request'

// 查询分享码管理列表
export function listCode(query) {
  return request({
    url: '/manager/code/list',
    method: 'get',
    params: query
  })
}

// 查询分享码管理详细
export function getCode(id) {
  return request({
    url: '/manager/code/' + id,
    method: 'get'
  })
}

// 新增分享码管理
export function addCode(data) {
  return request({
    url: '/manager/code',
    method: 'post',
    data: data
  })
}

// 修改分享码管理
export function updateCode(data) {
  return request({
    url: '/manager/code',
    method: 'put',
    data: data
  })
}

// 删除分享码管理
export function delCode(id) {
  return request({
    url: '/manager/code/' + id,
    method: 'delete'
  })
}
