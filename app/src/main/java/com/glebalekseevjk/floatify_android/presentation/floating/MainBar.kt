package com.glebalekseevjk.floatify_android.presentation.floating

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.databinding.FloatingMainBarBinding
import com.glebalekseevjk.floatify_android.presentation.floating.base.MovableFloatingView
import kotlinx.coroutines.launch

class MainBar(context: Context, val viewModel: MainBarViewModel) : MovableFloatingView(context) {
    override val layoutId: Int
        get() = R.layout.floating_main_bar

    override val initialPosition: Point
        get() = Point(0, 0)
    override val flags: Int
        get() = FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCH_MODAL or FLAG_NOT_TOUCHABLE

    private val binding: FloatingMainBarBinding = FloatingMainBarBinding.bind(rootLayout)
//    private val viewModel: MainBarViewModel by lazy { MainBarViewModel(viewScope) }

//    private fun startImage(){
////        binding.start.setOnClickListener {
////            stopImage()
////        }
//        viewModel.setImage(
//            { bitmap ->
//                binding.imageView.setImageBitmap(bitmap)
//            },
//            context
//        )
//        binding.imageView.visibility = View.VISIBLE
//    }

//    private fun stopImage(){
////        binding.start.text = "Старт"
////        binding.start.setOnClickListener {
////            startImage()
////        }
//        binding.imageView.visibility = View.INVISIBLE
//    }

    init {
        viewScope.launch {
            viewModel.stateImage.collect{
                binding.imageView.setImageBitmap(it)
                if (it == null){
                    binding.imageView.visibility = View.INVISIBLE

                }else {
                    binding.imageView.visibility = View.VISIBLE
                }
            }
        }

//        viewScope.launch {
////            viewModel.state.collect{
////                binding.layoutLl.setBackgroundColor(it)
////            }
//        }

//        binding.composeView.apply {
////            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setContent {
//                // In Compose world
//                MaterialTheme {
//                    Text("Hello Compose!")
//                }
//            }
////            setViewTreeLifecycleOwner()
////            setViewTreeSavedStateRegistryOwner(myLifecycleOwner)
//        }
    }
//    override val enableDeviceDirectionTracker: Boolean
//        get() = true
//
//    override val moveToEdgeAfterMoved: Boolean
//        get() = true


//    override fun onAttachedToScreen() {
//        super.onAttachedToScreen()
//    }
//
//    override fun onDetachedFromScreen() {
//        super.onDetachedFromScreen()
//    }
}
