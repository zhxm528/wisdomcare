# WisdomCare

Android 原生应用，Kotlin + Jetpack Compose。数据库通过本项目的 Node.js API 访问。

## 环境对应关系

| Android 构建 | 应用 ID | API | 后端数据库 |
| --- | --- | --- | --- |
| debug | com.wisdomcare.app.dev | 默认 http://10.0.2.2:3100 | dev，本机 PostgreSQL |
| release | com.wisdomcare.app | UI 演示可不配置；访问服务必须配置 HTTPS 地址 | 配置服务后为 prod，qmdj 远程 PostgreSQL |

APK 只包含 API 地址和环境标识，不包含数据库密码。后端启动时固定环境，切换后端环境需要重启进程。Android 检查返回的环境必须与构建一致，失败不会回退到另一环境。

## 本地开发

服务端连接方式迁移自 `D:/workspace/qmdj/lib/db.ts` 和 `infra/config-center/{dev,prod}/db.datasource.qmdj.yaml`，保留连接参数、连接池大小、超时设置；并按数据源的 `tlsEnabled` 配置 TLS。

真实配置位于 `server/config/dev/database.json` 和 `server/config/prod/database.json`，已加入 Git 忽略规则。配置仅用于后端，不放入 Android 资源目录。新机器参考 `server/config.example` 创建实际配置，再填写凭据。

```powershell
cd server
npm ci
npm test
npm run check:dev
npm run dev
```

Android Studio 选择 `debug` 运行，当前入口为登录/注册 UI 演示，不调用后端。原 `DatabaseConnectionPanel` 调试工具仍保留，可在开发时单独挂载。模拟器使用 `10.0.2.2` 访问电脑。真机可执行 `adb reverse tcp:3100 tcp:3100`，并在根目录 `local.properties` 添加 `wisdomcare.api.dev=http://127.0.0.1:3100`。如果通过局域网访问，设置电脑 IP 并使用 `$env:HOST='0.0.0.0'` 启动开发服务。

本机 JDK 在 `D:/JDK21`，现有系统 `JAVA_HOME=D:/JDK17` 无效。可以仅在当前 PowerShell 中设置 `$env:JAVA_HOME='D:/JDK21'`。项目 Wrapper 首次运行需要下载 Gradle；也可用本机 `D:/Android/tools/gradle-8.13/bin/gradle.bat`。

## 生产部署

在 qmdj 生产服务器上使用独立目录，例如 `/opt/wisdomcare/server`，上传服务端源码、锁文件和 prod 私有配置。仅上传 prod 配置；不需要 Android 工程或 dev 凭据。

```bash
cd /opt/wisdomcare/server
npm ci --omit=dev
chmod 700 config config/prod
chmod 600 config/prod/database.json
npm run check:prod
pm2 start ecosystem.config.cjs
pm2 save
curl --fail http://127.0.0.1:3100/health/database
```

默认仅监听 `127.0.0.1:3100`，不占用 qmdj 的服务端口。确认端口未被占用；必要时修改 PM2 的 `PORT` 和 Nginx 上游端口。将 `server/deploy/nginx-location.conf` 的 location 加入现有 HTTPS 虚拟主机，校验 `nginx -t` 后重载。若已有反向代理不是 Nginx，按同一路径转发到该端口。

通过 HTTPS URL 验证 `/wisdomcare-api/health/database` 返回 `{"status":"ok","environment":"prod"}` 后，在 `local.properties` 填写：

```properties
wisdomcare.api.prod=https://你的实际域名/wisdomcare-api
```

也可以用环境变量 `WISDOMCARE_API_PROD` 或 Gradle 参数 `-Pwisdomcare.api.prod=...`，优先级为 Gradle 参数、环境变量、local.properties。当前登录/注册只实现 UI：生产地址未配置时允许生成 UI 演示 release APK，数据库工具会在发送请求前拒绝调用，不回退到开发地址。配置生产地址后，必须是合法 HTTPS URL，不能包含凭据、查询参数或片段，否则 release 构建会失败。接入真实后端前必须配置并验证实际生产地址。

```powershell
.\gradlew.bat :app:assembleRelease
```

上述命令生成未签名 release APK，正式分发还需要配置生产签名。本项目尚未设置签名凭据。

生产账号及 TLS 设置沿用 qmdj；数据库无需建表或迁移数据。若原数据库未启用 TLS，请在服务器部署网络中控制数据库端口访问，启用 TLS 时使用可信证书。本次只提供 `GET /health/database` 只读检查，不暴露 SQL 执行接口。后续业务接口需加入登录认证和权限校验。

## 验证

Android Auth Feature 的页面、共享组件、导航与演示范围见 [ANDROID_AUTH_FEATURE.md](docs/ANDROID_AUTH_FEATURE.md)。默认主题及组件规则见 [UI_DESIGN_SYSTEM.md](docs/UI_DESIGN_SYSTEM.md)。

`server/npm test` 覆盖环境隔离、配置缺失不回退、TLS 配置、事务提交/回滚及 HTTP 故障信息隐藏。`npm run check:dev`、`npm run check:prod` 仅执行 `SELECT 1`，不会改动数据库。

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```
