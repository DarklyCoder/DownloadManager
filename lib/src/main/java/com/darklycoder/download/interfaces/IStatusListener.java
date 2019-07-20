package com.darklycoder.download.interfaces;

import android.support.annotation.FloatRange;

import com.darklycoder.download.info.TaskCellInfo;

/**
 * 下载状态监听
 */
public interface IStatusListener {

    /**
     * 单个下载任务进度回调
     *
     * @param info     下载任务
     * @param progress 相对进度
     */
    void onCellProgress(TaskCellInfo info, @FloatRange(from = 0F, to = 1F) float progress);

    /**
     * 单个下载成功
     *
     * @param info 下载任务
     */
    void onCellSuccess(TaskCellInfo info);

    /**
     * 单个下载失败
     *
     * @param info 下载任务
     */
    void onCellFail(TaskCellInfo info);

    /**
     * 总体下载进度回调
     *
     * @param progress     相对进度
     * @param count        总个数
     * @param successCount 成功个数
     * @param failCount    失败次数
     */
    void onTotalProgress(@FloatRange(from = 0F, to = 1F) float progress, long count, long successCount, long failCount);

}
