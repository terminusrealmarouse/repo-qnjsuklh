# WToolSDK Android 示例

这是一个最小 Android 示例工程，用于演示
[weechatfly/wtoolxposed](https://github.com/weechatfly/wtoolxposed/tree/master/sdk%E5%BA%93%26%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E/SDK%E5%BA%93)
中的 `WToolSDK` 二进制 SDK 接入方式。

## 工程结构

```text
app/
  libs/wtoolsdk.jar
  src/main/jniLibs/
    arm64-v8a/libwtoolsdk.so
    armeabi-v7a/libwtoolsdk.so
    armeabi/libwtoolsdk.so
    x86/libwtoolsdk.so
    x86_64/libwtoolsdk.so
  src/main/java/com/example/wtoolsample/MainActivity.java
```

`MainActivity` 使用原生 Android `Activity` 和程序化 UI，不依赖 AndroidX。示例界面包含：

- `init(appId, authCode)` 初始化 SDK
- `getVersion()` / `getModuleVersion()` 查询版本
- `startMessageListener("")` / `stopMessageListener()` 管理消息监听
- `sendTask(...)` 发送 `type=1` 文本任务
- `setOnMessageListener(...)` 接收消息回调
- `setOnTaskEndListener(...)` 接收任务结束回调
- `unload()` 卸载 SDK

## 构建

需要：

- JDK 17
- Android SDK Platform 35
- Android SDK Build Tools 35.0.0

本地已配置 Android SDK 后运行：

```powershell
.\gradlew.bat :app:assembleDebug
```

首次构建会自动下载 Gradle 和 Android Gradle Plugin。

## 运行说明

1. 安装 debug APK 到测试设备或模拟器。
2. 输入可用的 `appId` 和 `authCode`。
3. 点击“初始化 SDK”。
4. 点击“启动消息监听”观察消息回调日志。
5. 填写接收方 `wxid` 或群 id，点击“发送文本任务”观察 `sendTask` 返回值和任务结束回调。

如果设备环境中缺少对应的 Xposed/微信二次开发模块，SDK native 调用可能返回错误或抛出加载/运行异常；示例会把异常显示在页面日志里，方便确认集成是否正确。

## SDK 来源

SDK 二进制文件来自用户提供的公开 GitHub 目录：

- `wtoolsdk.jar`
- `jniLibs/*/libwtoolsdk.so`

本仓库只提供最小调用示例，不包含原项目 PDF 文档和 PC 转换工具压缩包。