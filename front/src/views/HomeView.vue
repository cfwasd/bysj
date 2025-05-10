<template>
  <div class="code" :style="{ backgroundImage: `url(${imgUrl})` }">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ title }}</h3>
      <el-form-item prop="fileCode" :icon="Key">
        <el-input
          v-model="fileCode"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="请输入文件码"
        >
          <template #prefix><el-icon><Key /></el-icon></template>
        </el-input>
      </el-form-item>
     
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width:100%;"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">确认</span>
          <span v-else>跳 转 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import { useRouter,useRoute } from "vue-router";
import { login } from "@/api/index";
import { ElMessage } from 'element-plus';

const screenWidth = ref(0);
const screenHeight = ref(0);
const fileCode = ref("");
const loading = ref(false); // Add this line
const title = "文件共享站";
const imgUrl = ref("");
const loginRef = ref(null); // Add this line for form reference

const router = useRouter();

const updateScreenSize = () => {
  screenWidth.value = window.innerWidth;
  screenHeight.value = window.innerHeight;
  imgUrl.value = `https://picsum.photos/${screenWidth.value}/${screenHeight.value}`;
};


onMounted(() => {
  updateScreenSize();
  window.addEventListener('resize', updateScreenSize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateScreenSize);
});

function handleLogin(){
  
  if(!fileCode.value){
    ElMessage.error("文件码不能为空");
    return;
  }
  loading.value = true;
  login({"code":fileCode.value}).then(res=>{
    if(res.code == 200){
      localStorage.setItem("token",fileCode.value);
      router.push("/index");
    }
  })
  loading.value = false;
}
</script>

<style scoped>
.code {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 改为视口高度 */
  width: 100vw; /* 添加视口宽度 */
  background-size: cover;
  position: relative;
}

.login-form {
  border-radius: 6px;
  opacity: 0.8;
  background: rgba(255, 250, 248, 0.76);
  padding: 25px 25px 5px 25px;
  max-width: 400px; /* 改为最大宽度 */
  width: 90%; /* 添加百分比宽度 */
  margin: 0 auto; /* 确保水平居中 */
  position: relative;
  box-sizing: border-box; /* 包含内边距在宽度内 */
}

/* 添加媒体查询适应手机端 */
@media (max-width: 768px) {
  .login-form {
    width: 95%;
    padding: 20px 15px;
  }
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  opacity: 0.9;
  background: rgba(255, 250, 248, 0.76);
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>