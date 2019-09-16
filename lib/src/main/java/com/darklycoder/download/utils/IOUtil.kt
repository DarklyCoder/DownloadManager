package com.darklycoder.download.utils

import java.io.Closeable

object IOUtil {

    fun closeAll(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            try {
                closeable?.close()

            } catch (ignored: Exception) {
            }
        }
    }

}
