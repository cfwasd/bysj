<template>
  <div class="doc-dashboard">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <h1>个人中心</h1>
      <p>您当前共有 {{ totalDocs }} 份markdown文档和 {{ totalFiles }} 份文件</p>
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
          </div>
        </div>
      </el-card>
    </div>

    <!-- 最近文档 -->
    <div class="recent-section">
      <el-card shadow="hover">
        <template #header>
          <span>最近上传</span>
        </template>
        <el-table :data="recentDocs" style="width: 100%">
          <el-table-column prop="fileRawName" label="文档名称" width="180" />
          <el-table-column prop="type" label="类型" width="80">
            <template #default="{row}">
              <el-tag :type="getTagType(row.fileExtentions)">{{ row.fileExtentions }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="folder" label="所在文件夹" width="100" />
          <el-table-column prop="uploadTime" label="上传时间" />
          <el-table-column label="操作" width="120">
            <template #default="{row}">
              <el-button size="small" @click="openDoc(row)">预览</el-button>
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
import {info} from '@/api/index'
import {Base64} from 'js-base64'
import { getPreview } from '@/api/manager/files'
import {useRouter} from "vue-router";

function init(){
  info().then(res => {
    totalFiles.value = res.data.fileTotal
    totalDocs.value = res.data.markdownTotal
    recentDocs.value = res.data.resentUpload
    docStats.value[0].count = res.data.word.count
    docStats.value[1].count = res.data.pdf.count
    docStats.value[2].count = res.data.excel.count
    docStats.value[3].count = res.data.powerpoint.count
    docStats.value[4].count = res.data.pic.count
    docStats.value[5].count = res.data.other.count
  })
}
init()
const router = useRouter();

const totalDocs = ref(128)
const totalFiles = ref(13)

const docStats = ref([
  { type: 'Word文档', count: 42, icon: 'el-icon-document', color: '#409EFF' },
  { type: 'PDF文件', count: 35, icon: 'el-icon-tickets', color: '#F56C6C' },
  { type: 'Excel表格', count: 28, icon: 'el-icon-data-line', color: '#67C23A' },
  { type: 'PPT演示', count: 15, icon: 'el-icon-data-board', color: '#E6A23C' },
  { type: '图片', count: 8, icon: 'el-icon-picture', color: '#909399' },
  { type: '其他', count: 8, icon: 'el-icon-picture', color: '#d50fc5' }
])

const recentDocs = ref([
])

const quickActions = ref([
  { label: '上传文档', type: 'primary', icon: 'el-icon-upload', action: 'upload' },
  { label: '新建文档', type: 'success', icon: 'el-icon-plus', action: 'create' },
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

const openDoc = (row) => {
  getPreview(row.id).then(response => {
    // const baseulrl = 'http://198.44.170.73:8012/onlinePreview?url='+encodeURIComponent(Base64.encode(response))
    const baseulrl = 'https://preview.wangziheng.site/onlinePreview?url='+encodeURIComponent(Base64.encode(response))
    window.open(baseulrl, '_blank');
  })
  // 实际项目中这里调用打开文档的方法
}



const handleAction = (action) => {

    if (action.label === '上传文档') {
      router.push("/file/upload")
    }else {
      router.push("/note/info")
    }

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