package com.nvr.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import static com.nvr.video.constant.CommonConstant.*;

/**
 * @author zhangbo
 * @date 2021/4/27 22:20
 * @description
 */
@Configuration
@EnableAsync
public class VideoDownloadThreadPoolConfig {

    @Bean(name = "videoDownloadThreadPool")
    public ThreadPoolTaskExecutor getVideoDownloadThreadPool(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(VIDEO_DOWNLOAD_CORE_THREAD_NUM);
        threadPoolTaskExecutor.setMaxPoolSize(VIDEO_DOWNLOAD_MAX_THREAD_NUM);
        threadPoolTaskExecutor.setQueueCapacity(VIDEO_DOWNLOAD_MAX_QUEUE_NUM);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix(VIDEO_DOWNLOAD_THREAD_NAME_PREFIX);
        threadPoolTaskExecutor.initialize();
        return  threadPoolTaskExecutor;
    }
}
