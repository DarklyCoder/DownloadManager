package com.darklycoder.download.utils;

import com.darklycoder.download.info.DownloadConfig;
import com.darklycoder.download.info.TaskCellInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载工具
 */
class DownloadUtils {

    private OkHttpClient mOkHttpClient;

    private DownloadUtils() {
        this.mOkHttpClient = new OkHttpClient();
    }

    private static class DownloadUtilsInner {
        static DownloadUtils instance = new DownloadUtils();
    }

    static DownloadUtils getInstance() {
        return DownloadUtils.DownloadUtilsInner.instance;
    }

    /**
     * 下载
     */
    public boolean download(TaskCellInfo info, DownloadConfig config) {
        boolean success = false;

        try {
            File file = new File(info.path, info.name);
            if (file.exists()) {
                return true;
            }
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

            InputStream is = null;
            FileOutputStream fos = null;

            try {
                is = responseBody.byteStream();
                fos = new FileOutputStream(fileTmp);

                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();

                if (fileTmp.renameTo(file)) {
                    // 下载成功
                    success = true;
                }

            } finally {
                IOUtil.closeAll(is, fos);
            }

        } catch (Exception ignored) {
        }

        return success;
    }

}
