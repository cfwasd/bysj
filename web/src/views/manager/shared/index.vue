<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分享码" prop="sharedCode">
        <el-input
          v-model="queryParams.sharedCode"
          placeholder="请输入分享码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>

      <el-form-item label="是否过期" prop="isOutdate">
        <el-select v-model="queryParams.isOutdate" placeholder="Select" style="width: 240px">
          <el-option
              v-model="queryParams.isOutdate"
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
      </el-form-item>
    </el-form>



    <el-table v-loading="loading" :data="sharedList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="分享码" align="center" prop="sharedCode" />
      <el-table-column label="文件id" align="center" prop="fileId" />
      <el-table-column label="文件类型" align="center" prop="type" >
        <template #default="scope">
          <span>{{ (scope.row.type === "file" ? "file" : "markdown") }}</span>
        </template>
      </el-table-column>
      <el-table-column label=是否过期 align="center" prop="isOutdate" >
        <template #default="scope">
          <el-tag :type="scope.row.isOutdate === 1 ? 'danger' : 'success'">
            {{ scope.row.isOutdate === 1 ? '已过期' : '未过期' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数据更新时间" align="center" prop="updateDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:shared:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:shared:remove']">删除</el-button>
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

    <!-- 添加或修改文件分享相关对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="codeRef" :model="form"  label-width="80px">
        <el-form-item label="是否过期" prop="code">
          <el-select v-model="form.isOutdate" placeholder="Select" style="width: 240px">
            <el-option
                v-model="form.isOutdate"
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
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

<script setup name="Shared">
import { listShared, getShared, delShared, addShared, updateShared } from "@/api/manager/shared";
import {ElMessage} from "element-plus";

const { proxy } = getCurrentInstance();

const sharedList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const options = [
  {
    value: '1',
    label: '已过期',
  },
  {
    value: '0',
    label: '未过期',
  }]


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sharedCode: null,
    fileId: null,
    type: null,
    isOutdate: null,
    updateDate: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询文件分享相关列表 */
function getList() {
  loading.value = true;
  listShared(queryParams.value).then(response => {
    sharedList.value = response.rows;
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
    sharedCode: null,
    fileId: null,
    type: null,
    isOutdate: null,
    updateDate: null,
    createTime: null
  };
  proxy.resetForm("sharedRef");
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
  title.value = "添加文件分享相关";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getShared(_id).then(response => {
    if (response.data.isOutdate === 1){
      response.data.isOutdate = '已过期'
    }else {
      response.data.isOutdate = '未过期'
    }
    form.value = response.data;
    open.value = true;
    title.value = "修改文件分享相关";
  });
}

/** 提交按钮 */
function submitForm() {
  if (form.value.id != null) {
    updateShared(form.value).then(response => {
      if (response.code === 200){
        ElMessage.success(response.msg);
        open.value = false;
        getList();
      }else {
        ElMessage.error(response.msg);
      }

    });
  }
}


/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除文件分享相关编号为"' + _ids + '"的数据项？').then(function() {
    return delShared(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/shared/export', {
    ...queryParams.value
  }, `shared_${new Date().getTime()}.xlsx`)
}

getList();
</script>
