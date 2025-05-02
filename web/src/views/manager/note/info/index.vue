<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文档标题" prop="title">
        <el-input
            v-model="queryParams.title"
            placeholder="请输入文档标题"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createdAt">
        <el-date-picker clearable
                        v-model="queryParams.createdAt"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间" prop="updatedAt">
        <el-date-picker clearable
                        v-model="queryParams.updatedAt"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择更新时间">
        </el-date-picker>
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
            v-hasPermi="['manager:info:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['manager:info:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['manager:info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            :disabled="multiple"
            @click="handleshared"
            v-hasPermi="['manager:info:export']"
        >分享</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文档ID" align="center" prop="id" />
      <el-table-column label="文档标题" align="center" prop="title" />
<!--      <el-table-column label="文档内容" align="center" prop="content" />-->
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180" />
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180" />
<!--      <el-table-column label="删除状态" align="center" prop="isDeleted" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:info:edit']">修改标题</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdateContent(scope.row)" v-hasPermi="['manager:info:edit']">修改内容</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:info:remove']">删除</el-button>
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

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="htitle" v-model="open" width="500px" append-to-body>
      <el-form ref="infoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文档标题" prop="title">
          <el-input v-model="title" placeholder="请输入文档标题" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>


    <!--    文件分享对话框-->
    <el-dialog title="文件分享" v-model="isOpen" width="500px" append-to-body>
      <el-form ref="codeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="请选择分享码" label-width="160px" prop="code">
          <el-select v-model="sharedFrom.sharedCode" placeholder="Select" style="width: 240px">
            <el-option
                v-model="sharedFrom.sharedCode"
                v-for="item in options"
                :key="item.id"
                :label="item.code"
                :value="item.code"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="shared">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Info">
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/manager/info";
import router from "../../../../router/index.js";
import {ElMessage} from "element-plus";
import { addShared } from '@/api/manager/shared'
import { listCode } from '@/api/manager/code'

const { proxy } = getCurrentInstance();

const infoList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const htitle = ref("");
const options = ref([])
const isOpen = ref(false);

const sharedFrom = reactive({
  sharedCode:"",
  fileId:"",
  type:"md",
  isOutdate: 0,
})


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: null,
    content: null,
    createdAt: null,
    updatedAt: null,
    isDeleted: null
  },
  rules: {
    title: [
      { required: true, message: "不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询【请填写功能名称】列表 */
function getList() {
  loading.value = true;
  listInfo(queryParams.value).then(response => {
    infoList.value = response.rows;
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
    title: null,
    content: null,
    createdAt: null,
    updatedAt: null,
    isDeleted: null
  };
  proxy.resetForm("infoRef");
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
  htitle.value = "添加Markdown文档";
}

function submitForm(){
  addInfo({"title": title.value}).then(response => {
    console.log(response)
    if (response.code === 200) {
      ElMessage.success(response.msg);
      open.value = false;
      getList(); // 刷新列表数据
      router.push({
        path: '/note/mdedit',
        query: {
          id: response.data
        }
      });
    }else{
      ElMessage.error(response.msg);
    }
  })
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getInfo(_id).then(response => {
    form.value = response.data;
    open.value = true;
    htitle.value = "修改Markdown文档";
  });
}

/** 修改内容按钮操作 */
function handleUpdateContent(row) {
  router.push({
    path: '/note/mdedit',
    query: {
      id: row.id
    }
  });
}



/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除Markdown文档编号为"' + _ids + '"的数据项？').then(function() {
    return delInfo(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/info/export', {
    ...queryParams.value
  }, `info_${new Date().getTime()}.xlsx`)
}

function handleshared(row){
  isOpen.value = true;
  listCode({"status":0}).then(response => {
    options.value = response.rows
  })
}
function shared(){
  let files = "";
  for (let i = 0; i<ids.value.length; i++) {
    if (i === 0) {
      files = ids.value[i];
    }else {
      files += ","+ids.value[i];
    }
  }
  sharedFrom.fileId = files;
  addShared(sharedFrom).then(response => {
    if (response.code === 200){
      ElMessage.success(response.msg);
      isOpen = false;
    }else {
      ElMessage.error(response.msg);
    }
  })
}
getList();
</script>
