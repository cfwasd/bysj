<template>
  <MdEditor v-model="text" :toolbars="toolbars">
    <template #defToolbars>
      <ExportPDF :modelValue="text" @onProgress="handleProgress" />
    </template>
  </MdEditor>
  <div class="button-group">
    <el-button 
      type="primary" 
      @click="fetchContent"
      size="small"
      icon="Refresh"
      :loading="loading"
    >刷新内容</el-button>
    <el-button 
      type="success" 
      @click="saveContent"
      size="small"
      icon="Check"
      :loading="loading"
    >保存修改</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import { ExportPDF } from '@vavt/v3-extension';
import '@vavt/v3-extension/lib/asset/ExportPDF.css';
import { toolbars } from "./data.js";
import { useRoute } from 'vue-router';
import { getInfo,updateInfo } from '@/api/manager/info';
import { watch } from 'vue';

import {ElMessage} from "element-plus";


const route = useRoute();
const text = ref('');
const loading = ref(false);

watch(() => route.query.id, (newId) => {
  if(newId) {
    fetchContent(newId); // 根据新ID获取内容
  }
});



// 获取文档内容
const fetchContent = async () => {
  if (route.query.id) {
    loading.value = true;
    try {
      const response = await getInfo(route.query.id);
      text.value = response.data.content || '';
    } finally {
      loading.value = false;
    }
  }
};

function saveContent() {
  loading.value = true;
  const reqData = {
    id: route.query.id, // 假设你有一个文档ID
    content: text.value // 文档内容
  };
  updateInfo(reqData).then(() => {
    ElMessage.success('保存成功');
  }).finally(() => {
    loading.value = false;
  });
}

onMounted(() => {
  fetchContent();
});

const handleProgress = (progress) => {
  console.log(`Export progress: ${progress.ratio * 100}%`);
};
</script>



<style scoped>
.button-group {
  margin-top: 30px;
  margin-left: 40%;
  display: flex;
  gap: 10px;
}
</style>
