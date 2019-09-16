package com.darklycoder.download.interfaces

import androidx.annotation.FloatRange

import com.darklycoder.download.info.TaskCellInfo

/*
 * 下载状态监听
 */
interface IStatusListener {

    /*
     * 单个下载任务进度回调
     *
     * @param info     下载任务
     * @param progress 相对进度
     */
    fun onCellProgress(info: TaskCellInfo, @FloatRange(from = 0.0, to = 1.0) progress: Float)

    /*
     * 单个下载成功
     *
     * @param info 下载任务
     */
    fun onCellSuccess(info: TaskCellInfo)

    /*
     * 单个下载失败
     *
     * @param info 下载任务
     */
    fun onCellFail(info: TaskCellInfo)

    /*
     * 总体下载进度回调
     *
     * @param progress     相对进度
     * @param count        总个数
     * @param successCount 成功个数
     * @param failCount    失败次数
     */
    fun onTotalProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float, count: Long, successCount: Long, failCount: Long)

}
