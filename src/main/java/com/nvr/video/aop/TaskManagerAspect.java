package com.nvr.video.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author zhangbo
 * @date 2021/4/29 16:35
 * @description 任务管理Aspect
 */
@Aspect
@Component
public class TaskManagerAspect {

    @Pointcut("execution(* com.nvr.video.service.VideoDownloadService.*(..))")
    public void aspectTaskManager(){}

    @Before("aspectTaskManager()")
    public void addTaskToList(){

    }
}
