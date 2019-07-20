package com.darklycoder.download.utils;

import java.io.Closeable;

public final class IOUtil {

    public static void closeAll(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }

        for (Closeable closeable : closeables) {
            try {
                closeable.close();

            } catch (Exception ignored) {
            }
        }
    }

}
