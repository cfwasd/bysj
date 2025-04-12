import request from '@/utils/request'

export function uploadFile(data) {
    return request({
        url: '/manager/file/uploadMinio',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        data:data
    })
}