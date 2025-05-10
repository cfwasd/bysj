import { ElMessage } from 'element-plus';
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: { title: '验证页' }
  },
  {
    path: '/index',
    name: 'Index',
    component: () => import('@/views/IndexList.vue'),
  },
  {
    path: '/preview',
    name: 'Preview',
    component: () => import('@/views/MarkdownPreview.vue'),
  }
  
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  // 如果访问的是根路径，直接放行
  if (to.path === '/') {
    return next();
  }

  // 检查是否有token
  const token = localStorage.getItem('token');
  if (!token) {
    ElMessage.error('请输入文件码');
    // 如果没有token且不是访问根路径，重定向到登录页
    return next('/');
  }

  // 其他情况正常放行
  next();
});

export default router
