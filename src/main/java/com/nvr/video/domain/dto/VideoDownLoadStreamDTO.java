package com.nvr.video.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangbo
 * @Date 2021/4/27 10:09 下午
 * @Description 海康CVR按流模式下载视频
 */
@Data
@ApiModel(value = "VideoDownLoadStreamDTO" ,description = "视频下载请求参数")
public class VideoDownLoadStreamDTO {

    @ApiModelProperty(value = "不传")
    private Long traceId;

    @ApiModelProperty(value = "NVR_IP" ,example = "192.168.1.100")
    private String nvrIp;

    @ApiModelProperty(value = "NVR服务端口号" ,example = "8000")
    private String port;

    @ApiModelProperty(value = "流ID" ,example = "520127135318292515")
    private String streamId;

    @ApiModelProperty(value = "NVR登录账号" ,example = "admin")
    private String account;

    @ApiModelProperty(value = "NVR登录密码" ,example = "admin123")
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "视频截取开始时间" ,example = "2020-09-10 13:00:00")
    private Date clipStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "视频截取结束时间" ,example = "2020-09-10 15:00:00")
    private Date clipEndTime;

    @ApiModelProperty(value = "文件名称",example = "2020_09_10_13_00-2020_09_10_15_00")
    private String fileName;
}
