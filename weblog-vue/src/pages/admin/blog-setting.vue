<template>
  <div class="settings-page">
    <section class="settings-hero">
      <div class="settings-hero-copy">
        <span class="settings-eyebrow">Blog Settings</span>
        <h1 class="settings-title">博客设置</h1>
        <p class="settings-subtitle">用更克制、清爽的方式整理博客信息，让前台展示更像一个真正的个人站点。</p>
      </div>
      <div class="settings-actions">
        <el-button class="settings-save-btn" type="primary" :loading="btnLoading" @click="onSubmit">保存更改</el-button>
      </div>
    </section>

    <section class="settings-overview-card">
      <div class="overview-brand">
        <div class="overview-logo-wrap">
          <img v-if="form.logo" :src="form.logo" class="overview-logo" />
        </div>
        <div class="overview-meta">
          <div class="overview-name">{{ form.name || '未设置博客名称' }}</div>
          <div class="overview-author">by {{ form.author || '未设置作者名' }}</div>
          <p class="overview-intro">{{ form.introduction || '这里会显示你的博客介绍语。' }}</p>
        </div>
      </div>
      <div class="overview-avatar-wrap">
        <img v-if="form.avatar" :src="form.avatar" class="overview-avatar" />
      </div>
    </section>

    <el-form ref="formRef" class="settings-form" :model="form" label-position="top" :rules="rules">
      <div class="settings-grid">
        <section class="settings-panel settings-panel-main">
          <div class="panel-header">
            <h2>基础信息</h2>
            <p>名称、作者、介绍语与视觉素材会直接影响前台首页和文章页展示。</p>
          </div>

          <div class="settings-field-grid">
            <el-form-item label="博客名称" prop="name">
              <el-input v-model="form.name" clearable placeholder="例如：Lionff 的技术博客" />
            </el-form-item>

            <el-form-item label="作者名" prop="author">
              <el-input v-model="form.author" clearable placeholder="例如：lionff" />
            </el-form-item>
          </div>

          <el-form-item label="介绍语" prop="introduction">
            <el-input v-model="form.introduction" type="textarea" :rows="4" placeholder="写一句简洁的站点介绍，让访客快速知道这个博客在记录什么。" />
          </el-form-item>
        </section>

        <section class="settings-panel settings-panel-side">
          <div class="panel-header">
            <h2>视觉素材</h2>
            <p>建议使用清晰、留白充足的图像，保持首页和导航区更干净。</p>
          </div>

          <div class="upload-group">
            <el-form-item label="博客 LOGO" prop="logo">
              <el-upload class="apple-uploader" action="#"
                         :on-change="handleLogoChange" :auto-upload="false"
                         :show-file-list="false">
                <img v-if="form.logo" :src="form.logo" class="upload-image upload-image-logo" />
                <div v-else class="upload-empty">
                  <el-icon class="avatar-uploader-icon">
                    <Plus />
                  </el-icon>
                  <span>上传 Logo</span>
                </div>
              </el-upload>
            </el-form-item>

            <el-form-item label="作者头像" prop="avatar">
              <el-upload class="apple-uploader" action="#"
                         :on-change="handleAvatarChange" :auto-upload="false"
                         :show-file-list="false">
                <img v-if="form.avatar" :src="form.avatar" class="upload-image upload-image-avatar" />
                <div v-else class="upload-empty upload-empty-avatar">
                  <el-icon class="avatar-uploader-icon">
                    <Plus />
                  </el-icon>
                  <span>上传头像</span>
                </div>
              </el-upload>
            </el-form-item>
          </div>
        </section>
      </div>

      <section class="settings-panel settings-panel-social">
        <div class="panel-header">
          <h2>社交平台</h2>
          <p>只打开你真正想展示的平台，保持页面简洁，不必把所有入口都堆上去。</p>
        </div>

        <div class="social-list">
          <div class="social-item">
            <div class="social-item-head">
              <div>
                <div class="social-name">GitHub</div>
                <div class="social-desc">适合展示代码仓库与项目主页</div>
              </div>
              <el-switch v-model="isGithubChecked" inline-prompt :active-icon="Check" :inactive-icon="Close"
                         @change="githubSwitchChange" />
            </div>
            <el-form-item v-if="isGithubChecked" label="GitHub 主页访问地址">
              <el-input v-model="form.githubHomepage" clearable placeholder="请输入 GitHub 主页访问的 URL" />
            </el-form-item>
          </div>

          <div class="social-item">
            <div class="social-item-head">
              <div>
                <div class="social-name">Gitee</div>
                <div class="social-desc">适合国内项目镜像或课程作业仓库</div>
              </div>
              <el-switch v-model="isGiteeChecked" inline-prompt :active-icon="Check" :inactive-icon="Close"
                         @change="giteeSwitchChange" />
            </div>
            <el-form-item v-if="isGiteeChecked" label="Gitee 主页访问地址">
              <el-input v-model="form.giteeHomepage" clearable placeholder="请输入 Gitee 主页访问的 URL" />
            </el-form-item>
          </div>

          <div class="social-item">
            <div class="social-item-head">
              <div>
                <div class="social-name">知乎</div>
                <div class="social-desc">适合放更偏文章型、问答型内容入口</div>
              </div>
              <el-switch v-model="isZhihuChecked" inline-prompt :active-icon="Check" :inactive-icon="Close"
                         @change="zhihuSwitchChange" />
            </div>
            <el-form-item v-if="isZhihuChecked" label="知乎主页访问地址">
              <el-input v-model="form.zhihuHomepage" clearable placeholder="请输入知乎主页访问的 URL" />
            </el-form-item>
          </div>

          <div class="social-item">
            <div class="social-item-head">
              <div>
                <div class="social-name">CSDN</div>
                <div class="social-desc">适合保留技术笔记或历史文章入口</div>
              </div>
              <el-switch v-model="isCSDNChecked" inline-prompt :active-icon="Check" :inactive-icon="Close"
                         @change="csdnSwitchChange" />
            </div>
            <el-form-item v-if="isCSDNChecked" label="CSDN 主页访问地址">
              <el-input v-model="form.csdnHomepage" clearable placeholder="请输入 CSDN 主页访问的 URL" />
            </el-form-item>
          </div>
        </div>
      </section>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { Check, Close } from '@element-plus/icons-vue'
import { getBlogSettingsDetail,updateBlogSettings } from '@/api/admin/blogsetting.js'
import { uploadFile } from '@/api/admin/file'
import { showMessage } from '@/composables/util'
import { useBlogSettingsStore } from '@/stores/blogsettings'

// 是否显示保存按钮的 loading 状态，默认为 false
const btnLoading = ref(false)

// 表单引用
const formRef = ref(null)
const blogSettingsStore = useBlogSettingsStore()

// 保存当前博客设置
const onSubmit = () => {
  // 先验证 form 表单字段
  formRef.value.validate((valid) => {
    if (!valid) {
      console.log('表单验证不通过')
      return false
    }

    // 显示保存按钮 loading
    btnLoading.value = true
    updateBlogSettings(form).then((res) => {
      if (res.success == false) {
        // 获取服务端返回的错误消息
        let message = res.message
        // 提示错误消息
        showMessage(message, 'error')
        return
      }

      // 重新渲染页面中的信息
      blogSettingsStore.setBlogSettings({ ...form })
      initBlogSettings()
      showMessage('保存成功')
    }).finally(() => btnLoading.value = false) // 隐藏保存按钮 loading
  })
}

// 上传 logo 图片
const handleLogoChange = (file) => {
  // 表单对象
  let formData = new FormData()
  // 添加 file 字段，并将文件传入
  formData.append('file', file.raw)
  uploadFile(formData).then((e) => {
    // 响参失败，提示错误消息
    if (e.success == false) {
      let message = e.message
      showMessage(message, 'error')
      return
    }

    // 成功则设置 logo 链接，并提示成功
    form.logo = e.data.url
    showMessage('上传成功')
  })
}

// 上传作者头像
const handleAvatarChange = (file) => {
  // 表单对象
  let formData = new FormData()
  // 添加 file 字段，并将文件传入
  formData.append('file', file.raw)
  uploadFile(formData).then((e) => {
    // 响参失败，提示错误消息
    if (e.success == false) {
      let message = e.message
      showMessage(message, 'error')
      return
    }

    // 成功则设置作者头像链接，并提示成功
    form.avatar = e.data.url
    showMessage('上传成功')
  })
}

// 是否开启 GitHub
const isGithubChecked = ref(false)
// 是否开启 Gitee
const isGiteeChecked = ref(false)
// 是否开启知乎
const isZhihuChecked = ref(false)
// 是否开启 CSDN
const isCSDNChecked = ref(false)

// 监听 Github Switch 改变事件
const githubSwitchChange = (checked) => {
  if (checked == false) {
    form.githubHomepage = ''
  }
}

// 监听 Gitee Switch 改变事件
const giteeSwitchChange = (checked) => {
  if (checked == false) {
    form.giteeHomepage = ''
  }
}

// 监听知乎 Switch 改变事件
const zhihuSwitchChange = (checked) => {
  if (checked == false) {
    form.zhihuHomepage = ''
  }
}

// 监听 CSDN Switch 改变事件
const csdnSwitchChange = (checked) => {
  if (checked == false) {
    form.csdnHomepage = ''
  }
}

// 表单对象
const form = reactive({
  name: '',
  author: '',
  logo: '',
  avatar: '',
  introduction: '',
  githubHomepage: '',
  giteeHomepage: '',
  zhihuHomepage: '',
  csdnHomepage: '',
})

// 规则校验
const rules = {
  name: [{ required: true, message: '请输入博客名称', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者名', trigger: 'blur' }],
  logo: [{ required: true, message: '请上传博客 LOGO', trigger: 'blur' }],
  avatar: [{ required: true, message: '请上传作者头像', trigger: 'blur' }],
  introduction: [{ required: true, message: '请输入介绍语', trigger: 'blur' }],
}

// 初始化博客设置数据，并渲染到页面上
function initBlogSettings() {
  // 请求后台接口
  getBlogSettingsDetail().then((e) => {
    if (e.success == true) {
      blogSettingsStore.setBlogSettings(e.data)
      // 设置表单数据
      form.name = e.data.name
      form.author = e.data.author
      form.logo = e.data.logo
      form.avatar = e.data.avatar
      form.introduction = e.data.introduction

      // 第三方平台信息设置，先判断后端返回平台链接是否为空，若不为空，则将 switch 组件置为选中状态，并设置表单对应数据
      if (e.data.githubHomepage) {
        isGithubChecked.value = true
        form.githubHomepage = e.data.githubHomepage
      }

      if (e.data.giteeHomepage) {
        isGiteeChecked.value = true
        form.giteeHomepage = e.data.giteeHomepage
      }

      if (e.data.zhihuHomepage) {
        isZhihuChecked.value = true
        form.zhihuHomepage = e.data.zhihuHomepage
      }

      if (e.data.csdnHomepage) {
        isCSDNChecked.value = true
        form.csdnHomepage = e.data.csdnHomepage
      }
    }
  })
}
// 手动调用一下初始化方法
initBlogSettings()
</script>
<style scoped>
.settings-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
  color: #0f172a;
}

.settings-hero {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
}

.settings-eyebrow {
  display: inline-block;
  margin-bottom: 10px;
  color: #64748b;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.14em;
  text-transform: uppercase;
}

.settings-title {
  margin: 0;
  font-size: 34px;
  line-height: 1.1;
  font-weight: 700;
}

.settings-subtitle {
  max-width: 760px;
  margin: 10px 0 0;
  color: #64748b;
  font-size: 15px;
  line-height: 1.7;
}

.settings-save-btn {
  min-width: 118px;
  height: 44px;
  border: none;
  border-radius: 999px;
  background: linear-gradient(180deg, #1f2937 0%, #111827 100%);
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.12);
}

.settings-save-btn:hover {
  background: linear-gradient(180deg, #111827 0%, #0f172a 100%);
}

.settings-overview-card,
.settings-panel {
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.06);
  backdrop-filter: blur(22px);
}

.settings-overview-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 26px 30px;
}

.overview-brand {
  display: flex;
  align-items: center;
  gap: 18px;
  min-width: 0;
}

.overview-logo-wrap {
  flex-shrink: 0;
  width: 88px;
  height: 88px;
  padding: 10px;
  border-radius: 24px;
  background: linear-gradient(180deg, #f8fafc 0%, #edf2f7 100%);
  box-shadow: inset 0 0 0 1px rgba(148, 163, 184, 0.16);
}

.overview-logo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 18px;
}

.overview-meta {
  min-width: 0;
}

.overview-name {
  font-size: 26px;
  font-weight: 700;
  line-height: 1.2;
}

.overview-author {
  margin-top: 6px;
  color: #64748b;
  font-size: 14px;
}

.overview-intro {
  margin: 12px 0 0;
  color: #475569;
  font-size: 14px;
  line-height: 1.75;
}

.overview-avatar-wrap {
  flex-shrink: 0;
  width: 84px;
  height: 84px;
  padding: 6px;
  border-radius: 999px;
  background: linear-gradient(180deg, #ffffff 0%, #f1f5f9 100%);
  box-shadow: inset 0 0 0 1px rgba(148, 163, 184, 0.18);
}

.overview-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 999px;
}

.settings-form {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.settings-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.3fr) minmax(320px, 0.8fr);
  gap: 22px;
}

.settings-panel {
  padding: 26px;
}

.panel-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
}

.panel-header p {
  margin: 8px 0 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.7;
}

.settings-field-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.upload-group {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.social-list {
  display: grid;
  gap: 16px;
  margin-top: 22px;
}

.social-item {
  padding: 18px 18px 4px;
  border-radius: 22px;
  background: linear-gradient(180deg, rgba(248, 250, 252, 0.92) 0%, rgba(241, 245, 249, 0.92) 100%);
  box-shadow: inset 0 0 0 1px rgba(226, 232, 240, 0.9);
}

.social-item-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 14px;
}

.social-name {
  font-size: 17px;
  font-weight: 600;
}

.social-desc {
  margin-top: 4px;
  color: #64748b;
  font-size: 13px;
}

.upload-image {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-image-logo,
.upload-empty {
  width: 148px;
  height: 148px;
}

.upload-image-avatar,
.upload-empty-avatar {
  width: 148px;
  height: 148px;
  border-radius: 28px;
}

.upload-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #64748b;
  font-size: 14px;
}

@media (max-width: 1080px) {
  .settings-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .settings-hero,
  .settings-overview-card,
  .overview-brand,
  .social-item-head {
    flex-direction: column;
    align-items: flex-start;
  }

  .settings-field-grid,
  .upload-group {
    grid-template-columns: 1fr;
  }

  .settings-panel,
  .settings-overview-card {
    padding: 20px;
  }
}
</style>

<style>
/* 解决 textarea :focus 状态下，边框消失的问题 */
.el-textarea__inner:focus {
  outline: 0 !important;
  box-shadow: 0 0 0 1px var(--el-input-focus-border-color) inset !important;
}

.settings-form .el-form-item {
  margin-bottom: 20px;
}

.settings-form .el-form-item__label {
  color: #334155;
  font-weight: 600;
}

.settings-form .el-input__wrapper,
.settings-form .el-textarea__inner {
  border-radius: 18px;
  background: rgba(248, 250, 252, 0.96);
  box-shadow: 0 0 0 1px rgba(226, 232, 240, 0.92) inset !important;
  min-height: 48px;
}

.settings-form .el-textarea__inner {
  min-height: 120px;
  padding-top: 14px;
}

.settings-form .el-input__wrapper:hover,
.settings-form .el-textarea__inner:hover {
  box-shadow: 0 0 0 1px rgba(191, 219, 254, 1) inset !important;
}

.settings-form .el-switch {
  --el-switch-on-color: #111827;
  --el-switch-off-color: #cbd5e1;
}

.apple-uploader .el-upload {
  border: 1px dashed rgba(148, 163, 184, 0.35);
  border-radius: 28px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  background: linear-gradient(180deg, rgba(248, 250, 252, 0.96) 0%, rgba(241, 245, 249, 0.96) 100%);
  transition: all 0.2s ease;
}

.apple-uploader .el-upload:hover {
  border-color: rgba(59, 130, 246, 0.35);
  transform: translateY(-1px);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #94a3b8;
  text-align: center;
}
</style>
