<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label-width="auto" label="文件逻辑位置" prop="folder">
        <el-input
          v-model="queryParams.folder"
          placeholder="请输入文件逻辑位置"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label-width="auto" label="文件原始名" prop="fileRawName">
        <el-input
          v-model="queryParams.fileRawName"
          placeholder="请输入文件原始名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label-width="auto" label="文件后缀名" prop="fileExtentions">
        <el-input
          v-model="queryParams.fileExtentions"
          placeholder="请输入文件后缀名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label-width="auto" label="文件oss地址" prop="ossUrl">
        <el-input
          v-model="queryParams.ossUrl"
          placeholder="请输入文件oss地址"
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
          v-hasPermi="['manager:fileInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:fileInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:fileInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['manager:fileInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fileInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件uuid" align="center" prop="uuid" />
      <el-table-column label="文件逻辑位置" align="center" prop="folder" />
      <el-table-column label="文件服务器位置" align="center" prop="fileRealFolder" />
      <el-table-column label="文件原始名" align="center" prop="fileRawName" />
      <el-table-column label="文件后缀名" align="center" prop="fileExtentions" />
      <el-table-column label="文件oss地址" align="center" prop="ossUrl" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:fileInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:fileInfo:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改文件信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="fileInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件逻辑位置" prop="folder">
          <el-input v-model="form.folder" placeholder="请输入文件逻辑位置" />
        </el-form-item>
        <el-form-item label="文件服务器位置" prop="fileRealFolder">
          <el-input v-model="form.fileRealFolder" placeholder="请输入文件服务器位置" />
        </el-form-item>
        <el-form-item label="文件原始名" prop="fileRawName">
          <el-input v-model="form.fileRawName" placeholder="请输入文件原始名" />
        </el-form-item>
        <el-form-item label="文件后缀名" prop="fileExtentions">
          <el-input v-model="form.fileExtentions" placeholder="请输入文件后缀名" />
        </el-form-item>
        <el-form-item label="文件oss地址" prop="ossUrl">
          <el-input v-model="form.ossUrl" placeholder="请输入文件oss地址" />
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

<script setup name="FileInfo">
import { listFileInfo, getFileInfo, delFileInfo, addFileInfo, updateFileInfo } from "@/api/manager/fileInfo";

const { proxy } = getCurrentInstance();

const fileInfoList = ref([]);
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
    uuid: null,
    folder: null,
    folderId: null,
    fileRealFolder: null,
    fileRawName: null,
    fileName: null,
    fileExtentions: null,
    ossUrl: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询文件信息列表 */
function getList() {
  loading.value = true;
  listFileInfo(queryParams.value).then(response => {
    fileInfoList.value = response.rows;
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
    uuid: null,
    folder: null,
    folderId: null,
    fileRealFolder: null,
    fileRawName: null,
    fileName: null,
    fileExtentions: null,
    ossUrl: null
  };
  proxy.resetForm("fileInfoRef");
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
  title.value = "添加文件信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getFileInfo(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改文件信息";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["fileInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateFileInfo(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addFileInfo(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除文件信息编号为"' + _ids + '"的数据项？').then(function() {
    return delFileInfo(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/fileInfo/export', {
    ...queryParams.value
  }, `fileInfo_${new Date().getTime()}.xlsx`)
}

getList();
</script>
