package com.darklycoder.download.info

import com.darklycoder.download.interfaces.IStatusListener

/**
 * 下载任务状态信息
 *
 * <pre>
 * 1、回调单个任务下载状态：进度、下载完成(失败/成功)
 * 2、回调总体下载进度
 * </pre>
 */
class TaskStatusInfo(
        // 总数
        private val count: Int,
        // 回调
        private val listener: IStatusListener? = null
) {

    // 成功个数
    private var successCount: Int = 0
    // 失败个数
    private var failCount: Int = 0

    /**
     * 是否全部执行完毕
     */
    val isComplete: Boolean
        get() = count == successCount + failCount

    /**
     * 获取整体下载进度
     */
    private val progress: Float
        get() = (successCount + failCount) * 1f / count

    fun onCellProgress(info: TaskCellInfo, progress: Float) {
        listener?.onCellProgress(info, progress)
    }

    fun onCellSuccess(info: TaskCellInfo) {
        successCount++

        listener?.onCellSuccess(info)
        onTotalProgress()
    }

    fun onCellFail(info: TaskCellInfo) {
        failCount++

        listener?.onCellFail(info)
        onTotalProgress()
    }

    private fun onTotalProgress() {
        listener?.onTotalProgress(progress, count, successCount, failCount)
    }

}
