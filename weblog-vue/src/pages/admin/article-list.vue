<template>
  <div class="admin-page article-manage-page">
    <section class="page-hero">
      <div class="page-hero-copy">
        <span class="page-eyebrow">文章工作台</span>
        <h1 class="page-title">文章管理</h1>
        <p class="page-subtitle">用更安静、克制的方式管理筛选、发布、编辑与删除，让后台保持统一的内容工作流。</p>
      </div>
    </section>

    <el-card shadow="never" class="article-filter-card">
      <div class="panel-header">
        <h2>筛选条件</h2>
        <p>按标题关键词或发布时间区间快速定位需要处理的内容。</p>
      </div>

      <div class="filter-inline">
        <div class="filter-item filter-item-title">
          <span class="filter-label">文章标题</span>
          <el-input v-model="searchArticleTitle" placeholder="请输入文章标题" clearable />
        </div>

        <div class="filter-item filter-item-date">
          <span class="filter-label">创建日期</span>
          <el-date-picker
            v-model="pickDate"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            size="default"
            :shortcuts="shortcuts"
            @change="datepickerChange"
          />
        </div>

        <div class="filter-actions">
          <el-button type="primary" :icon="Search" @click="getTableData">查询</el-button>
          <el-button :icon="RefreshRight" @click="reset">重置</el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="article-table-card">
      <div class="table-panel-head">
        <div>
          <h2>文章列表</h2>
          <p>当前共 {{ total }} 篇文章，可在这里直接编辑、预览和删除。</p>
        </div>
        <el-button class="write-article-btn" type="primary" @click="openPublishDialog">
          <el-icon class="mr-1">
            <EditPen />
          </el-icon>
          写文章
        </el-button>
      </div>

      <el-table class="apple-table article-apple-table" :data="tableData" border stripe style="width: 100%" v-loading="tableLoading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="标题" min-width="360">
          <template #default="scope">
            <div class="article-title-cell">{{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="cover" label="封面" width="190">
          <template #default="scope">
            <div class="cover-thumb-wrap">
              <el-image class="cover-thumb" fit="cover" :src="scope.row.cover" />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="190" />
        <el-table-column label="操作" width="170" align="center">
          <template #default="scope">
            <div class="action-stack">
              <el-button class="action-btn action-btn-edit" size="small" @click="showArticleUpdateEditor(scope.row)">
                <el-icon class="mr-1">
                  <Edit />
                </el-icon>
                编辑
              </el-button>
              <el-button class="action-btn action-btn-preview" size="small" @click="goArticleDetailPage(scope.row.id)">
                <el-icon class="mr-1">
                  <View />
                </el-icon>
                预览
              </el-button>
              <el-button class="action-btn action-btn-delete" type="danger" size="small" @click="deleteArticleSubmit(scope.row)">
                <el-icon class="mr-1">
                  <Delete />
                </el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="article-pagination">
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
    </el-card>

    <el-dialog v-model="isArticlePublishEditorShow" class="article-editor-dialog" :fullscreen="true" :show-close="false">
      <template #header>
        <el-affix :offset="20" style="width: 100%">
          <div class="article-editor-header">
            <div>
              <h4 class="article-editor-heading">写文章</h4>
              <p class="article-editor-subtitle">整理好标题、正文、封面与元信息后，即可直接发布。</p>
            </div>
            <div class="article-editor-header-actions">
              <el-button class="article-editor-secondary-btn" @click="closePublishDialog">取消</el-button>
              <el-button class="article-editor-primary-btn" type="primary" @click="publishArticleSubmit">
                <el-icon class="mr-1">
                  <Promotion />
                </el-icon>
                发布
              </el-button>
            </div>
          </div>
        </el-affix>
      </template>

      <el-form class="article-editor-form" :model="form" ref="publishArticleFormRef" label-position="top" size="large">
        <div class="article-editor-layout">
          <div class="article-editor-main">
            <section class="editor-panel editor-panel-title">
              <el-form-item label="标题">
                <el-input
                  v-model="form.title"
                  class="article-title-input"
                  autocomplete="off"
                  size="large"
                  maxlength="40"
                  show-word-limit
                  clearable
                  placeholder="给文章起一个清晰、易识别的标题"
                />
              </el-form-item>
            </section>

            <section class="editor-panel editor-panel-content">
              <el-form-item label="内容">
                <div class="editor-md">
                  <MdEditor v-model="form.content" @onUploadImg="onUploadImg" editorId="publishArticleEditor" />
                </div>
              </el-form-item>
            </section>
          </div>

          <aside class="article-editor-side">
            <section class="editor-panel editor-panel-settings">
              <div class="editor-side-title">发布设置</div>
              <div class="editor-side-desc">补充封面、摘要、分类与标签，让文章信息在发布前更完整。</div>

              <el-form-item label="封面">
                <el-upload class="avatar-uploader" action="#" :on-change="handleCoverChange" :auto-upload="false" :show-file-list="false">
                  <img v-if="form.cover" :src="form.cover" class="avatar" />
                  <div v-else class="cover-upload-empty">
                    <el-icon class="avatar-uploader-icon">
                      <Plus />
                    </el-icon>
                    <span>上传封面</span>
                  </div>
                </el-upload>
              </el-form-item>

              <el-form-item label="摘要">
                <el-input v-model="form.summary" :rows="4" type="textarea" placeholder="请输入文章摘要" />
              </el-form-item>

              <el-form-item label="分类">
                <el-select v-model="form.categoryId" clearable placeholder="请选择分类" size="large">
                  <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>

              <el-form-item label="标签">
                <el-select
                  v-model="form.tags"
                  multiple
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入文章标签"
                  remote-show-suffix
                  allow-create
                  default-first-option
                  :remote-method="remoteMethod"
                  :loading="tagSelectLoading"
                  size="large"
                >
                  <el-option v-for="item in tags" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
            </section>
          </aside>
        </div>
      </el-form>
    </el-dialog>

    <el-dialog
      v-model="isArticleUpdateEditorShow"
      class="article-editor-dialog"
      :fullscreen="true"
      :show-close="false"
      :close-on-press-escape="false"
    >
      <template #header>
        <el-affix :offset="20" style="width: 100%">
          <div class="article-editor-header">
            <div>
              <h4 class="article-editor-heading">编辑文章</h4>
              <p class="article-editor-subtitle">修改内容后可直接保存，前台会同步展示最新版本。</p>
            </div>
            <div class="article-editor-header-actions">
              <el-button class="article-editor-secondary-btn" @click="closeUpdateDialog">取消</el-button>
              <el-button class="article-editor-primary-btn" type="primary" @click="updateSubmit">
                <el-icon class="mr-1">
                  <Promotion />
                </el-icon>
                保存
              </el-button>
            </div>
          </div>
        </el-affix>
      </template>

      <el-form class="article-editor-form" :model="updateArticleForm" ref="updateArticleFormRef" label-position="top" size="large">
        <div class="article-editor-layout">
          <div class="article-editor-main">
            <section class="editor-panel editor-panel-title">
              <el-form-item label="标题">
                <el-input
                  v-model="updateArticleForm.title"
                  class="article-title-input"
                  autocomplete="off"
                  size="large"
                  maxlength="40"
                  show-word-limit
                  clearable
                  placeholder="给文章起一个清晰、易识别的标题"
                />
              </el-form-item>
            </section>

            <section class="editor-panel editor-panel-content">
              <el-form-item label="内容">
                <div class="editor-md">
                  <MdEditor v-model="updateArticleForm.content" @onUploadImg="onUploadImg" editorId="updateArticleEditor" />
                </div>
              </el-form-item>
            </section>
          </div>

          <aside class="article-editor-side">
            <section class="editor-panel editor-panel-settings">
              <div class="editor-side-title">发布设置</div>
              <div class="editor-side-desc">在右侧设置卡片里更新封面、摘要、分类与标签。</div>

              <el-form-item label="封面">
                <el-upload class="avatar-uploader" action="#" :on-change="handleUpdateCoverChange" :auto-upload="false" :show-file-list="false">
                  <img v-if="updateArticleForm.cover" :src="updateArticleForm.cover" class="avatar" />
                  <div v-else class="cover-upload-empty">
                    <el-icon class="avatar-uploader-icon">
                      <Plus />
                    </el-icon>
                    <span>上传封面</span>
                  </div>
                </el-upload>
              </el-form-item>

              <el-form-item label="摘要">
                <el-input v-model="updateArticleForm.summary" :rows="4" type="textarea" placeholder="请输入文章摘要" />
              </el-form-item>

              <el-form-item label="分类">
                <el-select v-model="updateArticleForm.categoryId" clearable placeholder="请选择分类" size="large">
                  <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>

              <el-form-item label="标签">
                <el-select
                  v-model="updateArticleForm.tags"
                  multiple
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入文章标签"
                  remote-show-suffix
                  allow-create
                  default-first-option
                  :remote-method="remoteMethod"
                  :loading="tagSelectLoading"
                  size="large"
                >
                  <el-option v-for="item in tags" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
            </section>
          </aside>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { Delete, Edit, EditPen, Plus, Promotion, RefreshRight, Search, View } from '@element-plus/icons-vue'
import moment from 'moment'
import { getArticlePageList, deleteArticle, publishArticle, getArticleDetail, updateArticle } from '@/api/admin/article'
import { showMessage, showModel } from '@/composables/util'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { uploadFile } from '@/api/admin/file'
import { getCategorySelectList } from '@/api/admin/category.js'
import { searchTags, getTagSelectList } from '@/api/admin/tag'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const goArticleDetailPage = (articleId) => {
  const username = userStore.userInfo?.username
  if (!username) {
    showMessage('当前用户信息未加载，暂时无法预览', 'warning')
    return
  }

  router.push(`/u/${username}/article/${articleId}`)
}

const searchArticleTitle = ref('')
const pickDate = ref('')
const startDate = ref(null)
const endDate = ref(null)

const isArticlePublishEditorShow = ref(false)
const isArticleUpdateEditorShow = ref(false)
const publishArticleFormRef = ref(null)
const updateArticleFormRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  content: '',
  cover: '',
  categoryId: null,
  tags: [],
  summary: '',
})

const updateArticleForm = reactive({
  id: null,
  title: '',
  content: '',
  cover: '',
  categoryId: null,
  tags: [],
  summary: '',
})

const resetPublishForm = () => {
  form.id = null
  form.title = ''
  form.content = ''
  form.cover = ''
  form.categoryId = null
  form.tags = []
  form.summary = ''
}

const resetUpdateForm = () => {
  updateArticleForm.id = null
  updateArticleForm.title = ''
  updateArticleForm.content = ''
  updateArticleForm.cover = ''
  updateArticleForm.categoryId = null
  updateArticleForm.tags = []
  updateArticleForm.summary = ''
}

const openPublishDialog = () => {
  resetPublishForm()
  isArticlePublishEditorShow.value = true
}

const closePublishDialog = () => {
  isArticlePublishEditorShow.value = false
}

const closeUpdateDialog = () => {
  isArticleUpdateEditorShow.value = false
}

const datepickerChange = (e) => {
  if (!e || e.length !== 2) {
    startDate.value = null
    endDate.value = null
    return
  }
  startDate.value = moment(e[0]).format('YYYY-MM-DD')
  endDate.value = moment(e[1]).format('YYYY-MM-DD')
}

const shortcuts = [
  {
    text: 'Last 7 days',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: 'Last 30 days',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: 'Last 90 days',
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
  getArticlePageList({
    current: current.value,
    size: size.value,
    startDate: startDate.value,
    endDate: endDate.value,
    title: searchArticleTitle.value,
  })
    .then((res) => {
      if (res.success === true) {
        tableData.value = res.data
        current.value = res.current
        size.value = res.size
        total.value = res.total
      }
    })
    .finally(() => {
      tableLoading.value = false
    })
}
getTableData()

const handleSizeChange = (chooseSize) => {
  size.value = chooseSize
  getTableData()
}

const reset = () => {
  pickDate.value = ''
  startDate.value = null
  endDate.value = null
  searchArticleTitle.value = ''
  getTableData()
}

const categories = ref([])
getCategorySelectList().then((res) => {
  categories.value = res.data || []
})

const tags = ref([])
const tagSelectLoading = ref(false)

getTagSelectList().then((res) => {
  tags.value = res.data || []
})

const remoteMethod = (query) => {
  if (!query) {
    return
  }

  tagSelectLoading.value = true
  searchTags(query)
    .then((res) => {
      if (res.success) {
        tags.value = res.data || []
      }
    })
    .finally(() => {
      tagSelectLoading.value = false
    })
}

const uploadCover = (file, targetForm) => {
  const formData = new FormData()
  formData.append('file', file.raw)
  uploadFile(formData).then((res) => {
    if (res.success === false) {
      showMessage('上传失败', 'error')
      return
    }

    targetForm.cover = res.data.url
    showMessage('上传成功')
  })
}

const handleCoverChange = (file) => uploadCover(file, form)
const handleUpdateCoverChange = (file) => uploadCover(file, updateArticleForm)

const onUploadImg = async (files, callback) => {
  const urls = []

  for (const file of files) {
    const formData = new FormData()
    formData.append('file', file)
    const res = await uploadFile(formData)

    if (res.success === false) {
      showMessage('文件上传失败', 'error')
      return
    }

    urls.push(res.data.url)
  }

  callback(urls)
}

const validateArticleForm = (targetForm) => {
  if (!targetForm.title || !targetForm.title.trim()) {
    return '请输入文章标题'
  }
  if (!targetForm.content || !targetForm.content.trim()) {
    return '请输入文章内容'
  }
  if (!targetForm.cover) {
    return '请上传封面图'
  }
  if (!targetForm.categoryId) {
    return '请选择分类'
  }
  if (!targetForm.tags || targetForm.tags.length === 0) {
    return '请至少选择一个标签'
  }
  return ''
}

const publishArticleSubmit = () => {
  const errorMessage = validateArticleForm(form)
  if (errorMessage) {
    showMessage(errorMessage, 'error')
    return
  }

  publishArticle(form).then((res) => {
    if (res.success === false) {
      showMessage(res.message || '发布失败', 'error')
      return
    }

    showMessage('发布成功')
    closePublishDialog()
    resetPublishForm()
    getTableData()
  })
}

const showArticleUpdateEditor = (row) => {
  getArticleDetail(row.id).then((res) => {
    if (res.success !== true) {
      showMessage(res.message || '获取文章详情失败', 'error')
      return
    }

    updateArticleForm.id = res.data.id
    updateArticleForm.title = res.data.title
    updateArticleForm.cover = res.data.cover
    updateArticleForm.content = res.data.content
    updateArticleForm.categoryId = res.data.categoryId
    updateArticleForm.tags = res.data.tagIds || []
    updateArticleForm.summary = res.data.summary
    isArticleUpdateEditorShow.value = true
  })
}

const updateSubmit = () => {
  const errorMessage = validateArticleForm(updateArticleForm)
  if (errorMessage) {
    showMessage(errorMessage, 'error')
    return
  }

  updateArticle(updateArticleForm).then((res) => {
    if (res.success === false) {
      showMessage(res.message || '保存失败', 'error')
      return
    }

    showMessage('保存成功')
    closeUpdateDialog()
    getTableData()
  })
}

const deleteArticleSubmit = (row) => {
  showModel('确定要删除这篇文章吗？')
    .then(() => {
      deleteArticle(row.id).then((res) => {
        if (res.success === false) {
          showMessage(res.message || '删除失败', 'error')
          return
        }

        showMessage('删除成功')
        getTableData()
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
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.page-title {
  margin: 0;
  color: #0f172a;
  font-size: 34px;
  font-weight: 700;
  letter-spacing: -0.04em;
}

.page-subtitle {
  max-width: 760px;
  margin: 10px 0 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.75;
}

.article-filter-card,
.article-table-card,
.editor-panel {
  border: 1px solid rgba(203, 213, 225, 0.72);
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.08);
}

.article-manage-page :deep(.article-filter-card > .el-card__body),
.article-manage-page :deep(.article-table-card > .el-card__body) {
  padding: 28px;
}

.panel-header h2,
.table-panel-head h2 {
  margin: 0;
  color: #0f172a;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: -0.03em;
}

.panel-header p,
.table-panel-head p {
  margin: 8px 0 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.7;
}

.filter-inline {
  display: flex;
  align-items: flex-end;
  gap: 16px;
  flex-wrap: wrap;
  margin-top: 24px;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.filter-item-title {
  width: 240px;
}

.filter-item-date {
  min-width: 360px;
  flex: 1 1 360px;
}

.filter-label {
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
}

.filter-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.table-panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}

.write-article-btn {
  height: 46px;
  padding: 0 20px;
  border: none;
  border-radius: 999px;
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 0 14px 26px rgba(15, 23, 42, 0.16);
}

.write-article-btn:hover {
  background: linear-gradient(180deg, #111827 0%, #0f172a 100%);
}

.article-title-cell {
  color: #0f172a;
  font-size: 15px;
  line-height: 1.8;
  font-weight: 600;
}

.cover-thumb-wrap {
  display: inline-flex;
  padding: 6px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 18px;
  background: rgba(248, 250, 252, 0.88);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.7);
}

.cover-thumb {
  width: 136px;
  height: 76px;
  border-radius: 12px;
}

.action-stack {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 8px 0;
}

.action-btn {
  width: 96px;
  margin-left: 0 !important;
  border-radius: 999px;
  justify-content: center;
  font-weight: 500;
  transition: all 0.2s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.action-btn-edit {
  border-color: rgba(203, 213, 225, 0.92);
  color: #334155;
  background: #ffffff;
}

.action-btn-edit:hover {
  border-color: rgba(148, 163, 184, 0.88);
  color: #0f172a;
  background: #f8fafc;
}

.action-btn-preview {
  border-color: rgba(203, 213, 225, 0.92);
  color: #1e293b;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.action-btn-preview:hover {
  border-color: rgba(148, 163, 184, 0.88);
  color: #0f172a;
  background: #eef2f7;
}

.article-pagination {
  display: flex;
  justify-content: center;
  margin-top: 28px;
}

.article-pagination :deep(.el-pagination.is-background .el-pager li),
.article-pagination :deep(.el-pagination.is-background .btn-prev),
.article-pagination :deep(.el-pagination.is-background .btn-next) {
  border-radius: 999px;
}

.article-editor-form {
  padding-top: 28px;
  overflow-x: hidden;
}

.article-editor-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 340px;
  gap: 24px;
  align-items: start;
}

.article-editor-main,
.article-editor-side,
.editor-md {
  min-width: 0;
}

.article-editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-height: 72px;
  padding: 14px 18px;
  border: 1px solid #e2e8f0;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.08);
}

.article-editor-heading {
  margin: 0;
  color: #0f172a;
  font-size: 26px;
  font-weight: 700;
}

.article-editor-subtitle {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 14px;
}

.article-editor-header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.article-editor-primary-btn,
.article-editor-secondary-btn {
  min-width: 96px;
  height: 42px;
  border-radius: 12px;
}

.article-editor-primary-btn {
  border: none;
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 0 14px 26px rgba(15, 23, 42, 0.16);
}

.article-editor-primary-btn:hover {
  background: linear-gradient(180deg, #111827 0%, #0f172a 100%);
}

.article-editor-secondary-btn {
  border-color: #cbd5e1;
  color: #334155;
  background: #fff;
}

.editor-panel {
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.94);
}

.editor-panel-title {
  padding: 22px 24px 10px;
}

.editor-panel-content {
  padding: 22px 24px 24px;
}

.editor-panel-settings {
  position: sticky;
  top: 28px;
  padding: 24px;
}

.editor-side-title {
  color: #0f172a;
  font-size: 18px;
  font-weight: 700;
}

.editor-side-desc {
  margin-top: 8px;
  margin-bottom: 24px;
  color: #64748b;
  font-size: 13px;
  line-height: 1.8;
}

.article-title-input :deep(.el-input__wrapper),
.editor-panel-settings :deep(.el-select__wrapper),
.editor-panel-settings :deep(.el-textarea__inner),
.filter-inline :deep(.el-input__wrapper),
.filter-inline :deep(.el-date-editor.el-input__wrapper) {
  border-radius: 16px;
  background: #f8fafc;
  box-shadow: inset 0 0 0 1px rgba(226, 232, 240, 0.86);
}

.editor-md :deep(.md-editor) {
  border-radius: 20px;
  overflow: hidden;
}

.avatar-uploader .avatar {
  width: 100%;
  height: 148px;
  display: block;
  border-radius: 18px;
  object-fit: cover;
}

.avatar-uploader {
  width: 100%;
}

.cover-upload-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  height: 148px;
  border: 1px dashed rgba(148, 163, 184, 0.55);
  border-radius: 18px;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
  color: #64748b;
}

.el-icon.avatar-uploader-icon {
  font-size: 26px;
}

.article-apple-table :deep(.el-table__inner-wrapper) {
  border-radius: 22px;
  overflow: hidden;
}

.article-apple-table :deep(th.el-table__cell) {
  background: #f8fafc;
  color: #64748b;
  font-weight: 600;
}

.article-apple-table :deep(.el-table__cell) {
  padding-top: 18px;
  padding-bottom: 18px;
}

.article-apple-table :deep(.el-table__row td.el-table__cell) {
  background: rgba(255, 255, 255, 0.76);
}

:deep(.article-editor-dialog .el-dialog__body) {
  overflow-x: hidden;
  padding: 22px 28px 32px;
  background: linear-gradient(180deg, #f8fafc 0%, #eef4ff 100%);
}

@media (max-width: 1200px) {
  .article-editor-layout {
    grid-template-columns: 1fr;
  }

  .editor-panel-settings {
    position: static;
  }
}

@media (max-width: 900px) {
  .page-hero,
  .table-panel-head,
  .filter-inline {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-item-title,
  .filter-item-date {
    width: 100%;
    min-width: 0;
  }
}

@media (max-width: 768px) {
  .article-editor-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .article-editor-header-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .article-manage-page :deep(.article-filter-card > .el-card__body),
  .article-manage-page :deep(.article-table-card > .el-card__body),
  .editor-panel-title,
  .editor-panel-content,
  .editor-panel-settings {
    padding-left: 18px;
    padding-right: 18px;
  }

  :deep(.article-editor-dialog .el-dialog__body) {
    padding-left: 14px;
    padding-right: 14px;
  }
}
</style>

<style>
.md-editor-footer {
  height: 40px;
}
</style>


