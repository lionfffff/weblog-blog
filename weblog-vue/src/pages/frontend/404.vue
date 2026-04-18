<template>
  <Header />

  <main class="not-found-shell">
    <section class="not-found-card">
      <span class="eyebrow">404</span>
      <h1>这个页面已经飞到别处去了</h1>
      <p>你访问的内容不存在，或者当前没有权限查看。回到博客选择页，重新挑一个管理员博客继续浏览吧。</p>
      <div class="actions">
        <button class="primary-button" @click="router.push('/blogs')">返回博客选择</button>
        <button class="ghost-button" @click="router.back()">返回上一页</button>
      </div>
    </section>
  </main>

  <Footer />
</template>

<script setup>
import Header from '@/layouts/frontend/components/Header.vue'
import Footer from '@/layouts/frontend/components/Footer.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped>
.not-found-shell {
  min-height: calc(100vh - 180px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 20px 56px;
}

.not-found-card {
  width: min(100%, 720px);
  padding: 48px;
  text-align: center;
  border-radius: 32px;
  border: 1px solid rgba(229, 231, 235, 0.95);
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 24px 70px rgba(15, 23, 42, 0.08);
}

.eyebrow {
  display: inline-flex;
  padding: 6px 12px;
  border-radius: 999px;
  background: rgba(219, 234, 254, 0.9);
  color: #2563eb;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.not-found-card h1 {
  margin: 14px 0 12px;
  color: #111827;
  font-size: clamp(34px, 6vw, 52px);
  line-height: 1.05;
  letter-spacing: -0.05em;
}

.not-found-card p {
  margin: 0;
  color: #6b7280;
  line-height: 1.8;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 24px;
  flex-wrap: wrap;
}

.primary-button,
.ghost-button {
  height: 46px;
  padding: 0 20px;
  border-radius: 999px;
  font-weight: 600;
}

.primary-button {
  border: none;
  background: linear-gradient(180deg, #1d4ed8 0%, #2563eb 100%);
  color: #fff;
  box-shadow: 0 14px 30px rgba(37, 99, 235, 0.22);
}

.ghost-button {
  border: 1px solid rgba(209, 213, 219, 0.9);
  background: rgba(255, 255, 255, 0.9);
  color: #111827;
}

@media (max-width: 640px) {
  .not-found-card {
    padding: 34px 22px;
    border-radius: 24px;
  }
}
</style>
