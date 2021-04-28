package com.nvr.video.sdk;

import java.util.concurrent.CountDownLatch;

import javax.swing.*;

import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.vo.TaskVO;
import com.nvr.video.exception.CommonException;
import com.nvr.video.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * AC
 */
@Slf4j
public class DHDvrVideoUtils {

    private String sourceFileName;

    private NetSDKLib.LLong m_hDownLoadByTimeHandle = new NetSDKLib.LLong(0); // 按时间下载句柄
    // 回调函数中使用的表示下载完成变量
    private boolean b_downloadByTime = false;

    private CountDownLatch latch = null;

    public String dvrDownloadByTimeEx(String traceId, VideoDownLoadChannelDTO videoDownLoadChannelDTO, TaskVO taskVO){
        NetSDKLib.NET_TIME stTimeStart = CommonUtils.getDHTime(videoDownLoadChannelDTO.getClipStartTime());
        NetSDKLib.NET_TIME stTimeEnd = CommonUtils.getDHTime(videoDownLoadChannelDTO.getClipEndTime());
        // 设备断线通知回调
        DisConnect disConnect = new DisConnect();
        // 网络连接恢复
        HaveReConnect haveReConnect = new HaveReConnect();
        latch = new CountDownLatch(1);
        LoginModule.init(disConnect, haveReConnect); // 打开工程，初始化
        Native.setCallbackThreadInitializer(m_DownLoadPosByTime,
                new CallbackThreadInitializer(false, false, "downloadbytime callback thread"));
        if (LoginModule.login(videoDownLoadChannelDTO.getNvrIp(), videoDownLoadChannelDTO.getPort(), videoDownLoadChannelDTO.getAccount(), videoDownLoadChannelDTO.getPassword())) {
            // 默认设置主辅码流
            DownLoadRecordModule.setStreamType(0);
        } else {
            log.info("【大华NVR视频下载{}】dhNetSDK :{} " ,traceId, "login fail!");
            throw CommonException.DH_LOGIN_ERROR;
        }
        // 记录时间相差
        int time = 0;
        if (stTimeEnd.dwDay - stTimeStart.dwDay == 1) {
            time = (24 + stTimeEnd.dwHour) * 60 * 60 + stTimeEnd.dwMinute * 60 + stTimeEnd.dwSecond
                    - stTimeStart.dwHour * 60 * 60 - stTimeStart.dwMinute * 60 - stTimeStart.dwSecond;
        } else {
            time = stTimeEnd.dwHour * 60 * 60 + stTimeEnd.dwMinute * 60 + stTimeEnd.dwSecond
                    - stTimeStart.dwHour * 60 * 60 - stTimeStart.dwMinute * 60 - stTimeStart.dwSecond;
        }
        log.info("【大华NVR视频下载{}】视频下载时长：{}",traceId,time);
        if (time > 6 * 60 * 60 || time <= 0) {
            log.info("【大华NVR视频下载{}】dhNetSDK : {}" ,traceId, "time fail!");
            return "300032";
        }
        DownLoadRecordModule.queryRecordFile(videoDownLoadChannelDTO.getChannel(),stTimeStart,stTimeEnd);
        if (!b_downloadByTime) {

            sourceFileName = videoDownLoadChannelDTO.getFileName();

            m_hDownLoadByTimeHandle = DownLoadRecordModule.downloadRecordFileV2(videoDownLoadChannelDTO.getChannel(), 0, stTimeStart, stTimeEnd,
                    sourceFileName, m_DownLoadPosByTime);
            if (m_hDownLoadByTimeHandle.longValue() != 0) {
                b_downloadByTime = true;
            } else {
                log.info("【大华NVR视频下载{}】{},{}",traceId,ToolKits.getErrorCodeShow(),Res.string().getErrorMessage());
            }
        } else {
            DownLoadRecordModule.stopDownLoadRecordFile(m_hDownLoadByTimeHandle);
            b_downloadByTime = false;
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        LoginModule.logout();
        LoginModule.cleanup(); // 关闭工程，释放资源
        return sourceFileName;
    }

    private NetSDKLib.NET_RECORDFILE_INFO[]  getFileInfo(int channel, int i, NetSDKLib.NET_TIME stTimeStart, NetSDKLib.NET_TIME stTimeEnd) {
        NetSDKLib.NET_RECORDFILE_INFO[] fileInfo = DownLoadRecordModule.getFileInfo(channel, i, stTimeStart, stTimeEnd);
        return fileInfo;
    }

    /*
     * 按时间下载回调
     */
    private DownLoadPosCallBackByTime m_DownLoadPosByTime = new DownLoadPosCallBackByTime(); // 录像下载进度

    class DownLoadPosCallBackByTime implements NetSDKLib.fTimeDownLoadPosCallBack {
        public void invoke(NetSDKLib.LLong lLoginID, final int dwTotalSize, final int dwDownLoadSize, int index,
                           NetSDKLib.NET_RECORDFILE_INFO.ByValue recordfileinfo, Pointer dwUser) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (dwDownLoadSize == -1) {
                        DownLoadRecordModule.stopDownLoadRecordFile(m_hDownLoadByTimeHandle);
                        b_downloadByTime = false;
                        latch.countDown();
                    }
                }

            });
        }
    }

    // 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
    private class DisConnect implements NetSDKLib.fDisConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            log.info("Device[{}] Port[{}] DisConnect!",pchDVRIP,nDVRPort);
            // 断线提示
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DownLoadRecordModule.stopDownLoadRecordFile(m_hDownLoadByTimeHandle);
                }
            });
        }
    }
    // 网络连接恢复，设备重连成功回调
    // 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
    private static class HaveReConnect implements NetSDKLib.fHaveReConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            log.info("ReConnect Device[{}] Port[{}}]\n", pchDVRIP, nDVRPort);
            // 重连提示
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.printf(Res.string().getDownloadRecord() + " : " + Res.string().getOnline());
                }
            });
        }
    }
}
