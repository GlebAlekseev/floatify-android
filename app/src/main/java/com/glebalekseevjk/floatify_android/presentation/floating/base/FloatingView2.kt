package com.glebalekseevjk.floatify_android.presentation.floating.base

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import android.view.WindowManager.LayoutParams.TYPE_WALLPAPER
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.glebalekseevjk.floatify_android.utils.PermissionUtil
import com.glebalekseevjk.floatify_android.utils.UIUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

abstract class FloatingView2(protected val context: Context) {
    companion object {
        private val attachedFloatingViews: MutableList<FloatingView2> = mutableListOf()

        fun detachAllFloatingViews() {
            attachedFloatingViews.toList().forEach { it.detachFromScreen() }
        }
    }

    private val windowManager: WindowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    open val initialPosition: Point = Point(0, 0)
    open val layoutFocusable: Boolean = false
    open val layoutCanMoveOutsideScreen: Boolean = false
    open val fullscreenMode: Boolean = false
    open val layoutWidth: Int = WindowManager.LayoutParams.WRAP_CONTENT
    open val layoutHeight: Int = WindowManager.LayoutParams.WRAP_CONTENT
    open val layoutGravity: Int = Gravity.TOP or Gravity.LEFT

    protected val params: WindowManager.LayoutParams by lazy {
        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE

        var flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//        if (!layoutFocusable)
//            flags = flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        if (layoutCanMoveOutsideScreen)
//            flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        if (fullscreenMode)
//            flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

        WindowManager.LayoutParams(layoutWidth, layoutHeight, type,
            flags,
//            flags or FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCH_MODAL or FLAG_NOT_TOUCHABLE or FLAG_FULLSCREEN,
//            TYPE_WALLPAPER,
            PixelFormat.TRANSLUCENT)
            .apply {

                width = windowManager.defaultDisplay.width
                val initPoint = initialPosition
                x = initPoint.x.fixXPosition()
                y = initPoint.y.fixYPosition()
                gravity = layoutGravity
            }
    }


    class MyLifecycleOwner2 : SavedStateRegistryOwner {

        private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
        private var mSavedStateRegistryController: SavedStateRegistryController =
            SavedStateRegistryController.create(this)

        val isInitialized: Boolean
            get() = true

        fun setCurrentState(state: Lifecycle.State) {
            mLifecycleRegistry.currentState = state
        }

        fun handleLifecycleEvent(event: Lifecycle.Event) {
            mLifecycleRegistry.handleLifecycleEvent(event)
        }

        fun performRestore(savedState: Bundle?) {
            mSavedStateRegistryController.performRestore(savedState)
        }

        fun performSave(outBundle: Bundle) {
            mSavedStateRegistryController.performSave(outBundle)
        }

        override val lifecycle: Lifecycle
            get() = mLifecycleRegistry
        override val savedStateRegistry: SavedStateRegistry
            get() = mSavedStateRegistryController.savedStateRegistry
    }

    abstract val composableContent: @Composable () -> Unit
    protected val rootView: ComposeView by lazy {
        val composeView = ComposeView(context)
        composeView.setContent(composableContent)
        val lifecycleOwner = MyLifecycleOwner2()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
//        val viewModelStore = ViewModelStore()
//        composeView.setViewTreeViewModelStoreOwner(viewModelStore)
//        ViewTreeViewModelStoreOwner.set(composeView) { viewModelStore }
//        composeView.setOnTouchListener { v, event -> false }
        composeView.setOnTouchListener { v, event ->
            println("v=$v event=$event")
            false
        }
        composeView
    }
//        .apply {
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setContent {
//                composableContent()
//            }
//            setViewTreeLifecycleOwner(myLifecycleOwner)
//            setViewTreeSavedStateRegistryOwner(myLifecycleOwner)
//        }
//    protected val rootView: BackButtonTrackerView =
//        BackButtonTrackerView(
//            context = context,
//            onAttachedToWindow = { onAttachedToScreen() },
//            onDetachedFromWindow = { onDetachedFromScreen() },
//            onBackButtonPressed = { onBackButtonPressed() },
//        ).apply {
//            addView(
//                ComposeView(context).apply {
//                    setViewCompositionStrategy(
//                        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//                    )
//                    id = R.id.compose_view_y
//                    setContent{
//                        Text(text = "YARRRRRRRRRRRR")
//                    }
//                }
//            )
//        }

//    protected val rootView: ComposeView by lazy {
//        ComposeView(context).apply {
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setContent {
//                composableContent()
//            }
//            setViewTreeLifecycleOwner(myLifecycleOwner)
//            setViewTreeSavedStateRegistryOwner(myLifecycleOwner)
//        }
//    }
//    protected val rootView: BackButtonTrackerView =
//        BackButtonTrackerView(
//            context = context,
//            onAttachedToWindow = { onAttachedToScreen() },
//            onDetachedFromWindow = { onDetachedFromScreen() },
//            onBackButtonPressed = { onBackButtonPressed() },
//        ).apply {
//            addView(
//                ComposeView(context).apply {
//                    setViewCompositionStrategy(
//                        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//                    )
//                    id = R.id.compose_view_y
//                    setContent(composableContent)
//                }
//            )
//        }


    var attached: Boolean = false
        private set

    @MainThread
    open fun attachToScreen() {
        if (attached) return
        if (!PermissionUtil.canDrawOverlays(context)) return
        if (Looper.myLooper() != Looper.getMainLooper()) return
        windowManager.addView(rootView, params)
        lifecycleOwner.onStateChanged(Lifecycle.State.RESUMED)
        attachedFloatingViews.add(this)
        attached = true
    }


    @MainThread
    open fun detachFromScreen() {
        if (!attached) return
        if (Looper.myLooper() != Looper.getMainLooper()) return
        viewScope.coroutineContext.cancelChildren()
        windowManager.removeView(rootView)
        lifecycleOwner.onStateChanged(Lifecycle.State.CREATED)
        attachedFloatingViews.remove(this)
        attached = false
    }

    open fun release() {
        detachFromScreen()
        lifecycleOwner.onStateChanged(Lifecycle.State.DESTROYED)
    }

    protected val lifecycleOwner: FloatingViewLifecycleOwner = FloatingViewLifecycleOwner()

    protected class FloatingViewLifecycleOwner : LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        fun onStateChanged(state: Lifecycle.State) {
            lifecycleRegistry.currentState = state
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
    }

    protected val savedStateRegistryOwner: FloatingSavedStateRegistryOwner =
        FloatingSavedStateRegistryOwner(context)

    protected class FloatingSavedStateRegistryOwner(context: Context) : SavedStateRegistryOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
//            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
        override val savedStateRegistry: SavedStateRegistry
            get() = SavedStateRegistryController.create(this).savedStateRegistry
    }


    protected val myLifecycleOwner: MyLifecycleOwner = MyLifecycleOwner()

    class MyLifecycleOwner() : SavedStateRegistryOwner {
        private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
        private var mSavedStateRegistryController: SavedStateRegistryController =
            SavedStateRegistryController.create(this)

        /**
         * @return True if the Lifecycle has been initialized.
         */
        val isInitialized: Boolean
            get() = true


        fun setCurrentState(state: Lifecycle.State) {
            mLifecycleRegistry.currentState = state
        }

        fun handleLifecycleEvent(event: Lifecycle.Event) {
            mLifecycleRegistry.handleLifecycleEvent(event)
        }

//        fun getSavedStateRegistry(): SavedStateRegistry {
//            return mSavedStateRegistryController.savedStateRegistry
//        }

        fun performRestore(savedState: Bundle?) {
            mSavedStateRegistryController.performRestore(savedState)
        }

        fun performSave(outBundle: Bundle) {
            mSavedStateRegistryController.performSave(outBundle)
        }

        override val lifecycle: Lifecycle = mLifecycleRegistry
        override val savedStateRegistry: SavedStateRegistry =
            mSavedStateRegistryController.savedStateRegistry
    }


    protected val viewScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    protected fun Int.fixXPosition(): Int =
        this.coerceAtLeast(0)
            .coerceAtMost(UIUtils.screenSize[0] - rootView.width)

    protected fun Int.fixYPosition(): Int =
        this.coerceAtLeast(0)
            .coerceAtMost(UIUtils.screenSize[1] - rootView.height)
}