package com.darklycoder.download.info;

import com.darklycoder.download.interfaces.IStatusListener;

/**
 * 下载任务状态信息
 *
 * <pre>
 *     1、回调单个任务下载状态：进度、下载完成(失败/成功)
 *     2、回调总体下载进度
 * </pre>
 */
public class TaskStatusInfo {

    // 总数
    private long count;
    // 成功个数
    private long successCount;
    // 失败个数
    private long failCount;
    private IStatusListener listener;

    public TaskStatusInfo(long count, IStatusListener listener) {
        this.count = count;
        this.listener = listener;
    }

    public void onCellProgress(TaskCellInfo info, float progress) {
        if (null == listener) {
            return;
        }

        listener.onCellProgress(info, progress);
    }

    public void onCellSuccess(TaskCellInfo info) {
        successCount++;
        if (null == listener) {
            return;
        }

        listener.onCellSuccess(info);
        onTotalProgress();
    }

    public void onCellFail(TaskCellInfo info) {
        failCount++;
        if (null == listener) {
            return;
        }

        listener.onCellFail(info);
        onTotalProgress();
    }

    /**
     * 是否全部执行完毕
     */
    public boolean isComplete() {
        return count == successCount + failCount;
    }

    /**
     * 获取整体下载进度
     */
    private float getProgress() {
        return (successCount + failCount) * 1F / count;
    }

    private void onTotalProgress() {
        listener.onTotalProgress(getProgress(), count, successCount, failCount);
    }

}
