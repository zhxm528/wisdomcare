# Android Auth Feature

应用入口为 `feature/auth/AuthNavigation`，默认展示登录页。登录、注册屏幕只依赖
`ui/components` 的 QButton、QInput、QCard，提交反馈使用 QDialog。共享组件按
`UI_DESIGN_SYSTEM.md` 的既有契约包装 Material 3；颜色、字号、形状和布局均消费
QMDJTheme 的语义 token，没有新增主题或 feature 局部设计值。

## 交互和边界

- 登录：手机号、密码、密码显示切换、去注册。
- 注册：手机号、密码、确认密码、返回登录。
- 两页使用可恢复的有限目的地状态，注册页的系统 Back 和可见返回操作均回到登录。
  没有新增导航依赖；未来接入应用级 NavHost 时可直接复用无状态屏幕。
- 两页草稿独立，在页面切换时保留。手机号和当前目的地支持状态恢复；密码仅存在
  Compose 内存中，不写入 Bundle、文件、日志或数据库，Activity 重建后需重新填写。
- 演示表单规则：11 位中国大陆手机号、至少 8 位密码、确认密码一致。
  这些仅为本地输入提示，不代表服务器账号规则或身份校验。
- 校验失败保留输入并聚焦第一个错误；IME Next 移动到下个输入框，Done 执行同一校验。
- 有效提交显示演示说明，不声称真实登录或注册成功。注册演示确认后返回登录并填入
  手机号，清空密码；取消弹窗保留草稿。登录演示确认后清空登录密码。
- 不接入 DatabaseGateway，不发送网络请求，不增加 Auth API、验证码、账号存储或登录会话。
  原数据库检查工具仍保留在工程中，但不在 Auth 入口显示。
- UI 演示的 Release 构建不要求生产 API 地址。地址为空时数据库工具在发出请求前
  明确拒绝调用，不使用示例域名或回退到开发服务；已配置地址仍须满足 HTTPS 校验。

## 验证

```powershell
$env:JAVA_HOME='D:/JDK21'
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest :app:lintDebug
# 连接设备/启动模拟器后：
.\gradlew.bat :app:connectedDebugAndroidTest
```

`AuthValidationTest` 检查输入边界；`AuthNavigationTest` 检查错误恢复、键盘提交、
返回与草稿、注册演示流程及状态恢复时不保存密码。两个页面均提供默认字号和
320dp / 200% 字号的 Compose Preview。页面使用可滚动单列、最大阅读宽度、系统及
键盘 insets，所有按钮至少 56dp，主要操作和输入至少 60dp，避免固定文字高度。
上线前还应进行真实设备的 TalkBack / Switch Access 和代表性老年用户可用性复核。

构建将 Kotlin JVM 目标对齐到既有 Java 11。AndroidX Test JUnit / Espresso 分别
使用 1.3.0 / 3.7.0；旧 Espresso 3.5.1 在 API 37 模拟器上因反射调用已移除的
InputManager.getInstance 而中止。[官方发行说明](https://developer.android.com/jetpack/androidx/releases/test#espresso-3.7.0)
记录了使用 getSystemService 替代此调用的修复。

## 本次验证记录（2026-10-13）

- Debug APK 构建成功；5 项 JVM 单元测试、API 37 模拟器的 8 项设备测试全部通过。
- 已检查两页默认字号，以及 320dp 宽 / 系统 200% 字号下的实际截图：文字可换行、
  按钮随字号增高，页面滚动可访问提交与返回操作。
- 截图保存在 `app/build/auth-preview/`，APK 在 `app/build/outputs/apk/debug/app-debug.apk`。
- Lint 尚未完成：环境未缓存 `intellij-core-31.13.0.jar` 和
  `kotlin-compiler-31.13.0.jar`，在线下载未完成，离线运行无法解析这两个工具依赖。
  下载恢复后可重新执行 `:app:lintDebug`。此项不应记为通过。
- TalkBack / Switch Access 人工验证及代表性老年用户评审尚未进行。

## UI-only Release 构建验证（2026-10-14）

- 未配置生产 API 时 `:app:assembleRelease` 成功，配置缓存成功保存并复用。
  生成 `app/build/outputs/apk/release/app-release-unsigned.apk`，未配置分发签名。
- 5 项 Debug 单元测试、6 项 Release 单元测试通过；新增 Release 专用测试确认
  空地址的数据库调用直接返回明确错误，不创建网络连接。
- 合法 HTTPS 地址通过校验；HTTP、含凭据/查询参数/片段、缺少主机、非法 URI
  共 6 类错误配置仍被拒绝。
- 本次 Release 构建的 `lintVitalRelease` 已通过；上述完整 `lintDebug` 未完成记录
  保留为此前的验证结果，不将两种检查等同。
