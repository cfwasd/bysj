import request from '@/utils/request.js'

export  function info (){
    return request({
        url: '/index/info',
        method: 'GET',
    })
}