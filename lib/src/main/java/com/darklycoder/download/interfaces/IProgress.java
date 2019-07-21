package com.darklycoder.download.interfaces;

import android.support.annotation.FloatRange;

public interface IProgress {

    void onProgress(@FloatRange(from = 0F, to = 1F) float progress);
}
