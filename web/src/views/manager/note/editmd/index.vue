<template>
  <MdEditor v-model="text" :toolbars="toolbars"  >
    <template #defToolbars >
      <ExportPDF :modelValue="text" @onProgress="handleProgress" />
    </template>
  </MdEditor>
</template>

<script setup>
import { ref } from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import { ExportPDF } from '@vavt/v3-extension';
import '@vavt/v3-extension/lib/asset/ExportPDF.css';
import { toolbars } from "./data.js";


const handleProgress = (progress) => {
  console.log(`Export progress: ${progress.ratio * 100}%`);
};

const text = ref('Hello Editor!');


</script>
