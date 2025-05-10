<template>
    <div class="markdown-container">
        <div class="header">
            <el-button 
                type="primary" 
                @click="handleQuery"
                class="back-btn"
                icon="ArrowLeft"
            >
                返回列表
            </el-button>
        </div>
        <div class="editor-wrapper">
            <MdEditor 
                v-model="text" 
                :toolbars="toolbars" 
                :preview="true"
                class="full-height-editor"
            >
                <template #defToolbars>
                    <ExportPDF :modelValue="text" @onProgress="handleProgress" />
                </template>
            </MdEditor>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted,watch } from 'vue'
import { queryCode,getFileInfo,preview,download,queryfile } from '@/api/index'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import { ExportPDF } from '@vavt/v3-extension';
import '@vavt/v3-extension/lib/asset/ExportPDF.css';
import { toolbars } from "./data.js";

const route = useRoute(); // 获取路由实例
const router = useRouter(); // 获取路由实例
const id = 'preview-only'
const text = ref(''); // 用于存储Markdown文本内容的ref
watch(() => route.query.fileId, (newId) => {
  if(newId) {
    init(newId); // 根据新ID获取内容
  }
});
function handleQuery(){
    router.push("/index"); // 跳转到登录页面
}
function init(fileId){
    getFileInfo({"fileId":fileId,"type":"md"}).then(response => {
        text.value = response.data.content; // 假设响应数据中有一个名为 data 的属性，其中包含列表数据
        console.log(response); // 假设响应数据中有一个名为 data 的属性，其中包含列表数据
    }); 
}
init(route.query.fileId);



</script>

<style scoped>
.markdown-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    padding: 1rem;
    background-color: #f5f7fa;
}

.header {
    margin-bottom: 1rem;
}

.back-btn {
    padding: 0.75rem 1.5rem;
    font-size: 1rem;
    border-radius: 0.375rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.editor-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    background: white;
    border-radius: 0.5rem;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    overflow: hidden;
}

.full-height-editor {
    flex: 1;
    min-height: 0; /* 修复flex布局下的高度问题 */
}

/* 响应式调整 */
@media (max-width: 768px) {
    .markdown-container {
        padding: 0.5rem;
    }
    .back-btn {
        padding: 0.5rem 1rem;
        font-size: 0.875rem;
    }
}
</style>