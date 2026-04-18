<template>
  <div class="admin-page list-page">
    <section class="page-hero">
      <div class="page-hero-copy">
        <span class="page-eyebrow">分类目录</span>
        <h1 class="page-title">分类管理</h1>
        <p class="page-subtitle">维护前台分类结构，按名称或日期快速筛选，并在这里新增或清理分类。</p>
      </div>
      <el-button class="page-primary-btn" type="primary" @click="addCategoryBtnClick">
        <el-icon class="mr-1"><Plus /></el-icon>
        新增分类
      </el-button>
    </section>

    <section class="apple-panel filter-panel">
      <div class="panel-header">
        <h2>筛选条件</h2>
        <p>通过名称模糊搜索或按创建时间区间筛选，快速定位需要维护的分类。</p>
      </div>

      <div class="filter-grid">
        <div class="filter-field">
          <span class="filter-label">分类名称</span>
          <el-input v-model="searchCategoryName" clearable placeholder="输入分类名称进行模糊查询" />
        </div>

        <div class="filter-field filter-field-wide">
          <span class="filter-label">创建日期</span>
          <el-date-picker
            v-model="pickDate"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :shortcuts="shortcuts"
            @change="datepickerChange"
          />
        </div>

        <div class="filter-actions">
          <el-button class="apple-primary-btn" type="primary" :icon="Search" @click="getTableData">查询</el-button>
          <el-button class="apple-secondary-btn" :icon="RefreshRight" @click="reset">重置</el-button>
        </div>
      </div>
    </section>

    <section class="apple-panel table-panel">
      <div class="table-panel-head">
        <div>
          <h2>分类列表</h2>
          <p>当前共 {{ total }} 条分类记录，文章数量会随内容归档自动更新。</p>
        </div>
      </div>

      <el-table class="apple-table" :data="tableData" border stripe style="width: 100%" v-loading="tableLoading">
        <el-table-column prop="name" label="分类名称" min-width="240">
          <template #default="scope">
            <div class="name-cell">
              <span class="entity-chip">{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="articlesTotal" label="文章数" width="120" align="center">
          <template #default="scope">
            <span class="count-badge">{{ scope.row.articlesTotal }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="220" />

        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button class="action-btn-delete" type="danger" size="small" @click="deleteCategorySubmit(scope.row)">
              <el-icon class="mr-1"><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-pagination">
        <el-pagination
          v-model:current-page="current"
          v-model:page-size="size"
          :page-sizes="[10, 20, 50]"
          :small="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="getTableData"
        />
      </div>
    </section>

    <FormDialog ref="formDialogRef" title="新增文章分类" destroyOnClose @submit="onSubmit">
      <el-form ref="formRef" class="dialog-form" :rules="rules" :model="form" label-position="top">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="输入新的分类名称" maxlength="20" show-word-limit clearable />
        </el-form-item>
      </el-form>
    </FormDialog>
  </div>
</template>

<script setup>
import { Delete, Plus, RefreshRight, Search } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue'
import { addCategory, deleteCategory, getCategoryPageList } from '@/api/admin/category'
import moment from 'moment'
import { showMessage, showModel } from '@/composables/util'
import FormDialog from '@/components/FormDialog.vue'

const searchCategoryName = ref('')
const pickDate = ref('')
const startDate = reactive({})
const endDate = reactive({})

const datepickerChange = (e) => {
  startDate.value = moment(e[0]).format('YYYY-MM-DD')
  endDate.value = moment(e[1]).format('YYYY-MM-DD')
}

const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]

const tableLoading = ref(false)
const tableData = ref([])
const current = ref(1)
const total = ref(0)
const size = ref(10)

function getTableData() {
  tableLoading.value = true
  getCategoryPageList({
    current: current.value,
    size: size.value,
    startDate: startDate.value,
    endDate: endDate.value,
    name: searchCategoryName.value,
  })
    .then((res) => {
      if (res.success === true) {
        tableData.value = res.data
        current.value = res.current
        size.value = res.size
        total.value = res.total
      }
    })
    .finally(() => (tableLoading.value = false))
}
getTableData()

const handleSizeChange = (chooseSize) => {
  size.value = chooseSize
  getTableData()
}

const reset = () => {
  searchCategoryName.value = ''
  pickDate.value = ''
  startDate.value = null
  endDate.value = null
}

const formDialogRef = ref(null)
const addCategoryBtnClick = () => {
  formDialogRef.value.open()
}

const formRef = ref(null)
const form = reactive({
  name: '',
})

const rules = {
  name: [
    {
      required: true,
      message: '分类名称不能为空',
      trigger: 'blur',
    },
    { min: 1, max: 20, message: '分类名称长度需在 1 到 20 个字符之间', trigger: 'blur' },
  ],
}

const onSubmit = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      return false
    }

    formDialogRef.value.showBtnLoading()
    addCategory(form)
      .then((res) => {
        if (res.success === true) {
          showMessage('添加成功')
          form.name = ''
          formDialogRef.value.close()
          getTableData()
        } else {
          showMessage(res.message, 'error')
        }
      })
      .finally(() => formDialogRef.value.closeBtnLoading())
  })
}

const deleteCategorySubmit = (row) => {
  showModel('是否确定要删除该分类？')
    .then(() => {
      deleteCategory(row.id).then((res) => {
        if (res.success === true) {
          showMessage('删除成功')
          getTableData()
        } else {
          showMessage(res.message, 'error')
        }
      })
    })
    .catch(() => {})
}
</script>

<style scoped>
.admin-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
  color: #0f172a;
}

.page-hero {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 18px;
}

.page-eyebrow {
  display: inline-block;
  margin-bottom: 10px;
  color: #64748b;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.14em;
  text-transform: uppercase;
}

.page-title {
  margin: 0;
  font-size: 34px;
  line-height: 1.1;
  font-weight: 700;
}

.page-subtitle {
  max-width: 760px;
  margin: 10px 0 0;
  color: #64748b;
  font-size: 15px;
  line-height: 1.7;
}

.apple-panel {
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.06);
  backdrop-filter: blur(22px);
}

.filter-panel,
.table-panel {
  padding: 24px 26px;
}

.panel-header h2,
.table-panel-head h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
}

.panel-header p,
.table-panel-head p {
  margin: 8px 0 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.7;
}

.filter-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
  align-items: end;
  margin-top: 20px;
}

.filter-field {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.filter-field-wide {
  grid-column: span 2;
}

.filter-label {
  color: #334155;
  font-size: 14px;
  font-weight: 600;
}

.filter-field :deep(.el-date-editor),
.filter-field :deep(.el-input),
.filter-field :deep(.el-select) {
  width: 100%;
}

.filter-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
}

.page-primary-btn,
.apple-primary-btn,
.apple-secondary-btn {
  height: 44px;
  border-radius: 999px;
}

.page-primary-btn,
.apple-primary-btn {
  border: none;
  background: linear-gradient(180deg, #1f2937 0%, #111827 100%);
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.12);
}

.page-primary-btn:hover,
.apple-primary-btn:hover {
  background: linear-gradient(180deg, #111827 0%, #0f172a 100%);
}

.apple-secondary-btn {
  border-color: rgba(203, 213, 225, 0.92);
  background: #ffffff;
  color: #334155;
}

.table-panel-head {
  margin-bottom: 18px;
}

.name-cell {
  display: flex;
  align-items: center;
}

.entity-chip {
  display: inline-flex;
  align-items: center;
  min-height: 34px;
  padding: 0 14px;
  border-radius: 999px;
  background: linear-gradient(180deg, #f8fafc 0%, #eef2ff 100%);
  box-shadow: inset 0 0 0 1px rgba(191, 219, 254, 0.75);
  color: #334155;
  font-size: 13px;
  font-weight: 600;
}

.count-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 34px;
  height: 34px;
  padding: 0 10px;
  border-radius: 999px;
  background: rgba(15, 23, 42, 0.06);
  color: #0f172a;
  font-size: 13px;
  font-weight: 700;
}

.action-btn-delete {
  min-width: 92px;
  border-radius: 12px;
}

.table-pagination {
  display: flex;
  justify-content: center;
  margin-top: 26px;
}

.dialog-form :deep(.el-form-item__label) {
  color: #334155;
  font-weight: 600;
}

.dialog-form :deep(.el-input__wrapper) {
  min-height: 48px;
  border-radius: 18px;
  background: rgba(248, 250, 252, 0.96);
  box-shadow: 0 0 0 1px rgba(226, 232, 240, 0.92) inset !important;
}

.apple-table :deep(.el-table__inner-wrapper) {
  border-radius: 22px;
  overflow: hidden;
}

.apple-table :deep(th.el-table__cell) {
  background: #f8fafc;
  color: #64748b;
  font-weight: 600;
}

.apple-table :deep(.el-table__cell) {
  padding-top: 18px;
  padding-bottom: 18px;
}

.apple-table :deep(.el-table__row td.el-table__cell) {
  background: rgba(255, 255, 255, 0.76);
}

.table-pagination :deep(.el-pagination.is-background .el-pager li),
.table-pagination :deep(.el-pagination.is-background .btn-prev),
.table-pagination :deep(.el-pagination.is-background .btn-next) {
  border-radius: 999px;
}

@media (max-width: 1080px) {
  .filter-grid {
    grid-template-columns: 1fr;
  }

  .filter-field-wide {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .page-hero {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-panel,
  .table-panel {
    padding: 20px;
  }

  .filter-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}
</style>
