package com.nvr.video.constant;

/**
 * @Author zhangbo
 * @Date 2021/4/27 8:57 下午
 * @Description 公共常量
 */
public class CommonConstant {

    private CommonConstant() {
    }

    //swagger静态地址
    public static final String SWAGGER_SUFFIX="/doc.html";

    //========================================视频线程池相关配置参数=============================================================================
    //视频下载并发核心线程数
    public static final Integer VIDEO_DOWNLOAD_CORE_THREAD_NUM=5;

    //视频下载最大线程数
    public static final Integer VIDEO_DOWNLOAD_MAX_THREAD_NUM=10;

    //视频下载最大队列数
    public static final Integer VIDEO_DOWNLOAD_MAX_QUEUE_NUM=20;

    //视频下载线程前缀
    public static final String VIDEO_DOWNLOAD_THREAD_NAME_PREFIX="VideoDownloadThreadPool-";

    //========================================下载状态相关参数=============================================================================
    //视频下载中状态代码
    public static final Integer VIDEO_DOWNLOADING_STATUS_CODE=0;

    //视频下载中状态名称
    public static final String VIDEO_DOWNLOADING_STATUS_NAME="视频下载中";

    //视频已下载状态代码
    public static final Integer VIDEO_DOWNLOADED_STATUS_CODE=10;

    //视频已下载状态名称
    public static final String VIDEO_DOWNLOADED_STATUS_NAME="视频下载完成";

    //视频下载异常状态名称
    public static final String VIDEO_DOWNLOAD_ERR_STATUS_NAME="视频下载异常——具体参照错误码";
}
