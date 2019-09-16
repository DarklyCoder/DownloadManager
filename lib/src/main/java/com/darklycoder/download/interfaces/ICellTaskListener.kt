package com.darklycoder.download.interfaces

import androidx.annotation.FloatRange

import com.darklycoder.download.info.TaskCellInfo

/*
 * 单个任务下载监听
 */
interface ICellTaskListener {

    /*
     * 下载进度
     */
    fun onProgress(key: Long, info: TaskCellInfo, @FloatRange(from = 0.0, to = 1.0) progress: Float)

    /*
     * 下载成功回调
     */
    fun onSuccess(key: Long, info: TaskCellInfo)

    /*
     * 下载失败
     */
    fun onFail(key: Long, info: TaskCellInfo, error: Int)

}
