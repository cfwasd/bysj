import axios from 'axios'
import { ElMessage,ElMessageBox } from 'element-plus';

const service = axios.create({
  baseURL: `${window.location.protocol}//${window.location.host}/prod-api`,
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const code = localStorage.getItem('token')
    if (code) {
      config.headers = config.headers || {}
      config.headers.code = `${code}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    const msg = res.data.msg
    // 二进制数据则直接返回
    if (res.request.responseType ===  'blob' || res.request.responseType ===  'arraybuffer') {
      return res.data
    }
    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true;
// 由于找不到 ElMessageBox，根据提示可能是想使用 ElMessage，这里将 ElMessageBox.confirm 替换为 ElMessage 的提示方式
// 不过 ElMessage 没有 confirm 方法，通常使用 ElMessageBox 来实现确认框功能，需要确保已经导入 ElMessageBox
// 若使用 ElMessage 可以简单提示，若要实现确认功能，需要正确导入并使用 ElMessageBox
// 这里假设需要导入 ElMessageBox 并修正代码，先在文件顶部添加 import { ElMessageBox } from 'element-plus';
// 以下是保持确认框功能的修正代码
        ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
          isRelogin.show = false;
          useUserStore().logOut().then(() => {
            location.href = '/index';
          })
      }).catch(() => {
        isRelogin.show = false;
      });
    }
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } else if (code === 500) {
      ElMessage({ message: msg, type: 'error' })
      return Promise.reject(new Error(msg))
    } else if (code === 601) {
      ElMessage({ message: msg, type: 'warning' })
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      ElNotification.error({ title: msg })
      return Promise.reject('error')
    } else {
      return  Promise.resolve(res.data)
    }
  },
  error => {
    console.log('err' + error)
    let { message } = error;
    if (message == "Network Error") {
      message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
// 由于找不到 ElMessage，推测可能是未导入相关组件，若使用 Element Plus，需要先导入
// 这里假设已经正确导入了 ElMessage，若未导入，需要在文件顶部添加 import { ElMessage } from 'element-plus';
    ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    return Promise.reject(error)
  }
)

export default service