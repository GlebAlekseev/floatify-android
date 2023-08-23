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
    }
}
