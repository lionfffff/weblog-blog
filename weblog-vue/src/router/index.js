import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/pages/admin/login.vue'
import Register from '@/pages/admin/register.vue'
import Blogs from '@/pages/frontend/blogs.vue'
import Index from '@/pages/frontend/index.vue'
import ArchiveList from '@/pages/frontend/archive-list.vue'
import StatisticsView from '@/pages/frontend/statistics.vue'
import CategoryList from '@/pages/frontend/category-list.vue'
import CategoryArticleList from '@/pages/frontend/category-article-list.vue'
import TagList from '@/pages/frontend/tag-list.vue'
import TagArticleList from '@/pages/frontend/tag-article-list.vue'
import ArticleDetail from '@/pages/frontend/article-detail.vue'
import NotFound from '@/pages/frontend/404.vue'
import Admin from '@/layouts/admin/admin.vue'
import AdminIndex from '@/pages/admin/index.vue'
import AdminArticleList from '@/pages/admin/article-list.vue'
import AdminCategoryList from '@/pages/admin/category-list.vue'
import AdminTagList from '@/pages/admin/tag-list.vue'
import AdminBlogSetting from '@/pages/admin/blog-setting.vue'

const routes = [
  { path: '/', redirect: '/blogs' },
  { path: '/login', component: Login, meta: { title: '登录' } },
  { path: '/register', component: Register, meta: { title: '注册' } },
  { path: '/blogs', component: Blogs, meta: { title: '选择博客' } },
  { path: '/u/:username', component: Index, meta: { title: '博客首页' } },
  { path: '/u/:username/article/:articleId', component: ArticleDetail, meta: { title: '文章详情' } },
  { path: '/u/:username/category/list', component: CategoryList, meta: { title: '分类' } },
  { path: '/u/:username/category/article/list', component: CategoryArticleList, meta: { title: '分类文章' } },
  { path: '/u/:username/tag/list', component: TagList, meta: { title: '标签' } },
  { path: '/u/:username/tag/article/list', component: TagArticleList, meta: { title: '标签文章' } },
  { path: '/u/:username/archive/list', component: ArchiveList, meta: { title: '归档' } },
  { path: '/u/:username/statistics', component: StatisticsView, meta: { title: '浏览统计' } },
  {
    path: '/admin',
    component: Admin,
    children: [
      { path: '/admin/index', component: AdminIndex, meta: { title: '仪表盘' } },
      { path: '/admin/article/list', component: AdminArticleList, meta: { title: '文章管理' } },
      { path: '/admin/category/list', component: AdminCategoryList, meta: { title: '分类管理' } },
      { path: '/admin/tag/list', component: AdminTagList, meta: { title: '标签管理' } },
      { path: '/admin/blog/setting', component: AdminBlogSetting, meta: { title: '博客设置' } },
    ],
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound, meta: { title: '404' } },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

export default router
