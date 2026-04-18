<template>
  <div class="tag-shell">
    <div class="tag-bar">
      <el-tabs v-model="activeTab" type="card" class="tag-tabs" @tab-remove="removeTab" @tab-change="tabChange">
        <el-tab-pane
          v-for="item in tabList"
          :key="item.path"
          :label="item.title"
          :name="item.path"
          :closable="item.path !== '/admin/index'"
        />
      </el-tabs>

      <el-dropdown @command="handleCloseTab">
        <button class="tag-action-button" type="button" aria-label="页签操作">
          <el-icon><ArrowDown /></el-icon>
        </button>

        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="closeOthers">关闭其他</el-dropdown-item>
            <el-dropdown-item command="closeAll">关闭全部</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useTabList } from '@/composables/useTagList.js'
import { ArrowDown } from '@element-plus/icons-vue'

const { activeTab, tabList, tabChange, removeTab, handleCloseTab } = useTabList()
</script>

<style scoped>
.tag-shell {
  width: 100%;
}

.tag-bar {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 58px;
  padding: 10px 12px;
  border-radius: 24px;
  overflow: hidden;
  isolation: isolate;
  background: #ffffff;
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.08);
}

.tag-bar::before {
  content: '';
  position: absolute;
  inset: 0;
  border: 1px solid rgba(226, 232, 240, 0.95);
  border-radius: inherit;
  pointer-events: none;
}

.tag-bar::after {
  content: '';
  position: absolute;
  left: 16px;
  right: 16px;
  top: 0;
  height: 1px;
  background: rgba(241, 245, 249, 0.98);
  pointer-events: none;
}

.tag-action-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 12px;
  background: rgba(248, 250, 252, 0.92);
  color: #475569;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tag-action-button:hover {
  background: #fff;
  color: #0f172a;
}

.tag-tabs {
  min-width: 0;
}

.tag-tabs :deep(.el-tabs__header) {
  margin: 0;
  border: 0;
}

.tag-tabs :deep(.el-tabs__nav-wrap) {
  display: flex;
  align-items: center;
}

.tag-tabs :deep(.el-tabs__nav) {
  border: 0 !important;
  background: transparent;
}

.tag-tabs :deep(.el-tabs__item) {
  height: 36px;
  line-height: 36px;
  margin-right: 8px;
  border: 1px solid rgba(226, 232, 240, 0.95) !important;
  border-radius: 999px !important;
  background: rgba(255, 255, 255, 0.9) !important;
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
}

.tag-tabs :deep(.el-tabs__item.is-active) {
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%) !important;
  border-color: transparent !important;
  color: #fff !important;
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.15);
}

.tag-tabs :deep(.el-tabs__item.is-active::before) {
  content: '';
  display: inline-block;
  width: 7px;
  height: 7px;
  margin-right: 6px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.92);
  vertical-align: middle;
}

.tag-tabs :deep(.el-tabs__nav-next),
.tag-tabs :deep(.el-tabs__nav-prev) {
  line-height: 36px;
}

.tag-tabs :deep(.is-disabled) {
  color: #cbd5e1;
}

@media (max-width: 900px) {
  .tag-bar {
    padding-left: 10px;
    padding-right: 10px;
  }
}
</style>
