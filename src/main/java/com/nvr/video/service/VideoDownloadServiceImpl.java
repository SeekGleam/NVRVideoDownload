package com.nvr.video.service;
import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import com.nvr.video.domain.vo.VideoDownLoadVO;
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
public class VideoDownloadServiceImpl implements VideoDownloadService{

    @Override
    public VideoDownLoadVO downloadHkNvrVideo(VideoDownLoadChannelDTO videoDownLoadChannelDTO) {
        return null;
    }

    @Override
    public VideoDownLoadVO downloadHkCvrVideo(VideoDownLoadStreamDTO videoDownLoadStreamDTO) {
        return null;
    }

    @Override
    public VideoDownLoadVO downloadDhNvrVideo(VideoDownLoadChannelDTO videoDownLoadChannelDTO) {
        return null;
    }
}
