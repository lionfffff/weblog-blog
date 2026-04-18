# Weblog - 前后端分离个人博客系统

一个基于 Spring Boot + Vue 3 的现代化前后端分离博客系统，支持文章管理、分类标签、全文搜索等功能。

## 📸 项目预览

- 前台首页：文章列表、分类、标签、归档
- 后台管理：仪表盘、文章管理、分类管理、标签管理、博客设置

## 🛠️ 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.6.3 | 基础框架 |
| Spring Security | - | 安全认证 |
| MyBatis-Plus | 3.5.2 | ORM 框架 |
| JWT | 0.11.2 | 身份认证 |
| MySQL | 8.x | 数据库 |
| MinIO | 8.2.1 | 对象存储 |
| Lucene | 8.11.1 | 全文检索 |
| Knife4j | 4.3.0 | API 文档 |
| Lombok | 1.18.28 | 简化代码 |
| MapStruct | 1.5.5 | 对象映射 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.x | 前端框架 |
| Vite | 6.x | 构建工具 |
| Vue Router | 4.5.x | 路由管理 |
| Pinia | 3.x | 状态管理 |
| Element Plus | 2.9.x | UI 组件库 |
| Tailwind CSS | 3.4.x | CSS 框架 |
| Axios | 1.8.x | HTTP 客户端 |
| ECharts | 5.6.x | 图表库 |
| md-editor-v3 | 5.7.x | Markdown 编辑器 |

## 📁 项目结构

```
├── weblog-springboot/          # 后端项目
│   ├── weblog-web/             # Web 启动模块（主入口）
│   ├── weblog-admin/           # 后台管理模块
│   ├── weblog-common/          # 公共模块（实体、工具类等）
│   ├── weblog-jwt/             # JWT 认证模块
│   └── weblog-search/          # Lucene 全文检索模块
│
└── weblog-vue/                 # 前端项目
    ├── src/
    │   ├── api/                # API 接口
    │   │   ├── admin/          # 后台管理接口
    │   │   └── fronted/        # 前台接口
    │   ├── components/         # 公共组件
    │   ├── composables/        # 组合式函数
    │   ├── layouts/            # 布局组件
    │   │   ├── admin/          # 后台布局
    │   │   └── frontend/       # 前台布局
    │   ├── pages/              # 页面
    │   │   ├── admin/          # 后台页面
    │   │   └── frontend/       # 前台页面
    │   ├── router/             # 路由配置
    │   └── stores/             # Pinia 状态管理
    └── ...
```

## ✨ 功能特性

### 前台功能
- 📝 文章列表展示（分页）
- 📂 分类浏览
- 🏷️ 标签浏览
- 📅 文章归档
- 🔍 全文搜索（Lucene）
- 📖 文章详情（Markdown 渲染）
- 👤 博主信息展示

### 后台功能
- 📊 仪表盘（文章数、分类数、标签数、PV 统计）
- 📈 文章发布热点图
- 📉 PV 访问量折线图
- ✍️ 文章管理（发布、编辑、删除）
- 📁 分类管理
- 🏷️ 标签管理
- ⚙️ 博客设置
- 🖼️ 图片上传（MinIO）

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+
- MinIO（可选，用于图片存储）


### 后端启动

1. 修改配置文件 `weblog-springboot/weblog-web/src/main/resources/application-dev.yml`
```yaml
server:
  port: 8086

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/weblog?useUnicode=true&characterEncoding=UTF-8
    username: your_username
    password: your_password

# MinIO 配置（可选）
minio:
  endpoint: http://127.0.0.1:9000
  accessKey: your_access_key
  secretKey: your_secret_key
  bucketName: weblog

# Lucene 索引目录
lucene:
  indexDir: /path/to/lucene-index
```

2. 编译打包
```bash
cd weblog-springboot
mvn clean package -DskipTests
```

3. 启动服务
```bash
java -jar weblog-web/target/weblog-web-0.0.1-SNAPSHOT.jar
```

### 前端启动

1. 安装依赖
```bash
cd weblog-vue
npm install
```

2. 开发模式启动
```bash
npm run dev
```

3. 生产构建
```bash
npm run build
```

### 访问地址
- 前台首页：http://localhost:5173
- 后台管理：http://localhost:5173/#/login
- API 文档：http://localhost:8086/doc.html

## 📝 API 接口

### 前台接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /article/list | POST | 获取文章列表 |
| /article/detail | POST | 获取文章详情 |
| /category/list | POST | 获取分类列表 |
| /tag/list | POST | 获取标签列表 |
| /archive/list | POST | 获取归档列表 |
| /search | POST | 全文搜索 |
| /statistics | GET | 获取统计信息 |
| /blogsettings/detail | POST | 获取博客设置 |

### 后台接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /admin/article/publish | POST | 发布文章 |
| /admin/article/update | POST | 更新文章 |
| /admin/article/delete | POST | 删除文章 |
| /admin/category/add | POST | 添加分类 |
| /admin/category/delete | POST | 删除分类 |
| /admin/tag/add | POST | 添加标签 |
| /admin/tag/delete | POST | 删除标签 |
| /admin/file/upload | POST | 上传文件 |
| /admin/blogsettings/update | POST | 更新博客设置 |

## 🔧 配置说明

### JWT 配置
```yaml
jwt:
  issuer: Eason              # 签发人
  secret: your_secret_key    # 密钥
  tokenExpireTime: 1440      # Token 过期时间（分钟）
  tokenHeaderKey: Authorization
  tokenPrefix: Bearer
```

### 文件上传配置
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
```

## 🤝 作者
手写线程池
欢迎提交 Issue 和 Pull Request！
