<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件夹名称" label-width="auto" prop="floderName">
        <el-input
          v-model="queryParams.floderName"
          placeholder="请输入文件夹名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['manager:folder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:folder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:folder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['manager:folder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
<el-text class="mx-1" type="success" size="large">当前目录:{{floder}}</el-text>
<!--    <el-table v-loading="loading" :data="folderList" @selection-change="handleSelectionChange">-->
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="文件夹名称" align="center" prop="floderName" />-->
<!--      <el-table-column label="父文件夹名称" align="center" prop="pFloderName" />-->
<!--      <el-table-column label="文件个数" align="center" prop="fileCount" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template #default="scope">-->
<!--          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:folder:edit']">修改</el-button>-->
<!--          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:folder:remove']">删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->
    
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

<script setup name="Folder">
import { listFolder, getFolder, delFolder, addFolder, updateFolder } from "@/api/manager/folder";

const { proxy } = getCurrentInstance();
let floder = ref("/")

const folderList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    floderName: null,
    pFloderName: null,
    fileCount: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询逻辑文件夹列表 */
function getList() {
  loading.value = true;
  listFolder(queryParams.value).then(response => {
    folderList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
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
    pFloderName: null,
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
  title.value = "添加逻辑文件夹";
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
        addFolder(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
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

getList();
</script>
