package com.glebalekseevjk.floatify_android.presentation.floating

import android.annotation.TargetApi
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.utils.PermissionUtil
import com.glebalekseevjk.floatify_android.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


object FloatingStateHolder {
    private val _showingStateChangedFlow = MutableStateFlow(false)
    val showingStateChangedFlow: StateFlow<Boolean> = _showingStateChangedFlow
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    private val viewModel: MainBarViewModel by lazy { MainBarViewModel(CoroutineScope(Dispatchers.IO)) }

    private val context: Context by lazy { Utils.context }
    private val mainBar: MainBar by lazy { MainBar(context,viewModel) }
    private val buttonBar: ButtonBar by lazy { ButtonBar(context,viewModel) }
    val isMainBarAttached: Boolean
        get() = buttonBar.attached
    val isButtonBarAttached: Boolean
        get() = buttonBar.attached


    fun showMainBar() {
        scope.launch {
            _showingStateChangedFlow.emit(true)
        }
        if (isMainBarAttached) return
        if (isButtonBarAttached) return
        mainBar.attachToScreen()
        buttonBar.attachToScreen()


    }
}
