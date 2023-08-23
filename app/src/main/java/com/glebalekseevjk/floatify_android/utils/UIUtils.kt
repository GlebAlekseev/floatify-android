package com.glebalekseevjk.floatify_android.utils

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.Surface
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object UIUtils {
    private val context by lazy { Utils.context }
    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val orientations = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }

    val displayMetrics: DisplayMetrics
        get() = DisplayMetrics().also { windowManager.defaultDisplay.getMetrics(it) }

    private val realDisplayMetrics: DisplayMetrics
        get() = DisplayMetrics().also { windowManager.defaultDisplay.getRealMetrics(it) }

    private val orientation: Int
        get() = windowManager.defaultDisplay.orientation

    val orientationDegree: Int
        get() = orientations.get(orientation + 90)

    val readSize: Point
        get() = Point().also { windowManager.defaultDisplay.getRealSize(it) }

    private val isPortrait: Boolean
        get() = readSize.let { it.y > it.x }

    val screenSize: IntArray
        get() {
            val metrics = displayMetrics

            var deviceWidth = metrics.widthPixels
            var deviceHeight = metrics.heightPixels
            if (deviceHeight > deviceWidth != isPortrait) {
                deviceWidth = metrics.heightPixels

                deviceHeight = metrics.widthPixels
            }

            return intArrayOf(deviceWidth, deviceHeight)
        }

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        ).toInt()
    }


}

fun Number.dpToPx(): Int = UIUtils.dpToPx(this.toFloat())
