package com.darklycoder.download.info

/**
 * 下载任务信息
 */
data class TaskInfo @JvmOverloads constructor(
        // 子下载任务列表
        var taskList: List<TaskCellInfo>? = null,
        // 下载任务唯一标识, 默认以时间戳作为任务标识
        var key: Long = System.currentTimeMillis()
) {

    val count: Int
        get() = taskList?.size ?: 0

}
