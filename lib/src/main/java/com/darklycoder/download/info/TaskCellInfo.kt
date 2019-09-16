package com.darklycoder.download.info

import java.io.File

/*
 * 单个下载任务信息
 */
data class TaskCellInfo(
        // 下载地址
        var url: String,
        // 文件路径
        var path: String,
        // 文件名称
        var name: String
) {

    /*
     * 获取文件本地地址
     */
    val filePath: String
        get() = path + File.separator + name

    /*
     * 文件是否存在
     */
    val isFileExists: Boolean
        get() {
            return File(path, name).exists()
        }

}
