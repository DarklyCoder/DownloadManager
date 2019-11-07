package com.darklycoder.download

import com.darklycoder.download.info.DownloadConfig
import com.darklycoder.download.info.TaskCellInfo
import com.darklycoder.download.info.TaskInfo
import com.darklycoder.download.info.TaskStatusInfo
import com.darklycoder.download.interfaces.ICellTaskListener
import com.darklycoder.download.interfaces.IProgress
import com.darklycoder.download.interfaces.IStatusListener
import com.darklycoder.download.utils.DownloadTask
import com.darklycoder.download.utils.DownloadUtils
import java.util.*
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * 下载管理器
 *
 *
 * 目前提供单个下载、批量下载实现
 *
 *
 * TODO 1、判断是否重复；2、支持多线程下载；3、支持暂停、恢复、移除下载；4、支持自定义header
 */
object DownloadManager {

    // 等待队列
    private val TASK_PREPARE = Collections.synchronizedList(ArrayList<Runnable>())
    // 下载任务线程池
    private lateinit var POOL_TASK: ThreadPoolExecutor
    // 记录批次任务信息，存放下载任务监听
    private val mStatusList = LinkedHashMap<Long, TaskStatusInfo>()
    private val lock = Any()
    private var mConfig = DownloadConfig()

    /**
     * 设置配置
     */
    @JvmStatic
    fun setConfig(config: DownloadConfig?) {
        if (null != config) {
            mConfig = config
        }

        POOL_TASK = ThreadPoolExecutor(0, mConfig.maxPoolSize, 30, TimeUnit.MILLISECONDS, SynchronousQueue()) { r, _ -> TASK_PREPARE.add(r) }
    }

    /**
     * 同步下载单个任务
     */
    @JvmOverloads
    @JvmStatic
    fun synDowload(info: TaskCellInfo, listener: IProgress? = null): Boolean {
        return DownloadUtils.download(info, mConfig, listener)
    }

    /**
     * 单个下载
     *
     * @param info     单个下载
     * @param listener 下载状态监听
     */
    @JvmOverloads
    @JvmStatic
    fun download(info: TaskCellInfo, listener: IStatusListener? = null) {
        download(TaskInfo(listOf(info)), listener)
    }

    /**
     * 批量下载
     *
     * @param taskInfo 下载任务
     * @param listener 下载状态监听
     */
    @JvmOverloads
    @JvmStatic
    fun download(taskInfo: TaskInfo, listener: IStatusListener? = null) {
        val count = taskInfo.count
        if (count <= 0) {
            return
        }

        mStatusList[taskInfo.key] = TaskStatusInfo(count.toLong(), listener)

        for (cellInfo in taskInfo.taskList!!) {
            POOL_TASK.execute(DownloadTask(taskInfo.key, cellInfo, mConfig, cellTaskListener))
        }
    }

    /**
     * 处理等待队列
     */
    private fun handlePrepare() {
        val size = TASK_PREPARE.size
        val activeCount = POOL_TASK.activeCount

        if (activeCount < mConfig.maxPoolSize && size > 0) {
            val runnableList = ArrayList<Runnable>()

            // 取出拒绝队列中的任务
            val minSize = min(size, mConfig.maxPoolSize - activeCount)
            for (i in 0 until minSize) {
                runnableList.add(TASK_PREPARE[i])
            }

            TASK_PREPARE.removeAll(runnableList)

            for (runnable in runnableList) {
                POOL_TASK.submit(runnable)
            }
        }
    }

    /**
     * 下载监听
     */
    private val cellTaskListener = object : ICellTaskListener {
        override fun onProgress(key: Long, info: TaskCellInfo, progress: Float) {
            synchronized(lock) {
                if (!mStatusList.containsKey(key)) {
                    return
                }

                val statusInfo = mStatusList[key]
                statusInfo?.onCellProgress(info, progress)
            }
        }

        override fun onSuccess(key: Long, info: TaskCellInfo) {
            synchronized(lock) {
                if (!mStatusList.containsKey(key)) {
                    return
                }

                val statusInfo = mStatusList[key]
                statusInfo?.onCellSuccess(info)

                if (true == statusInfo?.isComplete) {
                    // 批次任务完成，移除监听
                    mStatusList.remove(key)
                }

                handlePrepare()
            }
        }

        override fun onFail(key: Long, info: TaskCellInfo, error: Int) {
            synchronized(lock) {
                if (!mStatusList.containsKey(key)) {
                    return
                }

                val statusInfo = mStatusList[key]

                statusInfo?.onCellFail(info)

                if (true == statusInfo?.isComplete) {
                    // 批次任务完成，移除监听
                    mStatusList.remove(key)
                }

                handlePrepare()
            }
        }
    }

}
