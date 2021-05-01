package com.nvr.video.aop;

import com.nvr.video.constant.CommonConstant;
import com.nvr.video.constant.GlobalTaskListConstant;
import com.nvr.video.domain.dto.VideoDownLoadChannelDTO;
import com.nvr.video.domain.dto.VideoDownLoadStreamDTO;
import com.nvr.video.domain.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author zhangbo
 * @date 2021/4/29 16:35
 * @description 任务管理Aspect
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TaskManagerAspect {

    private final GlobalTaskListConstant globalTaskListConstant;

    @Pointcut("execution(* com.nvr.video.service.VideoDownloadService.*(..))")
    public void aspectTaskManager(){
        log.info("开始执行任务管理切面！");
    }

    /**
     * 任务管理---方法执行前将任务放入执行中
     * @param joinPoint 切入点对象
     */
    @Around("aspectTaskManager()")
    public void addTaskToList(ProceedingJoinPoint joinPoint) throws Throwable {
       Object[] args= joinPoint.getArgs();
        String taskId=(String)args[0];
        Date clipStartTime = null;
        Date clipEndTime = null;
        String fileName = null;
        if(args[1] instanceof VideoDownLoadChannelDTO){
            VideoDownLoadChannelDTO videoDownLoadChannelDTO=(VideoDownLoadChannelDTO) args[1];
            clipStartTime=videoDownLoadChannelDTO.getClipStartTime();
            clipEndTime=videoDownLoadChannelDTO.getClipEndTime();
            fileName=videoDownLoadChannelDTO.getFileName();

        }else if(args[1] instanceof VideoDownLoadStreamDTO){
            VideoDownLoadStreamDTO videoDownLoadStreamDTO=(VideoDownLoadStreamDTO) args[1];
            clipStartTime=videoDownLoadStreamDTO.getClipStartTime();
            clipEndTime=videoDownLoadStreamDTO.getClipEndTime();
            fileName=videoDownLoadStreamDTO.getFileName();
        }
        if (!StringUtils.hasLength(String.valueOf(clipStartTime))||!StringUtils.hasLength(String.valueOf(clipEndTime))||!StringUtils.hasLength(String.valueOf(fileName))){
            return ;
        }
        TaskVO taskVO=TaskVO.builder()
                .taskId(taskId)
                .clipStartTime(clipStartTime)
                .clipEndTime(clipEndTime)
                .videoFileName(fileName)
                .downloadStatusCode(CommonConstant.VIDEO_DOWNLOADING_STATUS_CODE)
                .downloadStatusName(CommonConstant.VIDEO_DOWNLOADING_STATUS_NAME)
                .build();
        globalTaskListConstant.addExecutionInTaskVO(taskVO);
        args[2] =taskVO;
        //修改对象参数继续执行
        joinPoint.proceed(args);
    }

    /**
     * 任务管理---方法执行后将任务放入完成队列
     */
    @After("aspectTaskManager()")
    public void endTaskList(JoinPoint joinPoint){
        Object[] args= joinPoint.getArgs();
        String taskId=(String)args[0];
        globalTaskListConstant.changeExecutionInToFinish(taskId);
    }

    /**
     * 任务管理---方法执行异常将任务放入异常队列
     */
    @AfterThrowing("aspectTaskManager()")
    public void exceptionTaskList(JoinPoint joinPoint){
        Object[] args= joinPoint.getArgs();
        String taskId=(String)args[0];
        globalTaskListConstant.changeExecutionInToException(taskId);
    }
}
