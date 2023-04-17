package com.glebalekseevjk.floatify_android.presentation

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.appComponent
import com.glebalekseevjk.floatify_android.databinding.ActivityMainBinding
import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.ocr.OCRLang
import com.glebalekseevjk.floatify_android.domain.entity.translate.TranslateLang
import com.glebalekseevjk.floatify_android.domain.interactor.OCRUseCase
import com.glebalekseevjk.floatify_android.domain.interactor.TranslateUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var ocrUseCase: OCRUseCase

    @Inject
    lateinit var translateUseCase: TranslateUseCase

//    private lateinit var binding: ActivityMainBinding;

    lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.createMainActivitySubcomponent().inject(this)
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        setContentView(binding.root)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE), 1
        );

        var counterErrors = 0
        var counterSuccess = 0
//        binding.testName.setOnClickListener {
//
//            job = lifecycleScope.launch {
//                for (i in 1..100) {
//                    binding.testName.text = "ТЕСТ $i"
//                    val file = takeScreenshot()
//                    val result = ocrUseCase.recognizeText(file, OCRLang.ANY)
//                    val resultTranslate = when (result) {
//                        is Resource.Failure -> {
//                            result
//                        }
//                        is Resource.Success -> {
//                            translateUseCase.translateText(
//                                listOf(result.data),
//                                TranslateLang.EN,
//                                TranslateLang.RU
//                            )
//                        }
//                    }
//
//                    when (resultTranslate) {
//                        is Resource.Failure<*> -> println("Итерация $i << ошибка").also { counterErrors++ }
//                        is Resource.Success<*> -> println("Итерация $i >> успех: ${result}").also { counterSuccess++ }
//                    }
//                    delay(5000)
//                }
//                println("\t\t$counterErrors ошибочных\n\t\t$counterSuccess успешных\n\t\tВсего итераций: ${counterErrors + counterSuccess}\n")
//            }
//        }
//        binding.stop.setOnClickListener {
//            job.cancel()
//            println("\t\t$counterErrors ошибочных\n\t\t$counterSuccess успешных\n\t\tВсего итераций: ${counterErrors + counterSuccess}\n")
//        }


    }
}

private fun Activity.takeScreenshot(): File {
    try {
        val mPath =
            Environment.getExternalStorageDirectory().absolutePath.toString() + "/" + "time0101" + ".jpg"
        val view = window.decorView.rootView
        view.isDrawingCacheEnabled = true
        val bitmap = convertViewToDrawable(view)
        view.isDrawingCacheEnabled = false

        val imageFile = File(mPath)
        val outputStream = FileOutputStream(imageFile)
        val quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()

        return File(mPath)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    throw RuntimeException("Что-то не так")
}

private fun convertViewToDrawable(view: View): Bitmap {
    val b = Bitmap.createBitmap(
        view.width, view.height,
        Bitmap.Config.ARGB_8888
    )
    val c = Canvas(b)
    c.translate((-view.scrollX).toFloat(), (-view.scrollY).toFloat())
    view.draw(c)
    return b
}