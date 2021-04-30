package com.nvr.video.service;

import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author zhangbo
 * @Date 2021/4/27 9:53 下午
 * @Description 视频下载接口
 */
public interface VideoDownloadService {

   /**
    * 海康NVR视频下载接口
    * @param taskId 任务ID
    * @param videoDownLoadChannelDTO 按通道号下载视频参数
    */
   @Async("videoDownloadThreadPool")
   void downloadHkNvrVideo(String taskId, VideoDownLoadChannelDTO videoDownLoadChannelDTO);
   /**
    * 海康CVR视频下载接口
    * @param taskId 任务ID
    * @param videoDownLoadStreamDTO 按流模式下载视频参数
    */
   @Async("videoDownloadThreadPool")
   void downloadHkCvrVideo(String taskId, VideoDownLoadStreamDTO videoDownLoadStreamDTO);
   /**
    * 大华NVR视频下载接口
    * @param taskId 任务ID
    * @param videoDownLoadChannelDTO 按通道号下载视频参数
    */
   @Async("videoDownloadThreadPool")
   void downloadDhNvrVideo(String taskId, VideoDownLoadChannelDTO videoDownLoadChannelDTO);
}
