package com.darklycoder.download.info;

import java.io.File;

/**
 * 单个下载任务信息
 */
public class TaskCellInfo {

    // 下载地址
    public String url;
    // 文件路径
    public String path;
    // 文件名称
    public String name;

    public TaskCellInfo(String url, String path, String name) {
        this.url = url;
        this.path = path;
        this.name = name;
    }

    /**
     * 获取文件本地地址
     */
    public String getFilePath() {
        return path + File.separator + name;
    }

    /**
     * 文件是否存在
     */
    public boolean isFileExists() {
        File file = new File(path, name);
        return file.exists();
    }

}
