package com.baidu.paddle.lite.demo.ocr

import android.graphics.Bitmap
import android.graphics.Point
import com.glebalekseevjk.floatify_android.data.paddle.common.OCRResultModel
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class OCRPredictorNative(private val config: Config) {
    private var nativePointer: Long = 0

    init {
        loadLibrary()
        nativePointer = init(
            config.detectionModelPath,
            config.recognitionModelPath,
            config.classificationModelPath,
            config.useOpenCL,
            config.cpuThreadNum,
            config.cpuPower
        )
    }

    fun runImage(
        originalImage: Bitmap?,
        max_size_len: Int,
        run_det: Int,
        run_cls: Int,
        run_rec: Int
    ): ArrayList<OCRResultModel> {
        val rawResults =
            forward(nativePointer, originalImage, max_size_len, run_det, run_cls, run_rec)
        return postprocess(rawResults)
    }

    fun destory() {
        if (nativePointer != 0L) {
            release(nativePointer)
            nativePointer = 0
        }
    }

    protected external fun init(
        detModelPath: String?,
        recModelPath: String?,
        clsModelPath: String?,
        useOpencl: Int,
        threadNum: Int,
        cpuMode: String?
    ): Long

    protected external fun forward(
        pointer: Long,
        originalImage: Bitmap?,
        max_size_len: Int,
        run_det: Int,
        run_cls: Int,
        run_rec: Int
    ): FloatArray

    protected external fun release(pointer: Long)
    private fun postprocess(raw: FloatArray): ArrayList<OCRResultModel> {
        val results = ArrayList<OCRResultModel>()
        var begin = 0
        while (begin < raw.size) {
            val point_num = Math.round(raw[begin])
            val word_num = Math.round(raw[begin + 1])
            val res = parse(raw, begin + 2, point_num, word_num)
            begin += 2 + 1 + point_num * 2 + word_num + 2
            results.add(res)
        }
        return results
    }

    private fun parse(raw: FloatArray, begin: Int, pointNum: Int, wordNum: Int): OCRResultModel {
        var current = begin
        val res = OCRResultModel()
        res.confidence = raw[current]
        current++
        for (i in 0 until pointNum) {
            res.points.add(
                Point(
                    raw[current + i * 2].roundToInt(),
                    raw[current + i * 2 + 1].roundToInt()
                )
            )
        }
        current += pointNum * 2
        for (i in 0 until wordNum) {
            val index = Math.round(raw[current + i])
            res.wordIndex.add(index)
        }
        current += wordNum
        res.clsIdx = raw[current]
        res.clsConfidence = raw[current + 1]
        return res
    }

    companion object {
        private val isSOLoaded = AtomicBoolean()

        @Throws(RuntimeException::class)
        fun loadLibrary() {
            if (!isSOLoaded.get() && isSOLoaded.compareAndSet(false, true)) {
                try {
                    System.loadLibrary("Native")
                } catch (e: Throwable) {
                    throw RuntimeException(
                        "Load libNative.so failed, please check it exists in apk file.", e
                    )
                }
            }
        }
        data class Config(
            var detectionModelPath: String?,
            var recognitionModelPath: String?,
            var classificationModelPath: String?,
            var useOpenCL: Int,
            var cpuThreadNum: Int,
            var cpuPower: String,
        ) {
            companion object {
                val DEFAULT = Config(
                    detectionModelPath = null,
                    recognitionModelPath = null,
                    classificationModelPath = null,
                    useOpenCL = 1, // 1 or 0 // true or false
                    cpuThreadNum = 1,
                    cpuPower = "LITE_POWER_HIGH",
                )
            }
        }
    }
}