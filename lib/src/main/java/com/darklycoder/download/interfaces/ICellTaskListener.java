package com.darklycoder.download.interfaces;

import android.support.annotation.FloatRange;

import com.darklycoder.download.info.TaskCellInfo;

/**
 * 单个任务下载监听
 */
public interface ICellTaskListener {

    /**
     * 下载进度
     */
    void onProgress(long key, TaskCellInfo info, @FloatRange(from = 0F, to = 1F) float progress);

    /**
     * 下载成功回调
     */
    void onSuccess(long key, TaskCellInfo info);

    /**
     * 下载失败
     */
    void onFail(long key, TaskCellInfo info, int error);

}
