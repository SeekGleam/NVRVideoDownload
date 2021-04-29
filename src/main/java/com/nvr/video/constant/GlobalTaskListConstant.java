package com.nvr.video.constant;

import com.nvr.video.domain.vo.TaskVO;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbo
 * @date 2021/4/29 16:21
 * @description 全局任务队列
 */
@Data
@Builder
@Component
public class GlobalTaskListConstant {

    //执行中的任务数组
    public List<TaskVO> executionInTaskVOList=new ArrayList<>();
    //已完成的任务数组
    public List<TaskVO> finishTaskVOList=new ArrayList<>();
    //异常的的任务数组
    public List<TaskVO> exceptionTaskVOList=new ArrayList<>();

    /**
     * 更改任务从执行中到已完成
     * @param taskId 任务ID
     */
    public void changeExecutionInToFinish(String taskId){

    }

    /**
     * 更改任务从执行中到执行错误
     * @param taskId 任务ID
     * @param errCode 错误码
     */
    public void changeExecutionInToException(String taskId,int errCode){}
}
