package com.nvr.video.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangbo
 * @date 2021/4/28 16:10
 * @description
 */
@Data
@Builder
@ApiModel(value = "TaskVO" ,description = "下载任务展示数据")
public class TaskVO {

    @ApiModelProperty(name = "任务ID",example = "")
    private String taskId;

    @ApiModelProperty(name = "下载进度名称",example = "89%")
    private String downloadProgressName;

    @ApiModelProperty(name = "下载进度",example = "89")
    private Integer downloadProgressNum;

    @ApiModelProperty(name = "下载状态名称",example = "准备下载.....")
    private String downloadStatusName;

    @ApiModelProperty(name = "下载状态代码",example = "0")
    private Integer downloadStatusCode;

    @ApiModelProperty(name = "文件名称",example = "0")
    private String videoFileName;

    @ApiModelProperty(value = "视频截取开始时间" ,example = "2020-09-10 13:00:00")
    private Date clipStartTime;

    @ApiModelProperty(value = "视频截取结束时间" ,example = "2020-09-10 15:00:00")
    private Date clipEndTime;

}
