package com.glebalekseevjk.floatify_android.data.paddle.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import com.baidu.paddle.lite.demo.ocr.OCRPredictorNative
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.data.free_translate.FreeTranslateRetrofit
import com.glebalekseevjk.floatify_android.utils.copyDirectoryFromAssets
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.InputStream

class OCRPredictor(
    private val context: Context
) {
    private var paddlePredictor: OCRPredictorNative? = null
    private var inputImage: Bitmap? = null
    private var outputImage: Bitmap? = null
    private val wordLabels = mutableListOf<String>()
    private var detLongSize: Int = 960
    private var warmupIterNum: Int = 1
    private var _isLoaded = false
    private var isLoaded: Boolean
        get() = _isLoaded && paddlePredictor != null
        set(value) {
            _isLoaded = value
        }

    init {
        loadModel()
        loadLabel()
    }

    private fun loadModel() {
        val realPath =
            context.cacheDir.toString() + "/" + context.getString(R.string.paddle_model_path)
        context.copyDirectoryFromAssets(context.getString(R.string.paddle_model_path), realPath)

        val config = OCRPredictorNative.Companion.Config.DEFAULT.copy(
            detectionModelPath = realPath + File.separator + "det_db.nb",
            recognitionModelPath = realPath + File.separator + "rec_crnn.nb",
            classificationModelPath = realPath + File.separator + "cls.nb",
        )

        paddlePredictor = OCRPredictorNative(config)
        isLoaded = true
    }

    private fun loadLabel() {
        wordLabels.clear()
        wordLabels.add("black")
        val assetsInputStream: InputStream =
            context.assets.open(context.getString(R.string.paddle_label_path))
        val available = assetsInputStream.available()
        val lines = ByteArray(available)
        assetsInputStream.read(lines)
        assetsInputStream.close()
        val words = String(lines)
        val contents = words.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (content in contents) {
            wordLabels.add(content)
        }
        wordLabels.add(" ")
    }

    fun setInputImage(image: Bitmap) {
        inputImage = image.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun runModelReturnResults(runDetection: Boolean, runClassification: Boolean, runRecognition: Boolean): List<OCRResultModel>{
        if (inputImage == null || !isLoaded) throw RuntimeException("run isLoaded=$isLoaded inputImage=$inputImage")

        // Warm up
        for (i in 0 until warmupIterNum) {
            paddlePredictor!!.runImage(
                inputImage,
                detLongSize,
                if (runDetection) 1 else 0,
                if (runClassification) 1 else 0,
                if (runRecognition) 1 else 0
            )
        }
        warmupIterNum = 0

        val results =
            paddlePredictor!!.runImage(
                inputImage,
                detLongSize,
                if (runDetection) 1 else 0,
                if (runClassification) 1 else 0,
                if (runRecognition) 1 else 0
            )
        return postProcess(results)
    }


    fun runModel(runDetection: Boolean, runClassification: Boolean, runRecognition: Boolean): Bitmap {
        val results = runModelReturnResults(runDetection, runClassification, runRecognition)
        drawResultsModern(results)
        return outputImage!!
    }

    private fun postProcess(results: ArrayList<OCRResultModel>): ArrayList<OCRResultModel> {
        for (r in results) {
            val word = StringBuffer()
            for (index in r.wordIndex) {
                if (index >= 0 && index < wordLabels.size) {
                    word.append(wordLabels[index])
                } else {
                    word.append("×")
                }
            }
            r.label = word.toString()
            r.clsLabel = if (r.clsIdx.equals(1f)) "180" else "0"
        }
        return results
    }

    private fun drawResultsModern(results: List<OCRResultModel>) {
        outputImage = inputImage
        val canvas = Canvas(outputImage!!)
        val paintFillAlpha = Paint()
        paintFillAlpha.style = Paint.Style.FILL
        paintFillAlpha.color = Color.parseColor("#3B85F5")
        paintFillAlpha.alpha = 200
        val paint = Paint()
        paint.color = Color.parseColor("#3B85F5")
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE

        val fullText = results.map { it.label }.joinToString("\n###\n")
        val response = runBlocking {
            FreeTranslateRetrofit.translationApi.translateText("ru", fullText)
        }
        val translatedTextArray = (if (response.isSuccessful) response.body()?.destinationText ?: fullText else fullText)
            .split("\n###\n")


        for ((index, result) in results.withIndex()) {
            val path = Path()
            val points: List<Point> = result.points
            if (points.isEmpty()) {
                continue
            }
            path.moveTo(points[0].x.toFloat(), points[0].y.toFloat())
            for (i in points.indices.reversed()) {
                val p = points[i]
                path.lineTo(p.x.toFloat(), p.y.toFloat())
            }
            canvas.drawPath(path, paint)
            canvas.drawPath(path, paintFillAlpha)

            val text = translatedTextArray[index]

            val paint2 = Paint().apply {
                textSize = 100f
                textAlign = Paint.Align.LEFT
                color = Color.WHITE
            }

            val bounds = RectF()
            path.computeBounds(bounds, true)

            val targetWidth = bounds.width()
            val targetHeight = bounds.height()
            val textBounds = Rect()
            while (paint2.measureText(text) > targetWidth*0.9 || textBounds.height() > targetHeight*0.9) {
                paint2.textSize -= 1f
                paint2.getTextBounds(text, 0, text.length, textBounds)
            }

            val x = bounds.left + (bounds.width() - textBounds.width()) / 2
            val y = (bounds.bottom + bounds.top) / 2 + textBounds.height() / 2

            println(translatedTextArray[index])
            canvas.drawText(text, x, y, paint2)
        }
    }
}