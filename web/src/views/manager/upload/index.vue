<template>
  <div class="top">
    请选择上传目录:
    <el-tree-select
        v-model="fromData.id"
        :data="data"
        :render-after-expand="false"
        style="width: 240px"
    />
  </div>
  <div class="container">
  <el-upload
      class="upload-demo"
      drag
      multiple
      v-model:file-list="fromData.fileList"
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
    <el-button type="primary" round @click="uploadFiles">提交</el-button>
  </div>
</template>

<script setup lang="ts">
import { UploadFilled } from '@element-plus/icons-vue'
import { deepTree } from "@/api/manager/folder";
import { uploadFile } from '@/api/manager/upload'
import {Tree} from "../folder/data";
import {reactive} from "vue";
import {ElMessage} from "element-plus";

let data = ref<Tree>('')
let fromData = reactive({
  fileList: [],
  id: ''
})


function queryDeepTree(){
  deepTree().then(response => {
    // console.log("wwww"+JSON.stringify(response.data))
    let temp = JSON.stringify(response.data)
        .replace(/id/g,"value")
        .replace(/floderName/g,"label")
    console.log(JSON.parse(temp))
    data.value = JSON.parse(temp)
  })
}


function uploadFiles(){
  console.log(fromData)
  const fd = new FormData()
  fd.append("folderId", fromData.id)
  fromData.fileList.forEach(item=>{
    // console.log(item)
    fd.append("file", item.raw)
  })
  uploadFile(fd).then(response => {
    if (response.code == 200){
      ElMessage({
        message: response.msg,
        type: 'success',
      })
      fromData.fileList = []
    }
    else {
      ElMessage({
        message: response.data,
        type: 'error',
      })
    }
  })

}


function  init(){
  queryDeepTree()
}
init()
</script>

<style scoped>
.top{
  margin-top: 4%;
  margin-left: 4%;
}
.container{
  margin: 8% 4% 4%;
}
.bottom{
  padding-top: 1%;
  margin-top:1%;
  margin-left: 80%;
}
</style>
