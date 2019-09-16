package com.darklycoder.download.interfaces

import androidx.annotation.FloatRange

interface IProgress {

    fun onProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float)
}


