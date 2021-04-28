package com.nvr.video.util;


import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;
import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import com.nvr.video.domain.vo.TaskVO;
import com.nvr.video.exception.CommonException;
import com.nvr.video.sdk.DHDvrVideoUtils;
import com.nvr.video.sdk.HCNetSDK;
import com.nvr.video.sdk.LoginModule;
import com.nvr.video.sdk.NetSDKLib;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhangbo
 * @date 2021/4/28 15:57
 * @description
 */
@Slf4j
//@Component
public class VideoDownloadUtils {

    private static final HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;

    /**
     * 海康NVR视频下载--按通道号进行视频下载
     * @param traceId 链路ID taskID可作为链路ID
     * @param videoDownLoadChannelDTO 视频下载参数
     * @param taskVO 视频下载任务进度
     * @return s
     */
    public String downlaodHkNvrVideo(String traceId, VideoDownLoadChannelDTO videoDownLoadChannelDTO, TaskVO taskVO){
        String fileName = videoDownLoadChannelDTO.getFileName();
        String nvrIp=videoDownLoadChannelDTO.getNvrIp();
        short port=videoDownLoadChannelDTO.getPort().shortValue();
        String account=videoDownLoadChannelDTO.getAccount();
        String password=videoDownLoadChannelDTO.getPassword();
        Integer channel=videoDownLoadChannelDTO.getChannel();
        HCNetSDK.NET_DVR_TIME startTime=CommonUtils.getHkTime(videoDownLoadChannelDTO.getClipStartTime());
        HCNetSDK.NET_DVR_TIME endTime=CommonUtils.getHkTime(videoDownLoadChannelDTO.getClipEndTime());
        boolean initFlag = hcNetSDK.NET_DVR_Init();
        // 返回值为布尔值 fasle初始化失败
        if (!initFlag) {
            log.info("【海康NVR视频下载任务:{}】hcNetSDK :{} " ,traceId, "init fail!");
            throw CommonException.HK_LOGIN_ERROR;
        }
        HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        // 用户句柄
        NativeLong userId = hcNetSDK.NET_DVR_Login_V30(nvrIp, port, account, password, deviceInfo);
        long lUserId = userId.longValue();
        if (lUserId == -1) {
            log.info("【海康NVR视频下载任务:{}】hcNetSDK : {}" ,traceId, "login fail!");
            throw CommonException.HK_LOGIN_ERROR;
        }
        log.info("【海康NVR视频下载任务:{}】登录成功，用户ID：{}", traceId, userId);
        // 下载句柄
        NativeLong loadHandle = new NativeLong(-1);
        if (loadHandle.intValue() == -1) {
            log.info("【海康NVR视频下载任务:{}】调用下载方法，传入参数，userId:{},channel:{},startTime:{},endTime:{},fileName:{}", traceId, userId, channel, startTime, endTime, fileName);
            loadHandle = hcNetSDK.NET_DVR_GetFileByTime(userId, new NativeLong((channel)),startTime ,endTime, fileName);
            if (loadHandle.intValue() >= 0) {
                boolean downloadFlag = hcNetSDK.NET_DVR_PlayBackControl(loadHandle, HCNetSDK.NET_DVR_PLAYSTART, 0,null);
                int tmp = -1;
                IntByReference pos = new IntByReference();
                while (true) {
                    boolean backFlag = hcNetSDK.NET_DVR_PlayBackControl(loadHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0,pos);
                    // 防止单个线程死循环
                    if (!backFlag) {
                        return downloadFlag ? fileName : "";
                    }
                    int produce = pos.getValue();
                    // 输出进度
                    if ((produce % 10) == 0 && tmp != produce) {
                        tmp = produce;
                        taskVO.setDownloadProgressNum(tmp);
                        taskVO.setDownloadProgressName(tmp+"%");
                        log.info("【海康NVR视频下载任务:{}】下载视频进度:{}" ,traceId, tmp);
                    }
                    // 下载成功
                    if (produce == 100) {
                        taskVO.setDownloadProgressNum(tmp);
                        taskVO.setDownloadProgressName(tmp+"%");
                        hcNetSDK.NET_DVR_StopGetFile(loadHandle);
                        loadHandle.setValue(-1);
                        // 退出录像机
                        hcNetSDK.NET_DVR_Logout(userId);
                        return fileName;
                    }
                    if (produce > 100) {
                        hcNetSDK.NET_DVR_StopGetFile(loadHandle);
                        loadHandle.setValue(-1);
                        hcNetSDK.NET_DVR_Logout(userId);
                        log.info("【海康NVR视频下载任务:{}】获取下载视频进度失败，错误码 :{}" ,traceId, produce);
                        throw CommonException.DOWNLOAD_VIDEO_POS_FAILED;
                    }
                }
            } else {
                log.info("【海康NVR视频下载任务:{}】视频下载失败，错误码 :{}" ,traceId, hcNetSDK.NET_DVR_GetLastError());
                hcNetSDK.NET_DVR_Logout(userId);
                throw CommonException.DOWNLOAD_VIDEO_FAILED;
            }
        }
        log.info("【海康NVR视频下载任务:{}】视频下载失败，错误码 :{}" ,traceId, hcNetSDK.NET_DVR_GetLastError());
        hcNetSDK.NET_DVR_Logout(userId);
        throw CommonException.DOWNLOAD_VIDEO_FAILED;
    }

    /**
     * 海康Cvr视频截取下载-流模式下载
     * @param traceId 链路ID taskID可作为链路ID
     * @param videoDownLoadStreamDTO 流模式下载参数
     * @param taskVO 视频下载任务进度
     * @return
     */
    public String downlaodHkCvrVideo(String traceId, VideoDownLoadStreamDTO videoDownLoadStreamDTO, TaskVO taskVO){
               // 初始化SDK
            boolean initFlag = hcNetSDK.NET_DVR_Init();
            if (!initFlag) {
                log.info("【海康CVR视频下载任务:{}】海康SDK初始化失败", traceId);
                throw CommonException.SDK_INIT_ERROR;
            }
            // 登录
            HCNetSDK.NET_DVR_USER_LOGIN_INFO loginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();
            System.arraycopy(videoDownLoadStreamDTO.getNvrIp().getBytes(), 0, loginInfo.sDeviceAddress, 0, videoDownLoadStreamDTO.getNvrIp().length());
            System.arraycopy(videoDownLoadStreamDTO.getAccount().getBytes(), 0, loginInfo.sUserName, 0, videoDownLoadStreamDTO.getAccount().length());
            System.arraycopy(videoDownLoadStreamDTO.getPassword().getBytes(), 0, loginInfo.sPassword, 0, videoDownLoadStreamDTO.getPassword().length());
            loginInfo.wPort = Short.parseShort(videoDownLoadStreamDTO.getPort().toString());
            HCNetSDK.NET_DVR_DEVICEINFO_V40 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();
            loginInfo.write();
            NativeLong userId = hcNetSDK.NET_DVR_Login_V40(loginInfo.getPointer(), deviceInfo.getPointer());
            if (-1 == userId.longValue() || 4294967295L == userId.longValue()) {
                log.info("【海康CVR视频下载任务:{}】海康SDK登录失败，错误代码：{}", traceId, hcNetSDK.NET_DVR_GetLastError());
                throw CommonException.HK_LOGIN_ERROR;
            }
            log.info("【海康CVR视频下载任务:{}】登录成功，用户ID：{}", traceId, userId);
            // 下载
           NativeLong downloadHandle = new NativeLong(-1);
            if (-1 == downloadHandle.intValue()) {
                log.info("【海康CVR视频下载任务:{}】调用海康下载方法，传入参数，userId:{},streamId:{},startTime:{},endTime:{},fileName:{}", traceId, userId, videoDownLoadStreamDTO.getStreamId(), videoDownLoadStreamDTO.getClipStartTime(), videoDownLoadStreamDTO.getClipEndTime(), videoDownLoadStreamDTO.getFileName());
                HCNetSDK.NET_DVR_PLAYCOND pDownloadCond = new HCNetSDK.NET_DVR_PLAYCOND();
                pDownloadCond.dwChannel = 0xffffffff;
                pDownloadCond.struStartTime = CommonUtils.getHkTime(videoDownLoadStreamDTO.getClipStartTime());
                pDownloadCond.struStopTime = CommonUtils.getHkTime(videoDownLoadStreamDTO.getClipEndTime());
                pDownloadCond.byDrawFrame = 0;
                pDownloadCond.byStreamType = 0;
                pDownloadCond.byStreamID = videoDownLoadStreamDTO.getStreamId().getBytes();
                pDownloadCond.write();
                downloadHandle = hcNetSDK.NET_DVR_GetFileByTime_V40(userId, videoDownLoadStreamDTO.getFileName(), pDownloadCond);
                log.info("【海康CVR视频下载任务:{}】downloadHandle:{}",traceId, downloadHandle);
                if (downloadHandle.intValue() >= 0) {
                    boolean downloadFlag = hcNetSDK.NET_DVR_PlayBackControl_V40(downloadHandle, HCNetSDK.NET_DVR_PLAYSTART, null, 0, null, null);
                    log.info("【海康CVR视频下载任务:{}】downloadFlag：{}",traceId, downloadFlag);
                    int nPos = 0;
                    for (nPos = 0; nPos < 100 && nPos >= 0; nPos = hcNetSDK.NET_DVR_GetDownloadPos(downloadHandle)) {
                        taskVO.setDownloadProgressNum(nPos);
                        taskVO.setDownloadProgressName(nPos+"%");
                        log.info("【海康CVR视频下载任务:{}】下载视频进度:{}" ,traceId, nPos);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (nPos == 100) {
                        taskVO.setDownloadProgressNum(nPos);
                        taskVO.setDownloadProgressName(nPos+"%");
                        hcNetSDK.NET_DVR_StopGetFile(downloadHandle);
                        downloadHandle.setValue(-1);
                        // 退出录像机
                        hcNetSDK.NET_DVR_Logout(userId);
                        log.info("【海康CVR视频下载任务:{}】从海康CVR下载视频成功", traceId);
                            return videoDownLoadStreamDTO.getFileName();
                        }
                    if (nPos > 100) {
                        hcNetSDK.NET_DVR_StopGetFile(downloadHandle);
                        downloadHandle.setValue(-1);
                        hcNetSDK.NET_DVR_Logout(userId);
                        log.info("【海康CVR视频下载任务:{}】获取视频下载进度失败，错误状态码 :{}" ,traceId, nPos);
                        throw CommonException.DOWNLOAD_VIDEO_POS_FAILED;
                    }
                } else {
                    log.info("【海康CVR视频下载任务:{}】视频下载失败，错误码：{}", traceId, hcNetSDK.NET_DVR_GetLastError());
                    // 2020-12-29 加出现异常退出NVR登录
                    hcNetSDK.NET_DVR_Logout(userId);
                    throw CommonException.DOWNLOAD_VIDEO_FAILED;
                }
            }
            hcNetSDK.NET_DVR_Logout(userId);
            throw CommonException.DOWNLOAD_VIDEO_FAILED;
    }

    /**
     * 大华Nvr视频截取下载-通道号下载
     * @param traceId
     * @param videoDownLoadChannelDTO
     * @param taskVO
     * @return
     */
    public String downlaodDhNvrVideo(String traceId, VideoDownLoadChannelDTO videoDownLoadChannelDTO, TaskVO taskVO){

        return new DHDvrVideoUtils().dvrDownloadByTimeEx(traceId,videoDownLoadChannelDTO,taskVO);
    }


}
