package com.darklycoder.download.utils

import com.darklycoder.download.info.DownloadConfig
import com.darklycoder.download.info.TaskCellInfo
import com.darklycoder.download.interfaces.IProgress
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio
import java.io.File

/**
 * 下载工具
 */
object DownloadUtils {

    private val mOkHttpClient: OkHttpClient = OkHttpClient()

    /**
     * 下载
     */
    fun download(info: TaskCellInfo, config: DownloadConfig, listener: IProgress): Boolean {
        var success = false

        try {
            if (info.isFileExists) {
                return true
            }

            val file = File(info.path, info.name)
            if (!file.parentFile!!.exists()) {
                file.parentFile!!.mkdirs()
            }

            val fileTmp = File(config.tmpPath, info.name + ".tmp")
            if (fileTmp.exists()) {
                fileTmp.delete()
            }
            if (!fileTmp.parentFile!!.exists()) {
                fileTmp.parentFile!!.mkdirs()
            }

            val request = Request.Builder()
                    .url(info.url)
                    .build()

            val call = mOkHttpClient.newCall(request)
            val response = call.execute()

            val responseBody = response.body() ?: return false

            var sink: BufferedSink? = null
            var source: BufferedSource? = null

            try {
                val prb = ProgressResponseBody(responseBody, listener)
                sink = Okio.buffer(Okio.sink(fileTmp))
                source = Okio.buffer(prb.source()!!)
                sink.writeAll(source!!)

                if (fileTmp.renameTo(file)) {
                    // 下载成功
                    success = true
                }

            } finally {
                IOUtil.closeAll(source, sink)
            }

        } catch (ignored: Exception) {
        }

        return success
    }

}
