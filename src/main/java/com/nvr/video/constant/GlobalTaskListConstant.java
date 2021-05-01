package com.nvr.video.constant;

import com.nvr.video.domain.vo.TaskVO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangbo
 * @date 2021/4/29 16:21
 * @description 全局任务队列
 */
@Data
@Component
public class GlobalTaskListConstant {

    //执行中的任务数组
    private List<TaskVO> executionInTaskVOList=new ArrayList<>();
    //已完成的任务数组
    private List<TaskVO> finishTaskVOList=new ArrayList<>();
    //异常的的任务数组
    private List<TaskVO> exceptionTaskVOList=new ArrayList<>();

    /**
     * 添加任务到执行中任务数组
     * @param taskVO 任务对象
     */
    public void addExecutionInTaskVO(TaskVO taskVO){
        executionInTaskVOList.add(taskVO);
    }
    /**
     * 更改任务从执行中到已完成
     * @param taskId 任务ID
     */
    public void changeExecutionInToFinish(String taskId){
        //将执行中元素移动至已完成元素
        finishTaskVOList.addAll(executionInTaskVOList.stream().filter(x->taskId.equals(x.getTaskId())).collect(Collectors.toList()));
        //去除已执行元素
        executionInTaskVOList.removeIf(x -> taskId.equals(x.getTaskId()));
    }

    /**
     * 更改任务从执行中到执行错误
     * @param taskId 任务ID
     */
    public void changeExecutionInToException(String taskId){
        //将执行中元素移动至已完成元素
        exceptionTaskVOList.addAll(executionInTaskVOList.stream().filter(x->taskId.equals(x.getTaskId())).collect(Collectors.toList()));
        //去除已执行元素
        executionInTaskVOList.removeIf(x -> taskId.equals(x.getTaskId()));
    }

}
