package com.darklycoder.download.info;

import android.os.Environment;

import java.io.File;

/**
 * 设置信息
 */
public class DownloadConfig {

    /**
     * 临时的下载目录
     */
    public String tmpPath = getDownloadTmpPath();

    private String getDownloadTmpPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".downloadTmp";
    }

}
