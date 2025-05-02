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
  
        <el-form-item label="是否过期" prop="isExpired">
          <el-select v-model="queryParams.isExpired" placeholder="请选择" style="width: 240px">
            <el-option
                v-for="item in statusOptions"
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
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAdd"
          >新增</el-button>
        </el-col>
        </el-row>
  
      <el-table v-loading="loading" :data="codeList">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="分享码" align="center" prop="code" />
        <el-table-column label="是否过期" align="center" prop="isExpired">
          <template #default="scope">
            <el-tag :type="scope.row.isExpired === '1' ? 'danger' : 'success'">
              {{ scope.row.isExpired === '1' ? '已过期' : '未过期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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
  
      <!-- 添加或修改分享码对话框 -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="分享码" prop="code">
            <el-input v-model="form.code" placeholder="请输入分享码" />
          </el-form-item>
          <el-form-item label="是否过期" prop="isExpired">
            <el-select v-model="form.isExpired" placeholder="请选择">
              <el-option
                v-for="item in statusOptions"
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
  
  <script setup name="ShareCode">
  import { ref, reactive } from 'vue';
  
  const { proxy } = getCurrentInstance();
  
  const codeList = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const total = ref(0);
  const title = ref("");
  
  const statusOptions = [
    { value: '1', label: '已过期' },
    { value: '0', label: '未过期' }
  ];
  
  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      code: null,
      isExpired: null
    },
    rules: {
      code: [{ required: true, message: "分享码不能为空", trigger: "blur" }],
      isExpired: [{ required: true, message: "请选择是否过期", trigger: "change" }]
    }
  });
  
  const { queryParams, form, rules } = toRefs(data);

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

  /** 查询分享码列表 */
  function getList() {
    loading.value = true;
    // 这里留空API接口
    // listShareCode(queryParams.value).then(response => {
    //   codeList.value = response.rows;
    //   total.value = response.total;
    //   loading.value = false;
    // });
    loading.value = false;
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
  
  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加分享码";
  }
  
  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    // 这里模拟数据，实际应从API获取
    form.value = { ...row };
    open.value = true;
    title.value = "修改分享码";
  }
  
  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["formRef"].validate(valid => {
      if (valid) {
        // 这里留空API接口
        // if (form.value.id != null) {
        //   updateShareCode(form.value).then(response => {
        //     proxy.$modal.msgSuccess("修改成功");
        //     open.value = false;
        //     getList();
        //   });
        // } else {
        //   addShareCode(form.value).then(response => {
        //     proxy.$modal.msgSuccess("新增成功");
        //     open.value = false;
        //     getList();
        //   });
        // }
        proxy.$modal.msgSuccess("操作成功");
        open.value = false;
        getList();
      }
    });
  }
  
  /** 删除按钮操作 */
  function handleDelete(row) {
    proxy.$modal.confirm('是否确认删除分享码为"' + row.code + '"的数据项？').then(() => {
      // 这里留空API接口
      // return delShareCode(row.id);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }
  
  // 初始化加载
  getList();
  </script>