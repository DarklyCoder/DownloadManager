package com.darklycoder.download.info

import android.os.Environment

import java.io.File

/**
 * 设置信息
 */
class DownloadConfig {

    /**
     * 核心线程池大小
     */
    var corePoolSize = 1

    /**
     * 线程池大小
     */
    var maxPoolSize = 10

    /**
     * 临时的下载目录
     */
    var tmpPath = downloadTmpPath

    init {
        maxPoolSize = Runtime.getRuntime().availableProcessors() + 1
    }

    private val downloadTmpPath: String
        get() = Environment.getExternalStorageDirectory().absolutePath + File.separator + ".downloadTmp"

}
