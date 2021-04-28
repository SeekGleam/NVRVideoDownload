package com.nvr.video.service.impl;
import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import com.nvr.video.domain.vo.TaskVO;
import com.nvr.video.service.VideoDownloadService;
import com.nvr.video.util.VideoDownloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author zhangbo
 * @Date 2021/4/27 10:13 下午
 * @Description 视频下载接口实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoDownloadServiceImpl implements VideoDownloadService {

    private final VideoDownloadUtils videoDownloadUtils;

    @Override
    public void downloadHkNvrVideo(String taskId, VideoDownLoadChannelDTO videoDownLoadChannelDTO) {
        TaskVO taskVO=TaskVO.builder()
                .taskId(taskId)
                .clipStartTime(videoDownLoadChannelDTO.getClipStartTime())
                .clipEndTime(videoDownLoadChannelDTO.getClipEndTime())
                .videoFileName(videoDownLoadChannelDTO.getFileName())
                .build();
       String fileName = videoDownloadUtils.downlaodHkNvrVideo(taskId,videoDownLoadChannelDTO,taskVO);
       
    }

    @Override
    public void downloadHkCvrVideo(String taskId, VideoDownLoadStreamDTO videoDownLoadStreamDTO) {
    }

    @Override
    public void downloadDhNvrVideo(String taskId, VideoDownLoadChannelDTO videoDownLoadChannelDTO) {
    }
}
