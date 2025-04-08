<template>
  <div class="top">
    请选择上传目录:
    <el-tree-select
        v-model="value"
        :data="data"
        :render-after-expand="false"
        show-checkbox
        style="width: 240px"
    />
  </div>
  <div class="container">
  <el-upload
      class="upload-demo"
      drag
      action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
      multiple
      :auto-upload="false"
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">
      拖拽文件到此上传 <em>点击上传</em>
    </div>
    <template #tip>
      <div class="el-upload__tip">
        jpg/png 等图片文件限制 50mb
      </div>
    </template>
  </el-upload>
  </div>

  <div class="bottom">
    <el-button type="primary" round>提交</el-button>
  </div>
</template>

<script setup lang="ts">
import { UploadFilled } from '@element-plus/icons-vue'
import { deepTree } from "@/api/manager/folder";

let data = ref('')
let value = ref('')



function queryDeepTree(){
  deepTree().then(response => {
    // console.log("wwww"+JSON.stringify(response.data))
    data.value = response.data;
  })
}


function upload(){
  console.log(value.value)
}
function  init(){
  queryDeepTree()
}
init()
</script>

<style scoped>
.container{
  margin: 8% 4% 4%;
}
.bottom{
  margin-top:4%;
  margin-left: 80%;
}
</style>
