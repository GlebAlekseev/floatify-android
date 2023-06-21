package com.glebalekseevjk.floatify_android.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

open class BackButtonTrackerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    var onAttachedToWindow: (() -> Unit)? = null,
    var onDetachedFromWindow: (() -> Unit)? = null,
    var onBackButtonPressed: (() -> Boolean)? = null,
) : FrameLayout(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onAttachedToWindow?.invoke()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDetachedFromWindow?.invoke()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && keyDispatcherState != null) {
            if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
                keyDispatcherState.startTracking(event, this)

                return true
            } else if (event.action == KeyEvent.ACTION_UP) {
                keyDispatcherState.handleUpEvent(event)

                if (event.isTracking && !event.isCanceled) {
                    if (onBackButtonPressed?.invoke() == true) {
                        return true
                    }
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }
}
