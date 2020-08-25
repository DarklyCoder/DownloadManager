package com.darklycoder.download.demo

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.darklycoder.download.DownloadManager
import com.darklycoder.download.info.TaskCellInfo
import com.darklycoder.download.info.TaskInfo
import com.darklycoder.download.interfaces.IStatusListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val TAG = "Download-log"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_download.setOnClickListener {
            val path = Environment.getExternalStorageDirectory().absolutePath + File.separator + ".downloadTmp"

            for (i in 0..100) {
                val urlTmp = "http://pic38.nipic.com/20140211/17882171_143443301183_2.jpg"
                val cellInfoTmp = TaskCellInfo(urlTmp, path, "test_tmp${i}.jpg")

                val list = ArrayList<TaskCellInfo>()
                list.add(cellInfoTmp)

                DownloadManager.download(TaskInfo(list), statusListener)
            }
        }
    }

    private val statusListener = object : IStatusListener {
        override fun onCellProgress(info: TaskCellInfo, progress: Float) {
            Log.d(TAG, info.name + ": " + progress)
        }

        override fun onCellSuccess(info: TaskCellInfo) {
            Log.d(TAG, info.name + "下载成功！")
        }

        override fun onCellFail(info: TaskCellInfo) {
            Log.d(TAG, info.name + "下载失败！")
        }

        override fun onTotalProgress(progress: Float, count: Int, successCount: Int, failCount: Int) {
            runOnUiThread { tv_desc?.text = "当前下载进度：$progress" }
        }
    }

}
