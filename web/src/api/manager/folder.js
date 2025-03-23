import request from '@/utils/request'

// 查询逻辑文件夹列表
export function listFolder(query) {
  return request({
    url: '/manager/folder/list',
    method: 'get',
    params: query
  })
}

// 查询逻辑文件夹详细
export function getFolder(id) {
  return request({
    url: '/manager/folder/' + id,
    method: 'get'
  })
}

// 新增逻辑文件夹
export function addFolder(data) {
  return request({
    url: '/manager/folder',
    method: 'post',
    data: data
  })
}

// 修改逻辑文件夹
export function updateFolder(data) {
  return request({
    url: '/manager/folder',
    method: 'put',
    data: data
  })
}

// 删除逻辑文件夹
export function delFolder(id) {
  return request({
    url: '/manager/folder/' + id,
    method: 'delete'
  })
}
