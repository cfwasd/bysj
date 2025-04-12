<template>
  <div class="app-container">
<el-text class="mx-1" type="success" size="large">当前目录:{{floder}}</el-text>
    <div class="common-layout">
      <el-container>
        <el-aside class="aside">
          <el-tree
              style="max-width: 200px"
              ref="treeRef"
              node-key="id"
              :highlight-current="true"
              :data="folderData"
              :props="defaultProps"
              @node-click="handleNodeClick">
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                    <span>{{ node.label }} &nbsp;
                      <el-text type="primary" link @click="appendFolder(data)">
                        <el-icon v-hasPermi="['manager:folder:add']" ><FolderAdd /></el-icon>
                      </el-text>
                    </span>
              </div>
            </template>
          </el-tree>
        </el-aside>
        <el-main>
          <el-table :data="tableData">
            <el-table-column prop="id" label="文件id"  />
            <el-table-column prop="fileRawName" label="文件名" />
            <el-table-column fixed="right" label="操作" min-width="120">
              <template #default="scope">
                <el-button link type="primary" size="small" @click="previewFile(scope.row.ossUrl)">
                  预览文件
                </el-button>
                <el-button link type="primary" size="small" @click="removeFile(scope.row.id,scope.row.folderId)">删除文件</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </div>



    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改逻辑文件夹对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="folderRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件夹名称" prop="floderName">
          <el-input v-model="form.floderName" placeholder="请输入文件夹名称" />

        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Folder">
import { listFolder, getFolder, delFolder, addFolder, updateFolder,deepTree } from "@/api/manager/folder";
import { queryByTreeId,delFileInfo } from '@/api/manager/fileInfo'
import {Tree,File} from './data'
import {ElMessage, TreeInstance} from 'element-plus'
import {ref} from "vue";

const { proxy } = getCurrentInstance();
let floder = ref("/")
const folderData = ref<Tree>()
const treeRef = ref<TreeInstance>()
const pName = ref("/")
const folderList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const tableData:File = ref<Array<File>>([])
const treeId:number = ref<number>(1)


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    treeId: treeId,
    floderName: null,
    pFloderName: null,
    fileCount: null
  },
  rules: {
  }
});

const defaultProps = {
  children: 'children',
  label: 'floderName',
}

const { queryParams, form, rules } = toRefs(data);

/** 查询逻辑文件夹列表 */
function getList() {
  loading.value = true;
  queryByTreeId(queryParams.value).then(response => {
    tableData.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
function  appendFolder(data){
  // console.log(data.floderName);
  reset();
  pName.value = data.floderName;
  open.value = true;
  title.value = "添加文件夹";
}
function previewFile(ossUrl: string){
  console.log("ossUrl: ", ossUrl);
}
function removeFile(fileId: number,folderId: number) {
  delFileInfo(fileId).then(response => {
    if (response.code === 200) {
      queryFileData(folderId)
    }
  })
}

const handleNodeClick = (data: Tree) =>{
  let temp= treeRef.value.getNode(data)
  queryFileData(data.id)
  floder.value = "/"+ handleFolder(temp,temp.data.floderName)
}

function handleFolder(d,resultName) {
  if (d.parent.level == 0){
    return resultName
  }else{
    let res = d.parent.data.floderName +"/"+ resultName
    return handleFolder(d.parent,res)
  }
}

function queryFileData(TreeId: Number){
  queryParams.value.treeId = TreeId;
  queryByTreeId(queryParams.value).then(response => {
    tableData.value = response.rows;
    total.value = response.total;
    loading.value = false;
  })
}

function queryDeepTree(){
  deepTree(queryParams.value).then(response => {
    // console.log("wwww"+JSON.stringify(response.data))
    folderData.value = response.data;
  })
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    floderName: null,
    pFloderName: pName.value,
    fileCount: null
  };
  proxy.resetForm("folderRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加文件夹";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getFolder(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改逻辑文件夹";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["folderRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateFolder(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        form.value.pFloderName = pName.value;
        addFolder(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          init();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除逻辑文件夹编号为"' + _ids + '"的数据项？').then(function() {
    return delFolder(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/folder/export', {
    ...queryParams.value
  }, `folder_${new Date().getTime()}.xlsx`)
}

/**
 * 下面执行初始化需要调用的函数
 */
function init(){
  queryFileData(1);
  queryDeepTree();
  getList();
}


init();
</script>

<style>
.aside{
  background: #ffffff;
  padding: 0px 0px;
  margin-bottom: 5px;
  border-radius: 2px;
  display: block;
  line-height: 32px;
  font-size: 16px;
  width: 20%;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
  color: #2c3e50;
  -webkit-font-smoothing: antialiased;
}
</style>
