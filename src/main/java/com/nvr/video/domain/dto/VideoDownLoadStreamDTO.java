package com.nvr.video.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @Author zhangbo
 * @Date 2021/4/27 10:09 下午
 * @Description 海康CVR按流模式下载视频
 */
@Data
@ApiModel(value = "VideoDownLoadStreamDTO" ,description = "视频下载请求参数")
public class VideoDownLoadStreamDTO {

    @Pattern(regexp = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)",message = "输入IP参数有误")
    @ApiModelProperty(value = "NVR_IP" ,example = "192.168.1.100",required = true)
    private String nvrIp;

    @Min(0)
    @NotEmpty
    @ApiModelProperty(value = "NVR服务端口号" ,example = "8000",required = true)
    private Integer port;

    @NotNull
    @ApiModelProperty(value = "流ID" ,example = "520127135318292515",required = true)
    private String streamId;

    @NotEmpty
    @ApiModelProperty(value = "NVR登录账号" ,example = "admin",required = true)
    private String account;

    @NotEmpty
    @ApiModelProperty(value = "NVR登录密码" ,example = "admin123",required = true)
    private String password;

    @Past
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "视频截取开始时间" ,example = "2020-09-10 13:00:00",required = true)
    private Date clipStartTime;

    @Past
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "视频截取结束时间" ,example = "2020-09-10 15:00:00",required = true)
    private Date clipEndTime;

    @NotEmpty
    @ApiModelProperty(value = "文件名称",example = "xxx.mp4",notes="不传则以任务名称作为文件名称")
    private String fileName;
}
