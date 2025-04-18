<!-- 收藏目录 -->
<template>
  <div class="coll_pop" v-if="!person.isCloseColl">
    <div class="coll_box">
      <div class="coll_top flex">
        <div>+加入资源篮</div>
        <el-icon @click="person.isCloseColl=!person.isCloseColl"><Close /></el-icon>
      </div>
      <div class="coll_center">
        <div class="coll_title flex">
          <div class="coll_title_back" @click="backPrev">返回上一级 </div>
          <div class="coll_title_nav flex">
            <el-breadcrumb :separator-icon="ArrowRight" separator="/">
              <el-breadcrumb-item v-for="(nav,navIndex) in person.navList" :key="navIndex" @click="navChange(nav,navIndex)">
                <a href="#">{{nav.name}}</a>
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
      </div>
      <div class="coll_cont">
        <div class="cont_scroll">
          <div class="cont_list" v-if="person.collList&&person.collList.length>0">
            <RightClickMenu ref="newbuilt" @newBuilt="GetNewBuilt" @openNew="GetOpenNew"></RightClickMenu>
            <div v-for="(col,colIndex) of person.collList" :key="colIndex">
              <el-tooltip class="box-item" effect="light" placement="right-end" :content="col.name">
                <div class="cont_item" @contextmenu.prevent="collClick(col,colIndex)" :class="[person.collActive===col.Id?'cont_item_active':'']" @dblclick="doubleClick(col,colIndex)" @click="collClick(col,colIndex)">
                  <RightClickCurrFile ref="currfile" v-if="person.currFile.Id===col.Id" @onMenus="GetonMenus" @openCurr="GetOpenCurr" :currItem="person.currFile"></RightClickCurrFile>
                  <div class="cont_item_ok" v-if="person.collActive===col.Id">
                    <svg t="1684373978603" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9781" width="18" height="18"><path d="M510.567403 63.852903c-246.304387 0-446.663336 200.358949-446.663336 446.663336 0 246.304387 200.358949 446.663336 446.663336 446.663336 246.304387 0 446.765664-200.358949 446.765664-446.663336C957.230738 264.211852 756.87179 63.852903 510.567403 63.852903L510.567403 63.852903zM787.979614 386.084941 454.593784 719.573099c-7.981613 7.981613-20.977316 7.981613-28.958929 0l-43.694214-43.694214c0 0 0 0 0 0L237.145998 531.084241c-7.981613-7.981613-7.981613-20.977316 0-28.958929l43.694214-43.694214c7.981613-7.981613 20.977316-7.981613 28.958929 0L440.063156 588.592785 715.326471 313.329469c7.981613-7.981613 20.977316-7.981613 29.061257 0L787.979614 357.126012C796.063556 365.107625 796.063556 378.103328 787.979614 386.084941L787.979614 386.084941z" p-id="9782" fill="#2080F7"></path></svg>
                  </div>
                  <svg t="1684303283032" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3201" width="48" height="48"><path d="M0 128a51.2 51.2 0 0 1 51.2-51.2h350.24a51.2 51.2 0 0 1 47.0592 31.0336L473.6 166.4h499.2a51.2 51.2 0 0 1 51.2 51.2v537.6a51.2 51.2 0 0 1-51.2 51.2H51.2a51.2 51.2 0 0 1-51.2-51.2V128z" fill="#FFA000" p-id="3202"></path><path d="M89.6 249.6m51.2 0l742.4 0q51.2 0 51.2 51.2l0 460.8q0 51.2-51.2 51.2l-742.4 0q-51.2 0-51.2-51.2l0-460.8q0-51.2 51.2-51.2Z" fill="#FFFFFF" p-id="3203"></path><path d="M0 332.8m51.2 0l921.6 0q51.2 0 51.2 51.2l0 512q0 51.2-51.2 51.2l-921.6 0q-51.2 0-51.2-51.2l0-512q0-51.2 51.2-51.2Z" fill="#FFCA28" p-id="3204"></path></svg>
                  <el-input ref="itemInput" class="cont_item_input" @focus="onFocus($event)" @blur="InputBlur($event,col,0)" v-if="person.isClick&&colIndex===0" v-model="col.Name" />
                  <el-input ref="itemInput" class="cont_item_input" @focus="onFocus($event)" @blur="InputBlur($event,col,1)" v-else-if="col.isDoubleClick" v-model="col.Name" />
                  <div class="cont_item_name" v-else @dblclick.stop="doubleReName()">{{col.name}}</div>
                </div>
              </el-tooltip>
            </div>
          </div>
          <div class="cont_null" v-else>
            <div>当前文件夹还没有内容哦~, 快试试新建吧！</div>
            <el-button @click="newFileBtn">+新建文件夹</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang='ts' setup>
import { onMounted, reactive, nextTick, ref, onUnmounted, watch } from 'vue';
import { ArrowRight } from '@element-plus/icons-vue'
import RightClickMenu from '@/components/collectCata/rightClickMenu.vue'
import RightClickCurrFile from '@/components/collectCata/rightClickCurrFile.vue'
import { ElMessage,ElMessageBox } from 'element-plus'
const person = reactive({
  collList1:[
    {level:1,Id:1,isDoubleClick:false,name:'物理'},
    {
      level:1,
      Id:2,
      isDoubleClick:false,
      name:'化学',
      list:[
        {
          level:2,
          Id:222,
          isDoubleClick:false,
          name:'化学1',
          list:[]
        },
        {
          level:2,
          Id:333,
          isDoubleClick:false,
          name:'化学2',
          list:[
            {
              level:3,
              Id:854,
              isDoubleClick:false,
              name:'化学22',
              list:[]
            }
          ]
        }
      ]
    },
    {level:1,Id:3,isDoubleClick:false,name:'生物'},
    {level:1,Id:4,isDoubleClick:false,name:'语文'},
    {level:1,Id:5,isDoubleClick:false,name:'数学幂函数算法精讲课程资料'},
    {level:1,Id:6,isDoubleClick:false,name:'数学三角函数计算公式'}
  ],
  collList:[],
  collActive: -1,
  isCloseColl:false,
  isClick:false, // 是否点击了新建文件夹
  currFile:{}, // 当前文件
  levelTitle:'',
  navList:[
    {name:'全部文件',Id:0}
  ],
  newName:'',
})
const itemInput=ref()
const newbuilt=ref()
const currfile=ref()

type TProps = {
  collData: any,
  isCollect:boolean
}
const props = withDefaults(defineProps<TProps>(), {})

watch(()=>props.isCollect,(newVal,oldVal)=>{
  if(newVal){
    person.isCloseColl=newVal
  }
})

onMounted(()=>{
  load()
  document.addEventListener("keyup", onxx)
})
onUnmounted(()=>{
  document.removeEventListener('keyup',onxx)
})
// 初始化
const load=()=>{
  person.collList = person.collList1
  // person.collList=props.collData
}
// 键盘事件
const onxx=(ev:any)=>{
  if(ev.keyCode===46){
    deleteCurr()
  }else if(ev.keyCode===113){
    Rename()
  }
}
// 删除当前
const deleteCurr=()=>{
  person.collList.forEach((co:any)=>{
    if(co.Id===person.currFile.Id){
      ElMessageBox({
        message:`确认删除名为 "${person.currFile.name}" 的文件夹吗？`,
        dangerouslyUseHTMLString: false,
        showCancelButton: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        person.collList=person.collList.filter(f=> f.Id!==person.currFile.Id)
        person.collActive=-1
      })
    }
  })
}

// 返回上一级
const backPrev=()=>{
  let a=person.navList[person.navList.length-1]
  if(a.name!=='全部文件'){
    person.navList.pop()
  }
  // 根据当前Id调用列表接口
  // GetFavoritelist(a.ParentId)
}

// 切换气泡标题
const navChange=(item:any,index:number)=>{
  if(person.navList.length-1!==index){
    // 根据当前Id调用列表接口
  }
  // 给切换后赋值
  var items = person.navList.filter((nav:any,nIndex:number)=>nIndex<=index)
  person.navList=items
}

// 选中当前文件夹
const collClick=(item:any,index:number)=>{
  person.collActive=item.Id
  person.currFile=item
}

// 当前文件夹双击事件
const doubleClick=(item:any,index:number)=>{
  doubleBtn()
}
// 当前文件双击
const doubleBtn=()=>{
  person.collList=person.currFile.list
  person.collActive=-1
  person.navList.push({name:person.currFile.name})
}

// 重命名双击事件
const doubleReName=()=>{
  Rename()
}
// 重命名
const Rename=()=>{
  person.collList.forEach((co:any,coIndex:number)=>{
    if(co.name===person.currFile.name){
      co.isDoubleClick=true
      nextTick(()=>{
        itemInput.value[0].focus()
      })
    }
  })
}

// 新建目录
const newFileBtn=()=>{
  person.isClick=true
  if(person.isClick){
    newBtn()
  }
}
// 获取新建点击
const GetNewBuilt=(isClick:boolean)=>{
  person.isClick=isClick
  if(person.isClick){
    newBtn()
  }
}
// 新建文件夹
const newBtn=()=>{
  person.collList.unshift({
    level:1,
    Id:23,
    name:'新建文件夹',
    isDoubleClick:false
  })
  if(person.currFile){
    person.collActive=person.currFile.Id
  }else{
    person.collActive=person.collList[0].Id
  }
  // 获取焦点
  nextTick(()=>{
    itemInput.value[0].focus()
  })
}


// 获取是否打开新建
const GetOpenNew=(isopen:boolean)=>{
  if(isopen){
    person.collActive=-1
    // nextTick(()=>{
    //   const cuu=document.getElementsByClassName('curr_text')[0]
    //   cuu.classList.add('disnone')
    // })
    // newbuilt.value.closeMenu()
  }
}

// 获取当前文件夹的右击菜单
const GetonMenus=(item:any)=>{
  if(item.name==='重命名'){
    Rename()
  }else if(item.name==='打开'){
    doubleBtn()
  }else if(item.name==='删除'){
    deleteCurr()
  }
}

// 获取是否打开当前文件夹
const GetOpenCurr=(isopen:boolean)=>{
  if(isopen){
    person.collActive=-1
  }
}

// 输入框失去焦点
const InputBlur=async (e:any,col:any,isRevise:number)=>{
  col.isDoubleClick=false
  if(col.Name===''){
    ElMessage.error('命名不能为空！')
  }else{
    var models = person.collList.filter((x:any)=>x.Name===col.Name && x.Id!==col.Id)
    if(models.length>0){
      await addChildren(col.Name,col.Name,col.Id,0);
      col.Name=person.newName
    }
    person.isClick=false
    if(isRevise===0){
      // 调用新建接口

    }else if(isRevise===1){
      // 调用修改接口

    }
  }
}
// 新建文件夹重名的问题
const addChildren=async (oldname:string,name:string,Id:number,index:number)=>{
  var models = person.collList.filter((x:any)=>x.Name===name && x.Id!==Id)
  if(models.length>0){
    index=index+1;
    name=oldname+index;
    await addChildren(oldname,name,models[0].Id,index)
  }else{
    person.newName=oldname+index
  }
}

// 输入框获取焦点
const onFocus=(e:any)=>{
  e.currentTarget.select();
}

</script>
<style lang='scss' scoped>
.disnone{
  display: none !important;
}
.coll_pop{
  position: fixed;
  left: 0;
  top: 0;
  z-index: 99;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,.3);
  .coll_box{
    margin: 5% auto 0 auto;
    width: 60%;
    background: #fff;
    height: 750px;
    .coll_top{
      padding: 10px 20px;
      justify-content: space-between;
      border-bottom: 1px solid #ddd;
      .el-icon{
        font-size: 20px;
      }
    }
    .coll_center{
      padding: 10px 20px;
      .coll_title{
        margin-top: 10px;
        .coll_title_back{
          margin-right: 10px;
          cursor: pointer;
          color: #2080F7;
        }
        .coll_title_nav{
          cursor: pointer;
        }
      }
    }
    .coll_cont{
      padding: 10px 20px;
      .cont_scroll{
        position: relative;
        height: calc(100vh - 340px);
        overflow: hidden;
        .cont_null{
          width: 100%;
          height: calc(100vh - 340px);
          display: flex;
          flex-direction: column;
          justify-content: center;
          text-align: center;
          color: #999;
          .el-button{
            background: #2080F7;
            color: #fff;
            border: 0;
            padding: 20px 20px;
            width: 150px;
            margin: 15px auto 0 auto;
          }
        }
      }
      .cont_list{
        display: flex;
        flex-wrap: wrap;
        :deep(.cont_item){
          margin-right: 15px;
          margin-bottom: 15px;
          max-width: 120px;
          min-width: 120px;
          min-height: 120px;
          padding: 10px;
          cursor: pointer;
          user-select:none;
          text-align: center;
          overflow: hidden;
          position: relative;
          z-index: 0;
          border: 1px solid #fff;
          .cont_item_ok{
            position: absolute;
            left: 5px;
            top: 5px;
          }
          .cont_item_name{
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            overflow: hidden;
            height: 40px;
            position: absolute;
            width: 80%;
          }
          .el-input__wrapper{
            border-radius: 0;
            height: 30px;
            border: 1px solid #2080F7;
            background: #fff;
          }
        }
        .cont_item_active{
          border: 1px solid #2080F7;
          background: #E7F3FF;
        }
        .cont_item:hover{
          background: #f6faff;
          border: 1px solid #f6faff;
        }
      }
    }
  }
}
</style>