<template>
    <el-dialog v-model="dialogVisible" class="apple-form-dialog" :title="title" :width="width"
    :destroy-on-close="destroyOnClose"
     :draggable="true" :close-on-click-modal="false"
        :close-on-press-escape="false">
        <slot></slot>
        <template #footer>
            <span class="dialog-footer">
                <el-button class="dialog-cancel-btn" @click="dialogVisible = false">取消</el-button>
                <el-button class="dialog-confirm-btn" type="primary" @click="submit" :loading="btnLoading">
                    {{ confirmText }}
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref } from 'vue'

const dialogVisible = ref(false)
const btnLoading = ref(false)
const showBtnLoading = () => (btnLoading.value = true)
const closeBtnLoading = () => (btnLoading.value = false)

const props = defineProps({
    title: String,
    width: {
        type: String,
        default: '40%'
    },
    destroyOnClose:  {
        type: Boolean,
        default: false
    },
    confirmText: {
        type: String,
        default: '提交'
    }
})

const open = () => (dialogVisible.value = true)
const close = () => (dialogVisible.value = false)

const emit  = defineEmits(['submit'])
const submit = () => emit('submit')

defineExpose({
    open,
    close,
    showBtnLoading,
    closeBtnLoading
})
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dialog-cancel-btn,
.dialog-confirm-btn {
  min-width: 94px;
  height: 42px;
  border-radius: 999px;
}

.dialog-cancel-btn {
  border-color: rgba(203, 213, 225, 0.92);
  color: #334155;
  background: #ffffff;
}

.dialog-confirm-btn {
  border: none;
  background: linear-gradient(180deg, #1f2937 0%, #111827 100%);
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.12);
}
</style>

<style>
.apple-form-dialog .el-dialog {
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.94);
  box-shadow: 0 30px 80px rgba(15, 23, 42, 0.12);
  backdrop-filter: blur(18px);
  overflow: hidden;
}

.apple-form-dialog .el-dialog__header {
  padding: 24px 26px 10px;
}

.apple-form-dialog .el-dialog__title {
  color: #0f172a;
  font-size: 24px;
  font-weight: 700;
}

.apple-form-dialog .el-dialog__body {
  padding: 10px 26px 0;
}

.apple-form-dialog .el-dialog__footer {
  padding: 18px 26px 26px;
  border-top: 1px solid rgba(226, 232, 240, 0.88);
}
</style>
