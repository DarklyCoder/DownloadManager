package com.darklycoder.download.utils

import com.darklycoder.download.interfaces.IProgress
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * 显示下载进度
 */
class ProgressResponseBody(
        // 实际的待包装响应体
        private val responseBody: ResponseBody,
        // 进度回调接口
        private val progressListener: IProgress?

) : ResponseBody() {

    // 包装完成的BufferedSource
    private var bufferedSource: BufferedSource? = null

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     */
    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     */
    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }

        return bufferedSource
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private fun source(source: Source): Source {

        return object : ForwardingSource(source) {
            var totalBytes = responseBody.contentLength()
            // 当前读取字节数
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // 增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                // 回调，如果contentLength()不知道长度，会返回-1

                if (-1L != bytesRead) {
                    progressListener?.onProgress(totalBytesRead * 1f / totalBytes)
                }

                return bytesRead
            }
        }
    }

}