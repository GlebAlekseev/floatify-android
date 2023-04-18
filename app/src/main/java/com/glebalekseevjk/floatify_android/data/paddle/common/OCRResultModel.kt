package com.glebalekseevjk.floatify_android.data.paddle.common

import android.graphics.Point

data class OCRResultModel(
    val points: MutableList<Point> = mutableListOf(),
    val wordIndex: MutableList<Int> = mutableListOf(),
    var label: String = "",
    var confidence: Float = 0f,
    var clsIdx: Float = 0f,
    var clsLabel: String = "",
    var clsConfidence: Float = 0f,
)
