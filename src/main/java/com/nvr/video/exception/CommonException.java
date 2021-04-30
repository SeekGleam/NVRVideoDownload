package com.nvr.video.exception;

/**
 * @author zhangbo
 * @date 2020/4/15 11:25
 * @description 错误码约定:错误码为字符串类型，共5位，分成两个部分：错误产生来源+四位数字编号。错误产生来源分为A/B/C，A表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付超时等问题；B表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题；C表示错误来源于第三方服务，比如CDN服务出错，消息投递超时等问题；四位数字编号从0001到9999，大类之间的步长间距预留100
 */
public class CommonException {

    /**
     * ************************************** 用户异常 **************************************
     */
    public static final BusinessException ERROR_REQUEST = new BusinessException("A0001", "无效的请求");
    public static final BusinessException REQUEST_TIME_ERROR = new BusinessException("A0020", "传入时间有误");
    public static final BusinessException REQUEST_TIME_INVALID = new BusinessException("A0025", "时间参数有误，开始时间大于或等于结束时间");

    /**
     * ************************************** 海康设备下载异常 **************************************
     */
    public static final BusinessException SYS_ERROR = new BusinessException("B0001", "系统内部异常");
    public static final BusinessException DOWNLOAD_VIDEO_SIZE_0 = new BusinessException("B0015", "下载的原始视频为0字节");
    public static final BusinessException DOWNLOAD_VIDEO_POS_FAILED = new BusinessException("B0020", "获取视频下载进度失败");
    public static final BusinessException DOWNLOAD_VIDEO_FAILED = new BusinessException("B0020", "视频下载失败");
    public static final BusinessException SDK_INIT_ERROR = new BusinessException("B0035", "海康SDK初始化失败");
    public static final BusinessException HK_LOGIN_ERROR = new BusinessException("B0035", "海康设备登录失败");

    /**
     * ************************************** 大华设备异常 **************************************
     */
    public static final BusinessException THREE_PART_DEMO = new BusinessException("C0001", "三方异常示例");
    public static final BusinessException DH_SDK_LOAD_FAILED = new BusinessException("C0005", "大华SDK加载失败，请确认大华SDK位置放置正确");
    public static final BusinessException NVR_VIDEO_NOT_FOUND = new BusinessException("C0010", "NVR上视频不存在");
    public static final BusinessException CANCEL_TASK_NOT_FOUND = new BusinessException("C0015", "需要取消的任务不存在");
    public static final BusinessException DH_LOGIN_ERROR = new BusinessException("C0020", "大华设备登录失败");

}
