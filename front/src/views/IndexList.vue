<template>
    <div class="app-container">
    <el-form :model="form" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label-width="auto" label="文件逻辑位置" prop="folder">
        <el-input
          v-model="form.fileName"
          placeholder="请输入文件名称"
          clearable
          @keyup.enter="handleQuery" 
          />

      </el-form-item>
      <el-form-item label="文件类型" prop="code">
          <el-select v-model="form.type" placeholder="Select" style="width: 240px">
            <el-option
                v-model="form.type"
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
      
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button icon="Switch" @click="handleSwitch">切换文件码</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="fileInfoList" border  stripe  @selection-change="handleSelectionChange">
      <el-table-column type="index" width="50" label="序号" />
      <el-table-column label="文件名" align="center">
        <template #default="scope">
            <span @dblclick="handlePreview(scope.row)" style="cursor: pointer;">{{ scope.row.fileRawName?scope.row.fileRawName:scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center" >
        <template #default="scope">
            <span @dblclick="handlePreview(scope.row)" style="cursor: pointer;">{{ scope.row.fileExtentions?scope.row.fileExtentions:"markdown" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Download" @click="handleDwonload(scope.row)" v-hasPermi="['manager:fileInfo:edit']">下载</el-button>
          <el-button link type="primary" icon="ChromeFilled" @click="handlePreview(scope.row)" v-hasPermi="['manager:fileInfo:remove']">预览</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
        v-show="total > 0"
        :total="total"
        :background="true"
        :current-page="form.pageNum"
        :page-size="form.pageSize"
        @current-change="query"
/>
  </div>

</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { queryCode,getFileInfo,preview,download,queryfile } from '@/api/index'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import {Base64} from 'js-base64'

const router = useRouter(); // 获取路由实例
const total = ref(0)
const showSearch = true
const fileInfoList = ref([])
const form = reactive({
    pageNum: 1, // 当前页码
    pageSize: 10, // 每页显示的记录数
    type: '',
    fileName: '',
})
function query(){
    queryCode().then(response => {
        fileInfoList.value = response.rows; // 假设响应数据中有一个名为 data 的属性，其中包含列表数据
        total.value = response.total; // 假设响应数据中有一个名为 total 的属性，其中包含总记录数
        console.log(total.value);
      }); 
}
query()

function resetQuery() {
    form.pageNum = 1; // 重置页码为1
    form.pageSize = 10; // 重置每页显示的记录数为10
    form.type = ''; // 重置其他查询
    form.fileName = ''; // 重置其他查询
    query(); // 重新查询 
}

function handleSwitch() {
    localStorage.removeItem('token'); // 移除 fileCode 键值对
    router.push('/'); // 跳转到登录页面
}

const options = [
  {
    value: 'file',
    label: '文件',
  },
  {
    value: 'md',
    label: 'Markdown文档',
  }]
function handleQuery(row) {
    if (form.type === '') {
        ElMessage.error('请选择文件类型');
        return;
    }
    queryfile(form).then(response => {
      fileInfoList.value = response.rows; // 假设响应数据中有一个名为 data 的属性，其中包含列表数据
      total.value = response.total; // 假设响应数据中有一个名为 total 的属性，其中包含总记录数
    });
}
function handleDwonload(row) {
    console.log(row);
    
    if (row.title) {
      ElMessage({
        message: '暂不支持下载Markdown文档,请前往预览页面进行下载',
        type: 'warning',
      });
    }else{
        download({"fileId":row.id}).then((response) => {
        const fileName = row.fileRawName // 获取文件名
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = decodeURIComponent(fileName); // 解码文件名
        link.click();
        URL.revokeObjectURL(link.href);
  }).catch((error) => {
    console.error('下载失败:', error);
  });
    }
}

function handlePreview(row) {
    if (row.title) {
      router.push({ path: '/preview', query: { fileId: row.id } });
    }else{
        preview({"fileId":row.id}).then(response => {
            const baseulrl = 'https://preview.wangziheng.site/onlinePreview?url='+encodeURIComponent(Base64.encode(response))
            window.open(baseulrl, '_blank');
        }) 
    }
 
}


</script>

<style scoped>
.app-container {
    padding: 20px;
    background-color: #f5f7fa;
    min-height: calc(100vh - 40px);
}

.el-form {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    margin-bottom: 20px;
}

.el-table {
    margin: 20px 0;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    border-radius: 8px;
    overflow: hidden;
}

.el-pagination {
    margin-top: 20px;
    justify-content: center;
    padding: 16px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.el-button {
    transition: all 0.3s;
}

.el-button:hover {
    transform: translateY(-2px);
}

.el-table-column--selection .cell {
    padding-left: 14px;
}

/* 响应式调整 */
@media (max-width: 768px) {
    .app-container {
        padding: 10px;
    }
    
    .el-form {
        padding: 15px;
    }
    
    .el-form-item {
        margin-bottom: 15px;
    }
}
</style>