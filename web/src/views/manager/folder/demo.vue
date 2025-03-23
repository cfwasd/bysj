<template>
  <div style="margin: 10px">
    <div style="width: 100%; display: flex; flex-direction: row">
      <div style="margin-right: 4px">
        <el-upload
            ref="uploadRef"
            multiple
            :auto-upload="false"
            :show-file-list="false"
            v-model:file-list="uploadFileList"
            :on-change="uploadFileChange"
        >
          <template #trigger>
            <el-button size="small" @click="openUploadFile($event, null)"
            >上传文件</el-button
            >
          </template>
        </el-upload>
      </div>
      <div style="margin-right: 4px">
        <el-button size="small" @click="openInsertFile($event, null)">
          添加文件
        </el-button>
      </div>
      <div style="margin-right: 4px">
        <el-button size="small" @click="openInsertFolder($event, null)">
          添加文件夹
        </el-button>
      </div>
    </div>
  </div>
  <el-tree
      style="max-width: 600px"
      empty-text="没有文件，快去上传吧!"
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      :data="dataSource"
      draggable
      default-expand-all
      node-key="id"
      highlight-current
      @node-drag-start="handleDragStart"
      @node-drag-enter="handleDragEnter"
      @node-drag-leave="handleDragLeave"
      @node-drag-over="handleDragOver"
      @node-drag-end="handleDragEnd"
      @node-drop="handleDrop"
  >
    <template #default="scope">
      <div style="width: 100%; display: flex; justify-content: space-between">
        <div style="display: flex; justify-content: left">
          <div
              style="
              display: flex;
              flex-direction: column;
              justify-content: center;
            "
              v-if="scope.node.data.type === 'folder'"
          >
            <!-- 文件夹展示 -->
            <el-icon><Folder /></el-icon>
          </div>
          <div v-else>
            <!-- 文件展示 -->
            <el-icon><Document /></el-icon>
          </div>
          <div style="margin-left: 3px">{{ scope.node.label }}</div>
        </div>
        <div style="margin-right: 20px">
          <el-popover placement="right" trigger="hover">
            <template #reference>
              <el-icon><Tools /></el-icon>
            </template>
            <div class="but-list" style="display: flex; flex-direction: column">
              <div v-if="scope.node.data.type === 'folder'">
                <el-upload
                    ref="uploadRef"
                    multiple
                    :auto-upload="false"
                    :show-file-list="false"
                    v-model:file-list="uploadFileList"
                    :on-change="uploadFileChange"
                >
                  <template #trigger>
                    <el-button
                        style="width: 124px"
                        @click="openUploadFile($event, scope.node)"
                    >上传文件</el-button
                    >
                  </template>
                </el-upload>
              </div>

              <div v-if="scope.node.data.type === 'folder'" style="width: 100%">
                <el-button
                    style="width: 100%"
                    @click="openInsertFile($event, scope.node)"
                >
                  添加文件
                </el-button>
              </div>
              <div v-if="scope.node.data.type === 'folder'" style="width: 100%">
                <el-button
                    style="width: 100%"
                    @click="openInsertFolder($event, scope.node)"
                >添加文件夹</el-button
                >
              </div>
              <div style="width: 100%">
                <el-button
                    style="width: 100%"
                    @click="openUpdate($event, scope.node)"
                >
                  修改
                </el-button>
              </div>
              <div style="width: 100%">
                <el-button
                    style="width: 100%"
                    @click="openDelete($event, scope.node)"
                >删除</el-button
                >
              </div>
            </div>
          </el-popover>
        </div>
      </div>
    </template>
  </el-tree>

  <!-- 新增和修改文件火文件夹名称使用 -->
  <el-dialog
      v-model="fileDialogVisible"
      :title="dialogTitle"
      width="500"
      :before-close="handleClose"
      draggable
      class="rounded-dialog"
  >
    当前文件路径: {{ dialogPath }}
    <div style="display: flex; justify-content: center; margin-top: 10px">
      <el-input v-model="dialogData" placeholder="Please input" />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="confirm"> 确定 </el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog
      v-model="deleteDialogVisible"
      title="删除"
      width="500"
      draggable
      class="rounded-dialog"
  >
    你确定要删除该文件吗？
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="deleteFileOrFolder"> 确定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
// TODO 确保文件地址的唯一性
import type Node from "element-plus/es/components/tree/src/model/node";
import type { DragEvents } from "element-plus/es/components/tree/src/model/useDragNode";
import type {
  AllowDropType,
  NodeDropType,
} from "element-plus/es/components/tree/src/tree.type";

const handleDragStart = (node: Node, ev: DragEvents) => {
  console.log("drag start", node);
};
const handleDragEnter = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
  console.log("tree drag enter:", dropNode.label);
};
const handleDragLeave = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
  console.log("tree drag leave:", dropNode.label);
};

const handleDragOver = (draggingNode: Node, dropNode: Node, ev: DragEvents) => {
  console.log("tree drag over:", dropNode.label);
};

const handleDragEnd = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
  console.log(
      "tree drag end:",
      draggingNode.data.label,
      dropNode && dropNode.label,
      dropType
  );
};
const handleDrop = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
  console.log("tree drop:", dropNode.label, dropType);
};
const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
  return dropNode.data.type === "folder";
};
const allowDrag = (draggingNode: Node) => {
  console.log("allowDrag");
  return !draggingNode.data.label.includes("Level three 3-1-1");
};

const dataSource = ref([
  {
    label: "Level one 1",
    type: "folder",
    path: "/Level one 1",
    children: [
      {
        label: "Level two 1-1",
        type: "folder",
        path: "/Level one 1/Level two 1-1",
        children: [
          {
            type: "file",
            path: "/Level one 1/Level three 1-1-1/Level three 1-1-1",
            label: "Level three 1-1-1",
          },
        ],
      },
    ],
  },
]);

const fileDialogVisible = ref(false);
const dialogTitle = ref(""); // 新增文件|文件夹,修改
const dialogData = ref("");
const dialogPath = ref("/");
const deleteDialogVisible = ref(false);
const fileOrFolderNode = ref();

const handleClose = (done: () => void) => {
  cancel();
  done();
};

/**
 * 取消
 */
const cancel = () => {
  fileDialogVisible.value = false;
};

/**
 * 确定
 */
const confirm = () => {
  console.log("confirm: ", fileOrFolderNode);
  if (dialogTitle.value === "新增文件" || dialogTitle.value === "新建文件夹") {
    let data = {
      label: dialogData.value,
      type: "",
      children: [],
      path: "/" + dialogData.value,
    };
    if (fileOrFolderNode.value) {
      data.path = fileOrFolderNode.value.data.path + "/" + dialogData.value;
    }
    if (dialogTitle.value === "新增文件") {
      data.type = "file";
    } else {
      data.type = "folder";
    }
    append(fileOrFolderNode.value, data);
  } else {
    // 修改处理
    let parent = null;
    let data = {
      label: dialogData.value,
      type: fileOrFolderNode.value.data.type,
      children: fileOrFolderNode.value.data.children,
      path: "/" + dialogData.value,
    };
    if (fileOrFolderNode.value.parent.level != 0) {
      parent = fileOrFolderNode.value.parent;
      data.path = parent.data.path + "/" + data.label;
    }
    updateTreeNode(parent, fileOrFolderNode.value.data, data);
  }
  fileDialogVisible.value = false;
};

const openInsertFile = (even, node) => {
  if (node) {
    dialogPath.value = node.data.path + "/";
  } else {
    dialogPath.value = "/";
  }
  dialogTitle.value = "新增文件";
  dialogData.value = "";
  fileOrFolderNode.value = node;
  fileDialogVisible.value = true;
};

/**
 * 开始修改
 * @param even
 * @param node
 */
const openUpdate = (even, node) => {
  if (node) {
    dialogPath.value = node.data.path;
  } else {
    dialogPath.value = "/";
  }
  fileDialogVisible.value = true;
  fileOrFolderNode.value = node;
  dialogData.value = fileOrFolderNode.value.data.label;
  dialogTitle.value = "修改";
};

const openInsertFolder = (even, node) => {
  if (node) {
    dialogPath.value = node.data.path;
  } else {
    dialogPath.value = "/";
  }
  dialogData.value = "";
  fileOrFolderNode.value = node;
  dialogTitle.value = "新建文件夹";
  fileDialogVisible.value = true;
};

const openDelete = (even, node) => {
  fileOrFolderNode.value = node;
  deleteDialogVisible.value = true;
};

const deleteFileOrFolder = (even) => {
  // 删除该文件
  remove(fileOrFolderNode.value, fileOrFolderNode.value.data);
  deleteDialogVisible.value = false;
};

/**
 * 添加
 * @param node 父节点
 * @param data 要添加的数据
 */
const append = (node, data) => {
  if (isNameDuplicate(node, null, data)) {
    ElMessage.error("文件名重复");
    return;
  }
  const newChild = data;
  if (node) {
    if (!node.data.children) {
      node.data.children = [];
    }
    node.data.children.push(newChild);
  } else {
    dataSource.value.push(newChild);
  }
};

/**
 * 删除
 * @param node 节点
 * @param data 数据
 */
const remove = (node: Node, data) => {
  console.log("all data:", dataSource.value);
  const parent = node.parent;
  const children = parent.data.children || parent.data;
  const index = children.findIndex((d) => d.path === data.path);
  children.splice(index, 1);
  dataSource.value = [...dataSource.value];
};

const updateTreeNode = (parentNode, oldData, newData) => {
  console.log(
      "parentNode:",
      parentNode,
      "oldData:",
      oldData,
      "newData:",
      newData
  );
  if (isNameDuplicate(parentNode, oldData, newData)) {
    ElMessage.error("文件名重复");
    return;
  }
  let index: number;
  if (parentNode && parentNode.data) {
    // 查找 newData.path 在 parentNode.data.children 中的索引
    index = parentNode.data.children.findIndex(
        (child) => child.path === oldData.path
    );
    // 如果找到索引，则更新该位置的数据
    if (index !== -1) {
      parentNode.data.children[index] = newData;
    } else {
      console.error(
          "找不到, index:",
          index,
          "parentNode.data",
          parentNode.data,
          "newData",
          newData
      );
    }
  } else {
    index = dataSource.value.findIndex((item) => item.path === oldData.path);
    // 如果找到索引，则更新该位置的数据
    if (index !== -1) {
      dataSource.value[index] = newData;
    } else {
      console.error(
          "找不到, index:",
          index,
          "parentNode.data",
          parentNode.data,
          "newData",
          newData
      );
    }
  }

  console.log(dataSource.value);
};

/**
 * 判断名称是否有相同的
 */
const isNameDuplicate = (parentNode, oldData, newData) => {
  console.log(
      "parentNode:",
      parentNode,
      "oldData:",
      oldData,
      "newData: ",
      newData
  );
  if (oldData && oldData.label === newData.label) return false;
  if (parentNode) {
    for (let i = 0; i < parentNode.data.children.length; i++) {
      const child = parentNode.data.children[i];
      if (child.label === newData.label) {
        return true;
      }
    }
  } else {
    for (let i = 0; i < dataSource.value.length; i++) {
      const child = dataSource.value[i];
      if (child.label === newData.label) {
        return true;
      }
    }
  }

  return false;
};

/**
 * 文件上传操作
 */
const uploadFileList = ref([]);

const openUploadFile = (even, node) => {
  if (node) {
    dialogPath.value = node.data.path;
  } else {
    dialogPath.value = "/";
  }
  fileOrFolderNode.value = node;
};

const uploadFileChange = (uploadFile, uploadFiles) => {
  let data = {
    label: uploadFile.name,
    type: "file",
    path: "/" + uploadFile.name,
    uid: uploadFile.uid,
  };
  if (fileOrFolderNode.value) {
    data.path = fileOrFolderNode.value.data.path + "/" + uploadFile.name;
  }
  append(fileOrFolderNode.value, data);
};
function flattenTree(trees) {
  const result = [];

  function traverse(node) {
    // 添加当前节点到结果数组
    result.push({
      label: node.label,
      type: node.type,
      path: node.path,
    });

    // 如果节点有children，则递归遍历它们
    if (Array.isArray(node.children)) {
      node.children.forEach(traverse);
    }
  }

  // 遍历输入的树数组
  trees.forEach((tree) => {
    traverse(tree); // 从每个树的根节点开始遍历
  });

  // 返回结果数组
  return result;
}

console.log("dataSource.value:", dataSource.value);
console.log("result:", flattenTree(dataSource.value));
</script>

<style>
.rounded-dialog {
  border-radius: 10px;
}

.but-list > div {
  margin: 2px;
}
</style>
