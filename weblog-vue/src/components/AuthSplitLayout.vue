<template>
  <main class="auth-shell">
    <section class="auth-frame">
      <aside class="auth-showcase">
        <div class="brand-row">
          <div class="brand-mark">W</div>
          <div class="brand-copy">
            <strong>WeBlog</strong>
            <span>多管理员独立博客平台</span>
          </div>
        </div>

        <div class="showcase-copy">
          <h1>管理博客内容</h1>
          <p>多管理员内容后台</p>
        </div>

        <AuthShowcaseCarousel :slides="showcaseSlides" />
      </aside>

      <section class="auth-panel">
        <div class="panel-head">
          <span v-if="panelLabel" class="panel-label">{{ panelLabel }}</span>
          <h2>{{ title }}</h2>
          <p v-if="description">{{ description }}</p>
        </div>

        <slot />

        <div v-if="footerText || footerActionText" class="auth-foot">
          <span>{{ footerText }}</span>
          <button v-if="footerActionText" class="link-button" type="button" @click="goFooter">
            {{ footerActionText }}
          </button>
        </div>
      </section>
    </section>
  </main>
</template>

<script setup>
import { useRouter } from 'vue-router'
import AuthShowcaseCarousel from '@/components/AuthShowcaseCarousel.vue'

const props = defineProps({
  panelLabel: {
    type: String,
    default: '',
  },
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: '',
  },
  footerText: {
    type: String,
    default: '',
  },
  footerActionText: {
    type: String,
    default: '',
  },
  footerActionTo: {
    type: String,
    default: '',
  },
})

const router = useRouter()

const showcaseSlides = [
  {
    id: 1,
    title: '作者空间',
    description: '每位作者单独维护',
    tags: ['独立博客', '作者后台'],
    preview: 'workspace',
  },
  {
    id: 2,
    title: '文章发布',
    description: '编辑后直接预览',
    tags: ['编辑器', '发布面板'],
    preview: 'editor',
  },
  {
    id: 3,
    title: '分类标签',
    description: '筛选和整理更清楚',
    tags: ['分类管理', '标签筛选'],
    preview: 'taxonomy',
  },
  {
    id: 4,
    title: '内容总览',
    description: '归档搜索随手查看',
    tags: ['归档视图', '数据总览'],
    preview: 'overview',
  },
]

const goFooter = () => {
  if (!props.footerActionTo) return
  router.push(props.footerActionTo)
}
</script>

<style scoped>
.auth-shell {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 28px 20px;
  background:
    radial-gradient(circle at top left, rgba(59, 130, 246, 0.08), transparent 20%),
    radial-gradient(circle at right bottom, rgba(148, 163, 184, 0.1), transparent 22%),
    linear-gradient(180deg, #f3f4f6 0%, #eef2f7 100%);
}

.auth-frame {
  display: grid;
  grid-template-columns: minmax(0, 1.08fr) minmax(380px, 460px);
  width: min(100%, 1220px);
  min-height: 760px;
  border: 1px solid rgba(226, 232, 240, 0.94);
  border-radius: 36px;
  background: rgba(255, 255, 255, 0.94);
  box-shadow: 0 28px 70px rgba(15, 23, 42, 0.08);
  overflow: hidden;
}

.auth-showcase {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 52px 56px;
  background:
    radial-gradient(circle at top left, rgba(219, 234, 254, 0.56), transparent 30%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.88) 0%, rgba(248, 250, 252, 0.98) 100%);
}

.auth-panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 52px 52px 44px;
  border-left: 1px solid rgba(226, 232, 240, 0.96);
  background: rgba(255, 255, 255, 0.98);
}

.brand-row {
  display: flex;
  align-items: center;
  gap: 14px;
}

.brand-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: 18px;
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  color: #fff;
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.04em;
}

.brand-copy {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.brand-copy strong {
  color: #111827;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: -0.03em;
}

.brand-copy span {
  color: #64748b;
  font-size: 15px;
}

.showcase-copy {
  margin-top: 28px;
}

.panel-label {
  display: inline-flex;
  color: #94a3b8;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.showcase-copy h1 {
  max-width: 320px;
  margin: 0;
  color: #111827;
  font-size: clamp(30px, 3vw, 42px);
  line-height: 1.16;
  letter-spacing: -0.05em;
  white-space: nowrap;
}

.showcase-copy p {
  max-width: 220px;
  margin: 10px 0 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.8;
}

.panel-head h2 {
  margin: 14px 0 10px;
  color: #111827;
  font-size: 34px;
  line-height: 1.12;
  letter-spacing: -0.04em;
}

.panel-head p {
  margin: 0;
  color: #64748b;
  font-size: 15px;
  line-height: 1.8;
}

:deep(.auth-form) {
  margin-top: 28px;
}

:deep(.auth-form .el-form-item) {
  margin-bottom: 18px;
}

:deep(.auth-form .el-form-item__label) {
  padding-bottom: 8px;
  color: #374151;
  font-size: 14px;
  font-weight: 600;
}

:deep(.auth-input .el-input__wrapper) {
  min-height: 54px;
  border-radius: 18px;
  background: #f8fafc;
  box-shadow: inset 0 0 0 1px rgba(203, 213, 225, 0.84);
}

:deep(.auth-input .el-input__wrapper.is-focus) {
  box-shadow:
    inset 0 0 0 1px rgba(37, 99, 235, 0.92),
    0 0 0 4px rgba(96, 165, 250, 0.14);
}

:deep(.primary-button) {
  width: 100%;
  height: 54px;
  margin-top: 8px;
  border: none;
  border-radius: 999px;
  background: linear-gradient(180deg, #1d4ed8 0%, #2563eb 100%);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  box-shadow: 0 18px 30px rgba(37, 99, 235, 0.2);
}

.auth-foot {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 22px;
  color: #64748b;
  font-size: 14px;
}

.link-button {
  border: none;
  background: transparent;
  color: #2563eb;
  font-weight: 700;
  cursor: pointer;
}

@media (max-width: 1080px) {
  .auth-frame {
    grid-template-columns: 1fr;
    min-height: unset;
  }

  .auth-panel {
    order: 1;
    border-left: none;
    border-top: 1px solid rgba(226, 232, 240, 0.96);
  }

  .auth-showcase {
    order: 2;
    padding-top: 34px;
  }

  .showcase-copy h1 {
    font-size: clamp(28px, 6vw, 38px);
    white-space: normal;
  }
}

@media (min-width: 1081px) {
  .auth-shell {
    height: 100vh;
    min-height: 100vh;
    overflow: hidden;
  }

  .auth-frame {
    height: min(820px, calc(100vh - 56px));
    min-height: 760px;
  }

  .auth-showcase,
  .auth-panel {
    min-height: 0;
  }
}

@media (max-width: 640px) {
  .auth-shell {
    padding: 16px;
  }

  .auth-frame {
    border-radius: 26px;
  }

  .auth-panel,
  .auth-showcase {
    padding: 28px 22px;
  }

  .brand-mark {
    width: 48px;
    height: 48px;
    font-size: 20px;
  }

  .brand-copy strong {
    font-size: 20px;
  }

  .showcase-copy h1 {
    font-size: 28px;
  }
}
</style>
