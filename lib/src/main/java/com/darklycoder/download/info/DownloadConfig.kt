package com.darklycoder.download.info

import android.os.Environment

import java.io.File

/**
 * 设置信息
 */
class DownloadConfig {

    /**
     * 线程池大小
     */
    var maxPoolSize = 40

    /**
     * 临时的下载目录
     */
    var tmpPath = downloadTmpPath

    private val downloadTmpPath: String
        get() = Environment.getExternalStorageDirectory().absolutePath + File.separator + ".downloadTmp"

}
