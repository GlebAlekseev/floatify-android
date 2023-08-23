//package com.glebalekseevjk.floatify_android.presentation.floating.base
//
//import android.content.Context
//import android.graphics.PixelFormat
//import android.graphics.Point
//import android.os.Build
//import android.os.Bundle
//import android.os.Looper
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.OrientationEventListener
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.annotation.CallSuper
//import androidx.annotation.MainThread
//import androidx.compose.ui.platform.ComposeView
//import androidx.compose.ui.platform.ViewCompositionStrategy
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.LifecycleRegistry
//import androidx.savedstate.SavedStateRegistry
//import androidx.savedstate.SavedStateRegistryController
//import androidx.savedstate.SavedStateRegistryOwner
//import com.glebalekseevjk.floatify_android.R
//import com.glebalekseevjk.floatify_android.presentation.widget.BackButtonTrackerView
//import com.glebalekseevjk.floatify_android.utils.PermissionUtil
//import com.glebalekseevjk.floatify_android.utils.UIUtils
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.cancelChildren
//import java.io.Closeable
//import kotlin.coroutines.CoroutineContext
//
//
//abstract class FloatingView3(protected val context: Context) {
//    companion object {
//        private val attachedFloatingViews: MutableList<FloatingView3> = mutableListOf()
//
//        fun detachAllFloatingViews() {
//            attachedFloatingViews.toList().forEach { it.detachFromScreen() }
//        }
//    }
//
//    private val windowManager: WindowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
//
//    open val initialPosition: Point = Point(0, 0)
//    open val layoutWidth: Int = WindowManager.LayoutParams.WRAP_CONTENT
//    open val layoutHeight: Int = WindowManager.LayoutParams.WRAP_CONTENT
//    open val layoutFocusable: Boolean = false
//    open val layoutCanMoveOutsideScreen: Boolean = false
//    open val fullscreenMode: Boolean = false
//    open val layoutGravity: Int = Gravity.TOP or Gravity.LEFT
//    open val enableHomeButtonWatcher: Boolean = false
//
//    protected val params: WindowManager.LayoutParams by lazy {
//        val type =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            else WindowManager.LayoutParams.TYPE_PHONE
//
//        var flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//        if (!layoutFocusable)
//            flags = flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        if (layoutCanMoveOutsideScreen)
//            flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        if (fullscreenMode)
//            flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//
//        WindowManager.LayoutParams(layoutWidth, layoutHeight, type, flags, PixelFormat.TRANSLUCENT)
//            .apply {
//                val initPoint = initialPosition
//                x = initPoint.x.fixXPosition()
//                y = initPoint.y.fixYPosition()
//                gravity = layoutGravity
//            }
//    }
//
//    abstract val layoutId: Int
//    protected val rootView: View = LinearLayout(context).apply {
//        addView(
//            ComposeView(context).apply {
//                setViewCompositionStrategy(
//                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//                )
//                id = R.id.compose_view_x
//                // ...
//            }
//        )
//        addView(TextView(context))
//        addView(
//            ComposeView(context).apply {
//                setViewCompositionStrategy(
//                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//                )
//                id = R.id.compose_view_y
//                // ...
//            }
//        )
//    }
////        BackButtonTrackerView(
////            context = context,
////            onAttachedToWindow = { onAttachedToScreen() },
////            onDetachedFromWindow = { onDetachedFromScreen() },
////            onBackButtonPressed = { onBackButtonPressed() },
////        ).apply {
////            rootLayout = LayoutInflater.from(context).inflate(layoutId, null)
////            addView(
////                rootLayout,
////                ViewGroup.LayoutParams(
////                    ViewGroup.LayoutParams.MATCH_PARENT,
////                    ViewGroup.LayoutParams.MATCH_PARENT
////                )
////            )
////        }
//
//    private var lastScreenWidth: Int = -1
//    open val enableDeviceDirectionTracker: Boolean = false
//    private val orientationEventListener = object : OrientationEventListener(context) {
//        override fun onOrientationChanged(orientation: Int) {
//            val screenWidth = UIUtils.screenSize[0]
//            if (screenWidth != lastScreenWidth) {
//                lastScreenWidth = screenWidth
//                onDeviceDirectionChanged()
//            }
//        }
//    }
//
//    var attached: Boolean = false
//        private set
//
//    var onAttached: (() -> Unit)? = null
//    var onDetached: (() -> Unit)? = null
//
//    @MainThread
//    open fun attachToScreen() {
//        if (attached) return
//        if (!PermissionUtil.canDrawOverlays(context)) {
//            return
//        }
//        if (Looper.myLooper() != Looper.getMainLooper()) {
//            return
//        }
//
//        println("------ pre  windowManager.addView(rootView, params)")
//        windowManager.addView(rootView, params)
//
//// Создание параметров для нового представления
//        // Создание параметров для нового представления
//
//
//        println("------ after  windowManager.addView(rootView, params)")
//
//
//        lifecycleOwner.onStateChanged(Lifecycle.State.RESUMED)
//
//        attachedFloatingViews.add(this)
//
//        if (enableDeviceDirectionTracker)
//            orientationEventListener.enable()
//
//        attached = true
//    }
//
//    @MainThread
//    open fun detachFromScreen() {
//        if (!attached) return
//        if (Looper.myLooper() != Looper.getMainLooper()) {
//            return
//        }
//
//        viewScope.coroutineContext.cancelChildren()
//
//        windowManager.removeView(rootView)
//
//        lifecycleOwner.onStateChanged(Lifecycle.State.CREATED)
//
//        attachedFloatingViews.remove(this)
//
//        if (enableDeviceDirectionTracker)
//            orientationEventListener.disable()
//
//        attached = false
//    }
//
//    open fun release() {
//        detachFromScreen()
//        lifecycleOwner.onStateChanged(Lifecycle.State.DESTROYED)
//    }
//
//    protected open fun onDeviceDirectionChanged() {
//        params.x = params.x.fixXPosition()
//        params.y = params.y.fixYPosition()
//        updateViewLayout()
//    }
//
//    @CallSuper
//    protected open fun onAttachedToScreen() {
//        onAttached?.invoke()
//    }
//
//    @CallSuper
//    protected open fun onDetachedFromScreen() {
//        onDetached?.invoke()
//    }
//
//    fun changeViewPosition(x: Int, y: Int) {
//        params.x = x
//        params.y = y
//        updateViewLayout()
//    }
//
//    private fun updateViewLayout() {
//        try {
//            windowManager.updateViewLayout(rootView, params)
//        } catch (e: Exception) {
////            logger.warn(t = e)
//        }
//    }
//
//    open fun onBackButtonPressed(): Boolean = false
//
//    open fun onHomeButtonPressed() {
//
//    }
//
//    open fun onHomeButtonLongPressed() {
//
//    }
//
//    protected val lifecycleOwner: FloatingViewLifecycleOwner = FloatingViewLifecycleOwner()
//
////    private val tasks = mutableListOf<WeakReference<Closeable>>()
//
//    protected val viewScope: CoroutineScope by lazy {
//        FloatingViewCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).apply {
////            tasks.add(WeakReference(this))
//        }
//    }
//
//    private class FloatingViewCoroutineScope(context: CoroutineContext) :
//        Closeable, CoroutineScope {
//        override val coroutineContext: CoroutineContext = context
//
//        override fun close() {
//            coroutineContext.cancel()
//        }
//    }
//
//    protected class FloatingViewLifecycleOwner : LifecycleOwner {
//        private val lifecycleRegistry = LifecycleRegistry(this)
//
//        fun onStateChanged(state: Lifecycle.State) {
//            lifecycleRegistry.currentState = state
//        }
//
//        override val lifecycle: Lifecycle
//            get() = lifecycleRegistry
//    }
//
//    protected fun Int.fixXPosition(): Int =
//        this.coerceAtLeast(0)
//            .coerceAtMost(UIUtils.screenSize[0] - rootView.width)
//
//    protected fun Int.fixYPosition(): Int =
//        this.coerceAtLeast(0)
//            .coerceAtMost(UIUtils.screenSize[1] - rootView.height)
//}
