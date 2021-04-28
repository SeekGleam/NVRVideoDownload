提供海康威视NVR、CVR，浙江大华NVR按时间段进行视频截取下载功能。


SDK环境准备：

| **设备** | **操作系统** | **SDK版本** | **安装说明** | **备注** |
| :---: | :---: | :---: | :---: | :---: |
| 海康威视 | windows_x64 | [设备网络SDK_V6.1.6.4(for Windows x64)](https://www.hikvision.com/cn/download_more_570.html)
| 1.指定**HCNETSDK_PATH为**HCNetSDK.dll路径。
2.指定**PLAYCTRL_PATH为**PlayCtrl.dll路径 | 支持海康威视NVR、CVR视频下载。 |
|  | linux_x64 | [设备网络SDK_V6.1.6.4(for Linux64)](https://www.hikvision.com/cn/download_more_403.html) |  |  |
| 浙江大华 | windows_x64 | [设备网络SDK_JAVA_Win64_V3.052.0000002.0.R.201103]() | 1.指定**DH_LIB_PATH为sdk根目录即可** | 此版本只支持大华私有文件格式（dav），mp4下载格式可联系大华客服提供。 |
|  | linux_x64 | [设备网SDK_JAVA_Linux64_V3.052.0000002.0.R.201103](https://www.dahuatech.com/service/downloadlists/836.html) |  |  |

NVRVideoDownload程序提供以下接口：

| **序号** | **接口名称** | **接口说明** |
| :---: | :---: | :---: |
| 1 | 按时间段截取海康NVR视频文件 |  |
| 2 | 按时间段截取海康CVR视频文件 |
|
| 3 | 按时间段截取大华NVR视频文件 |
|
| 4 | 获取提交任务下载状态 |
|
