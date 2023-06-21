package com.glebalekseevjk.floatify_android.presentation.floating

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.glebalekseevjk.floatify_android.data.paddle.common.OCRPredictor
import com.glebalekseevjk.floatify_android.data.paddle.common.OCRResultModel
import com.glebalekseevjk.floatify_android.data.paddle.paddle_ocr.PaddleOCRRepositoryImpl
import com.glebalekseevjk.floatify_android.domain.interactor.OCRUseCase
import com.glebalekseevjk.floatify_android.presentation.floating.base.FloatingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tw.firemaples.onscreenocr.screenshot.ScreenExtractor

class MainBarViewModel(viewScope: CoroutineScope) : FloatingViewModel(viewScope) {
//    private val repo by lazy { GeneralRepository() }
    val state = MutableStateFlow(Color.CYAN)
    val colors = listOf(Color.RED,Color.GRAY, Color.BLUE, Color.YELLOW)
    init {
//        viewScope.launch {
//            while (true){
//                state.emit(colors.random())
//                println("-------- pre delay ${state.value}")
//                delay(1000)
//            }
//        }

    }

    val stateImage = MutableStateFlow<Bitmap?>(null)
    val stateResults = MutableStateFlow<List<OCRResultModel>>(listOf())

    fun setImage(context: Context){
        viewScope.launch {
//            while (true){
                if (ScreenExtractor.isGranted){
//                    disableOverlay()
                    val image = ScreenExtractor.extractBitmapFromScreen(
                        Rect(0,55,12300,22300),
                        Rect(0,55,12300,22300),
                    )

//                    val predicator = OCRPredictor(context)
//                    println("-------------------- predicator initialized")
//                        predicator.setInputImage(image)
//                        val result = predicator.runModel(
//                            runDetection = true,
//                            runClassification = true,
//                            runRecognition = true,
//                        )
//                    stateImage.emit(result)


                    val predicator = OCRPredictor(context)
                    println("-------------------- predicator initialized")
                    predicator.setInputImage(image)
                    val results = predicator.runModel(
                        runDetection = true,
                        runClassification = true,
                        runRecognition = true,
                    )

                    stateImage.emit(results)




//                    stateResults.emit(results)

//                    expression(result)
//                    enableOverlay()
                }
//                delay(1110)
//            }

        }
    }
}