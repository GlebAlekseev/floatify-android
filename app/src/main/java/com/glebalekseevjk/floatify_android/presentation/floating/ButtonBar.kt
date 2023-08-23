package com.glebalekseevjk.floatify_android.presentation.floating

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.databinding.FloatingMainBarBinding
import com.glebalekseevjk.floatify_android.databinding.FloatingMainBarButtonBinding
import com.glebalekseevjk.floatify_android.presentation.floating.base.MovableFloatingView
import kotlinx.coroutines.launch

class ButtonBar(context: Context, val viewModel: MainBarViewModel) : MovableFloatingView(context) {
    override val layoutId: Int
        get() = R.layout.floating_main_bar_button

    override val initialPosition: Point
        get() = Point(0, 0)
    override val flags: Int
        get() = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

    override val layoutWidth: Int
        get() = WRAP_CONTENT
    override val layoutHeight: Int
        get() = WRAP_CONTENT

    private val binding: FloatingMainBarButtonBinding = FloatingMainBarButtonBinding.bind(rootLayout)

    private fun startImage(){
        binding.start.text = "Закрыть"
        binding.start.setOnClickListener {
            stopImage()
        }
        viewModel.setImage(
            context
        )
    }

    private fun stopImage(){
        binding.start.text = "Перевести"
        binding.start.setOnClickListener {
            startImage()
        }
        viewScope.launch {
            viewModel.stateImage.emit(null)
        }
    }

    init {

        binding.start.setOnClickListener {
            startImage()
        }

    }
}
