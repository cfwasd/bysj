import request from '@/utils/request'

export function getPreview(fileId){
    return request({
        url: '/files/preview?fileId=' + fileId,
        method: 'get'
    })
}

export function downloadFile(fileId){
    return request({
        url: '/files/download?fileId=' + fileId,
        method: 'get',
        responseType: 'blob'
    })
}