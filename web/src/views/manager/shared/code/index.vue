<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分享码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入分享码"
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
          v-hasPermi="['manager:code:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="codeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="分享码" align="center" prop="code" />
      <el-table-column label="是否过期" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'danger' : 'success'">
            {{ scope.row.status === 1 ? '已过期' : '未过期' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:code:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:code:remove']">删除</el-button>
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

    <!-- 添加或修改分享码管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="codeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="是否停用" prop="code">
          <el-select v-model="form.status" placeholder="Select" style="width: 240px">
            <el-option
                v-model="form.status"
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

<script setup name="Code">
import { listCode, getCode, delCode, addCode, updateCode } from "@/api/manager/code";
import {ElMessage} from "element-plus";

const { proxy } = getCurrentInstance();

const codeList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const options = [
  { value: '1', label: '已过期' },
  { value: '0', label: '未过期' }
];

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: null,
    status: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询分享码管理列表 */
function getList() {
  loading.value = true;
  listCode(queryParams.value).then(response => {
    codeList.value = response.rows;
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
    code: null,
    status: null,
    createTime: null
  };
  proxy.resetForm("codeRef");
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
    addCode(form).then(response => {
      if (response.code === 200) {
        ElMessage.success(response.msg);
      }else {
        ElMessage.error(response.msg);
      }
      listCode(queryParams.value).then(response => {
        codeList.value = response.rows;
        total.value = response.total;
        loading.value = false;
      });
    })
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getCode(_id).then(response => {
    if (response.data.status === 1){
      response.data.status = '已过期'
    }else {
      response.data.status = '未过期'
    }
    form.value = response.data;
    open.value = true;
    title.value = "修改分享码管理";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["codeRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateCode(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCode(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除分享码管理编号为"' + _ids + '"的数据项？').then(function() {
    return delCode(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/code/export', {
    ...queryParams.value
  }, `code_${new Date().getTime()}.xlsx`)
}

getList();
</script>
