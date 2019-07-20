package com.darklycoder.download.info;

import java.util.List;

/**
 * 下载任务信息
 */
public class TaskInfo {

    // 下载任务唯一标识
    public long key;
    // 子下载任务列表
    public List<TaskCellInfo> taskList;

    public TaskInfo(long key, List<TaskCellInfo> taskList) {
        this.key = key;
        this.taskList = taskList;
    }

    public TaskInfo(List<TaskCellInfo> taskList) {
        // 默认以时间戳作为任务标识
        this.key = System.currentTimeMillis();
        this.taskList = taskList;
    }

    public int getCount() {
        return null == taskList ? 0 : taskList.size();
    }

}
