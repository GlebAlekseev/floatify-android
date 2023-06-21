package com.glebalekseevjk.floatify_android.presentation.floating

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.floatify_android.presentation.floating.base.FloatingView2
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

//class MainBar2(context: Context) : FloatingView2(context) {
//    private val viewModel: MainBar2ViewModel by lazy { MainBar2ViewModel(viewScope) }
//
//    // получать каждую секунду новые данные для отрисовки данных
//    //
//    val colors = listOf(Color.Red,Color.Green, Color.Cyan, Color.Blue)
//
//    var state by mutableStateOf(colors.random())
////    var stateFlow = MutableStateFlow(colors.random())
//    init {
//        viewScope.launch {
//            while (true){
//                state = colors.random()
////                stateFlow.emit(colors.random())
//                println("------++ pre2 delay ${stateFlow.value}")
//                delay(1000)
//                composableContent.
//            }
//        }
//    }
//
//    override val composableContent: @Composable () -> Unit = {
//        val ssssss by stateFlow.collectAsState()
//        val stateColor = viewModel.stateColor
//        println(state)
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .alpha(0.3f)
//            .background(ssssss)
//            )
//
//    }



//}