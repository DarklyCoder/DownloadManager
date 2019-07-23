package com.darklycoder.download.utils;

import com.darklycoder.download.info.DownloadConfig;
import com.darklycoder.download.info.TaskCellInfo;
import com.darklycoder.download.interfaces.IProgress;

import java.io.File;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * 下载工具
 */
public class DownloadUtils {

    private OkHttpClient mOkHttpClient;

    private DownloadUtils() {
        this.mOkHttpClient = new OkHttpClient();
    }

    private static class DownloadUtilsInner {
        static DownloadUtils instance = new DownloadUtils();
    }

    public static DownloadUtils getInstance() {
        return DownloadUtils.DownloadUtilsInner.instance;
    }

    /**
     * 下载
     */
    public boolean download(TaskCellInfo info, DownloadConfig config, IProgress listener) {
        boolean success = false;

        try {
            if (info.isFileExists()) {
                return true;
            }

            File file = new File(info.path, info.name);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            File fileTmp = new File(config.tmpPath, info.name + ".tmp");
            if (fileTmp.exists()) {
                fileTmp.delete();
            }
            if (!fileTmp.getParentFile().exists()) {
                fileTmp.getParentFile().mkdirs();
            }

            Request request = new Request.Builder()
                    .url(info.url)
                    .build();

            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();

            ResponseBody responseBody = response.body();
            if (null == responseBody) {
                return false;
            }

            BufferedSink sink = null;
            BufferedSource source = null;

            try {
                ProgressResponseBody prb = new ProgressResponseBody(responseBody, listener);
                sink = Okio.buffer(Okio.sink(fileTmp));
                source = Okio.buffer(prb.source());
                sink.writeAll(source);

                if (fileTmp.renameTo(file)) {
                    // 下载成功
                    success = true;
                }

            } finally {
                IOUtil.closeAll(source, sink);
            }

        } catch (Exception ignored) {
        }

        return success;
    }

}
