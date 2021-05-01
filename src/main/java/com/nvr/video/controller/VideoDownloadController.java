package com.nvr.video.controller;

import com.nvr.video.constant.GlobalTaskListConstant;
import com.nvr.video.domain.common.Response;
import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import com.nvr.video.domain.vo.TaskVO;
import com.nvr.video.service.VideoDownloadService;
import com.nvr.video.util.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangbo
 * @Date 2021/4/27 9:49 下午
 * @Description 海康NVR、CVR，大华NVR视频下载控制层
 */
@Slf4j
@Api(tags = "海康NVR、CVR，大华NVR视频下载接口")
@RestController
@RequiredArgsConstructor
@CrossOrigin(allowedHeaders ="*" ,origins = "*")
public class VideoDownloadController {

    private final VideoDownloadService videoDownloadService;

    private final GlobalTaskListConstant globalTaskListConstant;

    @PostMapping("/videoDownload/interceptHkNvrVideo")
    @ApiOperation(value = "海康NVR视频截取下载", notes = "传入NVR登录信息、通道号、视频开始结束时间段（时间段无上限）进行视频下载")
    public Response<String> interceptHkNvrVideo(
             VideoDownLoadChannelDTO videoDownLoadChannelDTO
    ){
        //生成taskId
        String taskId= CommonUtils.generateTaskId();
        try {
            log.info("【海康NVR视频下载】任务ID：{}，开始执行",taskId);
            videoDownloadService.downloadHkNvrVideo(taskId,videoDownLoadChannelDTO,null);
        }catch (Exception e){
            return Response.failed(e);
        }
        return Response.success(taskId);
    }
    @PostMapping("/videoDownload/interceptHkCvrVideo")
    @ApiOperation(value = "海康CVR视频截取下载", notes = "传入海康CVR登录信息、流ID、视频开始结束时间段（时间段无上限）进行视频下载")
    public Response<String> interceptHkCvrVideo(
            VideoDownLoadStreamDTO videoDownLoadStreamDTO
    ){
        //生成taskId
        String taskId= CommonUtils.generateTaskId();
        try {
            videoDownloadService.downloadHkCvrVideo(taskId,videoDownLoadStreamDTO,null);
        }catch (Exception e){
            return Response.failed(e);
        }
        return Response.success(taskId);
    }

    @PostMapping("/videoDownload/interceptDhNvrVideo")
    @ApiOperation(value = "大华NVR视频截取下载", notes = "传入大华NVR登录信息、通道号、视频开始结束时间段（时间段无上限）进行视频下载")
    public Response<String> interceptDhNvrVideo(
           VideoDownLoadChannelDTO videoDownLoadChannelDTO
    ){
        String taskId= CommonUtils.generateTaskId();
        try {
            //生成taskId
            videoDownloadService.downloadDhNvrVideo(taskId,videoDownLoadChannelDTO,null);
        }catch (Exception e){
            return Response.failed(e);
        }
        return Response.success(taskId);
    }
    @PostMapping("/videoDownload/getVideoDownloadStatusList")
    @ApiOperation(value = "获取下载任务列表", notes = "获取内存中存储的当前执行中、已执行、异常的任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskStatus", value = "任务执行状态(1.执行中、2.已执行、3.异常)", paramType ="query" , required = true, dataType = "Integer"),
    })
    public Response<List<TaskVO>> getVideoDownloadStatusList(
            @RequestParam Integer taskStatus
    ){
        List<TaskVO> result;
        try {
            switch (taskStatus) {
                case 1:
                    result=globalTaskListConstant.getExecutionInTaskVOList();
                    break;
                case 2:
                    result=globalTaskListConstant.getFinishTaskVOList();
                    break;
                case 3:
                    result=globalTaskListConstant.getExceptionTaskVOList();
                    break;
                default:
                    result=new ArrayList<>();
            }
        }catch (Exception e){
            return Response.failed(e);
        }
        return Response.success(result);
    }
}
