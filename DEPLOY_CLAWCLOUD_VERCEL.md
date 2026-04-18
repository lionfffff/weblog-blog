# ClawCloud + Vercel 部署指南

## 你要准备的账号
1. GitHub
2. ClawCloud
3. Vercel

## 你的部署结构
- 前端：Vercel
- 后端：ClawCloud App Launchpad
- 数据库：ClawCloud MySQL

## 第 1 步：推送代码到 GitHub
1. 打开项目根目录：`E:\操作系统第四次实验\blog-master\blog-master`
2. 确保这是你的 Git 仓库根目录
3. 推送到 GitHub

## 第 2 步：创建 ClawCloud MySQL
1. 登录 ClawCloud
2. 打开 `Database`
3. 创建 MySQL
4. 数据库名填 `weblog`
5. 记下：
   - Host
   - Port
   - Username
   - Password

## 第 3 步：导出并导入本地数据库
导出：
```powershell
mysqldump -u root -p --default-character-set=utf8mb4 weblog > weblog_cloud.sql
```

导入：
```powershell
mysql -h <Host> -P <Port> -u <Username> -p weblog < weblog_cloud.sql
```

## 第 4 步：发布镜像
本仓库已经内置 GitHub Actions：
- 后端镜像：`ghcr.io/<你的 GitHub 用户名>/weblog-backend:latest`
- 前端镜像：`ghcr.io/<你的 GitHub 用户名>/weblog-frontend:latest`

你只需要：
1. 把代码推到 GitHub
2. 打开 GitHub 仓库的 `Actions`
3. 等待 `Build and Publish Docker Images` 成功

## 第 5 步：部署后端到 ClawCloud
1. 打开 `App Launchpad`
2. 选择 `Deploy from Docker`
3. 镜像填：
   `ghcr.io/<你的 GitHub 用户名>/weblog-backend:latest`
4. 端口填 `8080`
5. 添加环境变量：

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:mysql://<Host>:3306/weblog?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull
SPRING_DATASOURCE_USERNAME=<Username>
SPRING_DATASOURCE_PASSWORD=<Password>
JWT_SECRET=<你自己生成的一长串密钥>
MINIO_ENDPOINT=http://127.0.0.1:9000
MINIO_ACCESS_KEY=dummyaccess
MINIO_SECRET_KEY=dummysecret123
MINIO_BUCKET_NAME=weblog
LUCENE_INDEX_DIR=/app/weblog/lucene-index
LOCAL_UPLOAD_DIR=/app/weblog/uploaded-files
```

6. 添加持久化目录：
   - `/app/weblog/lucene-index`
   - `/app/weblog/uploaded-files`
7. 点击部署
8. 记下后端域名

## 第 6 步：部署前端到 Vercel
1. 登录 Vercel
2. 点击 `Add New Project`
3. 导入 GitHub 仓库
4. Root Directory 选 `weblog-vue`
5. Build Command 保持 `npm run build`
6. Output Directory 保持 `dist`
7. 点击部署

## 第 7 步：修改 Vercel 代理地址
打开文件：
`weblog-vue/vercel.json`

把这里：
```json
"destination": "https://your-clawcloud-backend.example.com/$1"
```

改成你的真实后端域名，例如：
```json
"destination": "https://weblog-backend-xxxx.clawcloudrun.com/$1"
```

然后重新推送 GitHub，让 Vercel 自动重新部署。

## 第 8 步：检查是否成功
1. 打开 Vercel 前端域名
2. 首页能看到你原本的博客数据
3. 能登录后台
4. 能打开分类、标签、归档、浏览统计
5. 搜索能返回结果
6. 上传图片时即使 MinIO 不可用，也能回退到本地磁盘

## 注意事项
- 当前阶段前端不放 ClawCloud，节省每月免费额度
- 当前阶段不单独部署 MinIO
- 生产上传目录和 Lucene 索引目录必须持久化，否则重启就丢
