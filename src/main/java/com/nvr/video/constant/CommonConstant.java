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
}
