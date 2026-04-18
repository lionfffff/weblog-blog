<template>
  <section class="carousel-card" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
    <button class="carousel-arrow carousel-arrow--left" type="button" aria-label="上一张" @click="prev">
      <el-icon><ArrowLeft /></el-icon>
    </button>

    <div class="carousel-track">
      <Transition name="slide-fade" mode="out-in">
        <article :key="activeSlide.id" class="slide-panel">
          <div class="slide-copy">
            <div class="slide-tags">
              <span v-for="tag in activeSlide.tags" :key="tag" class="slide-tag">{{ tag }}</span>
            </div>

            <h2>{{ activeSlide.title }}</h2>
            <p>{{ activeSlide.description }}</p>
          </div>

          <div class="slide-preview">
            <div v-if="activeSlide.preview === 'workspace'" class="preview preview--workspace">
              <div class="preview-shell">
                <div class="preview-shell__top">
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <div class="window-search"></div>
                </div>

                <div class="preview-shell__layout">
                  <aside class="preview-nav">
                    <span class="preview-nav__title"></span>
                    <i></i>
                    <i></i>
                    <i></i>
                    <i></i>
                  </aside>

                  <div class="preview-main">
                    <div class="preview-main__header">
                      <div>
                        <strong>作者空间</strong>
                        <small>4 位管理员</small>
                      </div>
                      <button>新建文章</button>
                    </div>

                    <div class="preview-grid preview-grid--authors">
                      <article class="mock-card mock-card--active">
                        <div class="mock-avatar"></div>
                        <div class="mock-lines">
                          <span class="mock-line mock-line--mid"></span>
                          <span class="mock-line mock-line--short"></span>
                        </div>
                        <div class="mock-pills">
                          <b></b>
                          <b></b>
                        </div>
                      </article>

                      <article class="mock-card">
                        <div class="mock-avatar mock-avatar--soft"></div>
                        <div class="mock-lines">
                          <span class="mock-line mock-line--mid"></span>
                          <span class="mock-line mock-line--short"></span>
                        </div>
                      </article>

                      <article class="mock-card mock-card--wide">
                        <div class="mock-list">
                          <span class="mock-line mock-line--long"></span>
                          <span class="mock-line mock-line--mid"></span>
                          <span class="mock-line"></span>
                        </div>
                      </article>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div v-else-if="activeSlide.preview === 'editor'" class="preview preview--editor">
              <div class="preview-shell">
                <div class="preview-shell__top">
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <div class="window-tabs">
                    <i></i>
                    <i></i>
                  </div>
                </div>

                <div class="editor-layout">
                  <section class="editor-body">
                    <span class="mock-line mock-line--short"></span>
                    <span class="mock-line mock-line--long editor-title"></span>
                    <div class="editor-cover"></div>
                    <div class="editor-copy">
                      <span class="mock-line mock-line--long"></span>
                      <span class="mock-line mock-line--mid"></span>
                      <span class="mock-line"></span>
                    </div>
                  </section>

                  <aside class="editor-side">
                    <div class="side-card">
                      <strong>发布设置</strong>
                      <div class="mock-pills">
                        <b></b>
                        <b></b>
                      </div>
                      <span class="mock-line mock-line--mid"></span>
                      <button class="side-button">立即发布</button>
                    </div>
                  </aside>
                </div>
              </div>
            </div>

            <div v-else-if="activeSlide.preview === 'taxonomy'" class="preview preview--taxonomy">
              <div class="preview-shell">
                <div class="preview-shell__top">
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <div class="window-search window-search--wide"></div>
                </div>

                <div class="taxonomy-layout">
                  <section class="taxonomy-card">
                    <div class="taxonomy-card__head">
                      <strong>分类</strong>
                      <button>+ 新建</button>
                    </div>
                    <div class="taxonomy-list">
                      <div class="taxonomy-item">
                        <span class="mock-line mock-line--mid"></span>
                        <b>12</b>
                      </div>
                      <div class="taxonomy-item">
                        <span class="mock-line mock-line--short"></span>
                        <b>8</b>
                      </div>
                      <div class="taxonomy-item">
                        <span class="mock-line"></span>
                        <b>5</b>
                      </div>
                    </div>
                  </section>

                  <section class="taxonomy-card">
                    <div class="taxonomy-card__head">
                      <strong>标签</strong>
                      <button>筛选</button>
                    </div>
                    <div class="tag-cloud">
                      <span></span>
                      <span></span>
                      <span></span>
                      <span></span>
                      <span></span>
                    </div>
                  </section>
                </div>
              </div>
            </div>

            <div v-else class="preview preview--overview">
              <div class="preview-shell">
                <div class="preview-shell__top">
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                  <span class="window-dot"></span>
                </div>

                <div class="overview-layout">
                  <div class="overview-stats">
                    <article></article>
                    <article></article>
                    <article></article>
                  </div>

                  <div class="overview-chart">
                    <div class="overview-chart__line"></div>
                  </div>

                  <div class="overview-list">
                    <span class="mock-line mock-line--long"></span>
                    <span class="mock-line mock-line--mid"></span>
                    <span class="mock-line"></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </article>
      </Transition>
    </div>

    <button class="carousel-arrow carousel-arrow--right" type="button" aria-label="下一张" @click="next">
      <el-icon><ArrowRight /></el-icon>
    </button>

    <div class="carousel-dots">
      <button
        v-for="(slide, index) in slides"
        :key="slide.id"
        class="carousel-dot"
        :class="{ 'carousel-dot--active': index === activeIndex }"
        type="button"
        @click="goTo(index)"
        :aria-label="`切换到第 ${index + 1} 张`"
      ></button>
    </div>
  </section>
</template>

<script setup>
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

const props = defineProps({
  slides: {
    type: Array,
    required: true,
  },
  interval: {
    type: Number,
    default: 4200,
  },
})

const activeIndex = ref(0)
let timer = null

const activeSlide = computed(() => props.slides[activeIndex.value] || props.slides[0])

const startAutoPlay = () => {
  stopAutoPlay()
  if (props.slides.length <= 1) return
  timer = window.setInterval(() => {
    next(false)
  }, props.interval)
}

const stopAutoPlay = () => {
  if (timer) {
    window.clearInterval(timer)
    timer = null
  }
}

const resetAutoPlay = () => {
  startAutoPlay()
}

const goTo = (index) => {
  activeIndex.value = index
  resetAutoPlay()
}

const prev = () => {
  activeIndex.value = (activeIndex.value - 1 + props.slides.length) % props.slides.length
  resetAutoPlay()
}

const next = (reset = true) => {
  activeIndex.value = (activeIndex.value + 1) % props.slides.length
  if (reset) {
    resetAutoPlay()
  }
}

onMounted(() => {
  startAutoPlay()
})

onBeforeUnmount(() => {
  stopAutoPlay()
})
</script>

<style scoped>
.carousel-card {
  position: relative;
  display: grid;
  gap: 18px;
  padding: 24px 26px 20px;
  border: 1px solid rgba(226, 232, 240, 0.92);
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.93);
  box-shadow: 0 20px 44px rgba(15, 23, 42, 0.05);
  height: 486px;
  min-height: 486px;
  overflow: hidden;
}

.carousel-track {
  height: 404px;
  min-height: 404px;
}

.slide-panel {
  display: grid;
  grid-template-columns: minmax(0, 0.72fr) minmax(320px, 1.18fr);
  gap: 22px;
  align-items: center;
  height: 404px;
  min-height: 404px;
}

.slide-copy {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 8px;
  height: 100%;
  min-height: 100%;
  overflow: hidden;
}

.slide-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.slide-tag {
  display: inline-flex;
  align-items: center;
  height: 28px;
  padding: 0 12px;
  border-radius: 999px;
  background: rgba(241, 245, 249, 0.94);
  color: #475569;
  font-size: 12px;
  font-weight: 600;
}

.slide-copy h2 {
  margin: 18px 0 8px;
  color: #111827;
  font-size: 28px;
  line-height: 1.14;
  letter-spacing: -0.05em;
  min-height: 64px;
}

.slide-copy p {
  max-width: 220px;
  margin: 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.8;
  min-height: 52px;
}

.slide-preview {
  min-width: 0;
}

.preview {
  border-radius: 26px;
  background: linear-gradient(180deg, rgba(248, 250, 252, 0.98) 0%, rgba(241, 245, 249, 0.98) 100%);
  border: 1px solid rgba(226, 232, 240, 0.94);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.88);
  overflow: hidden;
}

.preview-shell {
  height: 340px;
  min-height: 340px;
}

.preview-shell__top {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 52px;
  padding: 0 18px;
  border-bottom: 1px solid rgba(226, 232, 240, 0.86);
  background: rgba(255, 255, 255, 0.9);
}

.window-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(148, 163, 184, 0.38);
}

.window-search,
.window-tabs {
  margin-left: auto;
}

.window-search {
  width: 138px;
  height: 30px;
  border-radius: 999px;
  background: rgba(241, 245, 249, 0.96);
  border: 1px solid rgba(226, 232, 240, 0.92);
}

.window-search--wide {
  width: 188px;
}

.window-tabs {
  display: flex;
  gap: 8px;
}

.window-tabs i {
  display: block;
  width: 76px;
  height: 28px;
  border-radius: 999px;
  background: rgba(241, 245, 249, 0.96);
}

.preview-shell__layout,
.editor-layout,
.taxonomy-layout,
.overview-layout {
  min-height: 288px;
}

.preview-shell__layout {
  display: grid;
  grid-template-columns: 92px 1fr;
}

.preview-nav {
  display: grid;
  align-content: start;
  gap: 12px;
  padding: 18px 14px;
  border-right: 1px solid rgba(226, 232, 240, 0.84);
  background: rgba(255, 255, 255, 0.64);
}

.preview-nav__title,
.preview-nav i,
.mock-line,
.mock-pills b,
.taxonomy-item b {
  display: block;
  border-radius: 999px;
  background: rgba(148, 163, 184, 0.2);
}

.preview-nav__title {
  width: 44px;
  height: 10px;
}

.preview-nav i {
  width: 100%;
  height: 10px;
}

.preview-main {
  padding: 18px;
}

.preview-main__header,
.taxonomy-card__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.preview-main__header strong,
.taxonomy-card__head strong,
.side-card strong {
  color: #0f172a;
  font-size: 15px;
  font-weight: 700;
}

.preview-main__header small {
  display: block;
  margin-top: 4px;
  color: #94a3b8;
}

.preview-main__header button,
.taxonomy-card__head button,
.side-button {
  height: 34px;
  padding: 0 14px;
  border: none;
  border-radius: 999px;
  background: rgba(37, 99, 235, 0.12);
  color: #2563eb;
  font-size: 12px;
  font-weight: 700;
}

.preview-grid {
  display: grid;
  gap: 12px;
  margin-top: 16px;
}

.preview-grid--authors {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.mock-card {
  padding: 14px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(226, 232, 240, 0.92);
}

.mock-card--active {
  background: linear-gradient(180deg, rgba(239, 246, 255, 0.96) 0%, rgba(255, 255, 255, 0.98) 100%);
  border-color: rgba(147, 197, 253, 0.88);
}

.mock-card--wide {
  grid-column: 1 / -1;
}

.mock-avatar {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: linear-gradient(180deg, rgba(37, 99, 235, 0.22) 0%, rgba(96, 165, 250, 0.28) 100%);
}

.mock-avatar--soft {
  background: linear-gradient(180deg, rgba(148, 163, 184, 0.16) 0%, rgba(203, 213, 225, 0.28) 100%);
}

.mock-lines {
  margin-top: 14px;
}

.mock-line {
  height: 10px;
  margin-top: 10px;
}

.mock-line--long {
  width: 84%;
}

.mock-line--mid {
  width: 62%;
}

.mock-line--short {
  width: 42%;
}

.mock-pills {
  display: flex;
  gap: 8px;
  margin-top: 16px;
}

.mock-pills b {
  width: 66px;
  height: 26px;
}

.mock-list {
  padding-top: 4px;
}

.editor-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 172px;
  gap: 14px;
  padding: 18px;
}

.editor-body,
.side-card,
.taxonomy-card {
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
}

.editor-body {
  padding: 16px;
}

.editor-title {
  height: 16px;
  margin-top: 18px;
}

.editor-cover {
  height: 104px;
  margin-top: 18px;
  border-radius: 18px;
  background:
    linear-gradient(180deg, rgba(191, 219, 254, 0.84) 0%, rgba(219, 234, 254, 0.96) 100%);
}

.editor-copy {
  margin-top: 18px;
}

.editor-side {
  display: flex;
}

.side-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  padding: 16px;
}

.side-button {
  width: 100%;
  margin-top: auto;
  background: linear-gradient(180deg, #1d4ed8 0%, #2563eb 100%);
  color: #fff;
}

.taxonomy-layout {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  padding: 18px;
}

.taxonomy-card {
  padding: 16px;
}

.taxonomy-list {
  display: grid;
  gap: 12px;
  margin-top: 16px;
}

.taxonomy-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.taxonomy-item b {
  width: 36px;
  height: 26px;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 16px;
}

.tag-cloud span {
  display: inline-flex;
  width: 78px;
  height: 30px;
  border-radius: 999px;
  background: rgba(241, 245, 249, 0.96);
  border: 1px solid rgba(226, 232, 240, 0.88);
}

.overview-layout {
  padding: 18px;
}

.overview-stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
}

.overview-stats article {
  height: 72px;
  border-radius: 22px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(226, 232, 240, 0.9);
}

.overview-chart {
  position: relative;
  height: 138px;
  margin-top: 16px;
  border-radius: 24px;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.88) 0%, rgba(241, 245, 249, 0.96) 100%);
  border: 1px solid rgba(226, 232, 240, 0.9);
  overflow: hidden;
}

.overview-chart__line {
  position: absolute;
  left: 18px;
  right: 18px;
  bottom: 34px;
  height: 70px;
  border-bottom: 3px solid rgba(37, 99, 235, 0.65);
  border-radius: 0 0 80px 80px;
}

.overview-list {
  margin-top: 16px;
  padding: 16px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.88);
  border: 1px solid rgba(226, 232, 240, 0.9);
}

.carousel-arrow {
  position: absolute;
  top: 50%;
  z-index: 2;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: 1px solid rgba(226, 232, 240, 0.96);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.94);
  color: #475569;
  transform: translateY(-50%);
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.08);
  transition:
    background 0.2s ease,
    color 0.2s ease,
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.carousel-arrow:hover {
  color: #111827;
  background: #fff;
  transform: translateY(-50%) scale(1.02);
}

.carousel-arrow--left {
  left: 14px;
}

.carousel-arrow--right {
  right: 14px;
}

.carousel-dots {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.carousel-dot {
  width: 8px;
  height: 8px;
  border: none;
  border-radius: 999px;
  background: rgba(148, 163, 184, 0.34);
  transition: all 0.2s ease;
}

.carousel-dot--active {
  width: 24px;
  background: #2563eb;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.28s ease;
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateX(14px);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateX(-14px);
}

@media (max-width: 1240px) {
  .slide-panel {
    grid-template-columns: 1fr;
    height: auto;
    min-height: unset;
  }

  .slide-copy h2,
  .slide-copy p {
    min-height: unset;
  }

  .slide-copy p {
    max-width: none;
  }
}

@media (max-width: 760px) {
  .carousel-card {
    padding: 18px 18px 16px;
    border-radius: 26px;
    height: auto;
    min-height: 0;
  }

  .carousel-track {
    height: auto;
    min-height: 0;
  }

  .slide-copy {
    padding-left: 0;
    height: auto;
    min-height: 0;
  }

  .slide-copy h2 {
    font-size: 24px;
  }

  .preview-shell {
    height: auto;
    min-height: 292px;
  }

  .editor-layout,
  .taxonomy-layout {
    grid-template-columns: 1fr;
  }

  .carousel-arrow {
    width: 36px;
    height: 36px;
  }

  .carousel-arrow--left {
    left: 10px;
  }

  .carousel-arrow--right {
    right: 10px;
  }
}
</style>
