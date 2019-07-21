package com.darklycoder.download.utils;

import com.darklycoder.download.info.DownloadConfig;
import com.darklycoder.download.info.TaskCellInfo;
import com.darklycoder.download.interfaces.ICellTaskListener;
import com.darklycoder.download.interfaces.IProgress;

/**
 * 下载任务
 */
public class DownloadTask implements Runnable {

    private long key;
    private TaskCellInfo mInfo;
    private DownloadConfig mConfig;
    private ICellTaskListener listener;

    public DownloadTask(long key, TaskCellInfo mInfo, DownloadConfig config, ICellTaskListener listener) {
        this.key = key;
        this.mInfo = mInfo;
        this.mConfig = config;
        this.listener = listener;
    }

    @Override
    public void run() {
        boolean result = DownloadUtils.getInstance().download(mInfo, mConfig, progressListener);

        if (null == listener) {
            return;
        }

        if (result) {
            listener.onSuccess(key, mInfo);
            return;
        }

        // TODO 记录失败原因
        listener.onFail(key, mInfo, 0);
    }

    private IProgress progressListener = new IProgress() {
        @Override
        public void onProgress(float progress) {
            listener.onProgress(key, mInfo, progress);
        }
    };

}
