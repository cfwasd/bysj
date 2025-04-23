<template>
  <div class="doc-dashboard">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <h1>我的文档中心</h1>
      <p>您当前共有 {{ totalDocs }} 份文档，存储空间使用 {{ storageUsage }}</p>
    </div>

    <!-- 文档统计 -->
    <div class="stats-section">
      <el-card v-for="item in docStats" :key="item.type" shadow="hover">
        <div class="stat-item">
          <div class="stat-icon" :style="{ backgroundColor: item.color }">
            <i :class="item.icon"></i>
          </div>
          <div class="stat-info">
            <h3>{{ item.type }}</h3>
            <p class="count">{{ item.count }}</p>
            <p class="desc">{{ item.desc }}</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 最近文档 -->
    <div class="recent-section">
      <el-card shadow="hover">
        <template #header>
          <span>最近访问</span>
          <el-button type="text" @click="viewAllRecent">查看全部</el-button>
        </template>
        <el-table :data="recentDocs" style="width: 100%">
          <el-table-column prop="name" label="文档名称" width="180" />
          <el-table-column prop="type" label="类型" width="80">
            <template #default="{row}">
              <el-tag :type="getTagType(row.type)">{{ row.type }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="size" label="大小" width="100" />
          <el-table-column prop="lastAccess" label="最后访问" />
          <el-table-column label="操作" width="120">
            <template #default="{row}">
              <el-button size="small" @click="openDoc(row)">打开</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <h3>快速操作</h3>
      <div class="action-buttons">
        <el-button 
          v-for="action in quickActions" 
          :key="action.label" 
          :type="action.type" 
          :icon="action.icon"
          @click="handleAction(action)">
          {{ action.label }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

const totalDocs = ref(128)
const storageUsage = ref('35%')

const docStats = ref([
  { type: 'Word文档', count: 42, desc: '较上月新增5份', icon: 'el-icon-document', color: '#409EFF' },
  { type: 'PDF文件', count: 35, desc: '较上月新增2份', icon: 'el-icon-tickets', color: '#F56C6C' },
  { type: 'Excel表格', count: 28, desc: '较上月新增3份', icon: 'el-icon-data-line', color: '#67C23A' },
  { type: 'PPT演示', count: 15, desc: '较上月新增1份', icon: 'el-icon-data-board', color: '#E6A23C' },
  { type: '图片', count: 8, desc: '较上月新增0份', icon: 'el-icon-picture', color: '#909399' }
])

const recentDocs = ref([
  { name: '毕业论文.docx', type: 'Word', size: '2.4MB', lastAccess: '2023-05-15 14:30' },
  { name: '项目计划书.pdf', type: 'PDF', size: '5.1MB', lastAccess: '2023-05-14 09:15' },
  { name: '财务数据.xlsx', type: 'Excel', size: '1.2MB', lastAccess: '2023-05-13 16:45' },
  { name: '产品介绍.pptx', type: 'PPT', size: '8.7MB', lastAccess: '2023-05-12 11:20' },
  { name: '设计稿.jpg', type: '图片', size: '3.5MB', lastAccess: '2023-05-10 10:05' }
])

const quickActions = ref([
  { label: '上传文档', type: 'primary', icon: 'el-icon-upload', action: 'upload' },
  { label: '新建文档', type: 'success', icon: 'el-icon-plus', action: 'create' },
  { label: '文档分类', type: 'info', icon: 'el-icon-folder', action: 'category' },
  { label: '回收站', type: 'warning', icon: 'el-icon-delete', action: 'trash' }
])

const getTagType = (type: string) => {
  const map: Record<string, string> = {
    'Word': '',
    'PDF': 'danger',
    'Excel': 'success',
    'PPT': 'warning',
    '图片': 'info'
  }
  return map[type] || ''
}

const openDoc = (doc: any) => {
  ElMessage.success(`开发中`)
  // 实际项目中这里调用打开文档的方法
}

const viewAllRecent = () => {
    ElMessage.success(`开发中`)
  // 实际项目中这里跳转到最近文档页面
}

const handleAction = (action: any) => {
    ElMessage.success(`开发中`)
  // 根据action.type执行不同操作
}
</script>

<style scoped>
.doc-dashboard {
  padding: 20px;
}

.welcome-section {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.welcome-section h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.welcome-section p {
  margin: 10px 0 0;
  color: #606266;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  margin-right: 15px;
}

.stat-info h3 {
  margin: 0;
  font-size: 16px;
  color: #606266;
}

.stat-info .count {
  margin: 5px 0;
  font-size: 22px;
  font-weight: bold;
}

.stat-info .desc {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.recent-section {
  margin-bottom: 20px;
}

.quick-actions {
  margin-bottom: 20px;
}

.quick-actions h3 {
  margin: 0 0 15px;
  font-size: 18px;
  color: #303133;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.action-buttons .el-button {
  flex: 1;
}
</style>