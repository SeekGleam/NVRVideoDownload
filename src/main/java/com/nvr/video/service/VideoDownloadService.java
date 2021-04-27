package com.nvr.video.service;

import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import com.nvr.video.domain.vo.VideoDownLoadVO;

/**
 * @Author zhangbo
 * @Date 2021/4/27 9:53 下午
 * @Description 视频下载接口
 */
public interface VideoDownloadService {

   /**
    * 海康NVR视频下载接口
    * @param videoDownLoadChannelDTO
    * @return
    */
   VideoDownLoadVO downloadHkNvrVideo(VideoDownLoadChannelDTO videoDownLoadChannelDTO);
   /**
    * 海康CVR视频下载接口
    * @param videoDownLoadStreamDTO
    * @return
    */
   VideoDownLoadVO downloadHkCvrVideo(VideoDownLoadStreamDTO videoDownLoadStreamDTO);
   /**
    * 大华NVR视频下载接口
    * @param videoDownLoadChannelDTO
    * @return
    */
   VideoDownLoadVO downloadDhNvrVideo(VideoDownLoadChannelDTO videoDownLoadChannelDTO);
}
