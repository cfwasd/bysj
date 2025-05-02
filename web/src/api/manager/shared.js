import request from '@/utils/request'

// 查询文件分享相关列表
export function listShared(query) {
  return request({
    url: '/manager/shared/list',
    method: 'get',
    params: query
  })
}

// 查询文件分享相关详细
export function getShared(id) {
  return request({
    url: '/manager/shared/' + id,
    method: 'get'
  })
}

// 新增文件分享相关
export function addShared(data) {
  return request({
    url: '/manager/shared',
    method: 'post',
    data: data
  })
}

// 修改文件分享相关
export function updateShared(data) {
  return request({
    url: '/manager/shared',
    method: 'put',
    data: data
  })
}

// 删除文件分享相关
export function delShared(id) {
  return request({
    url: '/manager/shared/' + id,
    method: 'delete'
  })
}
