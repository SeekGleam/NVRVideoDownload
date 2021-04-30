package com.nvr.video.util;

import cn.hutool.core.util.IdUtil;
import com.nvr.video.sdk.HCNetSDK;
import com.nvr.video.sdk.NetSDKLib;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangbo
 * @date 2021/4/28 16:17
 * @description
 */
public class CommonUtils {

    private  CommonUtils(){}

    /**
     * 生成TASKID-雪花算法
     * @return
     */
    public static synchronized String generateTaskId(){
        return IdUtil.randomUUID();
    }

    /**
     * 获取海康录像机格式的时间
     *
     * @param time
     * @return
     */
    public static HCNetSDK.NET_DVR_TIME getHkTime(Date time) {
        HCNetSDK.NET_DVR_TIME structTime = new HCNetSDK.NET_DVR_TIME();
        String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
        String[] times = str.split("-");
        structTime.dwYear = Integer.parseInt(times[0]);
        structTime.dwMonth = Integer.parseInt(times[1]);
        structTime.dwDay = Integer.parseInt(times[2]);
        structTime.dwHour = Integer.parseInt(times[3]);
        structTime.dwMinute = Integer.parseInt(times[4]);
        structTime.dwSecond = Integer.parseInt(times[5]);
        return structTime;
    }
    /**
     * 获取海康录像机格式的时间
     *
     * @param time
     * @return
     */
    public static NetSDKLib.NET_TIME getDHTime(Date time) {
        NetSDKLib.NET_TIME structTime = new NetSDKLib.NET_TIME();
        String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
        String[] times = str.split("-");
        structTime.dwYear = Integer.parseInt(times[0]);
        structTime.dwMonth = Integer.parseInt(times[1]);
        structTime.dwDay = Integer.parseInt(times[2]);
        structTime.dwHour = Integer.parseInt(times[3]);
        structTime.dwMinute = Integer.parseInt(times[4]);
        structTime.dwSecond = Integer.parseInt(times[5]);
        return structTime;
    }
}
