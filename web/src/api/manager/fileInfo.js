import request from '@/utils/request'

// 查询文件信息列表
export function listFileInfo(query) {
  return request({
    url: '/manager/fileInfo/list',
    method: 'get',
    params: query
  })
}

// 通过文件夹id查询文件信息
export function queryByTreeId(query) {
  return request({
    url: '/manager/fileInfo/query',
    method: 'get',
    params: query
  })
}

// 查询文件信息详细
export function getFileInfo(id) {
  return request({
    url: '/manager/fileInfo/' + id,
    method: 'get'
  })
}

// 新增文件信息
export function addFileInfo(data) {
  return request({
    url: '/manager/fileInfo',
    method: 'post',
    data: data
  })
}

// 修改文件信息
export function updateFileInfo(data) {
  return request({
    url: '/manager/fileInfo',
    method: 'put',
    data: data
  })
}

// 删除文件信息
export function delFileInfo(id) {
  return request({
    url: '/manager/fileInfo/' + id,
    method: 'delete'
  })
}
