package com.darklycoder.download.demo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.darklycoder.download.DownloadManager;
import com.darklycoder.download.info.TaskCellInfo;
import com.darklycoder.download.info.TaskInfo;
import com.darklycoder.download.interfaces.IStatusListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Download-log";
    private TextView mTvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvDesc = findViewById(R.id.tv_desc);

        findViewById(R.id.btn_download).setOnClickListener(v -> {
            String url = "https://8ff2f6e45e37e0add27611068acbf0ea.dd.cdntips.com/imtt.dd.qq.com/16891/apk/26AAB74EF647495C8E0B4D6B24C80224.apk?mkey=5d331828700a7816&f=1849&fsname=com.tencent.mm_7.0.5_1440.apk&csr=1bbd&cip=112.10.94.227&proto=https";
            url = "http://pic41.nipic.com/20140508/18609517_112216473140_2.jpg";
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".downloadTmp";
            TaskCellInfo cellInfo = new TaskCellInfo(url, path, "test.jpg");

            String url2 = "http://pic38.nipic.com/20140211/17882171_143443301183_2.jpg";
            TaskCellInfo cellInfo2 = new TaskCellInfo(url2, path, "test2.jpg");

            DownloadManager.getInstance().download(new TaskInfo(Arrays.asList(cellInfo,cellInfo2)), statusListener);
        });
    }

    private IStatusListener statusListener = new IStatusListener() {
        @Override
        public void onCellProgress(TaskCellInfo info, float progress) {
            Log.d(TAG, info.name + ": " + progress);

            runOnUiThread(() -> mTvDesc.setText("当前下载进度：" + progress));
        }

        @Override
        public void onCellSuccess(TaskCellInfo info) {
            Log.d(TAG, info.name + "下载成功！");
        }

        @Override
        public void onCellFail(TaskCellInfo info) {
            Log.d(TAG, info.name + "下载失败！");
        }

        @Override
        public void onTotalProgress(float progress, long count, long successCount, long failCount) {

        }
    };

}
