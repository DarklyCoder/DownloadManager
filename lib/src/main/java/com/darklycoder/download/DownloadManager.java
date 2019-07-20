package com.darklycoder.download;

import com.darklycoder.download.info.DownloadConfig;
import com.darklycoder.download.info.TaskCellInfo;
import com.darklycoder.download.info.TaskInfo;
import com.darklycoder.download.info.TaskStatusInfo;
import com.darklycoder.download.interfaces.ICellTaskListener;
import com.darklycoder.download.interfaces.IStatusListener;
import com.darklycoder.download.utils.DownloadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 下载管理器
 */
public final class DownloadManager {

    // 最大线程数目
    private static final int MAX_POOL_SIZE = 40;
    // 等待队列
    private static final List<Runnable> TASK_PREPARE = Collections.synchronizedList(new ArrayList<>());
    // 下载任务线程池
    private static final ThreadPoolExecutor POOL_TASK = new ThreadPoolExecutor(0, MAX_POOL_SIZE, 30, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (r, executor) -> TASK_PREPARE.add(r));
    // 记录批次任务信息，存放下载任务监听
    private static final LinkedHashMap<Long, TaskStatusInfo> mStatusList = new LinkedHashMap<>();
    private static final Object lock = new Object();
    private static DownloadConfig mConfig = new DownloadConfig();

    private DownloadManager() {
    }

    private static class DownloadUtilsInner {
        static DownloadManager instance = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return DownloadUtilsInner.instance;
    }

    public void setConfig(DownloadConfig config) {
        if (null != config) {
            mConfig = config;
        }
    }

    /**
     * 单个下载
     *
     * @param info     单个下载
     * @param listener 下载状态监听
     */
    public void download(TaskCellInfo info, IStatusListener listener) {
        download(new TaskInfo(Collections.singletonList(info)), listener);
    }

    /**
     * 批量下载
     *
     * @param taskInfo 下载任务
     * @param listener 下载状态监听
     */
    public void download(TaskInfo taskInfo, IStatusListener listener) {
        int count = taskInfo.getCount();
        if (count <= 0) {
            return;
        }

        mStatusList.put(taskInfo.key, new TaskStatusInfo(count, listener));

        for (TaskCellInfo cellInfo : taskInfo.taskList) {
            POOL_TASK.execute(new DownloadTask(taskInfo.key, cellInfo, mConfig, cellTaskListener));
        }
    }

    /**
     * 下载监听
     */
    private ICellTaskListener cellTaskListener = new ICellTaskListener() {
        @Override
        public void onProgress(long key, TaskCellInfo info, float progress) {
            synchronized (lock) {
                if (!mStatusList.containsKey(key)) {
                    return;
                }

                TaskStatusInfo statusInfo = mStatusList.get(key);
                if (null == statusInfo) {
                    return;
                }

                statusInfo.onCellProgress(info, progress);
            }
        }

        @Override
        public void onSuccess(long key, TaskCellInfo info) {
            synchronized (lock) {
                if (!mStatusList.containsKey(key)) {
                    return;
                }

                TaskStatusInfo statusInfo = mStatusList.get(key);
                if (null == statusInfo) {
                    return;
                }

                statusInfo.onCellSuccess(info);

                if (statusInfo.isComplete()) {
                    // 批次任务完成，移除监听
                    mStatusList.remove(key);
                }

                handlePrepare();
            }
        }

        @Override
        public void onFail(long key, TaskCellInfo info, int error) {
            synchronized (lock) {
                if (!mStatusList.containsKey(key)) {
                    return;
                }

                TaskStatusInfo statusInfo = mStatusList.get(key);
                if (null == statusInfo) {
                    return;
                }

                statusInfo.onCellFail(info);

                if (statusInfo.isComplete()) {
                    // 批次任务完成，移除监听
                    mStatusList.remove(key);
                }

                handlePrepare();
            }
        }
    };

    /**
     * 处理等待队列
     */
    private void handlePrepare() {
        int size = TASK_PREPARE.size();
        long activeCount = POOL_TASK.getActiveCount();

        if (activeCount < MAX_POOL_SIZE && size > 0) {
            List<Runnable> runnableList = new ArrayList<>();

            // 取出拒绝队列中的任务
            long minSize = Math.min(size, MAX_POOL_SIZE - activeCount);
            for (int i = 0; i < minSize; i++) {
                runnableList.add(TASK_PREPARE.get(i));
            }

            TASK_PREPARE.removeAll(runnableList);

            for (Runnable runnable : runnableList) {
                POOL_TASK.submit(runnable);
            }
        }
    }

}
