package com.darklycoder.download.utils

import com.darklycoder.download.info.DownloadConfig
import com.darklycoder.download.info.TaskCellInfo
import com.darklycoder.download.interfaces.ICellTaskListener
import com.darklycoder.download.interfaces.IProgress

/**
 * 下载任务
 */
class DownloadTask(
        private val key: Long,
        private val mInfo: TaskCellInfo,
        private val mConfig: DownloadConfig,
        private val listener: ICellTaskListener?
) : Runnable {

    private val progressListener = object : IProgress {
        override fun onProgress(progress: Float) {
            listener?.onProgress(key, mInfo, progress)
        }
    }

    override fun run() {
        val result = DownloadUtils.download(mInfo, mConfig, progressListener)

        if (result) {
            listener?.onSuccess(key, mInfo)
            return
        }

        // TODO 记录失败原因
        listener?.onFail(key, mInfo, 0)
    }

}
